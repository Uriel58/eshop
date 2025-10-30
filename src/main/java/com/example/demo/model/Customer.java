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
	    * 
	    * 連到user,Cart
	*/
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id") // 對應資料表欄位
    private Long customerId;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id") // This links to the 'id' field in User table
    private User user;//關聯user one to one
    
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

    public Customer(User user,String name, String email, String telephone, String address, String keyword, Integer age, String gender,List<Order> orders,List<Cart> carts) {
    	this.user = user;  // Set the User reference
    	this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.keyword = keyword;
        this.age = age;
        this.gender = gender;
        this.orders = new ArrayList<>(); // 初始化 orders
		this.carts = new ArrayList<>(); // 初始化 orders
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

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}
	public User getUser() {
	    return user;
	}

	public void setUser(User user) {
	    this.user = user;
	}

	// toString()
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name= " + name +
                ", email= " + email  +
                ", telephone= " + telephone  +
                ", address= " + address  +
                ", keyword= " + keyword +
                ", age=" + age +
                ", gender='" + gender +
                ", user=" + user.getId() +  // （可選）顯示使用者 ID 或其他使用者屬性
                ", carts=" + carts.size() +  // 防止直接列印大量購物車詳細信息
                ", orders=" + orders.size() + // 防止直接列印大量訂單詳細信息
                '}';
    }
}
