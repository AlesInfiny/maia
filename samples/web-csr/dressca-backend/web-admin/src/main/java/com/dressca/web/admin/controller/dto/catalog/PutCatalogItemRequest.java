package com.dressca.web.admin.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログアイテムを変更する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutCatalogItemRequest {
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
  @NotNull
  private OffsetDateTime rowVersion;
  @NotNull
  private Boolean isDeleted;
}
