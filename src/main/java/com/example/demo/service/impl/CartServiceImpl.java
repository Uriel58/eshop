package com.example.demo.service.impl;

import com.example.demo.dao.CartDAO;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.model.Customer;
import com.example.demo.dao.OrderDAO;
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
	public void checkout(Long cartId , Order orderForm) {
	    // 1️⃣ 取得購物車
	    Cart cart = cartDao.findById(cartId);
	    if (cart == null || cart.getCartDetails().isEmpty()) {
	        return; // 沒有購物車或購物車是空的就直接結束
	    }

	    // 2️⃣ 建立訂單
	    Order order = new Order();
	    Customer customer = customerDao.findById(cart.getCustomer().getCustomerId())
	            .orElseThrow(() -> new RuntimeException("Customer not found"));
	    order.setCustomer(customer);
	    
	    // 使用前端選項
	    order.setCounty(orderForm.getCounty());
	    order.setPaymentMethod(orderForm.getPaymentMethod());
	    order.setDeliveryMethod(orderForm.getDeliveryMethod());
	    order.setOrderStatus("未處理");

	    // 3️⃣ 將購物車明細轉換成訂單明細
	    for (CartDetail cartDetail : cart.getCartDetails()) {
	        OrderDetail orderDetail = new OrderDetail();
	        orderDetail.setOrder(order); // 維持雙向關聯
	        orderDetail.setProduct(cartDetail.getProduct());
	        orderDetail.setProdNum(cartDetail.getProduct().getProdNum()); // ✅ 設定複合主鍵
	        orderDetail.setOrdQty(cartDetail.getCartQty());
	        orderDetail.setOrdPrice(cartDetail.getProdPrice());
	        orderDetail.setFare(cartDetail.getShippingFee()); // ✅ 加上運費
	        //orderDetail.setOrdTotal(cartDetail.getCartTotal());      // ✅ 加上總價（含運費）

	        order.addOrderDetail(orderDetail); // ✅ 放進訂單明細清單中
	    }

	    // 4️⃣ 保存訂單（因為 Order 有 cascade，會自動保存所有 OrderDetail）
	    orderDao.save(order);

	    // 5️⃣ 標記購物車為已結帳
	    cart.setCheckedOut(true);
	    cartDao.update(cart);
	}

	/*@Override
	@Transactional
	public void checkout(Long cartId) {
	    // 獲取購物車
	    Cart cart = cartDao.findById(cartId);
	    if (cart.getCartDetails().isEmpty()) return;

	    // 創建訂單
	    Order order = new Order();
	    Customer customer = customerDao.findById(cart.getCustomer().getCustomerId())
	            .orElseThrow(() -> new RuntimeException("Customer not found"));
	    order.setCustomer(customer);
	    order.setOrderStatus("未處理");

	    // 將購物車明細轉成訂單明細
	    for (CartDetail cartDetail : cart.getCartDetails()) {
	        OrderDetail orderDetail = new OrderDetail();
	        orderDetail.setProduct(cartDetail.getProduct());
	        orderDetail.setOrdQty(cartDetail.getCartQty());
	        orderDetail.setOrdPrice(cartDetail.getProdPrice());
	        order.addOrderDetail(orderDetail); // ✅ 加入 order 的明細列表
	    }

	    // 保存訂單（cascade 會自動 save 明細）
	    orderDao.save(order); 

	    // 標記購物車為已結帳
	    cart.setCheckedOut(true);
	    cartDao.update(cart);
	}*/

	

	// 設定或更新商品數量
	@Override
	@Transactional
	public void setProductQuantity(Long cartId, Long productId, int quantity) {
	    Cart cart = cartDao.findById(cartId);
	    boolean found = false;

	    for (CartDetail detail : cart.getCartDetails()) {
	        if (detail.getProduct().getProdNum().equals(productId)) {
	            // ✅ 直接設定數量（不是加法）
	            detail.setCartQty(quantity);
	            found = true;
	            break;
	        }
	    }

	    if (!found) {
	        // ✅ 商品不存在時，自動新增
	        Product product = productDao.findById(productId);
	        CartDetail detail = new CartDetail();
	        detail.setCart(cart);
	        detail.setProduct(product);
	        detail.setCartQty(quantity);
	        detail.setProdPrice(product.getProdPrice()); // ✅ 使用商品原價
	        detail.setShippingFee(detail.getShippingFee());
	        cart.getCartDetails().add(detail);
	    }

	    // ✅ 更新總價
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
	}
	// ✅ 新增這個方法
    @Override
    public Cart getCartByCustomerId(Long customerId) {
        List<Cart> carts = cartDao.findByCustomerId(customerId);
        return carts.isEmpty() ? null : carts.get(0); // 只回傳第一個購物車
    }
    @Override
    public void saveCartDetail(CartDetail cartDetail) {
    	cartDetailDao.save(cartDetail); // 保存 CartDetail 到数据库
    }

}
