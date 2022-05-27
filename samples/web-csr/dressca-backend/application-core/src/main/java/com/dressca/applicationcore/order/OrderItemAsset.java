package com.dressca.applicationcore.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文アイテムアセットのドメインモデルです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAsset {
  private long id;
  private String assetCode;
  private long orderItemId;
  private OrderItem orderItem;

  /**
   * コンストラクタ.
   * 
   * @param assetCode アセットコード.
   * @param orderItemId 注文アイテムId.
   */
  public OrderItemAsset(String assetCode, long orderItemId) {
    this.assetCode = assetCode;
    this.orderItemId = orderItemId;
  }
}
