package com.dressca.web.consumer.mapper;

import com.dressca.applicationcore.displayitem.DisplayCategory;
import com.dressca.web.consumer.controller.dto.displayitem.GetDisplayCategoryResponse;

/**
 * {@link DisplayCategory} と {@link GetDisplayCategoryResponse} のマッパーです。
 */
public class DisplayCategoryMapper {

  /**
   * {@link DisplayCategory} オブジェクトを {@link GetDisplayCategoryResponse} に変換します。
   * 
   * @param displayCategory {@link DisplayCategory} オブジェクト。
   * @return {@link GetDisplayCategoryResponse} オブジェクト。
   */
  public static GetDisplayCategoryResponse convert(DisplayCategory displayCategory) {
    if (displayCategory == null) {
      return null;
    }
    return new GetDisplayCategoryResponse(displayCategory.getId(), displayCategory.getName());
  }
}
