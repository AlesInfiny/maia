package com.dressca.cms.announcement.applicationcore.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせコンテンツ履歴の DTO クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementContentHistory implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * お知らせコンテンツ履歴 ID。
   */
  private UUID id;

  /**
   * お知らせメッセージ履歴 ID。
   */
  private UUID announcementHistoryId;

  /**
   * 言語コード。
   */
  private String languageCode;

  /**
   * タイトル。
   */
  private String title;

  /**
   * 表示メッセージ。
   */
  private String message;

  /**
   * リンク先 URL。
   */
  private String linkUrl;
}
