package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;

/**
 * お知らせメッセージコンテンツ履歴のリポジトリインターフェースです。
 */
public interface AnnouncementContentHistoryRepository {

  /**
   * お知らせメッセージコンテンツ履歴を追加します。
   * 
   * @param contentHistory お知らせメッセージコンテンツ履歴。
   * @return 追加した行数。
   */
  public int add(AnnouncementContentHistory contentHistory);
}
