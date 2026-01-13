package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntityExample;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementContentMapper;
import com.dressca.cms.announcement.infrastructure.translator.AnnouncementEntityTranslator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

  @Override
  public void update(AnnouncementContent announcementContent) {
    AnnouncementContentEntity entity = AnnouncementEntityTranslator.toContentEntity(announcementContent);
    announcementContentMapper.updateByPrimaryKey(entity);
  }

  @Override
  public List<AnnouncementContent> findByAnnouncementId(UUID announcementId) {
    AnnouncementContentEntityExample example = new AnnouncementContentEntityExample();
    example.createCriteria().andAnnouncementIdEqualTo(announcementId);
    List<AnnouncementContentEntity> entities = announcementContentMapper.selectByExample(example);
    return entities.stream()
        .map(AnnouncementEntityTranslator::toContent)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    announcementContentMapper.deleteByPrimaryKey(id);
  }
}
