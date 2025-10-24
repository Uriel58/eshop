package com.example.demo.service.impl;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;
    
    @Transactional
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }
    
    @Transactional
    @Override
    public Order getOrderById(Long id) {
        return orderDAO.findById(id);
    }
    @Transactional
    @Override
    public void saveOrder(Order order) {
        orderDAO.save(order);
    }
    
    @Transactional
    @Override
    public void updateOrder(Order order) {
        orderDAO.update(order);
    }
    
    @Transactional
    @Override
    public void deleteOrder(Long id) {
        orderDAO.delete(id);
    }
}
