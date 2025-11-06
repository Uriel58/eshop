package com.example.demo.service.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Product;
import com.example.demo.model.Category;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private CategoryDAO categoryDAO;

	@Override
	public List<Product> getAllProducts() {
		return productDAO.findAll();
	}

	@Override
	public Product getProductById(Long prodNum) {
		logger.info("查詢商品，Product ID = {}", prodNum);
		return productDAO.findById(prodNum);
	}

	@Override
	public void saveProduct(Product product) {
		productDAO.save(product);
	}

	@Override
	public void updateProduct(Long prodNum, Product updatedProduct) {
		Product existingProduct = productDAO.findById(prodNum);
		if (existingProduct == null) {
			throw new RuntimeException("Product not found");
		}

		// 更新欄位
		existingProduct.setProdName(updatedProduct.getProdName());
		existingProduct.setProdPrice(updatedProduct.getProdPrice());
		existingProduct.setProdInfo(updatedProduct.getProdInfo());
		existingProduct.setProdKeywords(updatedProduct.getProdKeywords());
		existingProduct.setProdBarcode(updatedProduct.getProdBarcode());
		existingProduct.setCreatedTime(updatedProduct.getCreatedTime());
		existingProduct.setProdTags(updatedProduct.getProdTags());
		existingProduct.setProdImages(updatedProduct.getProdImages());
		existingProduct.setProdStockQty(updatedProduct.getProdStockQty());

		// ✅ 修正後的 Category 更新邏輯
		if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getId() != null) {
			Category category = categoryDAO.findById(updatedProduct.getCategory().getId());
			/*
			 * category.setProdType(updatedProduct.getCategory().getProdType());
			 * category.setProdLine(updatedProduct.getCategory().getProdLine());
			 * category.setDescription(updatedProduct.getCategory().getDescription());
			 * categoryDAO.update(category);
			 */
			existingProduct.setCategory(category);
		}

		// 更新回資料庫
		productDAO.update(existingProduct);
	}

	@Override
	public void deleteProduct(Long prodNum) {
		productDAO.delete(prodNum);
	}

	@Override
	public Product findByBarcode(String barcode) {
		return productDAO.findByBarcode(barcode);
	}

	@Override
	public List<Product> findByKeyword(String keyword) {
		return productDAO.findByKeyword(keyword);
	}

	@Override
	public Product findByProdNum(Long prodNum) {
		return productDAO.findByProdNum(prodNum);
	}

	// 抓取Category
	@Override
	public Product getProductByIdWithCategory(Long prodNum) {
		return productDAO.findByIdWithCategory(prodNum);
	}
}
