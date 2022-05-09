package com.dressca.applicationcore.order;

import java.math.BigDecimal;
import java.util.List;
import com.dressca.applicationcore.accounting.AccountItem;
import lombok.Data;
import lombok.NonNull;

/**
 * 注文アイテムのドメインモデル.
 * 注文内の各アイテム毎の詳細情報（単価や数量など）を保持します.
 */
@Data
public class OrderItem {
  private long id;
  @NonNull
  private CatalogItemOrdered itemOrdered;
  private BigDecimal unitPrice;
  private int quantity;
  private long orderId;
  private List<OrderItemAsset> assets = List.of();
  private Order order;

  /**
   * コンストラクタ.
   * 
   * @param itemOrdered 注文されたカタログアイテム.
   * @param unitPrice 単価.
   * @param quantity 数量.
   */
  public OrderItem(CatalogItemOrdered itemOrdered, BigDecimal unitPrice, int quantity) {
    this.itemOrdered = itemOrdered;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
  }

  /**
   * 注文アイテムのアセットリストを追加します.
   * 
   * @param orderItemAssets 注文アイテムのアセットリスト.
   * @throws IllegalArgumentException orderItemAssetsにnullが設定された場合
   */
  public void addAsset(List<OrderItemAsset> orderItemAssets) {
    if (orderItemAssets == null) {
      throw new IllegalArgumentException("orderItemAssets が nullです.");
    }
    this.assets.addAll(orderItemAssets);
  }

  /**
   * 注文アイテムの小計を計算して金額を返却します.
   * 
   * @return 注文アイテムの小計額.
   */
  public BigDecimal getSubTotal() {
    return new AccountItem(this.quantity, this.unitPrice).getSubTotal();
  }
}
