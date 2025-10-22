package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;

import java.util.List;

public interface CartService {
    Cart findById(Long id);
    List<Cart> getCartByCustomerId(Long customerId);
    void addToCart(Long customerId, Product product, int quantity);
    void updateCartQuantity(Long cartId, int newQuantity);
    void markAsSavedForLater(Long cartId);
    void clearCart(Long customerId); // 設為 savedForLater=true
}
