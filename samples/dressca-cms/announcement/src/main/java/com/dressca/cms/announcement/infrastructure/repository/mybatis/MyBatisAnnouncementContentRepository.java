package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementContentMapper;
import com.dressca.cms.announcement.infrastructure.translator.AnnouncementEntityTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * AnnouncementContentRepository を実装する MyBatis リポジトリクラスです。
 */
@Repository
@RequiredArgsConstructor
public class MyBatisAnnouncementContentRepository implements AnnouncementContentRepository {

  private final AnnouncementContentMapper announcementContentMapper;

  @Override
  public void add(AnnouncementContent announcementContent) {
    AnnouncementContentEntity entity = AnnouncementEntityTranslator.toContentEntity(announcementContent);
    announcementContentMapper.insert(entity);
  }
}
