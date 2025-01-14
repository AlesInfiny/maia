package com.dressca.web.admin.mapper;

import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.web.admin.controller.dto.catalog.GetCatalogBrandsResponse;

/**
 * {@link CatalogBrand} と {@link GetCatalogBrandsResponse} のマッパーです。
 */
public class CatalogBrandMapper {

  /**
   * {@link CatalogBrand} オブジェクトを {@link GetCatalogBrandsResponse} に変換します。
   * 
   * @param catalogBrand オブジェクト。
   * @return {@link GetCatalogBrandsResponse} オブジェクト。
   */
  public static GetCatalogBrandsResponse convert(CatalogBrand catalogBrand) {
    if (catalogBrand == null) {
      return null;
    }
    return new GetCatalogBrandsResponse(catalogBrand.getId(), catalogBrand.getName());
  }
}
