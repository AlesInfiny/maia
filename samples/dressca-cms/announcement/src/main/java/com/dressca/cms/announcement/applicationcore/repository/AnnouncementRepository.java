package com.dressca.cms.announcement.applicationcore.repository;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

  /**
   * お知らせメッセージを追加します。
   *
   * @param announcement お知らせメッセージ。
   */
  void add(Announcement announcement);

  /**
   * 指定したIDのお知らせメッセージをお知らせコンテンツと併せて取得します。
   *
   * @param id お知らせメッセージID。
   * @return お知らせメッセージ。IDに対応するお知らせメッセージが存在しない、または論理削除済みの場合はnull。
   */
  Optional<Announcement> findByIdWithContents(UUID id);

  /**
   * お知らせメッセージを更新します。
   *
   * @param announcement お知らせメッセージ。
   */
  void update(Announcement announcement);

  /**
   * 指定したIDのお知らせメッセージを論理削除します。
   *
   * @param id お知らせメッセージID。
   * @return 論理削除したお知らせメッセージ。IDに対応するお知らせメッセージが存在しない、または論理削除済みの場合はnull。
   */
  Optional<Announcement> delete(UUID id);
}
