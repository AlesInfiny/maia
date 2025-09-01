package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import org.springframework.stereotype.Repository;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementContentMapper;
import com.dressca.cms.announcement.infrastructure.repository.translator.AnnouncementEntityTranslator;
import lombok.AllArgsConstructor;

/**
 * お知らせメッセージコンテンツのリポジトリです。
 */
@Repository
@AllArgsConstructor
public class MyBatisAnnouncementContentRepository implements AnnouncementContentRepository {

  private final AnnouncementContentMapper mapper;

  @Override
  public int add(AnnouncementContent content) {
    AnnouncementContentEntity entity =
        AnnouncementEntityTranslator.createAnnouncementContentEntity(content);
    return mapper.insert(entity);
  }
}
