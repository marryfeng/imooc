package com.imooc.project.cartVo;

import java.math.BigDecimal;

/**
 * 用户在未登录的状态下，向前端展示的商品vo类
 */
public class CartItmCookieVo {
    private Integer id;
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private String productMainImage;
    public CartItmCookieVo(){};

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }
}
