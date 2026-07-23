package com.dressca.applicationcore.display;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import java.util.UUID;

/**
 * 陳列品アセットのエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayItemAsset {
  private UUID id;
  private UUID displayItemId;
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

  /**
   * {@link DisplayItemAsset} クラスのインスタンスを初期化します。
   * 
   * @param id ID 。
   * @param displayItemId 陳列品 ID
   * @param assetCode アセットコード。
   */
  public DisplayItemAsset(UUID id, UUID displayItemId, @NonNull String assetCode) {
    this.id = id;
    this.displayItemId = displayItemId;
    this.assetCode = assetCode;
  }
}
