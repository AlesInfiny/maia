package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;

public class OrderItemEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_catalog_item_id")
    private Long orderedCatalogItemId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_product_name")
    private String orderedProductName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_product_code")
    private String orderedProductCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.unit_price")
    private BigDecimal unitPrice;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.quantity")
    private Integer quantity;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.order_id")
    private Long orderId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_catalog_item_id")
    public Long getOrderedCatalogItemId() {
        return orderedCatalogItemId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_catalog_item_id")
    public void setOrderedCatalogItemId(Long orderedCatalogItemId) {
        this.orderedCatalogItemId = orderedCatalogItemId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_product_name")
    public String getOrderedProductName() {
        return orderedProductName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_product_name")
    public void setOrderedProductName(String orderedProductName) {
        this.orderedProductName = orderedProductName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_product_code")
    public String getOrderedProductCode() {
        return orderedProductCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.ordered_product_code")
    public void setOrderedProductCode(String orderedProductCode) {
        this.orderedProductCode = orderedProductCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.unit_price")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.unit_price")
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.order_id")
    public Long getOrderId() {
        return orderId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: order_items.order_id")
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}