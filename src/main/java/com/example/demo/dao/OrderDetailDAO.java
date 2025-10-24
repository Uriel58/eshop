package com.example.demo.dao;

import com.example.demo.model.OrderDetail;
import java.util.List;

public interface OrderDetailDAO {
    List<OrderDetail> findByOrderId(Long orderId);
    void save(OrderDetail orderDetail);
    void delete(Long orderId, Long productId);  // 使用复合主键删除
}
