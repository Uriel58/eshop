package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartDetail;
import com.example.demo.service.CartService;
import com.example.demo.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartDetailService cartDetailService;

	// 顯示所有購物車
	@GetMapping("/cart-list")
	public String listCarts(Model model) {
		List<Cart> carts = cartService.getAllCarts();
		model.addAttribute("carts", carts);
		return "cart-list"; // 導向到 cart-list.html
	}

	// 根據 customer 查購物車
	@GetMapping("/customer/{customerId}")
	public String getCartByCustomer(@PathVariable Long customerId, Model model) {
		List<Cart> carts = cartService.getCartsByCustomer(customerId);
		model.addAttribute("carts", carts);
		return "customerCart";// 導向到 customerCart.html
	}

	// 新增購物車
	@PostMapping("/add")
	public String addCart(@ModelAttribute Cart cart) {
		cartService.saveCart(cart);
		return "redirect:/cart/cart-list";// 導向到 cart-list.html
	}

	// 刪除購物車
	@GetMapping("/delete/{id}")
	public String deleteCart(@PathVariable Long id) {
		cartService.deleteCart(id);
		return "redirect:/cart/cart-list";// 導向到 cart-list.html
	}

	//實做購物車功能，顧客使用的前端
	// 顯示購物車明細
    @GetMapping("/{cartId}/cartdetails")
    public String viewCartDetails(@PathVariable Long cartId, Model model) {
        Cart cart = cartService.getCart(cartId);
        model.addAttribute("cart", cart);
        List<CartDetail> details = cartDetailService.getDetailsByCart(cartId);
        model.addAttribute("details", cart.getCartDetails()); // 顯示購物車明細
        return "cartdetails"; // 導向到 cartdetails.html
    }
	// 加入購物車
	@PostMapping("/{cartId}/add-product")
	public String addProduct(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity,
			@RequestParam double price) {
		cartService.addOrUpdateProduct(cartId, productId, quantity, price);
		return "redirect:/cart/" + cartId + "/cartdetails";
	}
	
	// 更新購物車
	@PostMapping("/{cartId}/update-product")
    public String updateProduct(@PathVariable Long cartId, 
                                @RequestParam Long productId, 
                                @RequestParam int quantity) {
        cartService.addOrUpdateProduct(cartId, productId, quantity, 0); // 0代表單價不變
        return "redirect:/cart/" + cartId + "/cartdetails";
    }
	//刪除購物車的產品
	@GetMapping("/{cartId}/remove-product/{productId}")
	public String removeProduct(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.removeProduct(cartId, productId);
        return "redirect:/cart/" + cartId + "/cartdetails";
    }
	//送出訂單
	@PostMapping("/{cartId}/checkout")
    public String checkout(@PathVariable Long cartId) {
        cartService.checkout(cartId);
        return "redirect:/order/order-list"; // 結帳完成後跳轉到訂單列表
    }
}
