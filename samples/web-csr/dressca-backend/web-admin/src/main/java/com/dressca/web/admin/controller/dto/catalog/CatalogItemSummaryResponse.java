package com.dressca.web.admin.controller.dto.catalog;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログアイテムの概要を取得する際に用いるdtoクラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogItemSummaryResponse {
  @NotNull
  private long id;
  @NotNull
  private String name;
  @NotNull
  private String productCode;
  private List<String> assetCodes;
}