package com.dressca.infrastructure.repository.mybatis.generated.entity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.buyer_id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private String buyerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.order_date
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private Date orderDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_full_name
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private String shipToFullName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_postal_code
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private String shipToPostalCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_todofuken
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private String shipToTodofuken;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_shikuchoson
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private String shipToShikuchoson;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.ship_to_azana_and_others
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private String shipToAzanaAndOthers;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.consumption_tax_rate
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private BigDecimal consumptionTaxRate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.total_items_price
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private BigDecimal totalItemsPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.delivery_charge
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private BigDecimal deliveryCharge;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.consumption_tax
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private BigDecimal consumptionTax;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.total_price
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    private BigDecimal totalPrice;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.id
     *
     * @return the value of orders.id
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.order_date
     *
     * @param orderDate the value for orders.order_date
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.ship_to_full_name
     *
     * @return the value of orders.ship_to_full_name
     *
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
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
     * @mbg.generated Mon Feb 19 14:48:46 JST 2024
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}