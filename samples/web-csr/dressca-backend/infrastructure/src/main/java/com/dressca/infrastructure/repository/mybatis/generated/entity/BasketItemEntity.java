package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;

public class BasketItemEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.basket_id")
    private Long basketId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.catalog_item_id")
    private Long catalogItemId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.unit_price")
    private BigDecimal unitPrice;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.quantity")
    private Integer quantity;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.basket_id")
    public Long getBasketId() {
        return basketId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.basket_id")
    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.catalog_item_id")
    public Long getCatalogItemId() {
        return catalogItemId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.catalog_item_id")
    public void setCatalogItemId(Long catalogItemId) {
        this.catalogItemId = catalogItemId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.unit_price")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.unit_price")
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: basket_items.quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}