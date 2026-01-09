package com.dressca.cms.web.models;

import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージ削除確認画面のビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDeleteConfirmViewModel {

  /**
   * お知らせメッセージのビューモデル。
   */
  private AnnouncementViewModel announcement = new AnnouncementViewModel();

  /**
   * お知らせコンテンツのビューモデル一覧。
   */
  private List<AnnouncementContentViewModel> contents = new ArrayList<>();

  /**
   * お知らせメッセージ履歴のリスト。
   */
  private List<AnnouncementHistoryWithContentHistoriesViewModel> histories = new ArrayList<>();
}
