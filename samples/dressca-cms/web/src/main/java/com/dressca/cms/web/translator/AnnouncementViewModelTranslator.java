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
    
    AnnouncementViewModel viewModel = new AnnouncementViewModel();
    viewModel.setId(dto.getId());
    viewModel.setCategory(dto.getCategory());
    viewModel.setPostDateTime(dto.getPostDateTime());
    viewModel.setExpireDateTime(dto.getExpireDateTime());
    viewModel.setDisplayPriority(dto.getDisplayPriority());
    viewModel.setCreatedAt(dto.getCreatedAt());
    viewModel.setChangedAt(dto.getChangedAt());
    viewModel.setIsDeleted(dto.getIsDeleted());
    
    if (dto.getContents() != null) {
      List<AnnouncementContentViewModel> contentViewModels = dto.getContents().stream()
          .map(AnnouncementViewModelTranslator::toContentViewModel)
          .collect(Collectors.toList());
      viewModel.setContents(contentViewModels);
    }
    
    return viewModel;
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
    
    AnnouncementContentViewModel viewModel = new AnnouncementContentViewModel();
    viewModel.setId(dto.getId());
    viewModel.setAnnouncementId(dto.getAnnouncementId());
    viewModel.setLanguageCode(dto.getLanguageCode());
    viewModel.setTitle(dto.getTitle());
    viewModel.setMessage(dto.getMessage());
    viewModel.setLinkUrl(dto.getLinkUrl());
    
    return viewModel;
  }
  
  /**
   * お知らせメッセージの DTO から、お知らせメッセージとお知らせコンテンツをラップする
   * ビューモデルに変換します。
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
   * お知らせメッセージの DTO リストから、お知らせメッセージとお知らせコンテンツをラップする
   * ビューモデルのリストに変換します。
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
