package com.example.demo.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "order_detail")
@IdClass(OrderDetailPK.class)
public class OrderDetail {
	/**
	 * OrderDetail有ord_num(訂單編號),prodNum(產品編號),ord_qty(訂單數量),ord_price(總共產品價格),
	 * prod_price(各產品價格),fare(運費)
	 */

	@Id
	@Column(name = "ord_num")
	private Long ordNum;

	@Id
	@Column(name = "prodNum")
	private Long prodNum;

	@Column(name = "ord_qty")
	private Integer ordQty;

	@Column(name = "ord_price", precision = 10, scale = 2) // 金錢顯示前面 8 位 + 小數點後 2 位）。
	private BigDecimal ordPrice;
	
	@Column(name = "fare", precision = 10, scale = 2)
	private BigDecimal fare;

	@ManyToOne // 跟order連動
	@JoinColumn(name = "ord_num", insertable = false, updatable = false)
	private Order order;

	@ManyToOne // 跟product連動
	@JoinColumn(name = "prodNum", insertable = false, updatable = false)
	private Product product;

	// Getter for the composite primary key
	public OrderDetailPK getPk() {
		return new OrderDetailPK(ordNum, prodNum);
	}

	// Getter, Setter

	public Long getOrdNum() {
		return ordNum;
	}

	public void setOrdNum(Long ordNum) {
		this.ordNum = ordNum;
	}

	public Long getProdNum() {
		return prodNum;
	}

	public void setProdNum(Long prodNum) {
		this.prodNum = prodNum;
	}

	public Integer getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(Integer ordQty) {
		this.ordQty = ordQty;
	}

	public BigDecimal getOrdPrice() {
		return ordPrice;
	}

	public void setOrdPrice(BigDecimal ordPrice) {
		this.ordPrice = ordPrice;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	@Override
	public String toString() {
		return "OrderDetail{" + "ordNum=" + ordNum + ", prodNum=" + prodNum + ", ordQty=" + ordQty + ", ordPrice="
				+ ordPrice + ", fare=" + fare + '}';
	}

}
