package com.dressca.cms.web.models.base;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;
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
  @NotNull
  private UUID id;

  @Size(max = 128)
  private String category;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate postDate;

  @NotNull
  @DateTimeFormat(pattern = "HH:mm")
  private LocalTime postTime;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate expireDate;

  @NotNull
  @DateTimeFormat(pattern = "HH:mm")
  private LocalTime expireTime;

  @NotNull
  private Integer displayPriority;

  private OffsetDateTime createdAt;
  private OffsetDateTime changedAt;
  private Boolean isDeleted;
}
