package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentHistoryRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementContentHistoryMapper;
import com.dressca.cms.announcement.infrastructure.translator.AnnouncementEntityTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * AnnouncementContentHistoryRepository を実装する MyBatis リポジトリクラスです。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisAnnouncementContentHistoryRepository
    implements AnnouncementContentHistoryRepository {

  private final AnnouncementContentHistoryMapper announcementContentHistoryMapper;

  @Override
  public void add(AnnouncementContentHistory announcementContentHistory) {
    AnnouncementContentHistoryEntity entity =
        AnnouncementEntityTranslator.toContentHistoryEntity(announcementContentHistory);
    announcementContentHistoryMapper.insert(entity);
  }
}
