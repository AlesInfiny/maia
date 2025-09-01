package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import org.springframework.stereotype.Repository;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentHistoryRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementContentHistoryMapper;
import com.dressca.cms.announcement.infrastructure.repository.translator.AnnouncementEntityTranslator;
import lombok.AllArgsConstructor;

/**
 * お知らせメッセージコンテンツ履歴のリポジトリです。
 */
@Repository
@AllArgsConstructor
public class MyBatisAnnouncementContentHistoryRepository
    implements AnnouncementContentHistoryRepository {
  private final AnnouncementContentHistoryMapper mapper;

  @Override
  public int add(AnnouncementContentHistory contentHistory) {
    AnnouncementContentHistoryEntity entity =
        AnnouncementEntityTranslator.createAnnouncementContentHistoryEntity(contentHistory);
    return mapper.insert(entity);
  }

}
