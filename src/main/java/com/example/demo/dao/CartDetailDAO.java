package com.example.demo.dao;

import com.example.demo.model.CartDetail;

import java.util.List;

public interface CartDetailDAO {
    CartDetail findById(Long id);
    List<CartDetail> findByCustomerId(Long customerId);
    void save(CartDetail cartDetail); // 可新增或更新
    void update(CartDetail cartDetail);
    void deleteById(Long id); // 僅限特殊情況（不推薦）
    void deleteAllByCustomerId(Long customerId); // 結帳後清空
}
