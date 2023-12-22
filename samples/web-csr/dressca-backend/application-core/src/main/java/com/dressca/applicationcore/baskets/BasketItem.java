package com.dressca.applicationcore.baskets;

import java.math.BigDecimal;
import com.dressca.applicationcore.accounting.AccountItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごアイテムのドメインモデルです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {
  private long id;
  private long basketId;
  private long catalogItemId;
  private BigDecimal unitPrice;
  private int quantity;

  public void addQuantity(int quantity) {
    setQuantity(this.quantity + quantity);
  }

  public BigDecimal getSubtotal() {
    return new AccountItem(this.quantity, this.unitPrice).getSubTotal();
  }
}
