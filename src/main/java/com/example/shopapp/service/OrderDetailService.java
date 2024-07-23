package com.example.shopapp.service;

import com.example.shopapp.dto.request.OrderDetailRequestDTO;
import com.example.shopapp.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetail getOrderDetailById(long id);
    List<OrderDetail> getAllOrderDetailsByOrderId(long orderId);
    void deleteOrderDetail(long id);
    void updateOrderDetail(long id, OrderDetailRequestDTO orderDetailRequestDTO);
    OrderDetail createOrderDetail(OrderDetailRequestDTO orderDetailRequestDTO);
}
