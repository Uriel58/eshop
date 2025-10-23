package com.example.demo.dao.impl;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private OrderDAO orderDAO;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @PersistenceContext
    private EntityManager em;
    
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
    public Optional<Order> findByIdWithDetails(Long ordNum) {
        String jpql = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.ordNum = :id";
        TypedQuery<Order> query = em.createQuery(jpql, Order.class);
        query.setParameter("id", ordNum);
        Order order = null;
        try {
            order = query.getSingleResult();
        } catch (Exception e) {
            // 沒有找到或其他錯誤，可以記錄
        }
        return Optional.ofNullable(order);
    }
}
