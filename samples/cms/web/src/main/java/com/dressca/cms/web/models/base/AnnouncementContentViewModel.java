package com.dressca.cms.web.models.base;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージコンテンツのビューモデルです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementContentViewModel {
  private UUID id;
  @NotNull
  private UUID announcementId;
  @NotNull
  private String languageCode;
  @NotNull
  private String title;
  @NotNull
  private String message;
  private String linkUrl;
}
