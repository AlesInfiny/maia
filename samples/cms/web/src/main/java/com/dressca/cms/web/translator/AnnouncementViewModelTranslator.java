package com.dressca.cms.web.translator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;

/**
 * お知らせメッセージのエンティティとビューモデルのトランスレーターです。
 */
public class AnnouncementViewModelTranslator {
  /**
   * エンティティ {@link Announcement} をビューモデル {@link AnnouncementViewModel} に変換する。
   * 
   * @param announcement {@link Announcement} オブジェクト。
   * @return {@link AnnouncementViewModel} オブジェクト。
   */
  public static AnnouncementViewModel createAnnouncementViewModel(Announcement announcement) {
    AnnouncementViewModel viewModel = new AnnouncementViewModel();
    Optional.ofNullable(announcement.getCategory()).ifPresent(viewModel::setCategory);
    OffsetDateTime postDateTime = announcement.getPostDateTime();
    if (postDateTime != null) {
      viewModel.setPostDate(postDateTime.toLocalDate());
      viewModel.setPostTime(postDateTime.toLocalTime());
    }
    OffsetDateTime expireDateTime = announcement.getExpireDateTime();
    if (expireDateTime != null) {
      viewModel.setExpireDate(expireDateTime.toLocalDate());
      viewModel.setExpireTime(expireDateTime.toLocalTime());
    }
    Optional.ofNullable(announcement.getDisplayPriority()).ifPresent(viewModel::setDisplayPriority);
    return viewModel;
  }

  /**
   * ビューモデル {@link AnnouncementViewModel} をエンティティ {@link Announcement} に変換する。
   *
   * @param viewModel {@link AnnouncementViewModel} オブジェクト。
   * @return {@link Announcement} オブジェクト。
   */
  public static Announcement createAnnouncement(AnnouncementViewModel viewModel) {
    Announcement announcement = new Announcement();
    Optional.ofNullable(viewModel.getCategory()).ifPresent(announcement::setCategory);
    LocalDate postDate = viewModel.getPostDate();
    LocalTime postTime = viewModel.getPostTime();
    if (postDate != null && postTime != null) {
      announcement.setPostDateTime(
          LocalDateTime.of(postDate, postTime).atZone(ZoneId.systemDefault()).toOffsetDateTime());
    }
    LocalDate expireDate = viewModel.getExpireDate();
    LocalTime expireTime = viewModel.getExpireTime();
    if (expireDate != null && expireTime != null) {
      announcement.setExpireDateTime(LocalDateTime.of(expireDate, expireTime)
          .atZone(ZoneId.systemDefault()).toOffsetDateTime());
    }
    Optional.ofNullable(viewModel.getDisplayPriority()).ifPresent(announcement::setDisplayPriority);
    return announcement;
  }

  /**
   * エンティティ {@link AnnouncementContent} をビューモデル {@link AnnouncementContentViewModel} に変換する。
   *
   * @param content {@link AnnouncementContent} オブジェクト。
   * @return {@link AnnouncementContentViewModel} オブジェクト。
   */
  public static AnnouncementContentViewModel createContentViewModel(AnnouncementContent content) {
    AnnouncementContentViewModel viewModel = new AnnouncementContentViewModel();
    Optional.ofNullable(content.getLanguageCode()).ifPresent(viewModel::setLanguageCode);
    Optional.ofNullable(content.getTitle()).ifPresent(viewModel::setTitle);
    Optional.ofNullable(content.getMessage()).ifPresent(viewModel::setMessage);
    Optional.ofNullable(content.getLinkUrl()).ifPresent(viewModel::setLinkUrl);
    return viewModel;
  }

  /**
   * ビューモデル {@link AnnouncementContentViewModel} をエンティティ {@link AnnouncementContent} に変換する。
   *
   * @param viewModel {@link AnnouncementContentViewModel} オブジェクト。
   * @return {@link AnnouncementContent} オブジェクト。
   */
  public static AnnouncementContent createContent(AnnouncementContentViewModel viewModel) {
    AnnouncementContent content = new AnnouncementContent();
    Optional.ofNullable(viewModel.getLanguageCode()).ifPresent(content::setLanguageCode);
    Optional.ofNullable(viewModel.getTitle()).ifPresent(content::setTitle);
    Optional.ofNullable(viewModel.getMessage()).ifPresent(content::setMessage);
    Optional.ofNullable(viewModel.getLinkUrl()).ifPresent(content::setLinkUrl);
    return content;
  }

}
