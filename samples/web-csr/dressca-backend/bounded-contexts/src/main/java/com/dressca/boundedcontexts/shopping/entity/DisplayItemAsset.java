package com.dressca.boundedcontexts.shopping.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 陳列品アセットのエンティティです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayItemAsset {
  private UUID id;
  private UUID displayItemId;
  @NonNull
  private String assetCode;

  /**
   * {@link DisplayItemAsset} クラスのインスタンスを初期化します。
   * 
   * @param displayItemId 陳列品 ID 。
   * @param assetCode アセットコード 。
   */
  public DisplayItemAsset(UUID displayItemId, @NonNull String assetCode) {
    this.displayItemId = displayItemId;
    this.assetCode = assetCode;
  }
}
