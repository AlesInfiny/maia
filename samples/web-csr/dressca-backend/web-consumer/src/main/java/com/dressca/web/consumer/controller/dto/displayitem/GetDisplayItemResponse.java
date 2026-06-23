package com.dressca.web.consumer.controller.dto.displayitem;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 掲載品を取得する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDisplayItemResponse {

  @NotNull
  private long id;
  @NotNull
  private long catalogItemId;
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

}
