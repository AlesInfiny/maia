package com.dressca.web.admin.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログブランドの情報を取得する際に用いるdtoクラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogBrandResponse {
  @NotNull
  private long id;
  @NotNull
  private String name;
}
