package com.dressca.web.consumer.mapper;

import com.dressca.domainmodules.shopping.models.DisplayItemCategory;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayItemCategoriesResponse;

/**
 * {@link DisplayItemCategory} と {@link GetDisplayItemCategoriesResponse} のマッパーです。
 */
public class DisplayItemCategoryMapper {

  /**
   * {@link DisplayItemCategory} オブジェクトを {@link GetDisplayItemCategoriesResponse} に変換します。
   *
   * @param displayItemCategory {@link DisplayItemCategory} オブジェクト。
   * @return {@link GetDisplayItemCategoriesResponse} オブジェクト。
   */
  public static GetDisplayItemCategoriesResponse convert(DisplayItemCategory displayItemCategory) {
    if (displayItemCategory == null) {
      return null;
    }
    return new GetDisplayItemCategoriesResponse(displayItemCategory.getId(),
        displayItemCategory.getName());
  }
}
