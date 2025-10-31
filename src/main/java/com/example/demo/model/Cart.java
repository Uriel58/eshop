package com.example.demo.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
@Entity
@Table(name = "carts")
public class Cart {
	/**
	 * Cart  有 id customerid（抓取customer的customerid）,
	 * saved_for_late(願望清單),
	 * created_at(加入購物車的時間),updated_at(更新時間),checked_Out是否送出訂單
	 * 結帳需送進Order
	 * 連接到customer,cartDetail
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	// 關聯 Customer
	@ManyToOne
	@JoinColumn(name = "customerid", nullable = false)
	private Customer customer;

	@Column(name = "checked_Out")
	private Boolean checkedOut = false;

	@Column(name = "created_at")
	private ZonedDateTime createdAt;

	@Column(name = "updated_at")
	private ZonedDateTime updatedAt;
	
	
	
	// 修改： Cart 对应 CartDetail 的外键关联fetch = FetchType.EAGER,
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<CartDetail> cartDetails = new ArrayList<>(); 

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

	public Boolean getCheckedOut() {
		return checkedOut;
	}

	public void setCheckedOut(Boolean checkedOut) {
		this.checkedOut = checkedOut;
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
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public List<CartDetail> getCartDetails() {
		return cartDetails;
	}

	public void setCartDetails(List<CartDetail> cartDetails) {
		this.cartDetails = cartDetails;
	}
	// 添加 CartDetail 的方法
    public void addCartDetail(CartDetail cartDetail) {
        cartDetails.add(cartDetail);
        cartDetail.setCart(this); // 设置 CartDetail 对应的 Cart
    }
	@Override
	public String toString() {
	    return "Cart{" +
	            "id=" + id +
	            ", customerId=" + (customer != null ? customer.getCustomerId() : "null") +
	            ", checkedOut=" + checkedOut +
	            ", createdAt=" + createdAt +
	            ", updatedAt=" + updatedAt +
	            ", cartDetails=" + cartDetails.size() +
	            '}';
	}
}
