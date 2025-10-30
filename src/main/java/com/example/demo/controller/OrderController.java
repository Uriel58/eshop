package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
@SessionAttributes({"customerId", "name"})
public class OrderController extends LoginBaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        // ✅ 強制載入 orderDetails
        /*for (Order order : orders) {
            order.getOrderDetails().size(); // 觸發 Hibernate 載入
        }*/
        model.addAttribute("orders", orders);
        return "orders";  // 对应的 html 页面
    }
    
    @GetMapping("/add")
    public String createOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "add-order";  // 创建新订单的表单页面
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute("order") Order order) {
        
    	orderService.saveOrder(order);
        return "redirect:/orders";  // 保存后跳转到订单列表页面
    }

    @GetMapping("/edit/{orderId}")
    public String editOrderForm(@PathVariable("orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            // 處理找不到訂單的情況
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "edit-order";  // 编辑订单的表单页面
    }
    
    @PostMapping("/update/{orderId}")
    public String updateOrder(@PathVariable("orderId") Long orderId,@ModelAttribute("order") Order order) {
    	Order existing = orderService.getOrderById(orderId);
        if (existing != null) {
        	order.setOrdNum(existing.getOrdNum());  // 強制設定 ID
            order.setOrdDate(existing.getOrdDate());
            // ID 設定好，Hibernate 才能 update
            order.setOrdNum(existing.getOrdNum());
        }
    	orderService.updateOrder(order);
        return "redirect:/orders";  // 更新后跳转到订单列表页面
    }

    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/orders";  // 删除后跳转到订单列表页面
    }
    @GetMapping("/deleteDetail/{orderId}/{productId}")
    public String deleteOrderDetail(@PathVariable("orderId") Long orderId,
                                    @PathVariable("productId") Long productId) {
        orderDetailService.deleteOrderDetail(orderId, productId);
        return "redirect:/orders/" + orderId;  // 删除订单项后返回到该订单的详情页面
    }

    @GetMapping("/details/{orderId}")
    public String getOrderById(@PathVariable("orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
            model.addAttribute("order", order);
            model.addAttribute("orderDetails", orderDetails);
        } else {
            model.addAttribute("order", null);
            model.addAttribute("orderDetails", null);
        }
        return "orderdetails";  // 顯示單個訂單的詳細頁面
    }
    
}
