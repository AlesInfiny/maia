package com.dressca.web.admin.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * カタログアイテムを変更する際に用いるdtoクラスです。
 */
@Data
@AllArgsConstructor
public class PutCatalogItemRequest {
  @NotNull
  public String name = "";
  @NotNull
  public String description = "";
  @NotNull
  public long price;
  @NotNull
  public String productCode;
  @NotNull
  public long catalogCategoryId;
  @NotNull
  public long catalogBrandId;
}
