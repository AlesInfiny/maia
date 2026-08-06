package com.dressca.web.consumer.mapper;

import com.dressca.domainmodules.shopping.displayitem.DisplayItemBrand;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayItemBrandsResponse;

/**
 * {@link DisplayItemBrand} と {@link GetDisplayItemBrandsResponse} のマッパーです。
 */
public class DisplayItemBrandMapper {

  /**
   * {@link DisplayItemBrand} オブジェクトを {@link GetDisplayItemBrandsResponse} に変換します。
   *
   * @param displayItemBrand オブジェクト。
   * @return {@link GetDisplayItemBrandsResponse} オブジェクト。
   */
  public static GetDisplayItemBrandsResponse convert(DisplayItemBrand displayItemBrand) {
    if (displayItemBrand == null) {
      return null;
    }
    return new GetDisplayItemBrandsResponse(displayItemBrand.getId(), displayItemBrand.getName());
  }
}
