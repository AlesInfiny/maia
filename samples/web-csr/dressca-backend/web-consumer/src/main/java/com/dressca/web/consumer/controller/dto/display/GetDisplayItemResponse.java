package com.dressca.web.consumer.controller.dto.display;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陳列品を取得する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDisplayItemResponse {

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
  private UUID displayItemCategoryId;
  @NotNull
  private UUID displayItemBrandId;
}
