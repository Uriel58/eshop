package com.example.demo.dao.impl;

import com.example.demo.model.Cart;
import com.example.demo.dao.CartDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
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
    public List<Cart> findAll() {
        return getSession().createQuery("from Cart", Cart.class).list();
    }

    @Override
    public void save(Cart cart) {
        getSession().save(cart);
    }

    @Override
    public void update(Cart cart) {
        getSession().update(cart);
    }

    @Override
    public void delete(Long id) {
        Cart cart = findById(id);
        if (cart != null) {
            getSession().delete(cart);
        }
    }
    @Override
    public List<Cart> findByCustomerId(Long customerId) {
        return getSession()
                .createQuery("FROM Cart c WHERE c.customer.customerId = :customerId", Cart.class)
                .setParameter("customerId", customerId)
                .list();
    }
    /*@Override
    public Cart findByCustomerIdWithDetails(Long customerId) {
        return getSession()
                .createQuery(
                    "SELECT c FROM Cart c " +
                    "JOIN FETCH c.cartDetails cd " +
                    "JOIN FETCH cd.product " +
                    "WHERE c.customer.customerId = :customerId", 
                    Cart.class
                )
                .setParameter("customerId", customerId)
                .uniqueResult(); // 因為一個顧客通常只有一個 Cart
    }*/
}
