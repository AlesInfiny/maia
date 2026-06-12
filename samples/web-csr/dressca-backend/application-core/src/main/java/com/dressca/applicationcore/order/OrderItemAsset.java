package com.dressca.applicationcore.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * 注文アイテムアセットのエンティティです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAsset {
  private UUID id;
  private String assetCode;
  private UUID orderItemId;
  private OrderItem orderItem;

  /**
   * {@link OrderItemAsset} クラスのインスタンスを初期化します。
   * 
   * @param assetCode アセットコード。
   * @param orderItemId 注文アイテム ID 。
   */
  public OrderItemAsset(String assetCode, UUID orderItemId) {
    this.assetCode = assetCode;
    this.orderItemId = orderItemId;
  }
}
