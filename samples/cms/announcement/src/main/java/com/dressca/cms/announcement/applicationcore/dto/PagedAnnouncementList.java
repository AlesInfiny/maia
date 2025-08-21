package com.dressca.cms.announcement.applicationcore.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagedAnnouncementList {
  private int pageNumber;
  private int pageSize;
  private int totalCount;
  private List<Announcement> announcements;
  private int lastPageNumber;
}
