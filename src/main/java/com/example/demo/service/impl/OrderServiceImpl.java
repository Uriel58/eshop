package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartDetail;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.service.OrderService;
import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.OrderDetailDAO;
import com.example.demo.dao.CartDAO;
import com.example.demo.dao.CartDetailDAO;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.service.CartDetailService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.time.ZonedDateTime;
import java.time.ZoneId;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private OrderDetailDAO orderDetailDAO;

	@Autowired
	private CartDAO cartDAO;

	@Autowired
	private CartDetailDAO cartDetailDAO;

	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private CartDetailService cartDetailService;
	
	

	@Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    public Order getOrderById(Long ordNum) {
        return orderDAO.findById(ordNum);
    }

    @Override
    public void saveOrder(Order order) {
        orderDAO.save(order);
    }

    @Override
    public void deleteOrder(Long ordNum) {
        orderDAO.delete(ordNum);
    }
	
    @Override
	public void createOrderFromCart(Long customerId, String paymentMethod, String deliveryMethod) {
		// TODO: 把 Cart 和 CartDetail 的資料搬進 Order 和 OrderDetail
		// 1. 建立 Order
		Customer customer = customerDAO.findById(customerId);
		Order order = new Order();
		order.setCustomer(customer);
		order.setPaymentMethod(paymentMethod);
		order.setDeliveryMethod(deliveryMethod);
		order.setOrdDate(ZonedDateTime.now(ZoneId.of("Asia/Taipei")));

		orderDAO.save(order);

		// 2. 把 CartDetail 資料變成 OrderDetail
		List<CartDetail> cartDetails = cartDetailDAO.findByCustomerId(customerId);
		for (CartDetail cd : cartDetails) {
			OrderDetail od = new OrderDetail();
			od.setOrder(order);
			od.setProduct(cd.getProduct());
			od.setOrdQty(cd.getCartQty());
			od.setOrdPrice(cd.getProdPrice());
			od.setFare(cd.getShippingFee());
			orderDetailDAO.save(od);
		}

		// 3. 更新 Cart 和 CartDetail 與這個 Order 綁定
		List<Cart> carts = cartDAO.findByCustomerId(customerId);
		for (Cart c : carts) {
			if (c.getOrder() == null) {
				c.setOrder(order);
				cartDAO.update(c);
			}
		}

		for (CartDetail cd : cartDetails) {
			if (cd.getOrder() == null) {
				cd.setOrder(order);
				cartDetailDAO.update(cd);
			}
		}
	}
	@Override
	public void removeCart(Long cartId) {
	    Cart cart = cartDAO.findById(cartId);
	    if (cart != null) {
	        // 1. 刪除 CartDetail 中對應的資料
	        cartDetailService.deleteByCustomerIdAndProductId(
	            cart.getCustomer().getCustomerId(),
	            cart.getProduct().getProdNum()
	        );

	        // 2. 將 cart 標記為 saved for later（或直接刪除也可以）
	        cart.setSavedForLater(true);
	        cartDAO.update(cart);
	    }
	}

}