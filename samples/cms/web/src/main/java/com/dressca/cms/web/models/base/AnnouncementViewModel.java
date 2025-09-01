package com.dressca.cms.web.models.base;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import com.dressca.cms.web.models.validation.AnnouncementValidationGroup.AnnouncementStoreGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージのビューモデルです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementViewModel {
  @Size(max = 128, message = "カテゴリーは128文字以内で入力してください", groups = AnnouncementStoreGroup.class)
  @NotBlank(message = "カテゴリーは128文字以内で入力してください", groups = AnnouncementStoreGroup.class)
  private String category;

  @NotNull(
      message = "掲載開始日時を入力してください",
      groups = AnnouncementStoreGroup.class)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate postDate;

  @NotNull(
      message = "掲載開始日時を入力してください",
      groups = AnnouncementStoreGroup.class)
  @DateTimeFormat(pattern = "HH:mm")
  private LocalTime postTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate expireDate;

  @DateTimeFormat(pattern = "HH:mm")
  private LocalTime expireTime;

  @NotNull(message = "表示優先度を選択してください", groups = AnnouncementStoreGroup.class)
  private Integer displayPriority;
}
