package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import java.util.List;

/**
 * お知らせメッセージのリポジトリインターフェースです。
 */
public interface AnnouncementRepository {

  /**
   * 論理削除されていないお知らせメッセージの件数を取得します。
   *
   * @return お知らせメッセージの件数。
   */
  long countByIsDeletedFalse();

  /**
   * オフセットとリミットから、論理削除されていないお知らせメッセージを
   * 掲載開始日時の降順で取得します。
   *
   * @param offset オフセット。
   * @param limit  リミット。
   * @return お知らせメッセージのリスト。
   */
  List<Announcement> findByOffsetAndLimit(int offset, int limit);
}
