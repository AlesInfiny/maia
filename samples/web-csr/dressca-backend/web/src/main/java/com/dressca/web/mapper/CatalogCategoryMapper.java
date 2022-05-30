package com.dressca.web.mapper;

import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.web.controller.dto.catalog.CatalogCategoryResponse;

/**
 * {@link CatalogCategory} と {@link CatalogCategoryResponse} のマッパーです。 
 */
public class CatalogCategoryMapper {
  public static CatalogCategoryResponse convert(CatalogCategory catalogCategory) {
    if (catalogCategory == null) {
      return null;
    }
    return new CatalogCategoryResponse(catalogCategory.getId(), catalogCategory.getName());
  }
}
