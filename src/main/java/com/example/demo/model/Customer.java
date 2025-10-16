package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "customers") // 資料表名稱
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id") // 對應資料表欄位
    private Long customerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;


    // Constructors
    public Customer() {}

    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;

    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "Customer{customerId=" + customerId + ", name='" + name + "', email='" + email + "'}";
    }
}
