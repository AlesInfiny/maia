package com.dressca.applicationcore.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * カタログアイテムのドメインモデルです。
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

  /**
   * コンストラクタ。
   * 
   * @param id                ID
   * @param name              商品名
   * @param description       商品紹介
   * @param price             単価
   * @param productCode       プロダクトコード
   * @param catalogCategoryId カタログ商品ID
   * @param catalogBrandId    カタログブランドID
   */
  public CatalogItem(long id, @NonNull String name, @NonNull String description,
      @NonNull BigDecimal price, @NonNull String productCode, long catalogCategoryId,
      long catalogBrandId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.productCode = productCode;
    this.catalogCategoryId = catalogCategoryId;
    this.catalogBrandId = catalogBrandId;
  }
}
