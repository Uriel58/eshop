package com.example.demo.service.impl;

import com.example.demo.dao.OrderDetailDAO;
import com.example.demo.model.OrderDetail;
import com.example.demo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	
	
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    
    @Transactional
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailDAO.findByOrderId(orderId);
    }
    
    @Transactional
    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailDAO.save(orderDetail);
    }
    
    @Transactional
    @Override
    public void deleteOrderDetail(Long orderId, Long productId) {
        orderDetailDAO.delete(orderId, productId);
    }
}
