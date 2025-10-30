package com.example.demo.service;

import com.example.demo.model.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long customerId);
    void saveCustomer(Customer customer);
    void updateCustomer(Long customerId, Customer updatedCustomer);
    void deleteCustomer(Long customerId);
    Customer findByEmail(String email);
    ///// CartController抓目前登入顧客
    Customer getCurrentCustomer();
    Customer findByUsername(String username);
    public Customer getCustomerByUserId(Long userId);
    void save(Customer customer);
}
