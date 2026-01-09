package com.dressca.applicationcore.accounting;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 会計情報の値オブジェクトです。
 */
@Data
@AllArgsConstructor
public class AccountItem {
  private int quantity;
  private BigDecimal unitPrice;

  /**
   * 会計アイテムの小計金額を取得します。この計算結果には、消費税や送料は含まれません。
   * 
   * @return 会計アイテムの小計金額。
   */
  public BigDecimal getSubTotal() {
    return unitPrice.multiply(BigDecimal.valueOf(this.quantity));
  }
}
