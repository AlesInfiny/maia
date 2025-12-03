package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntityExample;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementMapper;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper.AnnouncementCustomMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * AnnouncementRepository を実装する MyBatis リポジトリクラスです。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisAnnouncementRepository implements AnnouncementRepository {

  private final AnnouncementMapper announcementMapper;
  private final AnnouncementCustomMapper announcementCustomMapper;

  @Override
  public long countByIsDeletedFalse() {
    AnnouncementEntityExample example = new AnnouncementEntityExample();
    example.createCriteria().andIsDeletedEqualTo(false);
    return announcementMapper.countByExample(example);
  }

  @Override
  public List<Announcement> findByOffsetAndLimit(int offset, int limit) {
    // カスタムマッパーでお知らせメッセージとコンテンツを JOIN して取得し、直接 DTO を返却
    return announcementCustomMapper.findAnnouncementsWithContentsByOffsetAndLimit(offset, limit);
  }
}
