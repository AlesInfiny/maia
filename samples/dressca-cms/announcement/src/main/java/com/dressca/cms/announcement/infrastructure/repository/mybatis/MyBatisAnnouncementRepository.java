package com.dressca.cms.announcement.infrastructure.repository.mybatis;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntityExample;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.mapper.AnnouncementMapper;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.mapper.AnnouncementCustomMapper;
import com.dressca.cms.announcement.infrastructure.translator.AnnouncementEntityTranslator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  @Override
  public void add(Announcement announcement) {
    AnnouncementEntity entity = AnnouncementEntityTranslator.toAnnouncementEntity(announcement);
    announcementMapper.insert(entity);
  }

  @Override
  public Optional<Announcement> findByIdWithContents(UUID id) {
    // カスタムマッパーでお知らせメッセージとコンテンツを JOIN して取得し、直接 DTO を返却
    Announcement announcement = announcementCustomMapper.findByIdWithContents(id);
    if (announcement == null || announcement.getIsDeleted()) {
      return Optional.empty();
    }
    return Optional.ofNullable(announcement);
  }

  @Override
  public void update(Announcement announcement) {
    AnnouncementEntity entity = AnnouncementEntityTranslator.toAnnouncementEntity(announcement);
    announcementMapper.updateByPrimaryKey(entity);
  }

  @Override
  public Optional<Announcement> delete(UUID id) {
    Announcement announcement = announcementCustomMapper.findByIdWithContents(id);
    if (announcement == null || announcement.getIsDeleted()) {
      return Optional.empty();
    }
    announcement.setIsDeleted(true);
    announcementMapper.updateByPrimaryKey(AnnouncementEntityTranslator.toAnnouncementEntity(announcement));
    return Optional.ofNullable(announcement);
  }
}
