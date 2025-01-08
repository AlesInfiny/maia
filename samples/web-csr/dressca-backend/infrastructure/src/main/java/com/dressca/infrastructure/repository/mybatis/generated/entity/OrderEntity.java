package com.dressca.infrastructure.repository.mybatis.generated.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.buyer_id
     *
     * @mbg.generated
     */
    private String buyerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.order_date
     *
     * @mbg.generated
     */
    private LocalDateTime orderDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_full_name
     *
     * @mbg.generated
     */
    private String shipToFullName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_postal_code
     *
     * @mbg.generated
     */
    private String shipToPostalCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_todofuken
     *
     * @mbg.generated
     */
    private String shipToTodofuken;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_shikuchoson
     *
     * @mbg.generated
     */
    private String shipToShikuchoson;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_azana_and_others
     *
     * @mbg.generated
     */
    private String shipToAzanaAndOthers;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.consumption_tax_rate
     *
     * @mbg.generated
     */
    private BigDecimal consumptionTaxRate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.total_items_price
     *
     * @mbg.generated
     */
    private BigDecimal totalItemsPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.delivery_charge
     *
     * @mbg.generated
     */
    private BigDecimal deliveryCharge;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.consumption_tax
     *
     * @mbg.generated
     */
    private BigDecimal consumptionTax;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.total_price
     *
     * @mbg.generated
     */
    private BigDecimal totalPrice;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.id
     *
     * @return the value of orders.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.id
     *
     * @param id the value for orders.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.buyer_id
     *
     * @return the value of orders.buyer_id
     *
     * @mbg.generated
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.buyer_id
     *
     * @param buyerId the value for orders.buyer_id
     *
     * @mbg.generated
     */
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.order_date
     *
     * @return the value of orders.order_date
     *
     * @mbg.generated
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.order_date
     *
     * @param orderDate the value for orders.order_date
     *
     * @mbg.generated
     */
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.ship_to_full_name
     *
     * @return the value of orders.ship_to_full_name
     *
     * @mbg.generated
     */
    public String getShipToFullName() {
        return shipToFullName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.ship_to_full_name
     *
     * @param shipToFullName the value for orders.ship_to_full_name
     *
     * @mbg.generated
     */
    public void setShipToFullName(String shipToFullName) {
        this.shipToFullName = shipToFullName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.ship_to_postal_code
     *
     * @return the value of orders.ship_to_postal_code
     *
     * @mbg.generated
     */
    public String getShipToPostalCode() {
        return shipToPostalCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.ship_to_postal_code
     *
     * @param shipToPostalCode the value for orders.ship_to_postal_code
     *
     * @mbg.generated
     */
    public void setShipToPostalCode(String shipToPostalCode) {
        this.shipToPostalCode = shipToPostalCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.ship_to_todofuken
     *
     * @return the value of orders.ship_to_todofuken
     *
     * @mbg.generated
     */
    public String getShipToTodofuken() {
        return shipToTodofuken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.ship_to_todofuken
     *
     * @param shipToTodofuken the value for orders.ship_to_todofuken
     *
     * @mbg.generated
     */
    public void setShipToTodofuken(String shipToTodofuken) {
        this.shipToTodofuken = shipToTodofuken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.ship_to_shikuchoson
     *
     * @return the value of orders.ship_to_shikuchoson
     *
     * @mbg.generated
     */
    public String getShipToShikuchoson() {
        return shipToShikuchoson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.ship_to_shikuchoson
     *
     * @param shipToShikuchoson the value for orders.ship_to_shikuchoson
     *
     * @mbg.generated
     */
    public void setShipToShikuchoson(String shipToShikuchoson) {
        this.shipToShikuchoson = shipToShikuchoson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.ship_to_azana_and_others
     *
     * @return the value of orders.ship_to_azana_and_others
     *
     * @mbg.generated
     */
    public String getShipToAzanaAndOthers() {
        return shipToAzanaAndOthers;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.ship_to_azana_and_others
     *
     * @param shipToAzanaAndOthers the value for orders.ship_to_azana_and_others
     *
     * @mbg.generated
     */
    public void setShipToAzanaAndOthers(String shipToAzanaAndOthers) {
        this.shipToAzanaAndOthers = shipToAzanaAndOthers;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.consumption_tax_rate
     *
     * @return the value of orders.consumption_tax_rate
     *
     * @mbg.generated
     */
    public BigDecimal getConsumptionTaxRate() {
        return consumptionTaxRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.consumption_tax_rate
     *
     * @param consumptionTaxRate the value for orders.consumption_tax_rate
     *
     * @mbg.generated
     */
    public void setConsumptionTaxRate(BigDecimal consumptionTaxRate) {
        this.consumptionTaxRate = consumptionTaxRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.total_items_price
     *
     * @return the value of orders.total_items_price
     *
     * @mbg.generated
     */
    public BigDecimal getTotalItemsPrice() {
        return totalItemsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.total_items_price
     *
     * @param totalItemsPrice the value for orders.total_items_price
     *
     * @mbg.generated
     */
    public void setTotalItemsPrice(BigDecimal totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.delivery_charge
     *
     * @return the value of orders.delivery_charge
     *
     * @mbg.generated
     */
    public BigDecimal getDeliveryCharge() {
        return deliveryCharge;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.delivery_charge
     *
     * @param deliveryCharge the value for orders.delivery_charge
     *
     * @mbg.generated
     */
    public void setDeliveryCharge(BigDecimal deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.consumption_tax
     *
     * @return the value of orders.consumption_tax
     *
     * @mbg.generated
     */
    public BigDecimal getConsumptionTax() {
        return consumptionTax;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.consumption_tax
     *
     * @param consumptionTax the value for orders.consumption_tax
     *
     * @mbg.generated
     */
    public void setConsumptionTax(BigDecimal consumptionTax) {
        this.consumptionTax = consumptionTax;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.total_price
     *
     * @return the value of orders.total_price
     *
     * @mbg.generated
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.total_price
     *
     * @param totalPrice the value for orders.total_price
     *
     * @mbg.generated
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}