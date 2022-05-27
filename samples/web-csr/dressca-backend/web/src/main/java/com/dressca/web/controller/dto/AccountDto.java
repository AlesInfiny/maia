package com.dressca.web.controller.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
  private double consumptionTaxRate;
  private BigDecimal totalItemsPrice;
  private BigDecimal deliveryCharge;
  private BigDecimal consumptionTax;
  private BigDecimal totalPrice;
}
