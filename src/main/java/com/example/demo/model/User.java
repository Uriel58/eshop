package com.example.demo.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
	/**
	 * User有id(使用者id),name(使用者名稱),email(使用者電子郵件),password(使用者密碼)
	 * 連到customer
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name" , nullable = false)
	private String name;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "identify_name", nullable = false)
	private String identifyName;

	@OneToOne(mappedBy = "user") // 和customer關聯
	private Customer customer;
	
	@OneToMany(mappedBy = "user") // 一個用戶對應多個密碼重置令牌
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<>();;

	// Constructors
	public User() {
	}

	public User(String name, String email, String password, String identifyName,List<PasswordResetToken> passwordResetTokens) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.identifyName = identifyName;
		this.passwordResetTokens = passwordResetTokens;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdentifyName() {
		return identifyName;
	} 

	public void setIdentifyName(String identifyName) {
		this.identifyName = identifyName;
	}
	
	public List<PasswordResetToken> getPasswordResetToken() {
		return passwordResetTokens;
	} 

	public void setPasswordResetToken(List<PasswordResetToken> passwordResetTokens) {
		this.passwordResetTokens = passwordResetTokens;
	}
	public Customer getCustomer() {
	    return customer;
	}

	public void setCustomer(Customer customer) {
	    this.customer = customer;
	}
	
	@Override
	public String toString() {
		return "User{id=" + id + ", name=" + name + ", email=" + email + ", identifyName=" + identifyName + '}';
	}
}