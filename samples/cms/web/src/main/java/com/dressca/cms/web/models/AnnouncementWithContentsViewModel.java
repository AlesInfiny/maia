package com.dressca.cms.web.models;

import java.util.List;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * お知らせメッセージとお知らせメッセージコンテンツをラップするビューモデルです。
 */
@Data
@AllArgsConstructor
public class AnnouncementWithContentsViewModel {
  private AnnouncementViewModel announcement;
  private List<AnnouncementContentViewModel> contents;
}
