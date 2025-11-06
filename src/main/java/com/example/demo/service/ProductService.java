package com.example.demo.service;

import com.example.demo.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long prodNum);
    void saveProduct(Product product);
    void updateProduct(Long prodNum, Product updatedProduct);
    void deleteProduct(Long prodNum);
    Product findByBarcode(String barcode);
    // ✅ 加這一行，讓 ProductServiceImpl 能呼叫
    List<Product> findByKeyword(String keyword);
    
    Product findByProdNum(Long prodNum);
    //產品包含篩選
    Product getProductByIdWithCategory(Long prodNum);
}
