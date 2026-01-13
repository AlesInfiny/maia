package com.dressca.cms.announcement.infrastructure.translator;

import org.springframework.beans.BeanUtils;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentHistoryEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntity;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementHistoryEntity;

/**
 * お知らせメッセージのエンティティと DTO を相互変換するトランスレータークラスです。
 */
public final class AnnouncementEntityTranslator {

  private AnnouncementEntityTranslator() {
    // ユーティリティクラスのため、インスタンス化を禁止
  }

  /**
   * お知らせメッセージの DTO からエンティティに変換します。
   *
   * @param dto お知らせメッセージの DTO。
   * @return お知らせメッセージのエンティティ。
   */
  public static AnnouncementEntity toAnnouncementEntity(Announcement dto) {
    if (dto == null) {
      return null;
    }
    AnnouncementEntity entity = new AnnouncementEntity();
    BeanUtils.copyProperties(dto, entity);
    return entity;
  }

  /**
   * お知らせコンテンツの DTO からエンティティに変換します。
   *
   * @param dto お知らせコンテンツの DTO。
   * @return お知らせコンテンツのエンティティ。
   */
  public static AnnouncementContentEntity toContentEntity(AnnouncementContent dto) {
    if (dto == null) {
      return null;
    }
    AnnouncementContentEntity entity = new AnnouncementContentEntity();
    BeanUtils.copyProperties(dto, entity);
    return entity;
  }

  /**
   * お知らせメッセージ履歴の DTO からエンティティに変換します。
   *
   * @param dto お知らせメッセージ履歴の DTO。
   * @return お知らせメッセージ履歴のエンティティ。
   */
  public static AnnouncementHistoryEntity toHistoryEntity(AnnouncementHistory dto) {
    if (dto == null) {
      return null;
    }
    AnnouncementHistoryEntity entity = new AnnouncementHistoryEntity();
    BeanUtils.copyProperties(dto, entity);
    return entity;
  }

  /**
   * お知らせコンテンツ履歴の DTO からエンティティに変換します。
   *
   * @param dto お知らせコンテンツ履歴の DTO。
   * @return お知らせコンテンツ履歴のエンティティ。
   */
  public static AnnouncementContentHistoryEntity toContentHistoryEntity(
      AnnouncementContentHistory dto) {
    if (dto == null) {
      return null;
    }
    AnnouncementContentHistoryEntity entity = new AnnouncementContentHistoryEntity();
    BeanUtils.copyProperties(dto, entity);
    return entity;
  }

  /**
   * お知らせメッセージのエンティティから DTO に変換します。
   * 
   * @param entity お知らせメッセージのエンティティ。
   * @return お知らせメッセージの DTO。
   */
  public static Announcement toAnnouncementDto(AnnouncementEntity entity) {
    if (entity == null) {
      return null;
    }
    Announcement dto = new Announcement();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }

  /**
   * お知らせコンテンツのエンティティから DTO に変換します。
   *
   * @param entity お知らせコンテンツのエンティティ。
   * @return お知らせコンテンツの DTO。
   */
  public static AnnouncementContent toContent(AnnouncementContentEntity entity) {
    if (entity == null) {
      return null;
    }
    AnnouncementContent dto = new AnnouncementContent();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }
}
