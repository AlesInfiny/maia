package com.dressca.cms.web.models.base;

import java.util.UUID;
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
  private UUID announcementId;
  private String languageCode;
  private String title;
  private String message;
  private String linkUrl;
}
