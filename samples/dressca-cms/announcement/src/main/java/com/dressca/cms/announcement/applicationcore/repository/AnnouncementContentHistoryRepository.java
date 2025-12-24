package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;

/**
 * お知らせコンテンツ履歴のリポジトリインターフェースです。
 */
public interface AnnouncementContentHistoryRepository {

  /**
   * お知らせコンテンツ履歴を追加します。
   *
   * @param announcementContentHistory お知らせコンテンツ履歴。
   */
  void add(AnnouncementContentHistory announcementContentHistory);
}
