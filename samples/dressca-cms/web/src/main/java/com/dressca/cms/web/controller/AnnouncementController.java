package com.dressca.cms.web.controller;

import com.dressca.cms.announcement.applicationcore.AnnouncementApplicationService;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementWithHistory;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementNotFoundException;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementValidationException;
import com.dressca.cms.systemcommon.constant.LanguageCodeConstants;
import com.dressca.cms.systemcommon.constant.SystemPropertyConstants;
import com.dressca.cms.systemcommon.exception.ValidationError;
import com.dressca.cms.systemcommon.util.UuidGenerator;
import com.dressca.cms.web.constant.DisplayPriority;
import com.dressca.cms.web.constant.LanguageCode;
import com.dressca.cms.web.constant.OperationType;
import com.dressca.cms.web.models.AnnouncementCreateViewModel;
import com.dressca.cms.web.models.AnnouncementDeleteCompleteViewModel;
import com.dressca.cms.web.models.AnnouncementDeleteConfirmViewModel;
import com.dressca.cms.web.models.AnnouncementEditViewModel;
import com.dressca.cms.web.models.AnnouncementHistoryWithContentHistoriesViewModel;
import com.dressca.cms.web.models.AnnouncementListViewModel;
import com.dressca.cms.web.models.AnnouncementWithContentsViewModel;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import com.dressca.cms.web.models.validation.AnnouncementValidationGroup;
import com.dressca.cms.web.session.AnnouncementCreateSession;
import com.dressca.cms.web.session.AnnouncementEditSession;
import com.dressca.cms.web.translator.AnnouncementViewModelTranslator;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * お知らせメッセージ管理画面のコントローラークラスです。
 */
@Controller
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
  private static final Logger apLog =
      LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);
  private final AnnouncementApplicationService announcementApplicationService;
  private final AnnouncementCreateSession announcementCreateSession;
  private final AnnouncementEditSession announcementEditSession;

  /**
   * お知らせメッセージ管理画面を表示します。
   *
   * @param pageNumber ページ番号（省略可）。
   * @param pageSize ページサイズ（省略可）。
   * @param model モデル。
   * @return ビュー名。
   */
  @GetMapping
  public String index(@RequestParam(required = false) String pageNumber,
      @RequestParam(required = false) String pageSize, Model model) {

    // クエリ文字列から値を取得し、数値以外の値は未指定にする
    Integer pageNumberInt = parseInteger(pageNumber);
    Integer pageSizeInt = parseInteger(pageSize);

    // ApplicationService を呼び出してページングされたお知らせメッセージを取得
    PagedAnnouncementList pagedList =
        announcementApplicationService.getPagedAnnouncementList(pageNumberInt, pageSizeInt);
    // ビューモデルに変換
    List<AnnouncementWithContentsViewModel> announcementViewModels = AnnouncementViewModelTranslator
        .toAnnouncementWithContentsViewModels(pagedList.getAnnouncements());

    // お知らせメッセージ管理画面のビューモデルを生成
    AnnouncementListViewModel viewModel =
        new AnnouncementListViewModel(pagedList.getPageNumber(), pagedList.getPageSize(),
            pagedList.getTotalCount(), announcementViewModels, pagedList.getLastPageNumber());

    // モデルに属性を格納
    model.addAttribute("viewModel", viewModel);
    model.addAttribute("displayPriority", DisplayPriority.values());

    // お知らせメッセージ管理画面に遷移
    return "announcement/index";
  }

  /**
   * お知らせメッセージ登録画面を表示します。
   *
   * @param model モデル。
   * @return ビュー名。
   */
  @GetMapping("/create")
  public String create(Model model) {
    // セッションにお知らせメッセージのエンティティが存在しない場合、初期化する
    if (announcementCreateSession.getAnnouncement() == null) {
      final Announcement announcement = new Announcement();
      announcement.setId(UuidGenerator.generate());
      announcement.setDisplayPriority(DisplayPriority.MEDIUM.getValue());
      AnnouncementContent jaContent = new AnnouncementContent(UuidGenerator.generate(),
          announcement.getId(), LanguageCodeConstants.LOCALE_JA.getLanguage(), "", "", "");
      announcement.setContents(new ArrayList<>(List.of(jaContent)));
      announcementCreateSession.setAnnouncement(announcement);
    }

    // セッションからお知らせメッセージを取得してビューモデルに変換
    Announcement announcement = announcementCreateSession.getAnnouncement();
    List<AnnouncementContent> content = announcement.getContents();
    AnnouncementViewModel announcementViewModel =
        AnnouncementViewModelTranslator.toAnnouncementViewModel(announcement);
    List<AnnouncementContentViewModel> contentViewModel =
        AnnouncementViewModelTranslator.toContentViewModels(content);
    AnnouncementCreateViewModel viewModel =
        new AnnouncementCreateViewModel(announcementViewModel, contentViewModel);

    model.addAttribute("viewModel", viewModel);
    model.addAttribute("displayPriority", DisplayPriority.values());
    model.addAttribute("languageCode", LanguageCode.values());

    return "announcement/create";
  }

  /**
   * お知らせメッセージを登録します。
   *
   * @param viewModel お知らせメッセージ登録画面のビューモデル。
   * @param bindingResult バインディング結果。
   * @param model モデル。
   * @param userDetails 認証ユーザー情報。
   * @return ビュー名またはリダイレクト先。
   */
  @PostMapping("/create")
  public String store(
      @Validated(AnnouncementValidationGroup.Store.class) @ModelAttribute("viewModel") AnnouncementCreateViewModel viewModel,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails userDetails) {

    if (viewModel.getAnnouncement().getPostTime() == null) {
      viewModel.getAnnouncement().setPostTime(LocalTime.of(0, 0, 0));
    }
    if (viewModel.getAnnouncement().getExpireDate() != null
        && viewModel.getAnnouncement().getExpireTime() == null) {
      viewModel.getAnnouncement().setExpireTime(LocalTime.of(0, 0, 0));
    }
    // 掲載終了日時のチェック
    if (viewModel.getAnnouncement().getExpireDate() != null) {
      OffsetDateTime postDateTime = AnnouncementViewModelTranslator.combineDateTime(
          viewModel.getAnnouncement().getPostDate(), viewModel.getAnnouncement().getPostTime());
      OffsetDateTime expireDateTime = AnnouncementViewModelTranslator.combineDateTime(
          viewModel.getAnnouncement().getExpireDate(), viewModel.getAnnouncement().getExpireTime());
      if (expireDateTime.isBefore(postDateTime)) {
        bindingResult.rejectValue("announcement.expireDate",
            "announcement.create.expireDateBeforePostDate");
      }
    }
    // 宣言的バリデーションでエラーがある場合は画面を再表示
    if (bindingResult.hasErrors()) {
      model.addAttribute("displayPriority", DisplayPriority.values());
      model.addAttribute("languageCode", LanguageCode.values());
      return "announcement/create";
    }

    Announcement announcement = AnnouncementViewModelTranslator
        .toAnnouncementDto(viewModel.getAnnouncement(), viewModel.getContents());
    try {

      // アプリケーションサービスを呼び出してお知らせメッセージを登録
      UUID announcementId = announcementApplicationService.addAnnouncementAndHistory(announcement,
          userDetails.getUsername());

      // セッションをクリア
      announcementCreateSession.clear();

      // お知らせメッセージ編集画面にリダイレクト
      return "redirect:/announcements/" + announcementId + "/edit";

    } catch (AnnouncementValidationException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      // 非宣言的バリデーションエラーの場合はエラーメッセージを設定して画面を再表示
      for (ValidationError error : e.getValidationErrors()) {
        if (error.getFieldName().equals("global")) {
          bindingResult.reject(error.getErrorCode());
          continue;
        }
        bindingResult.rejectValue(error.getFieldName(), error.getErrorCode());
      }
      model.addAttribute("displayPriority", DisplayPriority.values());
      model.addAttribute("languageCode", LanguageCode.values());
      return "announcement/create";
    }
  }

  /**
   * 登録画面で別の言語を追加します。
   *
   * @param viewModel お知らせメッセージ登録画面のビューモデル。
   * @return リダイレクト先。
   */
  @PostMapping(value = "create", params = "addLanguageToCreate")
  public String addLanguageToCreate(
      @ModelAttribute("viewModel") AnnouncementCreateViewModel viewModel) {

    // ビューモデルからDTOに変換
    Announcement announcement = AnnouncementViewModelTranslator
        .toAnnouncementDto(viewModel.getAnnouncement(), viewModel.getContents());

    // 登録済みの言語コードを取得
    List<String> existingLanguageCodes = new ArrayList<>();
    List<AnnouncementContent> contents = announcement.getContents();
    if (contents != null) {
      for (AnnouncementContent content : contents) {
        existingLanguageCodes.add(content.getLanguageCode());
      }
    }

    // 未登録の最も優先度の高い言語コードを追加
    String newLanguageCode = getNextPriorityLanguageCode(existingLanguageCodes);
    AnnouncementContent newContent = new AnnouncementContent(UuidGenerator.generate(),
        announcement.getId(), newLanguageCode, "", "", "");
    contents.add(newContent);

    // セッションに保存
    announcementCreateSession.setAnnouncement(announcement);

    return "redirect:/announcements/create";
  }

  /**
   * 登録画面で言語別お知らせメッセージを削除します。
   *
   * @param viewModel お知らせメッセージ登録画面のビューモデル。
   * @param announcementContentId 削除するお知らせコンテンツ ID。
   * @return リダイレクト先。
   */
  @PostMapping(value = "create", params = "deleteLanguageFromCreate")
  public String deleteLanguageFromCreate(
      @ModelAttribute("viewModel") AnnouncementCreateViewModel viewModel,
      @RequestParam("deleteLanguageFromCreate") UUID announcementContentId) {

    // ビューモデルからDTOに変換
    Announcement announcement = AnnouncementViewModelTranslator
        .toAnnouncementDto(viewModel.getAnnouncement(), viewModel.getContents());

    // 指定されたお知らせコンテンツ ID のコンテンツを削除
    List<AnnouncementContent> contents = announcement.getContents();
    if (contents != null) {
      contents.removeIf(content -> content.getId().equals(announcementContentId));
    }

    // セッションに保存
    announcementCreateSession.setAnnouncement(announcement);

    return "redirect:/announcements/create";
  }

  /**
   * お知らせメッセージ編集画面を表示します。
   * 
   * @param announcementId お知らせメッセージ ID。
   * @param model モデル。
   * @return お知らせメッセージ編集画面のビュー名。
   */
  @GetMapping("{announcementId}/edit")
  public String edit(@PathVariable("announcementId") UUID announcementId, Model model) {
    try {

      // セッションにお知らせメッセージが存在しない、またはIDが異なる場合、取得する
      if (announcementEditSession.getAnnouncement() == null
          || !announcementEditSession.getAnnouncement().getId().equals(announcementId)) {
        AnnouncementWithHistory announcementWithHistory =
            announcementApplicationService.getAnnouncementAndHistoriesById(announcementId);
        announcementEditSession.setAnnouncement(announcementWithHistory.getAnnouncement());
        announcementEditSession.setHistories(announcementWithHistory.getHistories());
      }

      // セッションからお知らせメッセージと履歴を取得してビューモデルに変換
      Announcement announcement = announcementEditSession.getAnnouncement();
      List<AnnouncementContent> contents = announcement.getContents();
      List<AnnouncementHistory> histories = announcementEditSession.getHistories();

      AnnouncementViewModel announcementViewModel =
          AnnouncementViewModelTranslator.toAnnouncementViewModel(announcement);
      List<AnnouncementContentViewModel> contentViewModels =
          AnnouncementViewModelTranslator.toContentViewModels(contents);

      List<AnnouncementHistoryWithContentHistoriesViewModel> historyViewModels = histories.stream()
          .map(AnnouncementViewModelTranslator::toHistoryWithContentHistoriesViewModel)
          .collect(Collectors.toList());

      AnnouncementEditViewModel viewModel = new AnnouncementEditViewModel(announcementViewModel,
          contentViewModels, historyViewModels);

      model.addAttribute("viewModel", viewModel);
      model.addAttribute("displayPriority", DisplayPriority.values());
      model.addAttribute("displayPriorityLabelMap", DisplayPriority.DISPLAY_PRIORITY_LABEL_MAP);
      model.addAttribute("languageCode", LanguageCode.values());
      model.addAttribute("languageCodeLabelMap", LanguageCode.LANGUAGE_CODE_LABEL_MAP);
      model.addAttribute("operationTypeLabelMap", OperationType.OPERATION_TYPE_LABEL_MAP);
      return "announcement/edit";
    } catch (AnnouncementNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return "not_found";
    }
  }

  /**
   * お知らせメッセージを更新します。
   *
   * @param announcementId お知らせメッセージ ID。
   * @param viewModel お知らせメッセージ編集画面のビューモデル。
   * @param bindingResult バインディング結果。
   * @param model モデル。
   * @param userDetails 認証ユーザー情報。
   * @return ビュー名またはリダイレクト先。
   */
  @PostMapping("{announcementId}/edit")
  public String update(@PathVariable("announcementId") UUID announcementId,
      @Validated(AnnouncementValidationGroup.Update.class) @ModelAttribute("viewModel") AnnouncementEditViewModel viewModel,
      BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails userDetails) {

    if (viewModel.getAnnouncement().getPostTime() == null) {
      viewModel.getAnnouncement().setPostTime(LocalTime.of(0, 0, 0));
    }
    if (viewModel.getAnnouncement().getExpireDate() != null
        && viewModel.getAnnouncement().getExpireTime() == null) {
      viewModel.getAnnouncement().setExpireTime(LocalTime.of(0, 0, 0));
    }
    // 掲載終了日時のチェック
    if (viewModel.getAnnouncement().getExpireDate() != null) {
      OffsetDateTime postDateTime = AnnouncementViewModelTranslator.combineDateTime(
          viewModel.getAnnouncement().getPostDate(), viewModel.getAnnouncement().getPostTime());
      OffsetDateTime expireDateTime = AnnouncementViewModelTranslator.combineDateTime(
          viewModel.getAnnouncement().getExpireDate(), viewModel.getAnnouncement().getExpireTime());
      if (expireDateTime.isBefore(postDateTime)) {
        bindingResult.rejectValue("announcement.expireDate",
            "announcement.edit.expireDateBeforePostDate");
      }
    }

    // 宣言的バリデーションでエラーがある場合は画面を再表示
    if (bindingResult.hasErrors()) {
      // セッションから履歴を取得してviewModelに設定
      List<AnnouncementHistory> histories = announcementEditSession.getHistories();
      List<AnnouncementHistoryWithContentHistoriesViewModel> historyViewModels = histories.stream()
          .map(AnnouncementViewModelTranslator::toHistoryWithContentHistoriesViewModel)
          .collect(Collectors.toList());
      viewModel.setHistories(historyViewModels);
      model.addAttribute("viewModel", viewModel);
      model.addAttribute("displayPriority", DisplayPriority.values());
      model.addAttribute("displayPriorityLabelMap", DisplayPriority.DISPLAY_PRIORITY_LABEL_MAP);
      model.addAttribute("languageCode", LanguageCode.values());
      model.addAttribute("languageCodeLabelMap", LanguageCode.LANGUAGE_CODE_LABEL_MAP);
      model.addAttribute("operationTypeLabelMap", OperationType.OPERATION_TYPE_LABEL_MAP);
      return "announcement/edit";
    }

    Announcement announcement = AnnouncementViewModelTranslator
        .toAnnouncementDto(viewModel.getAnnouncement(), viewModel.getContents());
    try {
      // アプリケーションサービスを呼び出してお知らせメッセージを更新
      announcementApplicationService.updateAnnouncement(announcement, userDetails.getUsername());

      // セッションをクリア
      announcementEditSession.clear();

      // お知らせメッセージ編集画面にリダイレクト
      return "redirect:/announcements/" + announcementId + "/edit";

    } catch (AnnouncementValidationException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      // 非宣言的バリデーションエラーの場合はエラーメッセージを設定して画面を再表示
      for (ValidationError error : e.getValidationErrors()) {
        if (error.getFieldName().equals("global")) {
          bindingResult.reject(error.getErrorCode());
          continue;
        }
        bindingResult.rejectValue(error.getFieldName(), error.getErrorCode());
      }
      // セッションから履歴を取得してviewModelに設定
      List<AnnouncementHistory> histories = announcementEditSession.getHistories();
      List<AnnouncementHistoryWithContentHistoriesViewModel> historyViewModels = histories.stream()
          .map(AnnouncementViewModelTranslator::toHistoryWithContentHistoriesViewModel)
          .collect(Collectors.toList());
      viewModel.setHistories(historyViewModels);
      model.addAttribute("viewModel", viewModel);
      model.addAttribute("displayPriority", DisplayPriority.values());
      model.addAttribute("displayPriorityLabelMap", DisplayPriority.DISPLAY_PRIORITY_LABEL_MAP);
      model.addAttribute("languageCode", LanguageCode.values());
      model.addAttribute("languageCodeLabelMap", LanguageCode.LANGUAGE_CODE_LABEL_MAP);
      model.addAttribute("operationTypeLabelMap", OperationType.OPERATION_TYPE_LABEL_MAP);
      return "announcement/edit";
    }
  }

  /**
   * 編集画面で別の言語を追加します。
   *
   * @param announcementId お知らせメッセージ ID。
   * @param viewModel お知らせメッセージ編集画面のビューモデル。
   * @return リダイレクト先。
   */
  @PostMapping(value = "{announcementId}/edit", params = "addLanguageToEdit")
  public String addLanguageToEdit(@PathVariable("announcementId") UUID announcementId,
      @ModelAttribute("viewModel") AnnouncementEditViewModel viewModel) {

    // ビューモデルからDTOに変換
    Announcement announcement = AnnouncementViewModelTranslator
        .toAnnouncementDto(viewModel.getAnnouncement(), viewModel.getContents());

    // 登録済みの言語コードを取得
    List<String> existingLanguageCodes = new ArrayList<>();
    if (announcement.getContents() != null) {
      for (AnnouncementContent content : announcement.getContents()) {
        existingLanguageCodes.add(content.getLanguageCode());
      }
    }

    // 未登録の最も優先度の高い言語コードを追加
    String newLanguageCode = getNextPriorityLanguageCode(existingLanguageCodes);
    AnnouncementContent newContent = new AnnouncementContent(UuidGenerator.generate(),
        announcement.getId(), newLanguageCode, "", "", "");
    announcement.getContents().add(newContent);

    // セッションに保存
    announcementEditSession.setAnnouncement(announcement);

    return "redirect:/announcements/" + announcementId + "/edit";
  }

  /**
   * 編集画面で言語別お知らせメッセージを削除します。
   *
   * @param announcementId お知らせメッセージ ID。
   * @param viewModel お知らせメッセージ編集画面のビューモデル。
   * @param announcementContentId 削除するお知らせコンテンツ ID。
   * @return リダイレクト先。
   */
  @PostMapping(value = "{announcementId}/edit", params = "deleteLanguageFromEdit")
  public String deleteLanguageFromEdit(@PathVariable("announcementId") UUID announcementId,
      @ModelAttribute("viewModel") AnnouncementEditViewModel viewModel,
      @RequestParam("deleteLanguageFromEdit") UUID announcementContentId) {

    // ビューモデルからDTOに変換
    Announcement announcement = AnnouncementViewModelTranslator
        .toAnnouncementDto(viewModel.getAnnouncement(), viewModel.getContents());

    // 指定された言語コードのコンテンツを削除
    if (announcement.getContents() != null) {
      announcement.getContents().removeIf(content -> content.getId().equals(announcementContentId));
    }

    // セッションに保存
    announcementEditSession.setAnnouncement(announcement);

    return "redirect:/announcements/" + announcementId + "/edit";
  }

  /**
   * お知らせメッセージ削除確認画面を表示します。
   *
   * @param announcementId お知らせメッセージ ID。
   * @param model モデル。
   * @return お知らせメッセージ削除確認画面のビュー名。
   */
  @GetMapping("{announcementId}/delete/confirm")
  public String deleteConfirm(@PathVariable("announcementId") UUID announcementId, Model model) {
    try {
      // アプリケーションサービスを呼び出してお知らせメッセージと履歴を取得
      AnnouncementWithHistory announcementWithHistory =
          announcementApplicationService.getAnnouncementAndHistoriesById(announcementId);

      // お知らせメッセージと履歴をビューモデルに変換
      Announcement announcement = announcementWithHistory.getAnnouncement();
      List<AnnouncementContent> contents = announcement.getContents();
      List<AnnouncementHistory> histories = announcementWithHistory.getHistories();

      AnnouncementViewModel announcementViewModel =
          AnnouncementViewModelTranslator.toAnnouncementViewModel(announcement);
      List<AnnouncementContentViewModel> contentViewModels =
          AnnouncementViewModelTranslator.toContentViewModels(contents);

      List<AnnouncementHistoryWithContentHistoriesViewModel> historyViewModels = histories.stream()
          .map(AnnouncementViewModelTranslator::toHistoryWithContentHistoriesViewModel)
          .collect(Collectors.toList());

      AnnouncementDeleteConfirmViewModel viewModel = new AnnouncementDeleteConfirmViewModel(
          announcementViewModel, contentViewModels, historyViewModels);

      model.addAttribute("viewModel", viewModel);
      model.addAttribute("displayPriorityLabelMap", DisplayPriority.DISPLAY_PRIORITY_LABEL_MAP);
      model.addAttribute("languageCodeLabelMap", LanguageCode.LANGUAGE_CODE_LABEL_MAP);
      model.addAttribute("operationTypeLabelMap", OperationType.OPERATION_TYPE_LABEL_MAP);

      return "announcement/delete_confirm";
    } catch (AnnouncementNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return "not_found";
    }
  }

  /**
   * お知らせメッセージを削除します。
   *
   * @param announcementId お知らせメッセージ ID。
   * @param userDetails 認証ユーザー情報。
   * @return リダイレクト先。
   */
  @PostMapping("{announcementId}/delete/confirm")
  public String delete(@PathVariable("announcementId") UUID announcementId,
      @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
    try {
      // アプリケーションサービスを呼び出してお知らせメッセージを削除
      AnnouncementWithHistory deletedAnnouncementWithHistory = announcementApplicationService
          .deleteAnnouncementAndRecordHistory(announcementId, userDetails.getUsername());

      Announcement announcement = deletedAnnouncementWithHistory.getAnnouncement();
      List<AnnouncementHistory> histories = deletedAnnouncementWithHistory.getHistories();
      List<AnnouncementContent> contents = announcement.getContents();

      AnnouncementViewModel announcementViewModel =
          AnnouncementViewModelTranslator.toAnnouncementViewModel(announcement);
      List<AnnouncementContentViewModel> contentViewModels =
          AnnouncementViewModelTranslator.toContentViewModels(contents);
      List<AnnouncementHistoryWithContentHistoriesViewModel> historyViewModels = histories.stream()
          .map(AnnouncementViewModelTranslator::toHistoryWithContentHistoriesViewModel)
          .collect(Collectors.toList());

      AnnouncementDeleteCompleteViewModel viewModel = new AnnouncementDeleteCompleteViewModel(
          announcementViewModel, contentViewModels, historyViewModels);

      redirectAttributes.addFlashAttribute("viewModel", viewModel);

      // お知らせメッセージ削除完了画面にリダイレクト
      return "redirect:/announcements/" + announcementId + "/delete/complete";

    } catch (AnnouncementNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      // お知らせメッセージ管理画面にリダイレクト
      return "redirect:/announcements";
    }
  }

  /**
   * お知らせメッセージ削除完了画面を表示します。
   *
   * @param viewModel お知らせメッセージ削除完了画面のビューモデル。
   * @param model モデル。
   * @return お知らせメッセージ削除完了画面のビュー名。
   */
  @GetMapping("{announcementId}/delete/complete")
  public String deleteComplete(
      @ModelAttribute(value = "viewModel") AnnouncementDeleteCompleteViewModel viewModel,
      Model model) {
    if (viewModel.getAnnouncement().getId() == null || viewModel.getHistories().isEmpty()) {
      return "redirect:/announcements";
    }

    model.addAttribute("displayPriorityLabelMap", DisplayPriority.DISPLAY_PRIORITY_LABEL_MAP);
    model.addAttribute("languageCodeLabelMap", LanguageCode.LANGUAGE_CODE_LABEL_MAP);
    model.addAttribute("operationTypeLabelMap", OperationType.OPERATION_TYPE_LABEL_MAP);

    return "announcement/delete_complete";
  }

  /**
   * 文字列を整数に変換します。変換できない場合は null を返します。
   *
   * @param value 文字列。
   * @return 整数、または変換できない場合は null。
   */
  private Integer parseInteger(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * 未登録の最も優先度の高い言語コードを取得します。
   *
   * @param existingLanguageCodes 既存の言語コードのリスト。
   * @return 未登録の最も優先度の高い言語コード。すべて登録済みの場合は日本語。
   */
  private String getNextPriorityLanguageCode(List<String> existingLanguageCodes) {
    String[] languageCodes = LanguageCodeConstants.LANGUAGE_CODE_PRIORITY.entrySet().stream()
        .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toArray(String[]::new);
    for (String languageCode : languageCodes) {
      if (!existingLanguageCodes.contains(languageCode)) {
        return languageCode;
      }
    }
    return LanguageCodeConstants.LOCALE_JA.getLanguage();
  }
}
