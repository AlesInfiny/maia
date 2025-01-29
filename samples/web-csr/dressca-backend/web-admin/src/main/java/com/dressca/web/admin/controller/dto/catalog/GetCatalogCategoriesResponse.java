package com.dressca.web.admin.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログカテゴリの情報を取得する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCatalogCategoriesResponse {
  @NotNull
  private long id;
  @NotNull
  private String name;
}
