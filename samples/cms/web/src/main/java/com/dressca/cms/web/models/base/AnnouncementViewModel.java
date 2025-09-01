package com.dressca.cms.web.models.base;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
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
  @Size(max = 128, message = "カテゴリーは128文字以内で入力してください")
  private String category;

  @NotNull(message = "掲載開始日時を入力してください")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate postDate;

  @NotNull(message = "掲載開始日時を入力してください")
  @DateTimeFormat(pattern = "HH:mm")
  private LocalTime postTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate expireDate;

  @DateTimeFormat(pattern = "HH:mm")
  private LocalTime expireTime;

  @NotNull(message = "表示優先度を選択してください")
  private Integer displayPriority;
}
