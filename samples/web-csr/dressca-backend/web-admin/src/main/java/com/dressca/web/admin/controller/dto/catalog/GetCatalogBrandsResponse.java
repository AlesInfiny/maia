package com.dressca.web.admin.controller.dto.catalog;

import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(type = "string", format = "uuid")
  private UUID id;
  @NotNull
  private String name;
}
