package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;

/**
 * お知らせメッセージ履歴のリポジトリインターフェースです。
 */
public interface AnnouncementHistoryRepository {

  /**
   * お知らせメッセージ履歴を追加します。
   * 
   * @param history お知らせメッセージ履歴。
   * @return 追加した行数。
   */
  public int add(AnnouncementHistory history);
}
