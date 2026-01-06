package com.dressca.cms.web.models;

import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージ編集画面のビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementEditViewModel {

  /**
   * お知らせメッセージのビューモデル。
   */
  @Valid
  private AnnouncementViewModel announcement = new AnnouncementViewModel();

  /**
   * お知らせコンテンツのビューモデル一覧。
   */
  @Valid
  private List<AnnouncementContentViewModel> contents = new ArrayList<>();

  /**
   * お知らせメッセージ履歴のリスト。
   */
  private List<AnnouncementHistoryWithContentHistoriesViewModel> histories = new ArrayList<>();
}
