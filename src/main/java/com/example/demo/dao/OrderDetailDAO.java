package com.example.demo.dao;

import com.example.demo.model.OrderDetail;

import java.util.List;

public interface OrderDetailDAO {
    List<OrderDetail> findByOrderId(Long ordNum);
    void save(OrderDetail detail);
    void delete(OrderDetail detail);
    
    boolean existsByOrdNumAndProdNum(Long ordNum, Long prodNum);
}
