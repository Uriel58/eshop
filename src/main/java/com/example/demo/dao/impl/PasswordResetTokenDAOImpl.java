package com.example.demo.dao.impl;

import com.example.demo.dao.PasswordResetTokenDAO;
import com.example.demo.model.PasswordResetToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {

    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(PasswordResetToken token) {
        sessionFactory.getCurrentSession().save(token);
    }
    
    @Override
    public PasswordResetToken findByToken(String token) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM PasswordResetToken t WHERE t.token = :token", PasswordResetToken.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(PasswordResetToken token) {
        sessionFactory.getCurrentSession().update(token);
    }

    @Override
    public void delete(PasswordResetToken token) {
        var session = sessionFactory.getCurrentSession();
        if (!session.contains(token)) {
        	token = (PasswordResetToken) session.merge(token); // merge 讓 Hibernate 管理 detached entity
        }else {
            System.out.println("⚠️ Token not found, skip delete: " + token.getId());
        }
        session.delete(token);
    }
    
 // ✅ 新增這個方法，解決錯誤
    @Override
    public List<PasswordResetToken> findAll() {
        String hql = "FROM PasswordResetToken";
        return getCurrentSession().createQuery(hql, PasswordResetToken.class).list();
    }
}
