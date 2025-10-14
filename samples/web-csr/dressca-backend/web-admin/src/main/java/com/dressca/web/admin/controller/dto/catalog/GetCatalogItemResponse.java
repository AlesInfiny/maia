package com.dressca.web.admin.controller.dto.catalog;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import jakarta.validation.constraints.NotNull;
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
  private long id;
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
  private long catalogCategoryId;
  @NotNull
  private long catalogBrandId;
  @NotNull
  private OffsetDateTime rowVersion;
  @NotNull
  private Boolean isDeleted;
}
