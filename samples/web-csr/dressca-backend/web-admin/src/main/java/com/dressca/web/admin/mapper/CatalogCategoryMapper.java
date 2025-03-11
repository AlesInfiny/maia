package com.dressca.web.admin.mapper;

import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.web.admin.controller.dto.catalog.GetCatalogCategoriesResponse;

/**
 * {@link CatalogCategory} と {@link GetCatalogCategoriesResponse} のマッパーです。
 */
public class CatalogCategoryMapper {

  /**
   * {@link CatalogCategory} オブジェクトを {@link GetCatalogCategoriesResponse} に変換します。
   * 
   * @param catalogCategory {@link CatalogCategory} オブジェクト。
   * @return {@link GetCatalogCategoriesResponse} オブジェクト。
   */
  public static GetCatalogCategoriesResponse convert(CatalogCategory catalogCategory) {
    if (catalogCategory == null) {
      return null;
    }
    return new GetCatalogCategoriesResponse(catalogCategory.getId(), catalogCategory.getName());
  }
}
