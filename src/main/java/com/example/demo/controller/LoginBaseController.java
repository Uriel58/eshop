package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import javax.servlet.http.HttpServletRequest;

@SessionAttributes({ "id", "name" })

public class LoginBaseController {
	@Autowired
	private CustomerService customerService;

	// 每個 controller 頁面都會調用這個方法來加入 id 和 name
	@ModelAttribute
	public void addCustomerInfo(HttpServletRequest request, Model model) {
		Long userId = (Long) request.getSession().getAttribute("id");
		if (userId != null) {
			Customer currentCustomer = customerService.getCustomerByUserId(userId);
			if (currentCustomer != null) {
				model.addAttribute("customer", currentCustomer);
				model.addAttribute("id", currentCustomer.getCustomerId());
				model.addAttribute("name", currentCustomer.getName());
			}
		}
	}
}
