package com.example.demo.dao;

import com.example.demo.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
	List<Customer> findAll();

	Optional<Customer> findById(Long id);//Customer findById(Long customerId);

	void save(Customer customer);

	void delete(Long customerId);

	Customer findByEmail(String email);
	
	// 新增這個方法
	Customer findByUserId(Long userId);
	/*
	 * // interface: CustomerDAO List<Customer> findByKeyword(String keyword);
	 * List<Customer> findByAgeRange(int minAge, int maxAge); List<Customer>
	 * findByGender(String gender);
	 */
}
