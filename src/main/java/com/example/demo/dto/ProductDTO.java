package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * 用於 API 回傳的 Product DTO,避免 LazyInitializationException
 */
public class ProductDTO {
    private Long prodNum;
    private String prodName;
    private BigDecimal prodPrice;
    private String prodInfo;
    private String prodKeywords;
    private String prodBarcode;
    private ZonedDateTime createdTime;
    private String prodTags;
    private String prodImages;
    private Integer prodStockQty;
    
    // Category 相關欄位(只包含需要的資料)
    private Long categoryId;
    private String prodType;
    private String prodLine;
    private String description;

    // 建構子
    public ProductDTO() {
    }

    // Getters and Setters
    public Long getProdNum() {
        return prodNum;
    }

    public void setProdNum(Long prodNum) {
        this.prodNum = prodNum;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public BigDecimal getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(BigDecimal prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getProdInfo() {
        return prodInfo;
    }

    public void setProdInfo(String prodInfo) {
        this.prodInfo = prodInfo;
    }

    public String getProdKeywords() {
        return prodKeywords;
    }

    public void setProdKeywords(String prodKeywords) {
        this.prodKeywords = prodKeywords;
    }

    public String getProdBarcode() {
        return prodBarcode;
    }

    public void setProdBarcode(String prodBarcode) {
        this.prodBarcode = prodBarcode;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getProdTags() {
        return prodTags;
    }

    public void setProdTags(String prodTags) {
        this.prodTags = prodTags;
    }

    public String getProdImages() {
        return prodImages;
    }

    public void setProdImages(String prodImages) {
        this.prodImages = prodImages;
    }

    public Integer getProdStockQty() {
        return prodStockQty;
    }

    public void setProdStockQty(Integer prodStockQty) {
        this.prodStockQty = prodStockQty;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
}