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

	// 篩選
	@Override
	public List<String> findAllProdTypes() {
		return getCurrentSession().createQuery("SELECT DISTINCT c.prodType FROM Category c", String.class).list();
	}

	@Override
	public List<String> findProdLinesByProdType(String prodType) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c.prodLine FROM Category c WHERE c.prodType = :prodType", String.class)
				.setParameter("prodType", prodType).list();
	}

	@Override
	public List<String> findDescriptionsByProdTypeAndProdLine(String prodType, String prodLine) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c.description FROM Category c WHERE c.prodType = :prodType AND c.prodLine = :prodLine",
				String.class).setParameter("prodType", prodType).setParameter("prodLine", prodLine).list();
	}
}
