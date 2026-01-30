package com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * お知らせメッセージの custom mapper interface です。
 */
@Mapper
public interface AnnouncementCustomMapper {

  /**
   * オフセットとリミットから、論理削除されていないお知らせメッセージとコンテンツを JOIN して取得します。 取得するお知らせメッセージは掲載開始日時である post_date_time
   * カラム日付の降順でソートされます。
   *
   * @param offset オフセット。
   * @param limit リミット。
   * @return お知らせメッセージとコンテンツを保持する DTO のリスト。
   */
  List<Announcement> findAnnouncementsWithContentsByOffsetAndLimit(@Param("offset") int offset,
      @Param("limit") int limit);

  /**
   * 指定した ID のお知らせメッセージをお知らせコンテンツと併せて取得します。 取得するお知らせメッセージは論理削除されていないものに限ります。
   * 
   * @param id お知らせメッセージID。
   * @return お知らせメッセージとコンテンツを保持する DTO。ID に対応するお知らせメッセージが存在しない、または論理削除済みの場合は null。
   */
  Announcement findByIdWithContents(@Param("id") UUID id);
}
