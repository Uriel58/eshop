package com.example.demo.dao.impl;

import com.example.demo.dao.OrderDetailDAO;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderDetailPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return currentSession()
                .createQuery("from OrderDetail where order.id = :orderId", OrderDetail.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public void save(OrderDetail orderDetail) {
        currentSession().saveOrUpdate(orderDetail);
    }

    @Override
    public void delete(Long orderId, Long productId) {
        OrderDetailPK pk = new OrderDetailPK(orderId, productId);
        OrderDetail orderDetail = currentSession().get(OrderDetail.class, pk);
        if (orderDetail != null) {
            currentSession().delete(orderDetail);
        }
    }
}
