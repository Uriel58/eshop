package com.example.demo.service;

import com.example.demo.model.CartDetail;
import java.util.List;

public interface CartDetailService {
    CartDetail getDetail(Long id);
    List<CartDetail> getAllDetails();
    void saveDetail(CartDetail detail);
    void updateDetail(CartDetail detail);
    void deleteDetail(Long id);
    List<CartDetail> getDetailsByCart(Long cartId);
}