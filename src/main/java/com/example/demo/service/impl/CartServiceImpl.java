package com.example.demo.service.impl;

import com.example.demo.dao.CartDAO;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.model.Customer;
import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.OrderDetailDAO;
import com.example.demo.dao.CartDetailDAO;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartDetail;
import com.example.demo.model.Order;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartDAO cartDao;

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private OrderDetailDAO orderDetailDao;

	@Autowired
	private CartDetailDAO cartDetailDao;

	@Autowired
	private ProductDAO productDao;

	@Autowired
	private CustomerDAO customerDao;

	@Override
	public Cart getCart(Long id) {
		return cartDao.findById(id);
	}

	@Override
	public List<Cart> getAllCarts() {
		return cartDao.findAll();
	}

	@Override
	public void saveCart(Cart cart) {
		cartDao.save(cart);
	}

	@Override
	public void updateCart(Cart cart) {
		cartDao.update(cart);
	}

	@Override
	public void deleteCart(Long id) {
		cartDao.delete(id);
	}

	@Override
	public List<Cart> getCartsByCustomer(Long customerId) {
		return cartDao.findByCustomerId(customerId);
	}

	// order-> cart , orderdetail-> cartdetail
	// 結帳
	@Override
	@Transactional
	public void checkout(Long cartId) {
		// 獲取購物車
		Cart cart = cartDao.findById(cartId);
		if (cart.getCartDetails().isEmpty()) {
			// 如果購物車沒有商品，直接返回
			return;
		}

		// 創建訂單 (Order)
		Order order = new Order();
		Customer customer = customerDao.findById(cart.getCustomer().getCustomerId()); // 查找 Customer
		order.setCustomer(customer); // 設定 Customer 物件
		order.setOrderStatus("未處理"); // 設定訂單狀態
		orderDao.save(order); // 保存訂單

		// 創建訂單明細 (OrderDetail) 並將每個 CartDetail 轉換成 OrderDetail
		for (CartDetail cartDetail : cart.getCartDetails()) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(order); // 設定訂單
			orderDetail.setProduct(cartDetail.getProduct()); // 設定商品ID
			orderDetail.setOrdQty(cartDetail.getCartQty()); // 設定數量
			orderDetail.setOrdPrice(cartDetail.getProdPrice()); // 設定價格
			orderDetailDao.save(orderDetail); // 保存訂單明細
		}

		// 標記購物車為已結帳
		cart.setCheckedOut(true);
		cartDao.update(cart); // 更新購物車狀態
	}

	// 新增或更新商品數量
	@Override
	@Transactional
	public void addOrUpdateProduct(Long cartId, Long productId, int quantity, double price) {
		Cart cart = cartDao.findById(cartId);
		boolean found = false;

		// 比較 cartDetail 中的商品 ID 與 productId
		for (CartDetail detail : cart.getCartDetails()) {
			// 使用 `prodNum` 或 `id` 比較 Product 和 productId
			if (detail.getProduct().getProdNum().equals(productId)) { // 假設 `prodNum` 是 Product 的 ID
				detail.setCartQty(detail.getCartQty() + quantity); // 更新數量
				found = true;
				break;
			}
		}

		if (!found) {
			// 如果商品不存在，則新增商品到購物車
			CartDetail detail = new CartDetail();
			detail.setCart(cart);
			Product product = productDao.findById(productId);
			detail.setProduct(product);
			detail.setCartQty(quantity);
			detail.setProdPrice(BigDecimal.valueOf(price));
			cart.getCartDetails().add(detail);
		}

		// 更新總價
		calculateTotal(cartId);
		cartDao.update(cart);
	}

	// 移除商品
	@Override
	@Transactional
	public void removeProduct(Long cartId, Long productId) {
	    Cart cart = cartDao.findById(cartId);
	    // 移除商品時，根據 `prodNum` 或 `id` 比較 Product
	    cart.getCartDetails().removeIf(d -> d.getProduct().getProdNum().equals(productId));  // 使用 `prodNum` 比較
	    calculateTotal(cartId);  // 更新總價
	    cartDao.update(cart);  // 更新購物車
	}

	// 計算總價
	@Override
	@Transactional
	public void calculateTotal(Long cartId) {
		// 获取购物车
		Cart cart = cartDao.findById(cartId);

		// 遍历购物车中的所有 CartDetail，更新每个 CartDetail 的 cartTotal
		cart.getCartDetails().forEach(cartDetail -> {
			// 计算每个商品的总价 = prodPrice * cartQty + shippingFee
			BigDecimal total = cartDetail.getProdPrice().multiply(new BigDecimal(cartDetail.getCartQty()))
					.add(cartDetail.getShippingFee() != null ? cartDetail.getShippingFee() : BigDecimal.ZERO);
			cartDetail.setCartTotal(total); // 设置每个 CartDetail 的 cartTotal
		});

		// 更新所有的 CartDetail
		for (CartDetail detail : cart.getCartDetails()) {
			cartDetailDao.update(detail); // 逐個更新 CartDetail
		}

		// 计算整个购物车的总价
		BigDecimal cartTotal = cart.getCartDetails().stream().map(CartDetail::getCartTotal).reduce(BigDecimal.ZERO,
				BigDecimal::add); // 计算总价

		// 更新购物车
		cartDao.update(cart);
	}
	// ✅ 新增這個方法
    @Override
    public Cart getCartByCustomerId(Long customerId) {
        List<Cart> carts = cartDao.findByCustomerId(customerId);
        return carts.isEmpty() ? null : carts.get(0); // 只回傳第一個購物車
    }
	/*
	 * @Override
	 * 
	 * @Transactional public void calculateTotal(Long cartId) { CartDetail cart =
	 * cartDao.findById(cartId); // 使用 BigDecimal 計算總價 BigDecimal total =
	 * cart.getCartDetails().stream() .map(detail ->
	 * detail.getProdPrice().multiply(new BigDecimal(detail.getCartQty())))
	 * .reduce(BigDecimal.ZERO, BigDecimal::add); // 計算總價 // 計算總價
	 * cartDetail.getCartTotal(total); // 設定總價 cartDetailDao.update(cartDetail); //
	 * 更新購物車 }
	 */
}
