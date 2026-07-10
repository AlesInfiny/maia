package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.util.UUID;

public class BasketItemEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID basketId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private UUID catalogItemId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal unitPrice;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer quantity;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(UUID id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getBasketId() {
        return basketId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBasketId(UUID basketId) {
        this.basketId = basketId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public UUID getCatalogItemId() {
        return catalogItemId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCatalogItemId(UUID catalogItemId) {
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