package com.example.demo.service;

import com.example.demo.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long ordNum);
    void saveOrder(Order order);
    void deleteOrder(Long ordNum);
    void createOrderFromCart(Long customerId, String paymentMethod, String deliveryMethod);
    
    void removeCart(Long cartId);
    Optional<Order> getOrderByIdWithDetails(Long ordNum);
    Optional<Order> getOrderWithDetails(Long ordNum);
}
