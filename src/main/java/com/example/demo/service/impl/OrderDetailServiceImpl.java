package com.example.demo.service.impl;

import com.example.demo.dao.OrderDetailDAO;
import com.example.demo.model.OrderDetail;
import com.example.demo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Override
    public List<OrderDetail> findByOrderId(Long ordNum) {
        return orderDetailDAO.findByOrderId(ordNum);
    }

    @Override
    public void saveOrderDetail(OrderDetail detail) {
        orderDetailDAO.save(detail);
    }

    @Override
    public void deleteOrderDetail(OrderDetail detail) {
        orderDetailDAO.delete(detail);
    }
    @Override
    public boolean existsByOrdNumAndProdNum(Long ordNum, Long prodNum) {
        return orderDetailDAO.existsByOrdNumAndProdNum(ordNum, prodNum);
    }

}
