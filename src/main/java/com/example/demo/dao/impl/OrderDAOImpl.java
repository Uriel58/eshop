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

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Order> findAll() {
        return currentSession().createQuery("from Order", Order.class).getResultList();
    }

    @Override
    public Order findById(Long id) {
        return currentSession().get(Order.class, id);
    }

    @Override
    public void save(Order order) {
        currentSession().saveOrUpdate(order);
    }

    @Override
    public void update(Order order) {
        currentSession().update(order);
    }

    @Override
    public void delete(Long id) {
        Order order = findById(id);
        if (order != null) {
            currentSession().delete(order);
        }
    }
}
