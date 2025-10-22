package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Product> findAll() {
        return getCurrentSession().createQuery("FROM Product", Product.class).list();
    }

    @Override
    public Product findById(Long prodNum) {
        return getCurrentSession().get(Product.class, prodNum);
    }

    @Override
    public void save(Product product) {
        getCurrentSession().saveOrUpdate(product);
    }

    @Override
    public void delete(Long prodNum) {
        Product product = getCurrentSession().get(Product.class, prodNum);
        if (product != null) {
            getCurrentSession().delete(product);
        }
    }

    @Override
    public Product findByBarcode(String barcode) {
        String hql = "FROM Product WHERE prodBarcode = :barcode";//barcode查找
        return getCurrentSession()
                .createQuery(hql, Product.class)
                .setParameter("barcode", barcode)
                .uniqueResult();
    }

    // ✅ ✅ ✅ 新增這個方法來解決報錯
    @Override
    public List<Product> findByKeyword(String keyword) {
        String hql = "FROM Product WHERE prodName LIKE :kw OR prodInfo LIKE :kw OR prodTags LIKE :kw";
        return getCurrentSession()
                .createQuery(hql, Product.class)
                .setParameter("kw", "%" + keyword + "%")
                .list();
    }
    @Override
    public Product findByProdNum(Long prodNum) {
        String hql = "FROM Product WHERE prodNum = :prodNum";
        return getCurrentSession()
                .createQuery(hql, Product.class)
                .setParameter("prodNum", prodNum)
                .uniqueResult();
    }
}
