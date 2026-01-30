package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import java.util.List;
import java.util.UUID;

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

  /**
   * 指定したお知らせメッセージIDに対応するお知らせメッセージ履歴をお知らせコンテンツ履歴と併せて取得します。
   *
   * @param announcementId お知らせメッセージID。
   * @return お知らせメッセージ履歴のリスト。
   */
  List<AnnouncementHistory> findByAnnouncementIdWithContents(UUID announcementId);
}
