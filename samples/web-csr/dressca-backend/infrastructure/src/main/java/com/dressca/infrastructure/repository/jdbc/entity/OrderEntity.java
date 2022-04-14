package com.dressca.infrastructure.repository.jdbc.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("orders")
@Data
public class OrderEntity {
  @Id
  private long id;
  private String buyerId;
  private LocalDateTime orderDate;
  private String shipToFullName;
  private String shipToPostalCode;
  private String shipToTodofuken;
  private String shipToShikuchoson;
  private String shipToAzanaAndOthers;
  private double consumptionTaxRate;
  private double totalItemsPrice;
  private double deliveryCharge;
  private double consumptionTax;
  private double totalPrice;
}
