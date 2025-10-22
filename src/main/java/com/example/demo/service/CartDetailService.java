package com.example.demo.service;

import com.example.demo.model.CartDetail;

import java.util.List;
import java.math.BigDecimal;

public interface CartDetailService {
    CartDetail findById(Long id);
    List<CartDetail> getCartDetailsByCustomerId(Long customerId);
    void saveCartDetail(CartDetail cartDetail);
    void updateCartDetail(CartDetail cartDetail);
    void deleteCartDetail(Long id); // optional
    void deleteAllByCustomerId(Long customerId); // after checkout
    BigDecimal calculateCartTotal(Long customerId);
}
