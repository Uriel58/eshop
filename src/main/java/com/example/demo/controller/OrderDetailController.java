package com.example.demo.controller;

import com.example.demo.model.OrderDetail;
import com.example.demo.model.User;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.UserService;
import com.example.demo.service.ProductService;
import com.example.demo.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({ "customerId", "name" })
@RequestMapping("/orderDetails")
public class OrderDetailController extends LoginBaseController {

	@Autowired
	private OrderDetailService orderDetailService;
	
	@Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;

	// 列出指定 Order 的 OrderDetail
	@GetMapping("/{orderId}")
	public String getOrderDetails(HttpSession session, @PathVariable("orderId") Long orderId, Model model) {
		// 檢查是否登入
        Long userId = (Long) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // 檢查是否為 customer (customer 不能進入)
        User user = userService.getUserById(userId);
        if (user == null || "customer".equals(user.getIdentifyName())) {
            return "redirect:/";
        }
        
		List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId);
		model.addAttribute("details", details);
		model.addAttribute("orderId", orderId);
		return "orderdetails";
	}

	// 顯示新增表單
    @GetMapping("/add/{orderId}")
    public String createOrderDetailForm(@PathVariable("orderId") Long orderId, Model model) {
        model.addAttribute("orderDetail", new OrderDetail());
        model.addAttribute("orderId", orderId);
        
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        
        return "add-orderdetail";
    }
    
    // API：根據產品ID獲取產品價格
    @GetMapping("/api/product/{productId}/price")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductPrice(@PathVariable("productId") Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                response.put("success", true);
                response.put("price", product.getProdPrice());
            } else {
                response.put("success", false);
                response.put("message", "Product not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
    // API：根據訂單ID獲取運費
    @GetMapping("/api/order/{orderId}/fare")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOrderFare(@PathVariable("orderId") Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = orderService.getOrderById(orderId);
            
            if (order != null) {
                String deliveryMethod = order.getDeliveryMethod();
                
                if (deliveryMethod == null || deliveryMethod.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "此訂單尚未設定運送方式");
                    response.put("deliveryMethod", null);
                    response.put("fare", BigDecimal.ZERO);
                    return ResponseEntity.ok(response);
                }
                
                BigDecimal fare = calculateFare(deliveryMethod);
                
                response.put("success", true);
                response.put("fare", fare);
                response.put("deliveryMethod", deliveryMethod);
            } else {
                response.put("success", false);
                response.put("message", "Order not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 計算運費的輔助方法
    private BigDecimal calculateFare(String deliveryMethod) {
        if (deliveryMethod == null) {
            return BigDecimal.ZERO;
        }
        
        String method = deliveryMethod.toLowerCase().trim();
        
        switch (method) {
            case "postal":
            case "郵寄":
                return new BigDecimal("12.00");
            case "store_delivery":
            case "超商取貨":
                return new BigDecimal("6.00");
            case "home_delivery":
            case "宅配到府":
                return new BigDecimal("24.00");
            case "pick_up":
            case "自取":
                return BigDecimal.ZERO;
            default:
                return new BigDecimal("0.00");
        }
    }

	@PostMapping("/save/{orderId}")
	public String saveOrderDetail(@PathVariable("orderId") Long orderId,
	        @ModelAttribute("orderDetail") OrderDetail orderDetail) {
	    Order order = new Order();
	    order.setOrdNum(orderId);
	    orderDetail.setOrder(order);
	    orderDetailService.saveOrderDetail(orderDetail);
	    return "redirect:/orderDetails/" + orderId;
	}

	// 顯示編輯表單
	@GetMapping("/edit/{orderId}/{productId}")
	public String editOrderDetailForm(@PathVariable("orderId") Long orderId, 
	                                   @PathVariable("productId") Long productId,
			                           Model model) {
		List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId);
		OrderDetail detail = details.stream()
		                             .filter(d -> d.getProdNum().equals(productId))
		                             .findFirst()
		                             .orElse(null);
		
		if (detail == null) {
	        return "redirect:/orderDetails/" + orderId;
	    }
		
		if (detail.getOrder() == null) {
	        Order order = new Order();
	        order.setOrdNum(orderId);
	        detail.setOrder(order);
	    } else if (detail.getOrder().getOrdNum() == null) {
	        detail.getOrder().setOrdNum(orderId);
	    }
	    
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("detail", detail);
        
		return "edit-orderdetail";
	}

	// 更新 OrderDetail
	@PostMapping("/update/{orderId}/{productId}")
	public String updateOrderDetail(@PathVariable("orderId") Long orderId, 
	                                 @PathVariable("productId") Long productId,
			                         @ModelAttribute("detail") OrderDetail detail) {
	    Order order = new Order();
	    order.setOrdNum(orderId);
	    detail.setOrder(order);
		detail.setProdNum(productId);
		orderDetailService.saveOrderDetail(detail);
		return "redirect:/orderDetails/" + orderId;
	}

	// 刪除 OrderDetail
	@GetMapping("/delete/{orderId}/{productId}")
	public String deleteOrderDetail(@PathVariable("orderId") Long orderId, 
	                                 @PathVariable("productId") Long productId) {
		orderDetailService.deleteOrderDetail(orderId, productId);
		return "redirect:/orderDetails/" + orderId;
	}
}