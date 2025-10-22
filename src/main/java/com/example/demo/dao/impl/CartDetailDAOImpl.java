package com.example.demo.dao.impl;

import com.example.demo.dao.CartDetailDAO;
import com.example.demo.model.CartDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CartDetailDAOImpl implements CartDetailDAO {
	
	@PersistenceContext
    private EntityManager entityManager;
	
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public CartDetail findById(Long id) {
        return getSession().get(CartDetail.class, id);
    }

    @Override
    public List<CartDetail> findByCustomerId(Long customerId) {
        String hql = "FROM CartDetail WHERE customer.customerId = :customerId AND order IS NULL";
        return getSession().createQuery(hql, CartDetail.class)
                .setParameter("customerId", customerId)
                .list();
    }

    @Override
    public void save(CartDetail cartDetail) {
        getSession().saveOrUpdate(cartDetail);
    }

    @Override
    public void update(CartDetail cartDetail) {
        getSession().update(cartDetail);
    }

    @Override
    public void deleteById(Long id) {
        CartDetail cd = findById(id);
        if (cd != null) {
            getSession().delete(cd);
        }
    }

    @Override
    public void deleteAllByCustomerId(Long customerId) {
        String hql = "DELETE FROM CartDetail WHERE customer.customerId = :customerId AND order IS NULL";
        getSession().createQuery(hql)
                .setParameter("customerId", customerId)
                .executeUpdate();
    }
    
    @Override
    public void deleteByCustomerIdAndProductId(Long customerId, Long productId) {
        entityManager.createQuery("DELETE FROM CartDetail cd WHERE cd.customer.customerId = :customerId AND cd.product.prodNum = :productId")
            .setParameter("customerId", customerId)
            .setParameter("productId", productId)
            .executeUpdate();
    }
}
