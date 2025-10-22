package com.example.demo.dao.impl;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Order> findAll() {
        return getCurrentSession().createQuery("FROM Order", Order.class).list();
    }

    @Override
    public Order findById(Long ordNum) {
        return getCurrentSession().get(Order.class, ordNum);
    }

    @Override
    public void save(Order order) {
        getCurrentSession().saveOrUpdate(order);
    }

    @Override
    public void delete(Long ordNum) {
        Order order = findById(ordNum);
        if (order != null) {
            getCurrentSession().delete(order);
        }
    }
}
