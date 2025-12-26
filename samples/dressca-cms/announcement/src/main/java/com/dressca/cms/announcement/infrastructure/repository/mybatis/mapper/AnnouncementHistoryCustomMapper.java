package com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * お知らせメッセージ履歴の custom mapper interface です。
 */
@Mapper
public interface AnnouncementHistoryCustomMapper {

  /**
   * 指定したお知らせメッセージIDに対応するお知らせメッセージ履歴をお知らせコンテンツ履歴と併せて取得します。
   * 取得するお知らせメッセージ履歴は作成日時（更新日時）である created_at カラム日付の降順でソートされます。
   *
   * @param announcementId お知らせメッセージID。
   * @return お知らせメッセージ履歴とコンテンツ履歴を保持する DTO のリスト。
   */
  List<AnnouncementHistory> findByAnnouncementIdWithContents(@Param("announcementId") UUID announcementId);
}
