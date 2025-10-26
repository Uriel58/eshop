package com.example.demo.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "carts")
public class Cart {
	/**
	 * Cart 有customerid（抓取customer的customerid）,prodNum（抓取product的prodNum）,
	 * ord_num(連接Order的ord_num),saved_for_late(願望清單),
	 * created_at(加入購物車的時間),updated_at(更新時間)
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 關聯 Customer
	@ManyToOne
	@JoinColumn(name = "customerid", nullable = false)
	private Customer customer;

	// 關聯 Product
	@ManyToOne
	@JoinColumn(name = "prodNum", nullable = false)
	private Product product;

	// 可選的關聯到 Order（送出訂單後填入）
	@ManyToOne
	@JoinColumn(name = "ord_num", referencedColumnName = "ord_num")
	private Order order;

	@Column(name = "saved_for_late")
	private Boolean savedForLate = false;

	@Column(name = "created_at")
	private ZonedDateTime createdAt;

	@Column(name = "updated_at")
	private ZonedDateTime updatedAt;

	// 自動時間設定
	@PrePersist
	protected void onCreate() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
	}

	// Getter 和 Setter 略（可自行補上）
	// ==== Getter & Setter ====

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Boolean getSavedForLater() {
		return savedForLate;
	}

	public void setSavedForLater(Boolean savedForLater) {
		this.savedForLate = savedForLater;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ZonedDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(ZonedDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
	    return "Cart{" +
	            "id=" + id +
	            ", customerId=" + (customer != null ? customer.getCustomerId() : "null") +
	            ", productId=" + (product != null ? product.getProdNum() : "null") +
	            ", productName='" + (product != null ? product.getProdName() : "null") + '\'' +
	            ", savedForLate=" + savedForLate +
	            ", createdAt=" + createdAt +
	            ", updatedAt=" + updatedAt +
	            ", orderId=" + (order != null ? order.getOrdNum() : "null") +
	            '}';
	}
	
	/*public Order checkoutCart(List<Cart> cartItems, Customer customer) {
	    Order order = new Order();
	    order.setCustomer(customer);
	    order.setOrderStatus("Pending");
	    order.setPaymentMethod("Credit Card"); // 依實際設定
	    order.setDeliveryMethod("Home Delivery");

	    for (Cart cart : cartItems) {
	        OrderDetail detail = new OrderDetail();
	        detail.setProduct(cart.getProduct());
	        detail.setProdPrice(cart.getProdPrice());
	        detail.setQuantity(cart.getCartQty());
	        detail.setOrder(order);
	        
	        order.addOrderDetail(detail);

	        cart.setOrder(order); // 設定關聯
	    }

	    return orderRepository.save(order); // 一併儲存 order 與 detail
	}*/
	


}
