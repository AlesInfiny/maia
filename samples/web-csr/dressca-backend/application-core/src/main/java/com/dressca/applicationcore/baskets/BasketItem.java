package com.dressca.applicationcore.baskets;

import java.math.BigDecimal;
import java.util.UUID;
import com.dressca.applicationcore.accounting.AccountItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごアイテムのエンティティです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {
  private UUID id;
  private UUID basketId;
  private UUID catalogItemId;
  private BigDecimal unitPrice;
  private int quantity;

  /**
   * 買い物かごアイテムの数量を追加します。
   * 
   * @param quantity 数量。
   */
  public void addQuantity(int quantity) {
    setQuantity(this.quantity + quantity);
  }

  /**
   * 買い物かごアイテムの小計金額を取得します。この計算結果には、消費税や送料は含まれません。
   * 
   * @return 買い物かごアイテムの小計金額。
   */
  public BigDecimal getSubtotal() {
    return new AccountItem(this.quantity, this.unitPrice).getSubTotal();
  }
}
