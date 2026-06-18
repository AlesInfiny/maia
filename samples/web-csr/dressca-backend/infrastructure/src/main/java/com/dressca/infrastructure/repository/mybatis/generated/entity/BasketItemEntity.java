package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;

public class BasketItemEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long basketId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long catalogItemId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal unitPrice;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer quantity;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getBasketId() {
        return basketId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getCatalogItemId() {
        return catalogItemId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatalogItemId(Long catalogItemId) {
        this.catalogItemId = catalogItemId;
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
}