package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartDetail;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CartDetailService;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@SessionAttributes({ "id", "name" })
@RequestMapping("/cart")
public class CartController extends LoginBaseController{

	@Autowired
	private CartService cartService;
	@Autowired
	private CartDetailService cartDetailService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;

	// 顯示所有購物車
	@GetMapping("/cart-list")
	public String listCarts(Model model) {
		List<Cart> carts = cartService.getAllCarts();
		model.addAttribute("carts", carts);
		return "cart-list";
	}

	// 根據 customer 查購物車
	@GetMapping("/customer/{customerId}")
	public String getCartByCustomer(@PathVariable Long customerId, Model model) {
		List<Cart> carts = cartService.getCartsByCustomer(customerId);
		model.addAttribute("carts", carts);
		return "customerCart";
	}

	// 新增購物車
	@PostMapping("/add")
	public String addCart(@ModelAttribute Cart cart) {
		cartService.saveCart(cart);
		return "redirect:/cart/cart-list";
	}

	// 刪除購物車
	@GetMapping("/delete/{id}")
	public String deleteCart(@PathVariable Long id) {
		cartService.deleteCart(id);
		return "redirect:/cart/cart-list";
	}

	// 顯示購物車明細
	@GetMapping("/{cartId}/cartdetails")
	public String viewCartDetails(@PathVariable Long cartId, Model model) {
		Cart cart = cartService.getCart(cartId);
		model.addAttribute("cart", cart);
		List<CartDetail> details = cartDetailService.getDetailsByCart(cartId);
		model.addAttribute("details", cart.getCartDetails());
		return "cartdetails";
	}

	// 顯示商品詳細頁（含登入檢查）
	@GetMapping("/product/{id}")
	public String viewProduct(@PathVariable Long id, @RequestParam(required = false) String success, Model model,
			HttpServletRequest request) {

		// 取得商品
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);

		// ✅ 直接從 session 取登入者
		// 直接從 session 取登入者
		Long userId = (Long) request.getSession().getAttribute("id");
		if (userId == null) {
		    return "redirect:/login"; // 尚未登入 → 導向登入頁
		}

		// ✅ 用 User ID 取得對應的 Customer
	    Customer currentCustomer = customerService.getCustomerByUserId(userId);
	    if (currentCustomer == null) {
	        return "redirect:/login"; // 防呆，確保有對應 Customer
	    }
	    model.addAttribute("customer", currentCustomer);

		// 取得該顧客的購物車
		// 取得或建立該顧客的購物車
	    Cart cart = cartService.getCartByCustomerId(currentCustomer.getCustomerId());
	    if (cart == null) {
	        // 尚未有購物車時，model 裡也可以放 null（頁面會判斷）
	        model.addAttribute("cart", null);
	    } else {
	        model.addAttribute("cart", cart);
	    }
		// 成功提示
		if ("true".equals(success)) {
			model.addAttribute("successMessage", "商品已成功加入購物車！");
		}
		
		// 將登入者資訊加入 model，方便顯示在頁面
	    model.addAttribute("id", currentCustomer.getCustomerId());
	    model.addAttribute("name", currentCustomer.getName());
		return "products_details";
	}

	// 加入購物車
	/*@PostMapping("/{cartId}/add-product")
	public String addProduct(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity,
			@RequestParam double price) {

		cartService.addOrUpdateProduct(cartId, productId, quantity, price);
		return "redirect:/cart/product/" + productId + "?success=true";
	}*/
	// 加入購物車（已有購物車）
	@PostMapping("/add-product/{cartId}")
	public String addProductToCart(@PathVariable Long cartId,
	                               @RequestParam Long productId,
	                               @RequestParam int quantity,
	                               @RequestParam double price,
	                               RedirectAttributes redirectAttributes) {

	    cartService.addOrUpdateProduct(cartId, productId, quantity, price);
	    redirectAttributes.addFlashAttribute("cartMessage", "✅ 商品已加入購物車！");
	    return "redirect:/cart/product/" + productId;
	}
	// 建立新購物車並加入商品
	@PostMapping("/add/{cartId}")
	public String createCartAndAddProduct(@RequestParam Long customerId,
	                                      @RequestParam Long productId,
	                                      @RequestParam int quantity,
	                                      @RequestParam double price,
	                                      RedirectAttributes redirectAttributes) {

	    // 建立新購物車
	    Cart cart = new Cart();
	    Customer customer = customerService.getCustomerById(customerId);
	    cart.setCustomer(customer);
	    cartService.saveCart(cart);

	    // 將商品加入新購物車
	    cartService.addOrUpdateProduct(cart.getId(), productId, quantity, price);

	    redirectAttributes.addFlashAttribute("cartMessage", "🛒 已建立新購物車並加入商品！");
	    return "redirect:/cart/product/" + productId;
	}

	// 更新購物車
	@PostMapping("/{cartId}/update-product")
	public String updateProduct(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {

		cartService.addOrUpdateProduct(cartId, productId, quantity, 0);
		return "redirect:/cart/" + cartId + "/cartdetails";
	}

	// 刪除購物車中的商品
	@GetMapping("/{cartId}/remove-product/{productId}")
	public String removeProduct(@PathVariable Long cartId, @PathVariable Long productId) {
		cartService.removeProduct(cartId, productId);
		return "redirect:/cart/" + cartId + "/cartdetails";
	}

	// 結帳
	@PostMapping("/{cartId}/checkout")
	public String checkout(@PathVariable Long cartId) {
		cartService.checkout(cartId);
		return "redirect:/order/order-list";
	}
}
