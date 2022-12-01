package com.dressca.web.controller.dto.accounting;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
  @NotNull
  private double consumptionTaxRate;
  @NotNull
  private BigDecimal totalItemsPrice;
  @NotNull
  private BigDecimal deliveryCharge;
  @NotNull
  private BigDecimal consumptionTax;
  @NotNull
  private BigDecimal totalPrice;
}
