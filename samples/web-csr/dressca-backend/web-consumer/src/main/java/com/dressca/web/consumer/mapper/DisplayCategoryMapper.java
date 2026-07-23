package com.dressca.web.consumer.mapper;

import com.dressca.applicationcore.display.DisplayCategory;
import com.dressca.web.consumer.controller.dto.display.GetDisplayCategoriesResponse;

/**
 * {@link DisplayCategory} と {@link GetDisplayCategoriesResponse} のマッパーです。
 */
public class DisplayCategoryMapper {

  /**
   * {@link DisplayCategory} オブジェクトを {@link GetDisplayCategoriesResponse} に変換します。
   *
   * @param displayCategory {@link DisplayCategory} オブジェクト。
   * @return {@link GetDisplayCategoriesResponse} オブジェクト。
   */
  public static GetDisplayCategoriesResponse convert(DisplayCategory displayCategory) {
    if (displayCategory == null) {
      return null;
    }
    return new GetDisplayCategoriesResponse(displayCategory.getId(), displayCategory.getName());
  }
}
