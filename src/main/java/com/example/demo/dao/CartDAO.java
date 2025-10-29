package com.example.demo.dao;

import com.example.demo.model.Cart;
import java.util.List;

public interface CartDAO {
    Cart findById(Long id);
    List<Cart> findAll();
    void save(Cart cart);
    void update(Cart cart);
    void delete(Long id);
    List<Cart> findByCustomerId(Long customerId);
}