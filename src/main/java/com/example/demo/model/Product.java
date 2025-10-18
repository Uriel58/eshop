package com.example.demo.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "products")
public class Product {

    // product有prod_num,prod_name,prod_type(大類),prod_line(大類的細分),prod_price,
    // prod_info(產品資訊),prod_keywords,prod_barcode(條碼）,created_time,
    // prod_tags,prod_images,prod_stock_qty

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodNum; // 商品編號（主鍵）

    @Column(name = "prod_name", nullable = false)
    private String prodName; // 產品名稱

    @Column(name = "prod_type", nullable = false)
    private String prodType; // 大類

    @Column(name = "prod_line")
    private String prodLine; // 細分類別

    @Column(name = "prod_price", nullable = false)
    private BigDecimal prodPrice; // 價格

    @Column(name = "prod_info", columnDefinition = "TEXT")
    private String prodInfo; // 商品資訊

    @Column(name = "prod_keywords")
    private String prodKeywords; // 搜尋關鍵字

    @Column(name = "prod_barcode", unique = true)
    private String prodBarcode; // 條碼
    
    // ✅ 加上日期格式化註解，告訴 Spring 用哪種格式解析
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "created_time")
    //private ZonedDateTime createdTime;
    private ZonedDateTime createdTime = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));// 建立時間（含時區）

    @Column(name = "prod_tags")
    private String prodTags; // 產品標籤，可考慮用 JSON 或逗號分隔字串

    @Column(name = "prod_images")
    private String prodImages; // 圖片路徑或 URL，可能為 JSON 或逗號分隔字串

    @Column(name = "prod_stock_qty")
    private Integer prodStockQty; // 產品庫存

    // Constructors
    public Product() {}

    public Product(String prodName, String prodType, String prodLine, BigDecimal prodPrice,
                   String prodInfo, String prodKeywords, String prodBarcode, ZonedDateTime createdTime,
                   String prodTags, String prodImages, Integer prodStockQty) {
        this.prodName = prodName;
        this.prodType = prodType;
        this.prodLine = prodLine;
        this.prodPrice = prodPrice;
        this.prodInfo = prodInfo;
        this.prodKeywords = prodKeywords;
        this.prodBarcode = prodBarcode;
        this.createdTime = createdTime;
        this.prodTags = prodTags;
        this.prodImages = prodImages;
        this.prodStockQty = prodStockQty;
    }

    // 自動注入現在時間，在新增時會呼叫此方法
    @PrePersist
    protected void onCreate() {
        // 固定使用台北時區，確保跨伺服器一致
        this.createdTime = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
    }

    // Getters and Setters
    public Long getProdNum() { return prodNum; }
    public void setProdNum(Long prodNum) { this.prodNum = prodNum; }

    public String getProdName() { return prodName; }
    public void setProdName(String prodName) { this.prodName = prodName; }

    public String getProdType() { return prodType; }
    public void setProdType(String prodType) { this.prodType = prodType; }

    public String getProdLine() { return prodLine; }
    public void setProdLine(String prodLine) { this.prodLine = prodLine; }

    public BigDecimal getProdPrice() { return prodPrice; }
    public void setProdPrice(BigDecimal prodPrice) { this.prodPrice = prodPrice; }

    public String getProdInfo() { return prodInfo; }
    public void setProdInfo(String prodInfo) { this.prodInfo = prodInfo; }

    public String getProdKeywords() { return prodKeywords; }
    public void setProdKeywords(String prodKeywords) { this.prodKeywords = prodKeywords; }

    public String getProdBarcode() { return prodBarcode; }
    public void setProdBarcode(String prodBarcode) { this.prodBarcode = prodBarcode; }

    public ZonedDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(ZonedDateTime createdTime) { this.createdTime = createdTime; }

    public String getProdTags() { return prodTags; }
    public void setProdTags(String prodTags) { this.prodTags = prodTags; }

    public String getProdImages() { return prodImages; }
    public void setProdImages(String prodImages) { this.prodImages = prodImages; }

    public Integer getProdStockQty() { return prodStockQty; }
    public void setProdStockQty(Integer prodStockQty) { this.prodStockQty = prodStockQty; }

    @Override
    public String toString() {
        return "Product{" +
                "prodNum=" + prodNum +
                ", prodName='" + prodName + '\'' +
                ", prodType='" + prodType + '\'' +
                ", prodLine='" + prodLine + '\'' +
                ", prodPrice=" + prodPrice +
                ", prodStockQty=" + prodStockQty +
                ", createdTime=" + createdTime +
                '}';
    }
}
