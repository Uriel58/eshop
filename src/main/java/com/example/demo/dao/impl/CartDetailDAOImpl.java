package com.example.demo.dao.impl;

import com.example.demo.model.CartDetail;
import com.example.demo.dao.CartDetailDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CartDetailDAOImpl implements CartDetailDAO {

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
    public List<CartDetail> findAll() {
        return getSession().createQuery("from CartDetail", CartDetail.class).list();
    }

    @Override
    public void save(CartDetail detail) {
        getSession().save(detail);
    }

    @Override
    public void update(CartDetail detail) {
        getSession().update(detail);
    }

    @Override
    public void delete(Long id) {
        CartDetail detail = findById(id);
        if (detail != null) {
            getSession().delete(detail);
        }
    }

    @Override
    public List<CartDetail> findByCartId(Long cartId) {
        return getSession()
                .createQuery("from CartDetail d where d.cart.id = :cartId", CartDetail.class)
                .setParameter("cartId", cartId)
                .list();
    }
}
