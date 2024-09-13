package com.dressca.web.admin.mapper;

import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.web.admin.controller.dto.catalog.CatalogCategoryResponse;

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
