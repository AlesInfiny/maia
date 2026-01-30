package com.dressca.cms.web.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージ管理画面のビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementListViewModel {

  /**
   * ページ番号。
   */
  private Integer pageNumber;

  /**
   * ページサイズ。
   */
  private Integer pageSize;

  /**
   * お知らせメッセージの総件数。
   */
  private Long totalCount;

  /**
   * お知らせメッセージのビューモデルのリスト。
   */
  private List<AnnouncementWithContentsViewModel> announcements;

  /**
   * 最後のページ番号。
   */
  private Integer lastPageNumber;

  /**
   * お知らせメッセージの開始件数。
   */
  private Long startCount;

  /**
   * お知らせメッセージの終了件数。
   */
  private Long endCount;

  /**
   * 前ページを持つか。
   */
  private Boolean hasPrevious;

  /**
   * 次ページを持つか。
   */
  private Boolean hasNext;

  /**
   * コンストラクタ。
   *
   * @param pageNumber ページ番号。
   * @param pageSize ページサイズ。
   * @param totalCount お知らせメッセージの総件数。
   * @param announcements お知らせメッセージのビューモデルのリスト。
   * @param lastPageNumber 最後のページ番号。
   */
  public AnnouncementListViewModel(Integer pageNumber, Integer pageSize, Long totalCount,
      List<AnnouncementWithContentsViewModel> announcements, Integer lastPageNumber) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalCount = totalCount;
    this.announcements = announcements;
    this.lastPageNumber = lastPageNumber;

    // 開始件数と終了件数を計算
    this.startCount = (long) (pageNumber - 1) * pageSize + 1;
    this.endCount = Math.min((long) pageNumber * pageSize, totalCount);

    // 前ページと次ページを持つかを判定
    this.hasPrevious = pageNumber > 1;
    this.hasNext = pageNumber < lastPageNumber;
  }

  /**
   * お知らせメッセージが存在するかを返します。
   * 
   * @return お知らせメッセージが存在する場合は true、存在しない場合は false。
   */
  public boolean existAnnouncement() {
    return this.totalCount != null && this.totalCount > 0;
  }
}
