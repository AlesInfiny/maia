package com.dressca.applicationcore.catalog;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

/**
 * カタログアイテムのドメインモデルです。
 */
@Data
public class CatalogItem {
  private long id;
  private List<CatalogItemAsset> assets = List.of();
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
}
