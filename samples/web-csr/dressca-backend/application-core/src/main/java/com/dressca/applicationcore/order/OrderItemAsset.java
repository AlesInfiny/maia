package com.dressca.applicationcore.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文アイテムアセットのエンティティです。
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
   * {@link OrderItemAsset} クラスのインスタンスを初期化します。
   * 
   * @param assetCode アセットコード。
   * @param orderItemId 注文アイテム ID 。
   */
  public OrderItemAsset(String assetCode, long orderItemId) {
    this.assetCode = assetCode;
    this.orderItemId = orderItemId;
  }
}
