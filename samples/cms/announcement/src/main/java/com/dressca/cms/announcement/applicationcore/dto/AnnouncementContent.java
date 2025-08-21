package com.dressca.cms.announcement.applicationcore.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementContent {
  private UUID id;
  private UUID announcementId;
  private String languageCode;
  private String title;
  private String message;
  private String linkUrl;
}
