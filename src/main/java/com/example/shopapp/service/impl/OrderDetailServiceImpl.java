package com.example.shopapp.service.impl;

import com.example.shopapp.configuration.Translator;
import com.example.shopapp.dto.request.OrderDetailRequestDTO;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.model.Order;
import com.example.shopapp.model.OrderDetail;
import com.example.shopapp.model.Product;
import com.example.shopapp.repository.OrderDetailRepository;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail getOrderDetailById(long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("order_details.not.found")));
    }

    @Override
    public List<OrderDetail> getAllOrderDetailsByOrderId(long orderId) {
        orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("order_details.notFound")));
        return orderDetailRepository.findByOrderId(orderId);
    }


    @Override
    public void deleteOrderDetail(long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public void updateOrderDetail(long id, OrderDetailRequestDTO orderDetailRequestDTO) {
        OrderDetail orderDetail = getOrderDetailById(id);

        Order order = orderRepository.findById(orderDetailRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("order_details.notFound")));

        Product product = productRepository.findById(orderDetailRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("product_details.notFound")));

        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        orderDetail.setPrice(orderDetailRequestDTO.getPrice());
        orderDetail.setColor(orderDetailRequestDTO.getColor());
        orderDetail.setNumberOfProducts(orderDetailRequestDTO.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailRequestDTO.getTotalMoney());
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail createOrderDetail(OrderDetailRequestDTO orderDetailRequestDTO) {
        Order order = orderRepository.findById(orderDetailRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("order_details.not.found")));

        Product product = productRepository.findById(orderDetailRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("product_details.not.found")));

        OrderDetail orderDetail =OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailRequestDTO.getNumberOfProducts())
                .totalMoney(orderDetailRequestDTO.getTotalMoney())
                .color(orderDetailRequestDTO.getColor())
                .build();

        return orderDetailRepository.save(orderDetail);
    }
}
