package com.dressca.web.controller.dto.catalog;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogItemSummaryResponse {
  private long id;
  private String name;
  private String productCode;
  private List<String> assetCodes;
}
