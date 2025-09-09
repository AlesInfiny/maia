package com.dressca.cms.announcement.infrastructure.repository.translator;

import java.util.Optional;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementHistoryEntity;

/**
 * お知らせメッセージのエンティティとテーブルエンティティを相互に変換するクラスです。
 */
public class AnnouncementEntityTranslator {

  /**
   * テーブルエンティティ {@link AnnouncementEntity} をエンティティ {@link Announcement} に変換します。
   *
   * @param entity {@link AnnouncementEntity} オブジェクト。
   * @return {@link Announcement} オブジェクト。
   */
  public static Announcement createAnnouncement(AnnouncementEntity entity) {
    Announcement announcement = new Announcement();
    Optional.ofNullable(entity.getId()).ifPresent(announcement::setId);
    Optional.ofNullable(entity.getCategory()).ifPresent(announcement::setCategory);
    Optional.ofNullable(entity.getPostDateTime()).ifPresent(announcement::setPostDateTime);
    Optional.ofNullable(entity.getExpireDateTime()).ifPresent(announcement::setExpireDateTime);
    Optional.ofNullable(entity.getDisplayPriority()).ifPresent(announcement::setDisplayPriority);
    Optional.ofNullable(entity.getCreatedAt()).ifPresent(announcement::setCreatedAt);
    Optional.ofNullable(entity.getChangedAt()).ifPresent(announcement::setChangedAt);
    Optional.ofNullable(entity.getIsDeleted()).ifPresent(announcement::setIsDeleted);
    return announcement;
  }

  /**
   * エンティティ {@link Announcement} をテーブルエンティティ {@link AnnouncementEntity} に変換します。
   *
   * @param announcement {@link Announcement} オブジェクト。
   * @return {@link AnnouncementEntity} オブジェクト。
   */
  public static AnnouncementEntity createAnnouncementEntity(Announcement announcement) {
    AnnouncementEntity entity = new AnnouncementEntity();
    Optional.ofNullable(announcement.getId()).ifPresent(entity::setId);
    Optional.ofNullable(announcement.getCategory()).ifPresent(entity::setCategory);
    Optional.ofNullable(announcement.getPostDateTime()).ifPresent(entity::setPostDateTime);
    Optional.ofNullable(
        announcement.getExpireDateTime()).ifPresent(entity::setExpireDateTime);
    Optional.ofNullable(
        announcement.getDisplayPriority()).ifPresent(entity::setDisplayPriority);
    Optional.ofNullable(announcement.getCreatedAt()).ifPresent(entity::setCreatedAt);
    Optional.ofNullable(announcement.getChangedAt()).ifPresent(entity::setChangedAt);
    Optional.ofNullable(announcement.getIsDeleted()).ifPresent(entity::setIsDeleted);
    return entity;
  }

  /**
   * テーブルエンティティ {@link AnnouncementContentEntity} をエンティティ
   * {@link AnnouncementContent} に変換します。
   *
   * @param entity {@link AnnouncementContentEntity} オブジェクト。
   * @return {@link AnnouncementContent} オブジェクト。
   */
  public static AnnouncementContent createContent(AnnouncementContentEntity entity) {
    AnnouncementContent content = new AnnouncementContent();
    Optional.ofNullable(entity.getId()).ifPresent(content::setId);
    Optional.ofNullable(entity.getAnnouncementId()).ifPresent(content::setAnnouncementId);
    Optional.ofNullable(entity.getLanguageCode()).ifPresent(content::setLanguageCode);
    Optional.ofNullable(entity.getTitle()).ifPresent(content::setTitle);
    Optional.ofNullable(entity.getMessage()).ifPresent(content::setMessage);
    Optional.ofNullable(entity.getLinkUrl()).ifPresent(content::setLinkUrl);
    return content;
  }

  /**
   * エンティティ {@link AnnouncementContent} をテーブルエンティティ
   * {@link AnnouncementContentEntity} に変換します。
   *
   * @param content {@link AnnouncementContent} オブジェクト。
   * @return {@link AnnouncementContentEntity} オブジェクト。
   */
  public static AnnouncementContentEntity createAnnouncementContentEntity(
      AnnouncementContent content) {
    AnnouncementContentEntity entity = new AnnouncementContentEntity();
    Optional.ofNullable(content.getId()).ifPresent(entity::setId);
    Optional.ofNullable(content.getAnnouncementId()).ifPresent(entity::setAnnouncementId);
    Optional.ofNullable(content.getLanguageCode()).ifPresent(entity::setLanguageCode);
    Optional.ofNullable(content.getTitle()).ifPresent(entity::setTitle);
    Optional.ofNullable(content.getMessage()).ifPresent(entity::setMessage);
    Optional.ofNullable(content.getLinkUrl()).ifPresent(entity::setLinkUrl);
    return entity;
  }

  /**
   * テーブルエンティティ {@link AnnouncementHistoryEntity} をエンティティ
   * {@link AnnouncementHistory} に変換します。
   *
   * @param entity {@link AnnouncementHistoryEntity} オブジェクト。
   * @return {@link AnnouncementHistory} オブジェクト。
   */
  public static AnnouncementHistory createHistory(AnnouncementHistoryEntity entity) {
    AnnouncementHistory history = new AnnouncementHistory();
    Optional.ofNullable(entity.getId()).ifPresent(history::setId);
    Optional.ofNullable(entity.getAnnouncementId()).ifPresent(history::setAnnouncementId);
    Optional.ofNullable(entity.getCategory()).ifPresent(history::setCategory);
    Optional.ofNullable(entity.getPostDateTime()).ifPresent(history::setPostDateTime);
    Optional.ofNullable(entity.getExpireDateTime()).ifPresent(history::setExpireDateTime);
    Optional.ofNullable(entity.getDisplayPriority()).ifPresent(history::setDisplayPriority);
    Optional.ofNullable(entity.getCreatedAt()).ifPresent(history::setCreatedAt);
    Optional.ofNullable(entity.getChangedBy()).ifPresent(history::setChangedBy);
    Optional.ofNullable(entity.getOperationType()).ifPresent(history::setOperationType);
    return history;
  }

  /**
   * エンティティ {@link AnnouncementHistory} をテーブルエンティティ
   * {@link AnnouncementHistoryEntity} に変換します。
   *
   * @param history {@link AnnouncementHistory} オブジェクト。
   * @return {@link AnnouncementHistoryEntity} オブジェクト。
   */
  public static AnnouncementHistoryEntity createAnnouncementHistoryEntity(
      AnnouncementHistory history) {
    AnnouncementHistoryEntity entity = new AnnouncementHistoryEntity();
    Optional.ofNullable(history.getId()).ifPresent(entity::setId);
    Optional.ofNullable(history.getAnnouncementId()).ifPresent(entity::setAnnouncementId);
    Optional.ofNullable(history.getCategory()).ifPresent(entity::setCategory);
    Optional.ofNullable(history.getPostDateTime()).ifPresent(entity::setPostDateTime);
    Optional.ofNullable(history.getExpireDateTime()).ifPresent(entity::setExpireDateTime);
    Optional.ofNullable(history.getDisplayPriority()).ifPresent(entity::setDisplayPriority);
    Optional.ofNullable(history.getCreatedAt()).ifPresent(entity::setCreatedAt);
    Optional.ofNullable(history.getChangedBy()).ifPresent(entity::setChangedBy);
    Optional.ofNullable(history.getOperationType()).ifPresent(entity::setOperationType);
    return entity;
  }

  /**
   * テーブルエンティティ {@link AnnouncementContentHistoryEntity} をエンティティ
   * {@link AnnouncementContentHistory} に変換します。
   *
   * @param entity {@link AnnouncementContentHistoryEntity} オブジェクト。
   * @return {@link AnnouncementContentHistory} オブジェクト。
   */
  public static AnnouncementContentHistory createContentHistory(
      AnnouncementContentHistoryEntity entity) {
    AnnouncementContentHistory contentHistory = new AnnouncementContentHistory();
    Optional.ofNullable(entity.getId()).ifPresent(contentHistory::setId);
    Optional.ofNullable(entity.getAnnouncementHistoryId())
        .ifPresent(contentHistory::setAnnouncementHistoryId);
    Optional.ofNullable(entity.getLanguageCode()).ifPresent(contentHistory::setLanguageCode);
    Optional.ofNullable(entity.getTitle()).ifPresent(contentHistory::setTitle);
    Optional.ofNullable(entity.getMessage()).ifPresent(contentHistory::setMessage);
    Optional.ofNullable(entity.getLinkUrl()).ifPresent(contentHistory::setLinkUrl);
    return contentHistory;
  }

  /**
   * エンティティ {@link AnnouncementContentHistory} をテーブルエンティティ
   * {@link AnnouncementContentHistoryEntity} に変換します。
   *
   * @param contentHistory {@link AnnouncementContentHistory} オブジェクト。
   * @return {@link AnnouncementContentHistoryEntity} オブジェクト。
   */
  public static AnnouncementContentHistoryEntity createAnnouncementContentHistoryEntity(
      AnnouncementContentHistory contentHistory) {
    AnnouncementContentHistoryEntity entity = new AnnouncementContentHistoryEntity();
    Optional.ofNullable(contentHistory.getId()).ifPresent(entity::setId);
    Optional.ofNullable(contentHistory.getAnnouncementHistoryId())
        .ifPresent(entity::setAnnouncementHistoryId);
    Optional.ofNullable(contentHistory.getLanguageCode()).ifPresent(entity::setLanguageCode);
    Optional.ofNullable(contentHistory.getTitle()).ifPresent(entity::setTitle);
    Optional.ofNullable(contentHistory.getMessage()).ifPresent(entity::setMessage);
    Optional.ofNullable(contentHistory.getLinkUrl()).ifPresent(entity::setLinkUrl);
    return entity;
  }

}
