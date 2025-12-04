package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;

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
}
