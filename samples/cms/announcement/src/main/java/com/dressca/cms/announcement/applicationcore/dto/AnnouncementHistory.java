package com.dressca.cms.announcement.applicationcore.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementHistory {
  private UUID id;
  private UUID announcementId;
  private String category;
  private OffsetDateTime postDateTime;
  private OffsetDateTime expireDateTime;
  private Integer displayPriority;
  private OffsetDateTime createdAt;
  private String changedBy;
  private Integer operationType;
}
