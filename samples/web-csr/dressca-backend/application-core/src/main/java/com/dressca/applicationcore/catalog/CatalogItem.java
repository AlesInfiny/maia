package com.dressca.applicationcore.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

/**
 * カタログアイテムのドメインモデルです。
 */
@Data
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
  @NonNull
  private long catalogCategoryId;
  @NonNull
  private long catalogBrandId;
  
  public CatalogItem(long id, @NonNull String name, @NonNull String description,
      @NonNull BigDecimal price, @NonNull String productCode, @NonNull long catalogCategoryId,
      @NonNull long catalogBrandId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.productCode = productCode;
    this.catalogCategoryId = catalogCategoryId;
    this.catalogBrandId = catalogBrandId;
  }

  
}
