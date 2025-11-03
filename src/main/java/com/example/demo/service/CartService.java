package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.model.CartDetail;
import java.util.List;

public interface CartService {
    Cart getCart(Long id);
    List<Cart> getAllCarts();
    void saveCart(Cart cart);
    void updateCart(Cart cart);
    void deleteCart(Long id);
    List<Cart> getCartsByCustomer(Long customerId);
    //送進cart->order,cartDeatil->orderDetail
    void setProductQuantity(Long cartId, Long productId, int quantity);
    void removeProduct(Long cartId, Long productId);
    void saveCartDetail(CartDetail cartDetail); // 新增保存 CartDetail 的方法
    void calculateTotal(Long cartId);
    void checkout(Long cartId, Order orderForm); // 將 Cart -> Order
    // ✅ 新增這行
    Cart getCartByCustomerId(Long customerId);
}

