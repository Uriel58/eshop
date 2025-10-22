package com.example.demo.dao;

import com.example.demo.model.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> findAll();
    Product findById(Long prodNum);
    void save(Product product);
    void delete(Long prodNum);
    Product findByBarcode(String barcode);
    // ✅ 加上這行 ↓↓↓，PuductServiceImpl.java的findByKeyword(String keyword)
    List<Product> findByKeyword(String keyword);
    Product findByProdNum(Long prodNum);  // ⬅️ 加在這裡
}
