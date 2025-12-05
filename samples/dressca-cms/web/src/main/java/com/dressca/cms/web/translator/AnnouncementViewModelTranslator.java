package com.dressca.cms.web.translator;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.web.models.AnnouncementWithContentsViewModel;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * お知らせメッセージの DTO とビューモデルを相互変換するトランスレータークラスです。
 */
public final class AnnouncementViewModelTranslator {

  private AnnouncementViewModelTranslator() {
    // ユーティリティクラスのため、インスタンス化を禁止
  }

  /**
   * お知らせメッセージの DTO からビューモデルに変換します。
   *
   * @param dto お知らせメッセージの DTO。
   * @return お知らせメッセージのビューモデル。
   */
  public static AnnouncementViewModel toAnnouncementViewModel(Announcement dto) {
    if (dto == null) {
      return null;
    }
    return AnnouncementViewModel.builder()
        .id(dto.getId())
        .category(dto.getCategory())
        .postDate(dto.getPostDateTime() != null ? dto.getPostDateTime().toLocalDate() : null)
        .postTime(dto.getPostDateTime() != null ? dto.getPostDateTime().toLocalTime() : null)
        .expireDate(dto.getExpireDateTime() != null ? dto.getExpireDateTime().toLocalDate() : null)
        .expireTime(dto.getExpireDateTime() != null ? dto.getExpireDateTime().toLocalTime() : null)
        .displayPriority(dto.getDisplayPriority())
        .createdAt(dto.getCreatedAt())
        .changedAt(dto.getChangedAt())
        .isDeleted(dto.getIsDeleted())
        .build();
  }

  /**
   * お知らせメッセージのビューモデルから DTO に変換します。
   *
   * @param viewModel お知らせメッセージのビューモデル。
   * @return お知らせメッセージの DTO。
   */
  public static Announcement toAnnouncementDto(AnnouncementViewModel viewModel,
      List<AnnouncementContentViewModel> contentViewModel) {
    if (viewModel == null) {
      return null;
    }

    // 日付と時刻を結合してOffsetDateTimeに変換
    OffsetDateTime postDateTime = combineDateTime(viewModel.getPostDate(), viewModel.getPostTime());
    OffsetDateTime expireDateTime = combineDateTime(viewModel.getExpireDate(), viewModel.getExpireTime());

    List<AnnouncementContent> contents = contentViewModel != null
        ? contentViewModel.stream().map(AnnouncementViewModelTranslator::toContentDto)
            .collect(Collectors.toCollection(ArrayList::new))
        : new ArrayList<>();
    return new Announcement(
        viewModel.getId(),
        viewModel.getCategory(),
        postDateTime,
        expireDateTime,
        viewModel.getDisplayPriority(),
        viewModel.getCreatedAt(),
        viewModel.getChangedAt(),
        viewModel.getIsDeleted(),
        contents);
  }

  /**
   * お知らせコンテンツの DTO からビューモデルに変換します。
   *
   * @param content お知らせコンテンツの DTO。
   * @return お知らせコンテンツのビューモデル。
   */
  public static AnnouncementContentViewModel toContentViewModel(AnnouncementContent content) {
    if (content == null) {
      return null;
    }
    return AnnouncementContentViewModel.builder()
        .id(content.getId())
        .announcementId(content.getAnnouncementId())
        .languageCode(content.getLanguageCode())
        .title(content.getTitle())
        .message(content.getMessage())
        .linkUrl(content.getLinkUrl())
        .build();
  }

  /**
   * お知らせコンテンツの DTO リストからビューモデルのリストに変換します。
   *
   * @param contents お知らせコンテンツの DTO リスト。
   * @return お知らせコンテンツのビューモデルのリスト。
   */
  public static List<AnnouncementContentViewModel> toContentViewModels(List<AnnouncementContent> contents) {
    if (contents == null) {
      return new ArrayList<>();
    }
    return contents.stream()
        .map(AnnouncementViewModelTranslator::toContentViewModel)
        .collect(Collectors.toList());
  }

  /**
   * お知らせコンテンツのビューモデルから DTO に変換します。
   *
   * @param viewModel お知らせコンテンツのビューモデル。
   * @return お知らせコンテンツの DTO。
   */
  public static AnnouncementContent toContentDto(AnnouncementContentViewModel viewModel) {
    if (viewModel == null) {
      return null;
    }
    return new AnnouncementContent(
        viewModel.getId(),
        viewModel.getAnnouncementId(),
        viewModel.getLanguageCode(),
        viewModel.getTitle(),
        viewModel.getMessage(),
        viewModel.getLinkUrl());
  }

  /**
   * お知らせメッセージの DTO から、お知らせメッセージとお知らせコンテンツをラップする ビューモデルに変換します。
   *
   * @param dto お知らせメッセージの DTO。
   * @return お知らせメッセージとお知らせコンテンツをラップするビューモデル。
   */
  public static AnnouncementWithContentsViewModel toAnnouncementWithContentsViewModel(
      Announcement dto) {
    if (dto == null) {
      return null;
    }

    AnnouncementViewModel announcementViewModel = toAnnouncementViewModel(dto);

    AnnouncementContentViewModel contentViewModel = null;
    if (dto.getContents() != null && !dto.getContents().isEmpty()) {
      contentViewModel = toContentViewModel(dto.getContents().get(0));
    }

    return new AnnouncementWithContentsViewModel(announcementViewModel, contentViewModel);
  }

  /**
   * お知らせメッセージの DTO リストから、お知らせメッセージとお知らせコンテンツをラップする ビューモデルのリストに変換します。
   *
   * @param dtos お知らせメッセージの DTO リスト。
   * @return お知らせメッセージとお知らせコンテンツをラップするビューモデルのリスト。
   */
  public static List<AnnouncementWithContentsViewModel> toAnnouncementWithContentsViewModels(
      List<Announcement> dtos) {
    if (dtos == null) {
      return new ArrayList<>();
    }

    return dtos.stream()
        .map(AnnouncementViewModelTranslator::toAnnouncementWithContentsViewModel)
        .collect(Collectors.toList());
  }

  /**
   * 日付と時刻を結合してOffsetDateTimeに変換します。
   *
   * @param date 日付。
   * @param time 時刻。
   * @return OffsetDateTime。日付または時刻がnullの場合はnullを返します。
   */
  private static OffsetDateTime combineDateTime(LocalDate date, LocalTime time) {
    if (date == null || time == null) {
      return null;
    }
    return ZonedDateTime.of(date, time, ZoneId.systemDefault()).toOffsetDateTime();
  }

}
