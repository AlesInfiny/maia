package com.dressca.web.admin.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログにアイテムを追加する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCatalogItemRequest {
  @NotNull
  private String name = "";
  @NotNull
  private String description = "";
  @NotNull
  private long price;
  @NotNull
  private String productCode;
  @NotNull
  private UUID catalogCategoryId;
  @NotNull
  private UUID catalogBrandId;
}
