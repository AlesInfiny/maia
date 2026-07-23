package com.dressca.web.consumer.mapper;

import com.dressca.applicationcore.display.DisplayBrand;
import com.dressca.web.consumer.controller.dto.display.GetDisplayBrandsResponse;

/**
 * {@link DisplayBrand} と {@link GetDisplayBrandsResponse} のマッパーです。
 */
public class DisplayBrandMapper {

  /**
   * {@link DisplayBrand} オブジェクトを {@link GetDisplayBrandsResponse} に変換します。
   *
   * @param displayBrand オブジェクト。
   * @return {@link GetDisplayBrandsResponse} オブジェクト。
   */
  public static GetDisplayBrandsResponse convert(DisplayBrand displayBrand) {
    if (displayBrand == null) {
      return null;
    }
    return new GetDisplayBrandsResponse(displayBrand.getId(), displayBrand.getName());
  }
}
