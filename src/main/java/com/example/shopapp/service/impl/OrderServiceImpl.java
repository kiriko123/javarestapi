package com.example.shopapp.service.impl;

import com.example.shopapp.configuration.Translator;
import com.example.shopapp.dto.request.OrderRequestDTO;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.model.Order;
import com.example.shopapp.model.User;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.repository.UserRepository;
import com.example.shopapp.service.OrderService;
import com.example.shopapp.util.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;


    @Override
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        //ModelMapper modelMapper = new ModelMapper();
        //tim xem userid co ton tai khong
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() ->new ResourceNotFoundException(Translator.toLocale("user.not.found")));

        //convert orderdto sang order
        //dung thu vien model mapper
        //tao 1 luong anh xa rieng de kiem soat viec anh xa
        modelMapper.typeMap(OrderRequestDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderRequestDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date()); //lay thoi diem hien tai
        order.setStatus(OrderStatus.PENDING.getValue());
        //kiem tra shipping date phai lon hon today
        LocalDate shippingDate = orderRequestDTO.getShippingDate() == null ? LocalDate.now() : orderRequestDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())) {
            throw new ResourceNotFoundException("Shipping date must be at least now");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        return order;
    }

    @Override
    public void updateOrder(Long orderId, OrderRequestDTO orderRequestDTO) {
        Order existingOrder = getOrderById(orderId);

        userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("user.not.found")));
        modelMapper.typeMap(OrderRequestDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = new Order();
        modelMapper.map(orderRequestDTO, order);
        order.setUser(existingOrder.getUser());
        order.setId(orderId);
        order.setActive(existingOrder.isActive());
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        //ko xoa cung hay xoa mem
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->new ResourceNotFoundException(Translator.toLocale("user.not.found")));
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow( () ->  new ResourceNotFoundException(Translator.toLocale("order.not.found")));
    }

}
