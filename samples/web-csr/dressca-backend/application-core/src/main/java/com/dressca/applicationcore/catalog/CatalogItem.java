package com.dressca.applicationcore.catalog;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * カタログアイテムのエンティティです。
 */
@Data
@NoArgsConstructor
public class CatalogItem {
  private long id;
  private List<CatalogItemAsset> assets = new ArrayList<>();
  private CatalogCategory catalogCategory;
  private CatalogBrand catalogBrand;
  @NonNull
  private String name;
  @NonNull
  private String description;
  @NonNull
  private BigDecimal price;
  @NonNull
  private String productCode;
  private long catalogCategoryId;
  private long catalogBrandId;
  private boolean isDeleted;
  private OffsetDateTime rowVersion;

  /**
   * {@link CatalogItem} クラスのインスタンスを初期化します。
   * 
   * @param id ID 。
   * @param name 商品名。
   * @param description 商品説明。
   * @param price 単価。
   * @param productCode プロダクトコード。
   * @param catalogCategoryId カタログカテゴリ ID 。
   * @param catalogBrandId カタログブランド ID 。
   * @param isDeleted 削除済みかどうか。 true なら削除済み、 false なら未削除。
   */
  public CatalogItem(long id, @NonNull String name, @NonNull String description,
      @NonNull BigDecimal price, @NonNull String productCode, long catalogCategoryId,
      long catalogBrandId, boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.productCode = productCode;
    this.catalogCategoryId = catalogCategoryId;
    this.catalogBrandId = catalogBrandId;
    this.isDeleted = isDeleted;
  }

  /**
   * 登録用のカタログアイテムを作成します。
   * 
   * @param name 商品名。
   * @param description 商品説明。
   * @param price 単価。
   * @param productCode プロダクトコード。
   * @param catalogCategoryId カタログカテゴリ ID 。
   * @param catalogBrandId カタログブランド ID 。
   * @return 登録用のカタログアイテム。
   */
  public static CatalogItem createCatalogItemForRegistration(@NonNull String name,
      @NonNull String description, @NonNull BigDecimal price, @NonNull String productCode,
      long catalogCategoryId, long catalogBrandId, boolean isDeleted) {
    CatalogItem item = new CatalogItem(0, name, description, price, productCode, catalogCategoryId,
        catalogBrandId, isDeleted);
    return item;
  }
}
