package com.dressca.cms.web.models.base;

import com.dressca.cms.web.models.validation.AnnouncementValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
  @Size(max = 128, groups = AnnouncementValidationGroup.Store.class)
  private String category;

  /**
   * 掲載開始日。
   */
  @NotNull(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.postDateIsRequired}")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate postDate;

  /**
   * 掲載開始時刻。
   */
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime postTime;

  /**
   * 掲載終了日。
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate expireDate;

  /**
   * 掲載終了時刻。
   */
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime expireTime;

  /**
   * 表示優先度。
   */
  @NotNull(groups = AnnouncementValidationGroup.Store.class, message = "{announcement.create.displayPriorityIsRequired}")
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
}
