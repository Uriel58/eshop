package com.example.demo.service;

import com.example.demo.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
    void saveOrderDetail(OrderDetail orderDetail);
    void deleteOrderDetail(Long orderId, Long productId);
}
