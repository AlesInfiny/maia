package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import java.util.List;
import java.util.UUID;

/**
 * お知らせコンテンツのリポジトリインターフェースです。
 */
public interface AnnouncementContentRepository {

  /**
   * お知らせコンテンツを追加します。
   *
   * @param announcementContent お知らせコンテンツ。
   */
  void add(AnnouncementContent announcementContent);

  /**
   * お知らせコンテンツを更新します。
   *
   * @param announcementContent お知らせコンテンツ。
   */
  void update(AnnouncementContent announcementContent);

  /**
   * 指定したお知らせメッセージIDに対応するお知らせコンテンツを取得します。
   *
   * @param announcementId お知らせメッセージID。
   * @return お知らせコンテンツのリスト。
   */
  List<AnnouncementContent> findByAnnouncementId(UUID announcementId);

  /**
   * 指定したお知らせコンテンツIDのお知らせコンテンツを削除します。
   *
   * @param id お知らせコンテンツID。
   */
  void deleteById(UUID id);
}
