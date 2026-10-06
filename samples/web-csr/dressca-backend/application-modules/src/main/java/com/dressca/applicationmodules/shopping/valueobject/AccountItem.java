package com.dressca.applicationmodules.shopping.valueobject;

import java.math.BigDecimal;

/**
 * 会計情報の値オブジェクトです。
 */
public record AccountItem(int quantity, BigDecimal unitPrice) {

  /**
   * 会計アイテムの小計金額を取得します。この計算結果には、消費税や送料は含まれません。
   * 
   * @return 会計アイテムの小計金額。
   */
  public BigDecimal subTotal() {
    return unitPrice.multiply(BigDecimal.valueOf(this.quantity));
  }
}
