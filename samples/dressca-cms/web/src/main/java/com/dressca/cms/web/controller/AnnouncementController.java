package com.dressca.cms.web.controller;

import com.dressca.cms.announcement.applicationcore.AnnouncementApplicationService;
import com.dressca.cms.announcement.applicationcore.constant.DisplayPriorityConstants;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementValidationException;
import com.dressca.cms.systemcommon.constant.LanguageCodeConstants;
import com.dressca.cms.systemcommon.constant.SystemPropertyConstants;
import com.dressca.cms.systemcommon.exception.ValidationError;
import com.dressca.cms.systemcommon.util.UuidGenerator;
import com.dressca.cms.web.constant.DisplayPriorityOptions;
import com.dressca.cms.web.constant.LanguageCodeOptions;
import com.dressca.cms.web.log.ErrorMessageBuilder;
import com.dressca.cms.web.models.AnnouncementCreateViewModel;
import com.dressca.cms.web.models.AnnouncementListViewModel;
import com.dressca.cms.web.models.AnnouncementWithContentsViewModel;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import com.dressca.cms.web.models.validation.AnnouncementValidationGroup;
import com.dressca.cms.web.session.AnnouncementCreateSession;
import com.dressca.cms.web.translator.AnnouncementViewModelTranslator;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);
  private final AnnouncementApplicationService announcementApplicationService;
  private final AnnouncementCreateSession announcementCreateSession;

  /**
   * お知らせメッセージ管理画面を表示します。
   *
   * @param pageNumber ページ番号（省略可）。
   * @param pageSize   ページサイズ（省略可）。
   * @param model      モデル。
   * @return ビュー名。
   */
  @GetMapping
  public String index(
      @RequestParam(required = false) String pageNumber,
      @RequestParam(required = false) String pageSize,
      Model model) {

    // クエリ文字列から値を取得し、数値以外の値は未指定にする
    Integer pageNum = parseInteger(pageNumber);
    Integer pageSz = parseInteger(pageSize);

    // ApplicationService を呼び出してページングされたお知らせメッセージを取得
    PagedAnnouncementList pagedList = announcementApplicationService.getPagedAnnouncementList(pageNum, pageSz);

    // ビューモデルに変換
    List<AnnouncementWithContentsViewModel> announcementViewModels = AnnouncementViewModelTranslator
        .toAnnouncementWithContentsViewModels(
            pagedList.getAnnouncements());

    // お知らせメッセージ管理画面のビューモデルを生成
    AnnouncementListViewModel viewModel = new AnnouncementListViewModel(
        pagedList.getPageNumber(),
        pagedList.getPageSize(),
        pagedList.getTotalCount(),
        announcementViewModels,
        pagedList.getLastPageNumber());

    // モデルに属性を格納
    model.addAttribute("viewModel", viewModel);
    model.addAttribute("displayPriorityOptions", DisplayPriorityOptions.values());

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
      announcement.setDisplayPriority(DisplayPriorityConstants.MEDIUM);
      AnnouncementContent jaContent = new AnnouncementContent(
          UuidGenerator.generate(),
          LanguageCodeConstants.LOCALE_JA.getLanguage(),
          "",
          "",
          "");
      announcement.setContents(List.of(jaContent));
      announcementCreateSession.setAnnouncement(announcement);
    }

    // セッションからお知らせメッセージを取得してビューモデルに変換
    Announcement announcement = announcementCreateSession.getAnnouncement();
    List<AnnouncementContent> content = announcement.getContents();
    AnnouncementViewModel announcementViewModel = AnnouncementViewModelTranslator.toAnnouncementViewModel(announcement);
    List<AnnouncementContentViewModel> contentViewModel = AnnouncementViewModelTranslator.toContentViewModels(content);
    AnnouncementCreateViewModel viewModel = new AnnouncementCreateViewModel(announcementViewModel, contentViewModel);

    model.addAttribute("viewModel", viewModel);
    model.addAttribute("displayPriorityOptions", DisplayPriorityOptions.values());
    model.addAttribute("languageCodeOptions", LanguageCodeOptions.values());

    return "announcement/create";
  }

  /**
   * お知らせメッセージを登録します。
   *
   * @param viewModel          お知らせメッセージ登録画面のビューモデル。
   * @param bindingResult      バインディング結果。
   * @param redirectAttributes リダイレクト属性。
   * @param model              モデル。
   * @return ビュー名またはリダイレクト先。
   */
  @PostMapping("/create")
  public String store(
      @Validated(AnnouncementValidationGroup.Store.class) @ModelAttribute("viewModel") AnnouncementCreateViewModel viewModel,
      BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    if (viewModel.getAnnouncement().getPostTime() == null) {
      viewModel.getAnnouncement().setPostTime(LocalTime.of(0, 0, 0));
    }
    if (viewModel.getAnnouncement().getExpireDate() != null
        && viewModel.getAnnouncement().getExpireTime() == null) {
      viewModel.getAnnouncement().setExpireTime(LocalTime.of(0, 0, 0));
    }
    // 宣言的バリデーションでエラーがある場合は画面を再表示
    if (bindingResult.hasErrors()) {
      model.addAttribute("displayPriorityOptions", DisplayPriorityOptions.values());
      model.addAttribute("languageCodeOptions", LanguageCodeOptions.values());
      return "announcement/create";
    }
    Announcement announcement = AnnouncementViewModelTranslator.toAnnouncementDto(viewModel.getAnnouncement(),
        viewModel.getContents());
    try {

      // アプリケーションサービスを呼び出してお知らせメッセージを登録
      UUID announcementId = announcementApplicationService.addAnnouncementAndHistory(announcement, "DummyUser");

      // セッションをクリア
      announcementCreateSession.clear();

      // お知らせメッセージ編集画面にリダイレクト
      return "redirect:/announcements/" + announcementId + "/edit";

    } catch (AnnouncementValidationException e) {
      // 非宣言的バリデーションエラーの場合はエラーメッセージを設定して画面を再表示
      for (ValidationError error : e.getErrorMessages()) {
        if (error.getFieldName().equals("global")) {
          bindingResult.reject(error.getErrorCode());
          continue;
        }
        bindingResult.rejectValue(error.getFieldName(), error.getErrorCode());
      }
      ErrorMessageBuilder errorMessageBuilder = new ErrorMessageBuilder(e, e.getExceptionId(), e.getLogMessageValue());
      apLog.info(errorMessageBuilder.createLogMessage());
      apLog.debug(errorMessageBuilder.createLogMessageStackTrace());
      model.addAttribute("displayPriorityOptions", DisplayPriorityOptions.values());
      model.addAttribute("languageCodeOptions", LanguageCodeOptions.values());
      return "announcement/create";
    }
  }

  /**
   * 別の言語を追加します。
   *
   * @param viewModel お知らせメッセージ登録画面のビューモデル。
   * @param model     モデル。
   * @return リダイレクト先。
   */
  @PostMapping(value = "create", params = "addLanguageToCreate")
  public String addLanguageToCreate(
      @ModelAttribute("viewModel") AnnouncementCreateViewModel viewModel, Model model) {

    // ビューモデルからDTOに変換
    Announcement announcement = AnnouncementViewModelTranslator.toAnnouncementDto(viewModel.getAnnouncement(),
        viewModel.getContents());

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
    AnnouncementContent newContent = new AnnouncementContent(UuidGenerator.generate(), newLanguageCode, "", "", "");
    contents.add(newContent);

    // セッションに保存
    announcementCreateSession.setAnnouncement(announcement);

    return "redirect:/announcements/create";
  }

  /**
   * 言語別お知らせメッセージを削除します。
   *
   * @param viewModel             お知らせメッセージ登録画面のビューモデル。
   * @param announcementContentId 削除するお知らせコンテンツ ID。
   * @param model                 モデル。
   * @return リダイレクト先。
   */
  @PostMapping(value = "create", params = "deleteLanguageFromCreate")
  public String deleteLanguageFromCreate(
      @ModelAttribute("viewModel") AnnouncementCreateViewModel viewModel,
      @RequestParam("deleteLanguageFromCreate") UUID announcementContentId, Model model) {

    // ビューモデルからDTOに変換
    Announcement announcement = AnnouncementViewModelTranslator.toAnnouncementDto(viewModel.getAnnouncement(),
        viewModel.getContents());

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
   * @param model          モデル。
   * @return お知らせメッセージ編集画面のビュー名。
   */
  @GetMapping("{announcementId}/edit")
  public String edit(@PathVariable("announcementId") String announcementId, Model model) {
    return "announcement/edit";
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
        .sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .toArray(String[]::new);
    for (String languageCode : languageCodes) {
      if (!existingLanguageCodes.contains(languageCode)) {
        return languageCode;
      }
    }
    return LanguageCodeConstants.LOCALE_JA.getLanguage();
  }
}
