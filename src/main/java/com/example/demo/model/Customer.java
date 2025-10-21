package com.example.demo.model;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "customers") // 資料表名稱，注意：和 order 表中的外鍵一致
public class Customer {
	/**
	    * Customer有customer_id(顧客id),name(顧客名稱),email(顧客電子郵件),telephone(顧客電話),
	    * address(顧客地址),keyword(顧客常搜尋的關鍵詞),age(年齡),gender(性別)
	*/
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id") // 對應資料表欄位
    private Long customerId;
    
    @OneToMany
    (mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();//關聯Cart

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;
    
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "address")
    private String address;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;
    
    // Constructors
    public Customer() {}

    public Customer(String name, String email, String telephone, String address, String keyword, Integer age, String gender) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.keyword = keyword;
        this.age = age;
        this.gender = gender;
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
    

    public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	// toString()
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", keyword='" + keyword + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
