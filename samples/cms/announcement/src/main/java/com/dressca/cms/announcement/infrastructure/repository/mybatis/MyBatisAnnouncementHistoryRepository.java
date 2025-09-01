package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import org.springframework.stereotype.Repository;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementHistoryRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementHistoryMapper;
import com.dressca.cms.announcement.infrastructure.repository.translator.AnnouncementEntityTranslator;
import lombok.AllArgsConstructor;

/**
 * お知らせメッセージ履歴のリポジトリです。
 */
@Repository
@AllArgsConstructor
public class MyBatisAnnouncementHistoryRepository implements AnnouncementHistoryRepository {

  private final AnnouncementHistoryMapper mapper;

  @Override
  public int add(AnnouncementHistory history) {
    AnnouncementHistoryEntity entity =
        AnnouncementEntityTranslator.createAnnouncementHistoryEntity(history);
    return mapper.insert(entity);
  }
}
