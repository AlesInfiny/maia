package com.dressca.web.admin.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログアイテムを取得する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCatalogItemResponse {

  @NotNull
  private UUID id;
  @NotNull
  private String name;
  @NotNull
  private String productCode;
  private List<String> assetCodes;
  @NotNull
  private String description;
  @NotNull
  private BigDecimal price;
  @NotNull
  private UUID catalogCategoryId;
  @NotNull
  private UUID catalogBrandId;
  @NotNull
  private OffsetDateTime rowVersion;
  @NotNull
  private Boolean isDeleted;
}
