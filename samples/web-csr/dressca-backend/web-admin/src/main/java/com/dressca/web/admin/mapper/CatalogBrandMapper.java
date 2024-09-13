package com.dressca.web.admin.mapper;

import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.web.admin.controller.dto.catalog.CatalogBrandResponse;

/**
 * {@link CatalogBrand} と {@link CatalogBrandResponse} のマッパーです。
 */
public class CatalogBrandMapper {

  /**
   * {@link CatalogBrand} オブジェクトを {@link CatalogBrandResponse} に変換します。
   * 
   * @param catalogBrand オブジェクト
   * @return {@link CatalogBrandResponse} オブジェクト
   */
  public static CatalogBrandResponse convert(CatalogBrand catalogBrand) {
    if (catalogBrand == null) {
      return null;
    }
    return new CatalogBrandResponse(catalogBrand.getId(), catalogBrand.getName());
  }
}
