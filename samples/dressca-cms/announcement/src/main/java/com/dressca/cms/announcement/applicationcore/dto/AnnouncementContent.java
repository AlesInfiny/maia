package com.dressca.cms.announcement.applicationcore.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせコンテンツの DTO クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementContent {

  /**
   * お知らせコンテンツ ID。
   */
  private UUID id;

  /**
   * お知らせメッセージ ID。
   */
  private UUID announcementId;

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
