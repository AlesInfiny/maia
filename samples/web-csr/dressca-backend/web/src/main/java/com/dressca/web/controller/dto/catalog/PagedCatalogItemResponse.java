package com.dressca.web.controller.dto.catalog;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 検索したカタログアイテムの情報を取得する際に用いるdtoクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedCatalogItemResponse {
  private List<CatalogItemResponse> items;
  private int totalCount;
  private int page;
  private int pageSize;
}
