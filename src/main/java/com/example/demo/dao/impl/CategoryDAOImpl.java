package com.example.demo.dao.impl;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.hibernate.query.Query;

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
		String hql = "SELECT DISTINCT c.prodType FROM Category c ORDER BY c.prodType";
		return getCurrentSession().createQuery(hql, String.class).list();
	}

	@Override
	public List<String> findProdLinesByProdType(String prodType) {
		String hql = "SELECT DISTINCT c.prodLine FROM Category c WHERE c.prodType=:prodType ORDER BY c.prodLine";
		return getCurrentSession().createQuery(hql, String.class).setParameter("prodType", prodType).list();
	}

	@Override
	public List<String> findDescriptionsByProdTypeAndProdLine(String prodType, String prodLine) {
		String hql = "SELECT DISTINCT c.description FROM Category c WHERE c.prodType=:prodType AND c.prodLine=:prodLine ORDER BY c.description";
		return getCurrentSession().createQuery(hql, String.class).setParameter("prodType", prodType)
				.setParameter("prodLine", prodLine).list();
	}

	@Override
	public List<Product> findProductsByCategoryFilter(String prodType, String prodLine, String description) {
		StringBuilder hql = new StringBuilder("FROM Product p WHERE 1=1");
		if (prodType != null && !prodType.isEmpty())
		    hql.append(" AND p.category.prodType = :prodType");
		if (prodLine != null && !prodLine.isEmpty())
		    hql.append(" AND p.category.prodLine = :prodLine");
		if (description != null && !description.isEmpty())
		    hql.append(" AND p.category.description = :description");

		Query<Product> query = getCurrentSession().createQuery(hql.toString(), Product.class);
		if (prodType != null && !prodType.isEmpty())
		    query.setParameter("prodType", prodType);
		if (prodLine != null && !prodLine.isEmpty())
		    query.setParameter("prodLine", prodLine);
		if (description != null && !description.isEmpty())
		    query.setParameter("description", description);

		return query.list();
	}
}
