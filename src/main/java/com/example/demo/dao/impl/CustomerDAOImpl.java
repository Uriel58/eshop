package com.example.demo.dao.impl;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Customer> findAll() {
        return getCurrentSession().createQuery("FROM Customer", Customer.class).list();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Customer customer = getCurrentSession().get(Customer.class, id);
        return Optional.ofNullable(customer);  // 返回 Optional 來處理可能的 null 值
    }

    @Override
    public void save(Customer customer) {
        getCurrentSession().saveOrUpdate(customer);
    }

    @Override
    public void delete(Long customerId) {
        Customer customer = getCurrentSession().get(Customer.class, customerId);
        if (customer != null) {
            getCurrentSession().delete(customer);
        }
    }

    @Override
    public Customer findByEmail(String email) {
        String hql = "FROM Customer WHERE email = :email";
        return getCurrentSession()
                .createQuery(hql, Customer.class)
                .setParameter("email", email)
                .uniqueResult();
    }
    @Override
    public Customer findByUserId(Long userId) {
        String hql = "FROM Customer c WHERE c.user.id = :userId";
        return getCurrentSession()
                .createQuery(hql, Customer.class)
                .setParameter("userId", userId)
                .uniqueResult();
    }
}
