package com.example.demo.model;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "prod_type", nullable = false)
	private String prodType; // 大類

	@Column(name = "prod_line")
	private String prodLine; // 細分類別
	
	@Column(name = "prod_description")
	private String description;//詳細描述
	
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // 定義台北時區常數
    private static final ZoneId ZONE_TAIPEI = ZoneId.of("Asia/Taipei");

    

    public Category(String prodType, String prodLine,String description) {
        this.prodType = prodType;
        this.prodLine = prodLine;
        this.description = description;
    }
    
    public Category() {
    }

    // 自動時間設定
    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = ZonedDateTime.now(ZONE_TAIPEI);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now(ZONE_TAIPEI);
        System.out.println("UpdatedAt set to: " + updatedAt);
    }

    // ==== Getter & Setter ====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdLine() {
        return prodLine;
    }

    public void setProdLine(String prodLine) {
        this.prodLine = prodLine;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", prodType='" + prodType + '\'' +
                ", prodLine='" + prodLine + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
