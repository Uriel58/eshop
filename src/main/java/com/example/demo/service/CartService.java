package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;

import java.util.List;
import java.math.BigDecimal;

public interface CartService {
    Cart findById(Long id);
    List<Cart> getCartByCustomerId(Long customerId);
    void addToCart(Long customerId, Product product, int quantity);
    void updateCartQuantity(Long cartId, int newQuantity);
    void markAsSavedForLater(Long cartId);
    void clearCart(Long customerId); // 設為 savedForLater=true
    
    // 新增的方法（為了支援 controller）
    void addProductToCart(Long customerId, Long productId, int quantity);  // <-- 新增
    void removeCart(Long cartId); // <-- 對應 controller 的 remove
    BigDecimal calculateCartTotal(Long customerId); // <-- 對應 controller 的 cart total
    boolean checkout(Long customerId, String paymentMethod, String deliveryMethod); // <-- 對應 controller 的 checkout
}
