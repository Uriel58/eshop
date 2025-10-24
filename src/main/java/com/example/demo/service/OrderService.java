package com.example.demo.service;

import com.example.demo.model.Order;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    void saveOrder(Order order);
    void updateOrder(Order order);
    void deleteOrder(Long id);
}
