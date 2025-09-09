package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;

/**
 * お知らせメッセージコンテンツのリポジトリインターフェースです。
 */
public interface AnnouncementContentRepository {

  /**
   * お知らせメッセージコンテンツを追加します。
   * 
   * @param content お知らせメッセージコンテンツ。
   * @return 追加した行数。
   */
  public int add(AnnouncementContent content);
}
