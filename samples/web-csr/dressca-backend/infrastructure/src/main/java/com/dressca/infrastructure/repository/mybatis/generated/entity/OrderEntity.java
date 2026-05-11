package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderEntity {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String buyerId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private OffsetDateTime orderDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String shipToFullName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String shipToPostalCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String shipToTodofuken;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String shipToShikuchoson;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String shipToAzanaAndOthers;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal consumptionTaxRate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal totalItemsPrice;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal deliveryCharge;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal consumptionTax;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal totalPrice;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getBuyerId() {
        return buyerId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public OffsetDateTime getOrderDate() {
        return orderDate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setOrderDate(OffsetDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getShipToFullName() {
        return shipToFullName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setShipToFullName(String shipToFullName) {
        this.shipToFullName = shipToFullName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getShipToPostalCode() {
        return shipToPostalCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setShipToPostalCode(String shipToPostalCode) {
        this.shipToPostalCode = shipToPostalCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getShipToTodofuken() {
        return shipToTodofuken;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setShipToTodofuken(String shipToTodofuken) {
        this.shipToTodofuken = shipToTodofuken;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getShipToShikuchoson() {
        return shipToShikuchoson;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setShipToShikuchoson(String shipToShikuchoson) {
        this.shipToShikuchoson = shipToShikuchoson;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getShipToAzanaAndOthers() {
        return shipToAzanaAndOthers;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setShipToAzanaAndOthers(String shipToAzanaAndOthers) {
        this.shipToAzanaAndOthers = shipToAzanaAndOthers;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BigDecimal getConsumptionTaxRate() {
        return consumptionTaxRate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setConsumptionTaxRate(BigDecimal consumptionTaxRate) {
        this.consumptionTaxRate = consumptionTaxRate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BigDecimal getTotalItemsPrice() {
        return totalItemsPrice;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTotalItemsPrice(BigDecimal totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BigDecimal getDeliveryCharge() {
        return deliveryCharge;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDeliveryCharge(BigDecimal deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BigDecimal getConsumptionTax() {
        return consumptionTax;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setConsumptionTax(BigDecimal consumptionTax) {
        this.consumptionTax = consumptionTax;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}