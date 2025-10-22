package com.example.demo.service;

import com.example.demo.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long ordNum);
    void saveOrder(Order order);
    void deleteOrder(Long ordNum);
    void createOrderFromCart(Long customerId, String paymentMethod, String deliveryMethod);
    
    void removeCart(Long cartId);
}
