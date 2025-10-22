package com.example.demo.service;

import com.example.demo.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> findByOrderId(Long ordNum);
    void saveOrderDetail(OrderDetail detail);
    void deleteOrderDetail(OrderDetail detail);
    
    boolean existsByOrdNumAndProdNum(Long ordNum, Long prodNum);
}
