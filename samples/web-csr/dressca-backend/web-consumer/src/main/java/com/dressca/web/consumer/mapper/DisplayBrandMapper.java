package com.dressca.web.consumer.mapper;

import com.dressca.applicationcore.displayitem.DisplayBrand;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayBrandResponse;

/**
 * {@link DisplayBrand} と {@link GetDisplayBrandResponse} のマッパーです。
 */
public class DisplayBrandMapper {

  /**
   * {@link DisplayBrand} オブジェクトを {@link GetDisplayBrandResponse} に変換します。
   * 
   * @param displayBrand オブジェクト。
   * @return {@link GetDisplayBrandResponse} オブジェクト。
   */
  public static GetDisplayBrandResponse convert(DisplayBrand displayBrand) {
    if (displayBrand == null) {
      return null;
    }
    return new GetDisplayBrandResponse(displayBrand.getId(), displayBrand.getName());
  }
}
