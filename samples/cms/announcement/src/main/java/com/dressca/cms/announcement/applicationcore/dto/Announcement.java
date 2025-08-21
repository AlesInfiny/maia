package com.dressca.cms.announcement.applicationcore.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
  private UUID id;
  private String category;
  private OffsetDateTime postDateTime;
  private OffsetDateTime expireDateTime;
  private Integer displayPriority;
  private OffsetDateTime createdAt;
  private OffsetDateTime changedAt;
  private Boolean isDeleted;
  private List<AnnouncementContent> contents = new ArrayList<AnnouncementContent>();
}
