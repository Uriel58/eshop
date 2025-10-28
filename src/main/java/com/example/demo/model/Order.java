package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.time.ZonedDateTime;
import java.time.ZoneId;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "`order`")  // 避免與 SQL 保留字衝突
public class Order {
	/**
	    * Order有ord_num(訂單編號),ord_date(創建訂單日期,自動),required_date(更新訂單日期,自動),county(購買地區),customerId（顧客編號),
	    * order_status(訂單狀態),payment_method(付款方式),order_barcode(訂單條碼,自動)
	    * 連接到OrderDetail,Customer
	*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ord_num")
    private Long ordNum; // ✅ 改成 Long 型別，自動編號
    
    
    @Column(name = "ord_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private ZonedDateTime ordDate = ZonedDateTime.now(ZoneId.of("Asia/Taipei")); // ✅ 自動產生訂單時間
    
    
    @Column(name = "required_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private ZonedDateTime requiredDate = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));;
    
    
    
    // ✅ 改成 ManyToOne 關聯到 Customer
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;
    
    
    @Column(name = "county", length = 50)
    private String county;

    @Column(name = "order_status", length = 20)
    private String orderStatus;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "delivery_method", length = 30)
    private String deliveryMethod;

    @Column(name = "order_barcode", length = 100, unique = true)
    private String orderBarcode; // ✅ 自動生成條碼

    @OneToMany(mappedBy = "order",  cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();//連到orderdetail fetch = FetchType.EAGER

    // ✅ 在新增前自動設定訂單時間與條碼
    @PrePersist
    protected void onCreate() {
        if (this.ordDate == null) {
            this.ordDate = ZonedDateTime.now();
        }
        else if (this.ordDate != null) {
        	this.requiredDate = ZonedDateTime.now();
        }

        if (this.orderBarcode == null) {
            // 用 UUID 或自訂格式產生條碼
            this.orderBarcode = "ORD-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12).toUpperCase();
        }
    }

    // ==== Getter & Setter ====

    public Long getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(Long ordNum) {
        this.ordNum = ordNum;
    }

    public ZonedDateTime getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(ZonedDateTime ordDate) {
        this.ordDate = ordDate;
    }
    
    public ZonedDateTime getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(ZonedDateTime requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getOrderBarcode() {
        return orderBarcode;
    }

    public void setOrderBarcode(String orderBarcode) {
        this.orderBarcode = orderBarcode;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void addOrderDetail(OrderDetail detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }

    public void removeOrderDetail(OrderDetail detail) {
        orderDetails.remove(detail);
        detail.setOrder(null);
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    
    @Override
    public String toString() {
        return "Order{" +
                "ordNum=" + ordNum +
                ", ordDate=" + ordDate +
                ", requiredDate=" + requiredDate +
                ", county='" + county + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", orderBarcode='" + orderBarcode + '\'' +
                ", customer=" + customer  +  
                ", orderDetails=" + orderDetails.size()  + 
                '}';
    }
}
