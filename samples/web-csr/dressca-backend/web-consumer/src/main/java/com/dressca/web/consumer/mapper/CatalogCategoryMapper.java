package com.dressca.web.consumer.mapper;

import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.web.consumer.controller.dto.catalog.CatalogCategoryResponse;

/**
 * {@link CatalogCategory} と {@link CatalogCategoryResponse} のマッパーです。 
 */
public class CatalogCategoryMapper {

  /**
   * {@link CatalogCategory} オブジェクトを {@link CatalogCategoryResponse} に変換します。
   * 
   * @param catalogCategory {@link CatalogCategory} オブジェクト
   * @return {@link CatalogCategoryResponse} オブジェクト
   */
  public static CatalogCategoryResponse convert(CatalogCategory catalogCategory) {
    if (catalogCategory == null) {
      return null;
    }
    return new CatalogCategoryResponse(catalogCategory.getId(), catalogCategory.getName());
  }
}
