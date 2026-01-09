package com.dressca.applicationcore.order;

import com.dressca.applicationcore.accounting.AccountItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文アイテムのエンティティです。 注文内の各アイテム毎の詳細情報（単価や数量など）を保持します。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
  private long id;
  private CatalogItemOrdered itemOrdered;
  private BigDecimal unitPrice;
  private int quantity;
  private long orderId;
  private List<OrderItemAsset> assets = new ArrayList<>();
  private Order order;

  /**
   * 注文されたカタログアイテム、単価、数量を指定して、 {@link OrderItem} クラスのインスタンスを初期化します。
   * 
   * @param itemOrdered 注文されたカタログアイテム。
   * @param bigDecimal 単価。
   * @param quantity 数量。
   */
  public OrderItem(CatalogItemOrdered itemOrdered, BigDecimal bigDecimal, int quantity) {
    this.itemOrdered = itemOrdered;
    this.unitPrice = bigDecimal;
    this.quantity = quantity;
  }

  /**
   * 注文アイテムのアセットリストを追加します。
   * 
   * @param orderItemAssets 注文アイテムのアセットリスト。
   * @throws IllegalArgumentException orderItemAssets に null が設定された場合。
   */
  public void addAsset(List<OrderItemAsset> orderItemAssets) {
    if (orderItemAssets == null) {
      throw new IllegalArgumentException("orderItemAssets が nullです.");
    }
    this.assets.addAll(orderItemAssets);
  }

  /**
   * 注文アイテムの小計を計算して金額を返却します。
   * 
   * @return 注文アイテムの小計額。
   */
  public BigDecimal getSubTotal() {
    return new AccountItem(this.quantity, this.unitPrice).getSubTotal();
  }
}
