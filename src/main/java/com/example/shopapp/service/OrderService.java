package com.example.shopapp.service;

import com.example.shopapp.dto.request.OrderRequestDTO;
import com.example.shopapp.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequestDTO order);
    void updateOrder(Long orderId,OrderRequestDTO order);
    void deleteOrder(Long orderId);
    List<Order> getAllOrdersByUserId(Long userId);
    Order getOrderById(Long orderId);

}
