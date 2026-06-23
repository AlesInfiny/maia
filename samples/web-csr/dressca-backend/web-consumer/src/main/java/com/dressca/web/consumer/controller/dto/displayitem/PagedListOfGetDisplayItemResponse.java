package com.dressca.web.consumer.controller.dto.displayitem;

import java.util.List;
import lombok.Data;

/**
 * 検索した掲載品の情報を取得する際に用いる dto クラスです。
 */
@Data
public class PagedListOfGetDisplayItemResponse {
  private List<GetDisplayItemResponse> items;
  private int totalCount;
  private int page;
  private int pageSize;
  private int totalPages;
  private boolean hasPrevious;
  private boolean hasNext;

  /**
   * {@link PagedListOfGetDisplayItemResponse} クラスのインスタンスを初期化します。
   * 
   * @param items 掲載品のリスト。
   * @param totalCount 掲載品の合計数。
   * @param page 現在のページ番号。
   * @param pageSize ページ数の合計。
   */
  public PagedListOfGetDisplayItemResponse(List<GetDisplayItemResponse> items, int totalCount,
      int page, int pageSize) {
    this.items = List.copyOf(items);
    this.totalCount = totalCount;
    this.page = page;
    this.pageSize = pageSize;
    this.totalPages =
        this.totalCount / this.pageSize + (this.totalCount % this.pageSize == 0 ? 0 : 1);
    this.hasPrevious = this.page > 1;
    this.hasNext = this.page < this.totalPages;
  }
}
