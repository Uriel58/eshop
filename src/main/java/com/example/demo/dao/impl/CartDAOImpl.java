package com.example.demo.dao.impl;

import com.example.demo.dao.CartDAO;
import com.example.demo.model.Cart;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartDAOImpl implements CartDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Cart findById(Long id) {
        return getSession().get(Cart.class, id);
    }

    @Override
    public List<Cart> findByCustomerId(Long customerId) {
        String hql = "FROM Cart WHERE customer.customerId = :customerId AND order IS NULL";
        return getSession().createQuery(hql, Cart.class)
                .setParameter("customerId", customerId)
                .list();
    }

    @Override
    public void save(Cart cart) {
        getSession().saveOrUpdate(cart);
    }

    @Override
    public void update(Cart cart) {
        getSession().update(cart);
    }

    @Override
    public void clearCartByCustomerId(Long customerId) {
        String hql = "UPDATE Cart SET savedForLate = true WHERE customer.customerId = :customerId AND order IS NULL";
        getSession().createQuery(hql)
                .setParameter("customerId", customerId)
                .executeUpdate();
    }
}
