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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private SessionFactory sessionFactory;

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
		existingCustomer.setTelephone(updatedCustomer.getTelephone());
		existingCustomer.setAddress(updatedCustomer.getAddress());
		existingCustomer.setKeyword(updatedCustomer.getKeyword());
		existingCustomer.setAge(updatedCustomer.getAge());
		existingCustomer.setGender(updatedCustomer.getGender());
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

	@Override
	public Customer getCurrentCustomer() {
		// 若有登入機制，從 SecurityContext 或 Session 取得使用者
		// 這裡可以暫時模擬，例如回傳第一位顧客：
		return customerDAO.findAll().stream().findFirst().orElse(null);
	}

	@Override
	public Customer findByUsername(String username) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		Customer customer = session.createQuery("FROM Customer c WHERE c.username = :username", Customer.class)
				.setParameter("username", username).uniqueResult();

		tx.commit();
		session.close();

		return customer;
	}

	@Override
	public Customer getCustomerByUserId(Long userId) {
		return customerDAO.findByUserId(userId);
	}
	public void save(Customer customer) {
		customerDAO.save(customer);
    }
}
