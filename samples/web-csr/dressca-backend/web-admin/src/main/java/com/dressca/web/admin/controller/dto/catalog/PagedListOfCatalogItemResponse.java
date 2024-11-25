package com.dressca.web.admin.controller.dto.catalog;

import java.util.List;
import lombok.Data;

/**
 * 検索したカタログアイテムの情報を取得する際に用いるdtoクラスです。
 */
@Data
public class PagedListOfCatalogItemResponse {
  private List<CatalogItemResponse> items;
  private int totalCount;
  private int page;
  private int pageSize;
  private int totalPages;
  private boolean hasPrevious;
  private boolean hasNext;

  /**
   * コンストラクタ。
   * 
   * @param items      カタログアイテムのリスト。
   * @param totalCount カタログアイテムの合計数。
   * @param page       現在のページ番号。
   * @param pageSize   ページ数の合計。
   */
  public PagedListOfCatalogItemResponse(List<CatalogItemResponse> items, int totalCount, int page, int pageSize) {
    this.items = items;
    this.totalCount = totalCount;
    this.page = page;
    this.pageSize = pageSize;
    this.totalPages = (int) Math.ceil(this.totalCount / (double) this.pageSize);
    this.hasPrevious = this.page > 1;
    this.hasNext = this.page < this.totalPages;
  }
}
