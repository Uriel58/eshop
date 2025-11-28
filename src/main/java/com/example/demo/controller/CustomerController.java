package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/customers")
@SessionAttributes({"customerId", "name"})
public class CustomerController extends LoginBaseController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private UserService userService;
    
    // 顯示所有客戶
    @GetMapping
    public String listCustomers(HttpSession session, Model model) {
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
        
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("users", userService.getAllUsers());
        return "customers";
    }
    
    // 顯示新增客戶表單
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        model.addAttribute("users", userService.getAllUsers());
        return "add-customer";
    }
    
    // 顯示編輯客戶表單
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/customers";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("users", userService.getAllUsers());
        return "edit-customer";
    }
    
    // 更新客戶資料（用 POST）
    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable("id") Long id,
                                 @ModelAttribute("customer") Customer customer) {
        System.out.println("更新客戶 ID: " + id);
        
        // 確保使用正確的 ID
        customer.setCustomerId(id);
        
        // 重新抓取完整的 User 實體
        if (customer.getUser() != null && customer.getUser().getId() != null) {
            User user = userService.getUserById(customer.getUser().getId());
            customer.setUser(user);
        }
        
        customerService.updateCustomer(id, customer);
        return "redirect:/customers";
    }
    
    // 儲存新客戶 - 最佳方案：創建新對象
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("customer") Customer formCustomer) {
        System.out.println("=== 開始新增客戶 ===");
        System.out.println("接收到的表單資料 - name: " + formCustomer.getName());
        
        // ✅ 最佳方案：創建全新的 Customer 對象
        Customer newCustomer = new Customer();
        newCustomer.setName(formCustomer.getName());
        newCustomer.setEmail(formCustomer.getEmail());
        newCustomer.setTelephone(formCustomer.getTelephone());
        newCustomer.setAddress(formCustomer.getAddress());
        newCustomer.setKeyword(formCustomer.getKeyword());
        newCustomer.setAge(formCustomer.getAge());
        newCustomer.setGender(formCustomer.getGender());
        
        // 綁定 User
        if (formCustomer.getUser() != null && formCustomer.getUser().getId() != null) {
            User user = userService.getUserById(formCustomer.getUser().getId());
            if (user != null) {
                newCustomer.setUser(user);
                System.out.println("綁定的 User: " + user.getName() + " (ID: " + user.getId() + ")");
            } else {
                System.err.println("錯誤：找不到指定的 User!");
                return "redirect:/customers/add";
            }
        } else {
            System.err.println("錯誤：未選擇 User!");
            return "redirect:/customers/add";
        }
        
        // 儲存新客戶（ID 為 null，集合自動初始化）
        Customer savedCustomer = customerService.save(newCustomer);
        System.out.println("成功新增客戶，ID = " + savedCustomer.getCustomerId());
        System.out.println("=== 新增客戶完成 ===");
        
        return "redirect:/customers";
    }
    
    // 刪除客戶
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}