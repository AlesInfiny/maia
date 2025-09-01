package com.dressca.cms.web.models;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class AnnouncementListViewModel {
  private int pageNumber;
  private int pageSize;
  private int totalCount;
  @Valid
  private List<AnnouncementWithContentsViewModel> announcementsWithContents = new ArrayList<>();
  private int lastPageNumber;
  private int startIndex;
  private int endIndex;
  private boolean hasPrevious;
  private boolean hasNext;

  public AnnouncementListViewModel(int pageNumber, int pageSize, int totalCount,
      List<AnnouncementWithContentsViewModel> announcementsWithContents, int lastPageNumber,
      int startIndex, int endIndex) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalCount = totalCount;
    this.announcementsWithContents = announcementsWithContents;
    this.lastPageNumber = lastPageNumber;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.hasPrevious = pageNumber > 1;
    this.hasNext = pageNumber < lastPageNumber;
  }
}
