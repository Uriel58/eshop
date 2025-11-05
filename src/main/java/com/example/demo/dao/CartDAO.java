package com.example.demo.dao;

import com.example.demo.model.Cart;
import java.util.List;

public interface CartDAO {
	Cart findById(Long id);

	List<Cart> findAll();

	void save(Cart cart);

	void update(Cart cart);

	void delete(Long id);

	List<Cart> findByCustomerId(Long customerId);

	// List<Cart> findByCustomerIdWithDetails(Long customerId); //
	// 新增，配合CartController
	// 根據 prodType 查詢 prodLine
	public List<String> findProdLinesByProdType(String prodType);

	// 根據 prodType 和 prodLine 查詢 description
	public List<String> findDescriptionsByProdTypeAndProdLine(String prodType, String prodLine);
}