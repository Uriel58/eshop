package com.example.demo.service;

import com.example.demo.model.Cart;
import java.util.List;

public interface CartService {
    Cart getCart(Long id);
    List<Cart> getAllCarts();
    void saveCart(Cart cart);
    void updateCart(Cart cart);
    void deleteCart(Long id);
    List<Cart> getCartsByCustomer(Long customerId);
    //送進cart->order,cartDeatil->orderDetail
    void addOrUpdateProduct(Long cartId, Long productId, int quantity, double price);
    void removeProduct(Long cartId, Long productId);
    void calculateTotal(Long cartId);
    void checkout(Long cartId); // 將 Cart -> Order
}

