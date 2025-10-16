package com.example.demo.dao;

import com.example.demo.model.Customer;
import java.util.List;

public interface CustomerDAO {
    List<Customer> findAll();
    Customer findById(Long customerId);
    void save(Customer customer);
    void delete(Long customerId);
    Customer findByEmail(String email);
}
