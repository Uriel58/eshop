package com.example.demo.dao.impl;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDAOImpl implements CategoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Category category) {
        getCurrentSession().save(category);
    }

    @Override
    public void update(Category category) {
        getCurrentSession().update(category);
    }

    @Override
    public void delete(Category category) {
        getCurrentSession().delete(category);
    }

    @Override
    public Category findById(Long id) {
        return getCurrentSession().get(Category.class, id);
    }

    @Override
    public List<Category> findAll() {
        return getCurrentSession().createQuery("from Category", Category.class).getResultList();
    }
}
