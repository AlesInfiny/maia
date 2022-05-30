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
