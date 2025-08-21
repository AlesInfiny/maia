package com.dressca.cms.announcement.applicationcore.repository;

import java.util.List;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;

/**
 * お知らせメッセージのリポジトリインターフェースです。
 */
public interface AnnouncementRepository {
  /**
   * 論理削除されていないお知らせメッセージの件数を取得します。
   *
   * @return 論理削除されていないお知らせメッセージの件数。
   */
  public long countByIsDeletedFalse();

  /**
   * ページ番号とページサイズに合致する論理削除されていないお知らせメッセージを取得します。
   * 
   * @param pageNumber ページ番号。
   * @param pageSize ページサイズ。
   * @return 条件に一致するお知らせメッセージのリスト。
   */
  public List<Announcement> findByPageNumberAndPageSize(int pageNumber, int pageSize);
}
