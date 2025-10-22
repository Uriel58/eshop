package com.example.demo.service.impl;

import com.example.demo.dao.CartDetailDAO;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.CartDetail;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class CartDetailServiceImpl implements CartDetailService {

	@Autowired
	private CartDetailDAO cartDetailDAO;

	@Autowired
	private CustomerDAO customerDAO; // 加上注入

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CartDetail findById(Long id) {
		return cartDetailDAO.findById(id);
	}

	@Override
	public List<CartDetail> getCartDetailsByCustomerId(Long customerId) {
		return cartDetailDAO.findByCustomerId(customerId);
	}

	@Override
	public void saveCartDetail(CartDetail cartDetail) {
		cartDetailDAO.save(cartDetail);
	}

	@Override
	public void updateCartDetail(CartDetail cartDetail) {
		cartDetailDAO.update(cartDetail);
	}

	@Override
	public void deleteCartDetail(Long id) {
		cartDetailDAO.deleteById(id);
	}

	@Override
	public void deleteAllByCustomerId(Long customerId) {
		cartDetailDAO.deleteAllByCustomerId(customerId);
	}

	@Override
	public BigDecimal calculateCartTotal(Long customerId) {
		List<CartDetail> cartDetails = cartDetailDAO.findByCustomerId(customerId);
		return cartDetails.stream()
				.map(cd -> cd.getProdPrice().multiply(BigDecimal.valueOf(cd.getCartQty()))
						.add(cd.getShippingFee() != null ? cd.getShippingFee() : BigDecimal.ZERO))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public void addOrUpdateCartDetail(Long customerId, Product product, int quantity) {
		// 查詢現有 CartDetail
		List<CartDetail> details = getCartDetailsByCustomerId(customerId); // 用 this 呼叫本service方法
		CartDetail exist = details.stream()
				.filter(cd -> cd.getProduct().getProdNum().equals(product.getProdNum()) && cd.getOrder() == null)
				.findFirst().orElse(null);

		if (exist != null) {
			// 更新數量
			exist.setCartQty(exist.getCartQty() + quantity);
			updateCartDetail(exist);
		} else {
			CartDetail newDetail = new CartDetail();
			Customer customer = customerDAO.findById(customerId);
			newDetail.setCustomer(customer);
			newDetail.setProduct(product);
			newDetail.setCartQty(quantity);
			newDetail.setProdPrice(product.getProdPrice());
			// 計算總價
			newDetail.setCartTotal(product.getProdPrice().multiply(BigDecimal.valueOf(quantity)));
			saveCartDetail(newDetail);
		}
	}

	@Override
	@Transactional
	public void deleteByCustomerIdAndProductId(Long customerId, Long productId) {
		entityManager.createQuery("DELETE FROM CartDetail cd WHERE cd.customer.customerId = :customerId AND cd.product.prodNum = :productId")
	    	.setParameter("customerId", customerId)
	    	.setParameter("productId", productId)
	    	.executeUpdate();

		// cartDetailDAO.deleteByCustomerIdAndProductId(customerId, productId);
	}

}
