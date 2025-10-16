package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
@SessionAttributes({"customerId", "name"}) // 如果你需要存在 session 中
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // 顯示所有客戶
    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers"; // 對應 /WEB-INF/views/customers.html
    }

    // 顯示新增客戶表單
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add-customer"; // 對應 /WEB-INF/views/add-customer.html
    }

    // 顯示編輯客戶表單
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "edit-customer"; // 對應 /WEB-INF/views/edit-customer.html
    }

    // 更新客戶資料（用 POST）
    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable("id") Long id,
                                 @ModelAttribute("customer") Customer customer) {
        customerService.updateCustomer(id, customer);
        return "redirect:/customers";
    }

    // 儲存新客戶
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/customers";
    }

    // 刪除客戶
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
