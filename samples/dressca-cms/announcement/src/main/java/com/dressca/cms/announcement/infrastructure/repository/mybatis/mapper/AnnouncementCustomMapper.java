package com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * お知らせメッセージの custom mapper interface です。
 */
@Mapper
public interface AnnouncementCustomMapper {

  /**
   * オフセットとリミットから、論理削除されていないお知らせメッセージとコンテンツを
   * JOIN して取得します。
   *
   * @param offset オフセット。
   * @param limit  リミット。
   * @return お知らせメッセージとコンテンツを保持する DTO のリスト。
   */
  List<Announcement> findAnnouncementsWithContentsByOffsetAndLimit(
      @Param("offset") int offset,
      @Param("limit") int limit);
}
