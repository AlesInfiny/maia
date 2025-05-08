package com.dressca.web.admin.controller.dto.catalog;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * カタログアイテムを変更する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
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
  private long catalogCategoryId;
  @NotNull
  private long catalogBrandId;
  @NotNull
  private OffsetDateTime rowVersion;
  @NotNull
  @JsonProperty("isDeleted")
  private boolean isDeleted;
}
