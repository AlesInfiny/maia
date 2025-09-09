package com.dressca.cms.announcement.applicationcore.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージコンテンツ履歴のエンティティです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementContentHistory {
  private UUID id;
  private UUID announcementHistoryId;
  private String languageCode;
  private String title;
  private String message;
  private String linkUrl;
}
