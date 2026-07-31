package com.dressca.web.consumer.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カタログブランドの情報を取得する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCatalogBrandsResponse {
  @NotNull
  private UUID id;
  @NotNull
  private String name;
}
