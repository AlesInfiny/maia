package com.dressca.web.controller.dto.catalog;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogCategoryResponse {
  @NotNull
  private long id;
  @NotNull
  private String name;
}
