package com.example.demo.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false)
    private String token; // 用于密码重置的随機令牌

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 關聯User

    @Column(name = "expiry_date", nullable = false)
    private ZonedDateTime expiryDate; // 令牌的過期时间

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt; // 令牌的創建时间

    @Column(name = "used", nullable = false)
    private Boolean used = false; // 是否已经使用
    
    private static final ZoneId ZONE_TAIPEI = ZoneId.of("Asia/Taipei");
	// 自動時間設定
	@PrePersist
    protected void onCreate() {
        ZonedDateTime now = ZonedDateTime.now(ZONE_TAIPEI);
        createdAt = expiryDate = now;
        if (expiryDate == null) {
        	expiryDate = now;
        }
    }
    @PreUpdate
    protected void onUpdate() {
    	expiryDate = ZonedDateTime.now(ZONE_TAIPEI);
    }
    
    //  Getter & Setter ====
    
    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user, ZonedDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.createdAt = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ZonedDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(ZonedDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", expiryDate=" + expiryDate +
                ", createdAt=" + createdAt +
                ", used=" + used +
                '}';
    }
}
