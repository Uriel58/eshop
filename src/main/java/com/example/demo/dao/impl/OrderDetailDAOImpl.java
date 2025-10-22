package com.example.demo.dao.impl;

import com.example.demo.dao.OrderDetailDAO;
import com.example.demo.model.OrderDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<OrderDetail> findByOrderId(Long ordNum) {
        String hql = "FROM OrderDetail WHERE ordNum = :ordNum";
        return getCurrentSession()
                .createQuery(hql, OrderDetail.class)
                .setParameter("ordNum", ordNum)
                .list();
    }

    @Override
    public void save(OrderDetail detail) {
        getCurrentSession().saveOrUpdate(detail);
    }

    @Override
    public void delete(OrderDetail detail) {
        getCurrentSession().delete(detail);
    }
    @Override
    public boolean existsByOrdNumAndProdNum(Long ordNum, Long prodNum) {
        String hql = "SELECT count(*) FROM OrderDetail WHERE ordNum = :ordNum AND prodNum = :prodNum";
        Long count = getCurrentSession()
                .createQuery(hql, Long.class)
                .setParameter("ordNum", ordNum)
                .setParameter("prodNum", prodNum)
                .uniqueResult();
        return count != null && count > 0;
    }
}
