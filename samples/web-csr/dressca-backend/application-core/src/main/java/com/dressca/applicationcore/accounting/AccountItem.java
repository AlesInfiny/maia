package com.dressca.applicationcore.accounting;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 会計情報のドメインモデルです。
 */
@Data
@AllArgsConstructor
public class AccountItem {
  private int quantity;
  private BigDecimal unitPrice;

  public BigDecimal getSubTotal() {
    return unitPrice.multiply(BigDecimal.valueOf(this.quantity));
  }

}
