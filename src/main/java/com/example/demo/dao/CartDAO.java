package com.example.demo.dao;

import com.example.demo.model.Cart;

import java.util.List;

public interface CartDAO {
    Cart findById(Long id);
    List<Cart> findByCustomerId(Long customerId);
    void save(Cart cart); // 可新增或更新
    void update(Cart cart);
    void clearCartByCustomerId(Long customerId); // 清空顧客的購物車
}
