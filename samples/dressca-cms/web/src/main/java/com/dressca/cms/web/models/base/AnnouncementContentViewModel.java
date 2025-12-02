package com.dressca.cms.web.models.base;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせコンテンツのビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AnnouncementContentViewModel {

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
