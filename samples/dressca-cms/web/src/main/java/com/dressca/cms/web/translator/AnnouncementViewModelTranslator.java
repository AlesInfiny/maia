package com.dressca.cms.web.translator;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.web.models.AnnouncementWithContentsViewModel;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
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
  public static AnnouncementViewModel toViewModel(Announcement dto) {
    if (dto == null) {
      return null;
    }
    return AnnouncementViewModel.builder()
        .id(dto.getId())
        .category(dto.getCategory())
        .postDateTime(dto.getPostDateTime())
        .expireDateTime(dto.getExpireDateTime())
        .displayPriority(dto.getDisplayPriority())
        .createdAt(dto.getCreatedAt())
        .changedAt(dto.getChangedAt())
        .isDeleted(dto.getIsDeleted())
        .contents(dto.getContents() == null
            ? null
            : dto.getContents().stream()
                .map(AnnouncementViewModelTranslator::toContentViewModel)
                .collect(Collectors.toList()))
        .build();
  }

  /**
   * お知らせコンテンツの DTO からビューモデルに変換します。
   *
   * @param dto お知らせコンテンツの DTO。
   * @return お知らせコンテンツのビューモデル。
   */
  public static AnnouncementContentViewModel toContentViewModel(AnnouncementContent dto) {
    if (dto == null) {
      return null;
    }

    return AnnouncementContentViewModel.builder()
        .id(dto.getId())
        .announcementId(dto.getAnnouncementId())
        .languageCode(dto.getLanguageCode())
        .title(dto.getTitle())
        .message(dto.getMessage())
        .linkUrl(dto.getLinkUrl())
        .build();
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

    AnnouncementViewModel announcementViewModel = toViewModel(dto);

    // 代表のコンテンツ（リストの最初のもの）を取得
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
}
