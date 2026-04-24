package com.dressca.infrastructure.repository.mybatis.generated.entity;

import jakarta.annotation.Generated;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderEntity {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.buyer_id")
    private String buyerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.order_date")
    private OffsetDateTime orderDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_full_name")
    private String shipToFullName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_postal_code")
    private String shipToPostalCode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_todofuken")
    private String shipToTodofuken;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_shikuchoson")
    private String shipToShikuchoson;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_azana_and_others")
    private String shipToAzanaAndOthers;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.consumption_tax_rate")
    private BigDecimal consumptionTaxRate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.total_items_price")
    private BigDecimal totalItemsPrice;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.delivery_charge")
    private BigDecimal deliveryCharge;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.consumption_tax")
    private BigDecimal consumptionTax;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.total_price")
    private BigDecimal totalPrice;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.buyer_id")
    public String getBuyerId() {
        return buyerId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.buyer_id")
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.order_date")
    public OffsetDateTime getOrderDate() {
        return orderDate;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.order_date")
    public void setOrderDate(OffsetDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_full_name")
    public String getShipToFullName() {
        return shipToFullName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_full_name")
    public void setShipToFullName(String shipToFullName) {
        this.shipToFullName = shipToFullName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_postal_code")
    public String getShipToPostalCode() {
        return shipToPostalCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_postal_code")
    public void setShipToPostalCode(String shipToPostalCode) {
        this.shipToPostalCode = shipToPostalCode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_todofuken")
    public String getShipToTodofuken() {
        return shipToTodofuken;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_todofuken")
    public void setShipToTodofuken(String shipToTodofuken) {
        this.shipToTodofuken = shipToTodofuken;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_shikuchoson")
    public String getShipToShikuchoson() {
        return shipToShikuchoson;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_shikuchoson")
    public void setShipToShikuchoson(String shipToShikuchoson) {
        this.shipToShikuchoson = shipToShikuchoson;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_azana_and_others")
    public String getShipToAzanaAndOthers() {
        return shipToAzanaAndOthers;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.ship_to_azana_and_others")
    public void setShipToAzanaAndOthers(String shipToAzanaAndOthers) {
        this.shipToAzanaAndOthers = shipToAzanaAndOthers;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.consumption_tax_rate")
    public BigDecimal getConsumptionTaxRate() {
        return consumptionTaxRate;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.consumption_tax_rate")
    public void setConsumptionTaxRate(BigDecimal consumptionTaxRate) {
        this.consumptionTaxRate = consumptionTaxRate;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.total_items_price")
    public BigDecimal getTotalItemsPrice() {
        return totalItemsPrice;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.total_items_price")
    public void setTotalItemsPrice(BigDecimal totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.delivery_charge")
    public BigDecimal getDeliveryCharge() {
        return deliveryCharge;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.delivery_charge")
    public void setDeliveryCharge(BigDecimal deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.consumption_tax")
    public BigDecimal getConsumptionTax() {
        return consumptionTax;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.consumption_tax")
    public void setConsumptionTax(BigDecimal consumptionTax) {
        this.consumptionTax = consumptionTax;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.total_price")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: orders.total_price")
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}