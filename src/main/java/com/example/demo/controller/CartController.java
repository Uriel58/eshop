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
import java.math.BigDecimal;

@Controller
@SessionAttributes({ "id", "name" })
@RequestMapping("/cart")
public class CartController extends LoginBaseController {

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
	/*
	 * @GetMapping("/customer/{customerId}") public String
	 * getCartByCustomer(@PathVariable Long customerId, Model model) { List<Cart>
	 * carts = cartService.getCartsByCustomer(customerId);
	 * model.addAttribute("carts", carts); return "customerCart"; }
	 * 
	 * // 新增購物車
	 * 
	 * @PostMapping("/add") public String addCart(@ModelAttribute Cart cart) {
	 * cartService.saveCart(cart); return "redirect:/cart/cart-list"; }
	 */

	// 刪除購物車
	/*
	 * @GetMapping("/delete/{id}") public String deleteCart(@PathVariable Long id) {
	 * cartService.deleteCart(id); return "redirect:/cart/cart-list"; }
	 */

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
	@PostMapping("/create-and-add-product")
	public String createCartAndAddProduct(@RequestParam(required = false) Long customerId, // 允许为空，如果为空则从会话中获取
			@RequestParam Long productId, @RequestParam int quantity, @RequestParam double price,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		// 如果没有传递 customerId，尝试从会话中获取
		if (customerId == null) {
			customerId = (Long) request.getSession().getAttribute("id");
		}

		// 如果还是没有 customerId，重定向到登录页
		if (customerId == null) {
			return "redirect:/login"; // 确保用户已经登录
		}

		// 根据 customerId 查找顾客
		Customer customer = customerService.getCustomerById(customerId);
		if (customer == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "找不到该顾客!");
			return "redirect:/error"; // 如果顾客不存在，重定向到错误页面
		}

		// 查找顾客的购物车
		Cart cart = cartService.getCartByCustomerId(customerId);  // 修改为通过 findByCustomerId 查找购物车	

		// 如果顾客已经有购物车，直接使用现有购物车
		if (cart == null) {
			 // 如果顾客没有购物车，则创建一个新的购物车
	        cart = new Cart();
	        cart.setCustomer(customer); // 显式地设置 customer 到 cart

	        // 将购物车添加到顾客的购物车列表
	        customer.addCart(cart); // 如果有双向关系

	        // 保存顾客和购物车（这会自动保存关联的购物车）
	        customerService.saveCustomer(customer);
		}

		// 获取商品信息
		Product product = productService.getProductById(productId);
		if (product == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "找不到该商品!");
			return "redirect:/error"; // 如果商品不存在，重定向到错误页面
		}

		// 创建新的购物车详情 (CartDetail)
		CartDetail cartDetail = new CartDetail();
		cartDetail.setCart(cart); // 将购物车与购物车详情关联
		cartDetail.setProduct(product); // 将商品与购物车详情关联
		cartDetail.setProdPrice(BigDecimal.valueOf(price)); // 设置商品价格
		cartDetail.setCartQty(quantity); // 设置商品数量
		cartDetail.setShippingFee(BigDecimal.ZERO); // 设置运费（假设暂时为零）
		cartDetail.setCartTotal(BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(quantity))); // 计算总价

		// 将购物车详情添加到购物车
		cart.addCartDetail(cartDetail); // 如果 Cart 类有 addCartDetail 方法

		// 保存购物车详情（这会自动保存关联的 CartDetail）
		cartService.saveCartDetail(cartDetail); // 保存 CartDetail 对象

		// 重定向并显示成功消息
		redirectAttributes.addFlashAttribute("cartMessage", "🛒 商品已加入购物车！");
		return "redirect:/products/details/" + productId;
	}

	/*
	 * @PostMapping("/create-and-add-product") public String
	 * createCartAndAddProduct(
	 * 
	 * @RequestParam(required = false) Long customerId, // 允许为空，如果为空则从会话中获取
	 * 
	 * @RequestParam Long productId,
	 * 
	 * @RequestParam int quantity,
	 * 
	 * @RequestParam double price, HttpServletRequest request, RedirectAttributes
	 * redirectAttributes) {
	 * 
	 * // 如果没有传递 customerId，尝试从会话中获取 if (customerId == null) { customerId = (Long)
	 * request.getSession().getAttribute("id"); }
	 * 
	 * // 如果还是没有 customerId，重定向到登录页 if (customerId == null) { return
	 * "redirect:/login"; // 确保用户已经登录 }
	 * 
	 * // 根据 customerId 查找顾客 Customer customer =
	 * customerService.getCustomerById(customerId); if (customer == null) {
	 * redirectAttributes.addFlashAttribute("errorMessage", "找不到该顾客!"); return
	 * "redirect:/error"; // 如果顾客不存在，重定向到错误页面 }
	 * 
	 * // 创建新的购物车并将其与顾客关联 Cart cart = new Cart(); cart.setCustomer(customer); //
	 * 显式地设置 customer 到 cart
	 * 
	 * // 将购物车添加到顾客的购物车列表 customer.addCart(cart); // 如果有双向关系
	 * 
	 * // 保存顾客和购物车（这会自动保存关联的购物车） customerService.saveCustomer(customer);
	 * 
	 * // 获取商品信息 Product product = productService.getProductById(productId); if
	 * (product == null) { redirectAttributes.addFlashAttribute("errorMessage",
	 * "找不到该商品!"); return "redirect:/error"; // 如果商品不存在，重定向到错误页面 }
	 * 
	 * // 创建新的购物车详情 (CartDetail) CartDetail cartDetail = new CartDetail();
	 * cartDetail.setCart(cart); // 将购物车与购物车详情关联 cartDetail.setProduct(product); //
	 * 将商品与购物车详情关联 cartDetail.setProdPrice(BigDecimal.valueOf(price)); // 设置商品价格
	 * cartDetail.setCartQty(quantity); // 设置商品数量
	 * cartDetail.setShippingFee(BigDecimal.ZERO); // 设置运费（假设暂时为零）
	 * cartDetail.setCartTotal(BigDecimal.valueOf(price).multiply(BigDecimal.valueOf
	 * (quantity))); // 计算总价
	 * 
	 * // 将购物车详情添加到购物车 cart.addCartDetail(cartDetail); // 如果 Cart 类有 addCartDetail
	 * 方法
	 * 
	 * // 保存购物车详情（这会自动保存关联的 CartDetail） cartService.saveCartDetail(cartDetail); //
	 * 保存 CartDetail 对象
	 * 
	 * // 重定向并显示成功消息 redirectAttributes.addFlashAttribute("cartMessage",
	 * "🛒 新购物车已创建并加入商品！"); return "redirect:/products/details/" + productId; }
	 * 
	 * 
	 * 
	 * // 加入購物車（已有購物車）
	 * 
	 * @PostMapping("/add-product/{cartId}") public String
	 * addProductToCart(@PathVariable Long cartId, @RequestParam Long
	 * productId, @RequestParam int quantity,
	 * 
	 * @RequestParam double price, RedirectAttributes redirectAttributes) {
	 * 
	 * cartService.addOrUpdateProduct(cartId, productId, quantity, price);
	 * redirectAttributes.addFlashAttribute("cartMessage", "✅ 商品已加入購物車！"); return
	 * "redirect:/cart/product/" + productId; }
	 */
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
