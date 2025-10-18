package com.example.demo.service.impl;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        logger.info("編輯資料，Customer ID={}", customerId);
        return customerDAO.findById(customerId);
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerDAO.save(customer);
    }

    @Override
    public void updateCustomer(Long customerId, Customer updatedCustomer) {
        Customer existingCustomer = customerDAO.findById(customerId);
        if (existingCustomer == null) {
            throw new RuntimeException("Customer not found");
        }

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());



        customerDAO.save(existingCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId) {
        customerDAO.delete(customerId);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerDAO.findByEmail(email);
    }
}
