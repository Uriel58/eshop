package com.example.demo.dao;

import com.example.demo.model.CartDetail;
import java.util.List;

public interface CartDetailDAO {
    CartDetail findById(Long id);
    List<CartDetail> findAll();
    void save(CartDetail detail);
    void update(CartDetail detail);
    void delete(Long id);
    List<CartDetail> findByCartId(Long cartId);
}
