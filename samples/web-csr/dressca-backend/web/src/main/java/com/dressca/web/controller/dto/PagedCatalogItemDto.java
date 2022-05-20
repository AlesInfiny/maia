package com.dressca.web.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedCatalogItemDto {
  private List<CatalogItemDto> items;
  private int totalCount;
  private int page;
  private int pageSize;
}
