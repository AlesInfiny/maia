package com.dressca.web.controller.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {
    private double consumptionTaxRate;
    private BigDecimal totalItemPrice;
    private BigDecimal deliveryCharge;
    private BigDecimal consumptionTax;
    private BigDecimal totalPrice;
}
