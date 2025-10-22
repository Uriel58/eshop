package com.example.demo.dao;

import com.example.demo.model.Order;

import java.util.List;

public interface OrderDAO {
    List<Order> findAll();
    Order findById(Long ordNum);
    void save(Order order);
    void delete(Long ordNum);
}
