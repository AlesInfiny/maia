package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;

public class OrderItemEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long orderedCatalogItemId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String orderedProductName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String orderedProductCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal unitPrice;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer quantity;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long orderId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getOrderedCatalogItemId() {
        return orderedCatalogItemId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOrderedCatalogItemId(Long orderedCatalogItemId) {
        this.orderedCatalogItemId = orderedCatalogItemId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getOrderedProductName() {
        return orderedProductName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOrderedProductName(String orderedProductName) {
        this.orderedProductName = orderedProductName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getOrderedProductCode() {
        return orderedProductCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOrderedProductCode(String orderedProductCode) {
        this.orderedProductCode = orderedProductCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getQuantity() {
        return quantity;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getOrderId() {
        return orderId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}