package com.dressca.web.consumer.controller.dto.displayitem;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陳列品の概要を取得する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayItemSummaryApiModel {
  @NotNull
  private UUID id;
  @NotNull
  private String name;
  @NotNull
  private String productCode;
  private List<String> assetCodes;
}
