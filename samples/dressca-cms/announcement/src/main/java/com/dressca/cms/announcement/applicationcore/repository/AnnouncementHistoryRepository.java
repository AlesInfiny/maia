package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;

/**
 * お知らせメッセージ履歴のリポジトリインターフェースです。
 */
public interface AnnouncementHistoryRepository {

  /**
   * お知らせメッセージ履歴を追加します。
   *
   * @param announcementHistory お知らせメッセージ履歴。
   */
  void add(AnnouncementHistory announcementHistory);
}
