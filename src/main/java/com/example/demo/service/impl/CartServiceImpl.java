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
import com.example.demo.dao.ProductDAO;
import com.example.demo.service.CartDetailService;
import com.example.demo.service.OrderService;

import java.util.List;
import java.math.BigDecimal;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private CustomerDAO customerDAO;
    
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CartDetailService cartDetailService;
    
    @Autowired
    private OrderService orderService;
    
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
    
    @Override
    public void addProductToCart(Long customerId, Long productId, int quantity) {
        Product product = productDAO.findById(productId);
        if (product != null) {
            // 加入 Cart table
            addToCart(customerId, product, quantity);

            // 加入 CartDetail table
            cartDetailService.addOrUpdateCartDetail(customerId, product, quantity);
        }
    }
    
    @Override
    public void removeCart(Long cartId) {
        Cart cart = cartDAO.findById(cartId);
        if (cart != null) {
            // 建議標記為 savedForLater，或加上刪除 flag，而不是直接刪除
            cart.setSavedForLater(true);
            cartDAO.update(cart);
        }
    }

    @Override
    public BigDecimal calculateCartTotal(Long customerId) {
        return cartDetailService.calculateCartTotal(customerId);
    }

    @Override
    public boolean checkout(Long customerId, String paymentMethod, String deliveryMethod) {
        // 這邊要你另外實作：建立 Order 和 OrderDetail 的邏輯
        // 並關聯 cart 與 cartDetail 到 orderId，然後更新狀態為已下單
        // 假設你有 OrderService 的話可以調用：
        
        // Example only
        try {
            orderService.createOrderFromCart(customerId, paymentMethod, deliveryMethod);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
