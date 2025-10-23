package com.example.demo.dao;

import com.example.demo.model.Order;

import java.util.List;
import java.util.Optional;
public interface OrderDAO {
    List<Order> findAll();
    Order findById(Long ordNum);
    void save(Order order);
    void delete(Long ordNum);
 // 新增此方法宣告
    Optional<Order> findByIdWithDetails(Long ordNum);
    
}
