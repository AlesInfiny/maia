package com.dressca.applicationcore.displayitem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 陳列品のエンティティです。
 */
@Data
@NoArgsConstructor
public class DisplayItem {
  private UUID id;
  private List<DisplayItemAsset> assets = new ArrayList<>();
  @NonNull
  private String name;
  @NonNull
  private String description;
  @NonNull
  private BigDecimal price;
  @NonNull
  private String productCode;
  private UUID displayCategoryId;
  private UUID displayBrandId;
  private boolean isDeleted;

  /**
   * {@link DisplayItem} クラスのインスタンスを初期化します。
   *
   * @param id ID 。
   * @param name 商品名。
   * @param description 商品説明。
   * @param price 単価。
   * @param productCode プロダクトコード。
   * @param displayCategoryId 陳列カテゴリ ID 。
   * @param displayBrandId 陳列ブランド ID 。
   * @param isDeleted 削除済みかどうか。 true なら削除済み、 false なら未削除。
   */
  public DisplayItem(UUID id, @NonNull String name, @NonNull String description,
      @NonNull BigDecimal price, @NonNull String productCode, UUID displayCategoryId,
      UUID displayBrandId, boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.productCode = productCode;
    this.displayCategoryId = displayCategoryId;
    this.displayBrandId = displayBrandId;
    this.isDeleted = isDeleted;
  }
}
