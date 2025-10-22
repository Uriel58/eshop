package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartDetail;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@SessionAttributes("customerId")
public class CartController {

    @Autowired
    private CartService cartService;

    // 顯示購物車頁面
    @GetMapping
    public String viewCart(Model model, @SessionAttribute("customerId") Long customerId) {
        // 取得客戶購物車（多筆商品）
        List<Cart> cartItems = cartService.getCartByCustomerId(customerId);
        model.addAttribute("cartItems", cartItems);

        // 計算總價
        BigDecimal total = cartService.calculateCartTotal(customerId);
        model.addAttribute("total", total);

        return "cart"; // 對應 Thymeleaf 模板
    }

    // 新增商品到購物車
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            @SessionAttribute("customerId") Long customerId) {
        cartService.addProductToCart(customerId, productId, quantity);
        return "redirect:/cart";
    }

    // 更新購物車中商品數量
    @PostMapping("/update")
    public String updateCart(@RequestParam("cartId") Long cartId,
                             @RequestParam("quantity") int quantity) {
        cartService.updateCartQuantity(cartId, quantity);
        return "redirect:/cart";
    }

    // 刪除購物車中的商品
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cartId") Long cartId) {
        cartService.removeCart(cartId);
        return "redirect:/cart";
    }

    // 結帳
    @PostMapping("/checkout")
    public String checkout(@SessionAttribute("customerId") Long customerId,
                           @RequestParam("paymentMethod") String paymentMethod,
                           @RequestParam("deliveryMethod") String deliveryMethod,
                           Model model) {
        boolean success = cartService.checkout(customerId, paymentMethod, deliveryMethod);
        if (success) {
            return "redirect:/order/confirmation";
        } else {
            model.addAttribute("error", "結帳失敗，請稍後再試");
            return "cart";
        }
    }
}
