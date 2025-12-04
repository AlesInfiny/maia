package com.dressca.cms.announcement.applicationcore.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージ履歴の DTO クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementHistory implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * お知らせメッセージ履歴 ID。
   */
  private UUID id;

  /**
   * お知らせメッセージ ID。
   */
  private UUID announcementId;

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
   * 変更者。
   */
  private String changedBy;

  /**
   * 操作種別。
   */
  private Integer operationType;

  /**
   * お知らせコンテンツ履歴のリスト。
   */
  private List<AnnouncementContentHistory> contentHistories = new ArrayList<>();
}
