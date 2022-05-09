package com.dressca.applicationcore.order;

import lombok.Data;
import lombok.NonNull;

/**
 * 注文アイテムアセットのドメインモデルです.
 */
@Data
public class OrderItemAsset {
  private long id;
  @NonNull
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
