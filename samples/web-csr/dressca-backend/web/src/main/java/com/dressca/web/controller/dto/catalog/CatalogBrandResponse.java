package com.dressca.web.controller.dto.catalog;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogBrandResponse {
  @NotNull
  private long id;
  @NotNull
  private String name;
}
