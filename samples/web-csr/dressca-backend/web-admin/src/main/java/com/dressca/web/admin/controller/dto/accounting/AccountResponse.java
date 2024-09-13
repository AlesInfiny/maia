package com.dressca.web.admin.controller.dto.accounting;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごの会計情報を格納するdtoクラスです。
 */
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
