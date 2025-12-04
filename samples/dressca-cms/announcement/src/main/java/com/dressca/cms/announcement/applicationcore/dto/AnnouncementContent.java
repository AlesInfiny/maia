package com.dressca.cms.announcement.applicationcore.dto;

import java.io.Serializable;
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
public class AnnouncementContent implements Serializable {

  private static final long serialVersionUID = 1L;

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

  /**
   * お知らせコンテンツ ID、言語コード、タイトル、表示メッセージ、リンク先 URL を指定して、
   * {@link AnnouncementContent} クラスの新しいインスタンスを生成します。
   * 
   * @param id           お知らせコンテンツ ID。
   * @param languageCode 言語コード。
   * @param title        タイトル。
   * @param message      表示メッセージ。
   * @param linkUrl      リンク先 URL。
   */
  public AnnouncementContent(UUID id, String languageCode, String title, String message, String linkUrl) {
    this.id = id;
    this.languageCode = languageCode;
    this.title = title;
    this.message = message;
    this.linkUrl = linkUrl;
  }

}
