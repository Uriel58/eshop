package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
@SessionAttributes({"customerId", "name"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    // ======== 訂單相關操作 ========

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/add")
    public String showAddOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "add-order";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order, RedirectAttributes redirectAttributes) {
        orderService.saveOrder(order);
        redirectAttributes.addFlashAttribute("success", "訂單新增成功！");
        return "redirect:/orders";
    }

    @GetMapping("/edit/{ordNum}")
    public String showEditForm(@PathVariable("ordNum") Long ordNum, Model model) {
        Order order = orderService.getOrderById(ordNum);
        model.addAttribute("order", order);
        return "edit-order";
    }

    @PostMapping("/update/{ordNum}")
    public String updateOrder(@PathVariable("ordNum") Long ordNum,
                              @ModelAttribute Order order,
                              RedirectAttributes redirectAttributes) {
        order.setOrdNum(ordNum);
        orderService.saveOrder(order);
        redirectAttributes.addFlashAttribute("success", "訂單更新成功！");
        return "redirect:/orders";
    }

    @GetMapping("/delete/{ordNum}")
    public String deleteOrder(@PathVariable("ordNum") Long ordNum,
                              RedirectAttributes redirectAttributes) {
        orderService.deleteOrder(ordNum);
        redirectAttributes.addFlashAttribute("success", "訂單刪除成功！");
        return "redirect:/orders";
    }

    @GetMapping("/details/{ordNum}")
    public String showOrderDetails(@PathVariable("ordNum") Long ordNum, Model model) {
        Order order = orderService.getOrderById(ordNum);
        List<OrderDetail> details = orderDetailService.findByOrderId(ordNum);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", details);

        // 建立新的訂單明細物件
        OrderDetail newDetail = new OrderDetail();
        newDetail.setOrdNum(ordNum);
        model.addAttribute("orderDetail", newDetail);

        return "order-details";
    }

    // ======== 訂單明細相關操作 ========

    @PostMapping("/details/{ordNum}/add-detail")
    public String addOrderDetail(@PathVariable("ordNum") Long ordNum,
                                 @ModelAttribute("orderDetail") OrderDetail orderDetail,
                                 RedirectAttributes redirectAttributes) {
        // 檢查是否已存在此商品於該訂單
        boolean exists = orderDetailService.existsByOrdNumAndProdNum(ordNum, orderDetail.getProdNum());
        if (exists) {
            redirectAttributes.addFlashAttribute("error", "此商品已存在於訂單明細中");
            return "redirect:/orders/details/" + ordNum;
        }

        // 取得商品資訊並設定價格（可選）
        Product product = productService.findByProdNum(orderDetail.getProdNum());
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "商品不存在！");
            return "redirect:/orders/details/" + ordNum;
        }

        orderDetail.setOrdNum(ordNum);
        orderDetail.setOrdPrice(product.getProdPrice());

        orderDetailService.saveOrderDetail(orderDetail);
        redirectAttributes.addFlashAttribute("success", "訂單明細新增成功！");
        return "redirect:/orders/details/" + ordNum;
    }

    @GetMapping("/details/{ordNum}/delete-detail/{prodNum}")
    public String deleteOrderDetail(@PathVariable("ordNum") Long ordNum,
                                    @PathVariable("prodNum") Long prodNum,
                                    RedirectAttributes redirectAttributes) {
        OrderDetail detail = new OrderDetail();
        detail.setOrdNum(ordNum);
        detail.setProdNum(prodNum);
        orderDetailService.deleteOrderDetail(detail);
        redirectAttributes.addFlashAttribute("success", "已成功刪除商品明細");
        return "redirect:/orders/details/" + ordNum;
    }
    
    /*@GetMapping("/details/{ordNum}")
    public String showOrderDetails(@PathVariable("ordNum") Long ordNum, Model model) {
        Order order = orderService.getOrderById(ordNum);
        List<OrderDetail> details = orderDetailService.findByOrderId(ordNum);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", details);

        OrderDetail newDetail = new OrderDetail();
        newDetail.setOrdNum(ordNum);
        model.addAttribute("orderDetail", newDetail);

        return "order-details";
    }*/

}
