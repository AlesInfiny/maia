package com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;

/**
 * お知らせメッセージテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedAnnouncementMapper {
  /**
   * ページサイズとオフセットを指定して、お知らせメッセージのリストを取得します。
   * 
   * @param pageSize ページサイズ。
   * @param offset   オフセット。
   * @return お知らせメッセージのリスト。
   */
  List<Announcement> findByPageSizeAndOffset(int pageSize, int offset);
}
