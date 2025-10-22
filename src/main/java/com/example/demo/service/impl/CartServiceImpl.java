package com.example.demo.service.impl;

import com.example.demo.dao.CartDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public Cart findById(Long id) {
        return cartDAO.findById(id);
    }

    @Override
    public List<Cart> getCartByCustomerId(Long customerId) {
        return cartDAO.findByCustomerId(customerId);
    }

    @Override
    public void addToCart(Long customerId, Product product, int quantity) {
        List<Cart> existingCarts = cartDAO.findByCustomerId(customerId);
        Cart existing = existingCarts.stream()
                .filter(c -> c.getProduct().getProdNum().equals(product.getProdNum()) && c.getOrder() == null)
                .findFirst()
                .orElse(null);

        if (existing != null) {
            // 更新數量（這裡你可自行定義 cart 裡有沒有數量欄位）
            // 或當作純商品紀錄，更新時間即可
            existing.setUpdatedAt(null); // 觸發 @PreUpdate
            cartDAO.update(existing);
        } else {
            Cart newCart = new Cart();
            Customer customer = customerDAO.findById(customerId);

            newCart.setCustomer(customer);
            newCart.setProduct(product);
            newCart.setSavedForLater(false);
            cartDAO.save(newCart);
        }
    }

    @Override
    public void updateCartQuantity(Long cartId, int newQuantity) {
        // 數量實際存在 CartDetail，這裡只更新時間、驗證邏輯等
        Cart cart = cartDAO.findById(cartId);
        if (cart != null) {
            cart.setUpdatedAt(null); // @PreUpdate
            cartDAO.update(cart);
        }
    }

    @Override
    public void markAsSavedForLater(Long cartId) {
        Cart cart = cartDAO.findById(cartId);
        if (cart != null) {
            cart.setSavedForLater(true);
            cartDAO.update(cart);
        }
    }

    @Override
    public void clearCart(Long customerId) {
        cartDAO.clearCartByCustomerId(customerId);
    }
}
