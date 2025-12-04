package com.dressca.cms.web.models;

import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージとお知らせコンテンツをラップするビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementWithContentsViewModel {

  /**
   * お知らせメッセージのビューモデル。
   */
  private AnnouncementViewModel announcement;

  /**
   * 代表のお知らせコンテンツのビューモデル（言語コードの優先順で選択されたもの）。
   */
  private AnnouncementContentViewModel content;
}
