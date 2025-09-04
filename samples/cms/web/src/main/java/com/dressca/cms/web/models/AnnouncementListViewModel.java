package com.dressca.cms.web.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * お知らせメッセージリストのビューモデルです。
 */
@Data
public class AnnouncementListViewModel {
  private int pageNumber;
  private int pageSize;
  private int totalCount;
  private List<AnnouncementWithContentsViewModel> announcementsWithContents = new ArrayList<>();
  private int lastPageNumber;
  private int startIndex;
  private int endIndex;
  private boolean hasPrevious;
  private boolean hasNext;

  /**
   * {@link AnnouncementListViewModel} オブジェクトを初期化します。
   * 
   * @param pageNumber                ページ番号。
   * @param pageSize                  ページサイズ。
   * @param totalCount                お知らせメッセージの総件数。
   * @param announcementsWithContents お知らせメッセージのビューモデルとお知らせメッセージコンテンツのビューモデルをラップするビューモデルのリスト。
   * @param lastPageNumber            最後のページ番号。
   * @param startIndex                開始件数。
   * @param endIndex                  終了件数。
   */
  public AnnouncementListViewModel(int pageNumber, int pageSize, int totalCount,
      List<AnnouncementWithContentsViewModel> announcementsWithContents, int lastPageNumber,
      int startIndex, int endIndex) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalCount = totalCount;
    this.announcementsWithContents = announcementsWithContents;
    this.lastPageNumber = lastPageNumber;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.hasPrevious = pageNumber > 1;
    this.hasNext = pageNumber < lastPageNumber;
  }
}
