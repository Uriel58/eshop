package com.example.demo.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@IdClass(OrderDetailPK.class)
public class OrderDetail {
	/**
	 * OrderDetail有ord_num(訂單編號),prodNum(產品編號),ord_qty(訂單數量),
	 * ord_price(這個產品價格),fare(運費) 有關連到order product OrderDetailPK 連接到Order,product
	 */

	@Id
	@ManyToOne
	@JoinColumn(name = "ord_num") // ✅ 關聯 order
	private Order order;

	@Id
	@Column(name = "prodNum")
	private Long prodNum;

	@Column(name = "ord_qty")
	private Integer ordQty;

	@Column(name = "ord_price", precision = 10, scale = 2) // 金錢顯示前面 8 位 + 小數點後 2 位）。
	private BigDecimal ordPrice;

	@Column(name = "fare", precision = 10, scale = 2)
	private BigDecimal fare;

	@ManyToOne // 跟product連動
	@JoinColumn(name = "prodNum", insertable = false, updatable = false)
	private Product product;

	// Getter for the composite primary key
	public OrderDetailPK getPk() {
		return new OrderDetailPK(order.getOrdNum(), prodNum);
	}

	// Getter, Setter

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	@Transient
	public Long getOrdNum() {
	    return (order != null) ? order.getOrdNum() : null;
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
		return "OrderDetail{" + "order=" + (order != null ? order.getOrdNum() : null) 
				+", prodNum=" + prodNum + ", ordQty=" + ordQty + ", ordPrice= "
				+ ordPrice + ", fare=" + fare + ", product=" + product + '}';
	}

}
