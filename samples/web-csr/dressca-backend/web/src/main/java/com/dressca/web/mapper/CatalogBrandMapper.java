package com.dressca.web.mapper;

import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.web.controller.dto.catalog.CatalogBrandResponse;

/**
 * {@link CatalogBrand} と {@link CatalogBrandResponse} のマッパーです。
 */
public class CatalogBrandMapper {
  public static CatalogBrandResponse convert(CatalogBrand catalogBrand) {
    if (catalogBrand == null) {
      return null;
    }
    return new CatalogBrandResponse(catalogBrand.getId(), catalogBrand.getName());
  }
}
