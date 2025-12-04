package com.dressca.cms.announcement.applicationcore.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ページングされたお知らせメッセージリストを表現するクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedAnnouncementList {

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
   * お知らせメッセージのリスト。
   */
  private List<Announcement> announcements;

  /**
   * 最後のページ番号。
   */
  private Integer lastPageNumber;
}
