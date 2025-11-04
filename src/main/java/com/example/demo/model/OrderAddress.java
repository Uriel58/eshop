package com.example.demo.model;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_addresses")
@Data
@NoArgsConstructor
public class OrderAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY) // 🔹 加上 fetch 屬性避免不必要的查詢(ManyToOne)?
	@JoinColumn(name = "ord_num", nullable = false) // 🔹 指明外鍵不可為 null
	private Order order;

	@Column(name = "recipient_name", nullable = false)
	private String recipientName;

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private String country;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String district;

	@Column(name = "street_address", nullable = false)
	private String streetAddress;

	@Column(name = "postal_code", nullable = false)
	private String postalCode;

	@Column(name = "created_at", updatable = false)
	private ZonedDateTime createdAt;

	@Column(name = "updated_at")
	private ZonedDateTime updatedAt;
	
	private static final ZoneId ZONE_TAIPEI = ZoneId.of("Asia/Taipei");

	@PrePersist
	protected void onCreate() {
		createdAt = updatedAt = ZonedDateTime.now(ZONE_TAIPEI);
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = ZonedDateTime.now(ZONE_TAIPEI);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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

}
