package com.example.demo.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_details")
public class CartDetail {
	/**
	 * CartDetail 有： - customerid（抓取 customer 的 customerid） - prodNum（抓取 product 的
	 * prodNum） - prodPrice（抓取 product 的 prodPrice 並且送出訂單後存到 ord_price） - ord_num（連接
	 * Order 的 ord_num） - cart_qty（暫存的購物車數量，送出訂單後存在 OrderDetail） - cart_total（使用 HQL
	 * 暫存給顧客看） - shipping_fee（運費）
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
    @MapsId // 這表示使用相同的主鍵來對應 Cart 的主鍵
    @JoinColumn(name = "id")
    private Cart cart;

	// 關聯 Customer
	@ManyToOne
	@JoinColumn(name = "customerid", nullable = false)
	private Customer customer;

	// 關聯 Product
	@ManyToOne
	@JoinColumn(name = "prodNum", nullable = false)
	private Product product;

	// 關聯 Order（送出訂單後關聯）
	@ManyToOne
	@JoinColumn(name = "ord_num", referencedColumnName = "ord_num")
	private Order order;

	// 商品價格（來源 product，送出訂單時固定下來）
	@Column(name = "prod_price", precision = 10, scale = 2)
	private BigDecimal prodPrice;

	// 購物車數量
	@Column(name = "cart_qty")
	private Integer cartQty;

	// 運費
	@Column(name = "shipping_fee", precision = 10, scale = 2)
	private BigDecimal shippingFee;

	// 總價 = prodPrice * cartQty +運費
	@Column(name = "cart_total", precision = 10, scale = 2)
	private BigDecimal cartTotal;

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

	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}

	public Integer getCartQty() {
		return cartQty;
	}

	public void setCartQty(Integer cartQty) {
		this.cartQty = cartQty;
	}

	public BigDecimal getCartTotal() {
		return cartTotal;
	}

	public void setCartTotal(BigDecimal cartTotal) {
		this.cartTotal = cartTotal;
	}

	public BigDecimal getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	@Override
	public String toString() {
		return "CartDetail{" +
				"id=" + id +
				", customerId=" + (customer != null ? customer.getCustomerId() : "null") +
				", productId=" + (product != null ? product.getProdNum() : "null") +
				", productName='" + (product != null ? product.getProdName() : "null") + '\'' +
				", prodPrice=" + prodPrice +
				", cartQty=" + cartQty +
				", cartTotal=" + cartTotal +
				", shippingFee=" + shippingFee +
				", orderId=" + (order != null ? order.getOrdNum() : "null") +
				'}';
	}
}
