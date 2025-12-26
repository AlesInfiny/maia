package com.dressca.cms.web.models;

import com.dressca.cms.web.models.base.AnnouncementContentHistoryViewModel;
import com.dressca.cms.web.models.base.AnnouncementHistoryViewModel;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お知らせメッセージ履歴とお知らせコンテンツ履歴をラップするビューモデルクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementHistoryWithContentHistoriesViewModel {

  /**
   * お知らせメッセージ履歴のビューモデル。
   */
  private AnnouncementHistoryViewModel history;

  /**
   * お知らせコンテンツ履歴のリスト。
   */
  private List<AnnouncementContentHistoryViewModel> contentHistories = new ArrayList<>();
}
