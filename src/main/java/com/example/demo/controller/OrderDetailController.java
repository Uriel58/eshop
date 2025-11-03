package com.example.demo.controller;

import com.example.demo.model.OrderDetail;
import com.example.demo.model.Order;
import com.example.demo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({ "customerId", "name" })
@RequestMapping("/orderDetails")
public class OrderDetailController extends LoginBaseController {

	@Autowired
	private OrderDetailService orderDetailService;

	// 列出指定 Order 的 OrderDetail
	@GetMapping("/{orderId}")
	public String getOrderDetails(@PathVariable("orderId") Long orderId, Model model) {
		List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId);
		model.addAttribute("details", details);
		model.addAttribute("orderId", orderId);// ✅ 一定要有
		return "orderdetails"; // 對應 HTML
	}

	// 顯示新增表單
    @GetMapping("/add/{orderId}")
    public String createOrderDetailForm(@PathVariable("orderId") Long orderId, Model model) {
        model.addAttribute("orderDetail", new OrderDetail()); // 對應 th:object
        model.addAttribute("orderId", orderId);              // 用於生成 action URL
        return "add-orderdetail";
    }

	@PostMapping("/save/{orderId}")
	public String saveOrderDetail(@PathVariable("orderId") Long orderId,
	        @ModelAttribute("orderDetail") OrderDetail orderDetail) {
		// ✅ 建立一個暫時的 Order 物件，只設主鍵就行
	    Order order = new Order();
	    order.setOrdNum(orderId);
	    orderDetail.setOrder(order);
	    orderDetailService.saveOrderDetail(orderDetail);
	    return "redirect:/orderDetails/" + orderId;// 保存后跳转到订单列表页面
	}

	// 顯示編輯表單
	@GetMapping("/edit/{orderId}/{productId}")
	public String editOrderDetailForm(@PathVariable("orderId") Long orderId, @PathVariable("productId") Long productId,
			Model model) {
		List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(orderId);
		OrderDetail detail = details.stream().filter(d -> d.getProdNum().equals(productId)).findFirst().orElse(null);
		
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
		model.addAttribute("detail", detail);
		return "edit-orderdetail"; // 對應 HTML
	}

	// 更新 OrderDetail
	@PostMapping("/update/{orderId}/{productId}")
	public String updateOrderDetail(@PathVariable("orderId") Long orderId, @PathVariable("productId") Long productId,
			@ModelAttribute("detail") OrderDetail detail) {
		// 保證 ID 不會丟失
		 // ✅ 建立 Order 並關聯
	    Order order = new Order();
	    order.setOrdNum(orderId);
	    detail.setOrder(order);
		detail.setProdNum(productId);

		// 用 saveOrderDetail 代替 update
		orderDetailService.saveOrderDetail(detail);

		return "redirect:/orderDetails/" + orderId;
	}

	// 刪除 OrderDetail
	@GetMapping("/delete/{orderId}/{productId}")
	public String deleteOrderDetail(@PathVariable("orderId") Long orderId, @PathVariable("productId") Long productId) {
		orderDetailService.deleteOrderDetail(orderId, productId);
		return "redirect:/orderDetails/" + orderId;
	}
}
