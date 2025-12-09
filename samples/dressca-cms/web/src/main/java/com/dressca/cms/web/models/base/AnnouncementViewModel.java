package com.dressca.cms.web.models.base;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージのビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AnnouncementViewModel {

  /**
   * お知らせメッセージ ID。
   */
  private UUID id;

  /**
   * カテゴリー。
   */
  private String category;

  /**
   * 掲載開始日時。
   */
  private OffsetDateTime postDateTime;

  /**
   * 掲載終了日時。
   */
  private OffsetDateTime expireDateTime;

  /**
   * 表示優先度。
   */
  private Integer displayPriority;

  /**
   * レコード作成日時。
   */
  private OffsetDateTime createdAt;

  /**
   * レコード更新日時。
   */
  private OffsetDateTime changedAt;

  /**
   * 論理削除フラグ。
   */
  private Boolean isDeleted;

  /**
   * お知らせコンテンツのリスト。
   */
  private List<AnnouncementContentViewModel> contents;
}
