package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementHistoryRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementHistoryMapper;
import com.dressca.cms.announcement.infrastructure.translator.AnnouncementEntityTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * AnnouncementHistoryRepository を実装する MyBatis リポジトリクラスです。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisAnnouncementHistoryRepository implements AnnouncementHistoryRepository {

  private final AnnouncementHistoryMapper announcementHistoryMapper;

  @Override
  public void add(AnnouncementHistory announcementHistory) {
    AnnouncementHistoryEntity entity = AnnouncementEntityTranslator.toHistoryEntity(announcementHistory);
    announcementHistoryMapper.insert(entity);
  }
}
