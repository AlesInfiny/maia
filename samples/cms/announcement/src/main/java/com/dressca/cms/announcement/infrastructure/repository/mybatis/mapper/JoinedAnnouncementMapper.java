package com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;

/**
 * お知らせメッセージテーブルにアクセスするためのマッパーのインターフェースです。
 */
@Mapper
public interface JoinedAnnouncementMapper {
  List<Announcement> findByPageNumberAndPageSize(int pageSize, int offset);
}
