package com.dressca.cms.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dressca.cms.announcement.applicationcore.AnnouncementApplicationService;
import com.dressca.cms.announcement.applicationcore.constants.LanguageCodeConstants;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementValidationException;
import com.dressca.cms.web.constants.DisplayPriorityConstants;
import com.dressca.cms.web.models.AnnouncementCreateViewModel;
import com.dressca.cms.web.models.AnnouncementListViewModel;
import com.dressca.cms.web.models.AnnouncementWithContentsViewModel;
import com.dressca.cms.web.models.base.AnnouncementContentViewModel;
import com.dressca.cms.web.models.base.AnnouncementViewModel;
import com.dressca.cms.web.models.validation.AnnouncementValidationGroup.AnnouncementStoreGroup;
import com.dressca.cms.web.session.AnnouncementCreateSession;
import com.dressca.cms.web.translator.AnnouncementViewModelTranslator;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * お知らせメッセージの編集を行うコントローラーです。
 */
@Controller
@RequestMapping("/announcements")
@AllArgsConstructor
public class AnnouncementController {

  private final AnnouncementApplicationService announcementApplicationService;
  private final AnnouncementCreateSession createSession;

  @ModelAttribute("displayPriorityMap")
  public Map<Integer, String> displayPriorityMap() {
    return DisplayPriorityConstants.DISPLAY_PRIORITY_MAP;
  }

  /**
   * お知らせメッセージ管理画面を表示します。
   * 
   * @param pageNumber ページ番号。
   * @param pageSize ページサイズ。
   * @param model モデル。
   * @return お知らせメッセージ管理画面。
   */
  @GetMapping()
  public String index(@RequestParam(value = "pageNumber", required = false) String pageNumber,
      @RequestParam(value = "pageSize", required = false) String pageSize, Model model) {

    Integer pageNumberInt = null;
    Integer pageSizeInt = null;

    try {
      if (pageNumber != null) {
        pageNumberInt = Integer.valueOf(pageNumber);
      }
    } catch (NumberFormatException e) {
      pageNumberInt = null;
    }

    try {
      if (pageSize != null) {
        pageSizeInt = Integer.valueOf(pageSize);
      }
    } catch (NumberFormatException e) {
      pageSizeInt = null;
    }

    PagedAnnouncementList pagedAnnouncementList =
        announcementApplicationService.getPagedAnnouncementList(pageNumberInt, pageSizeInt);

    List<AnnouncementWithContentsViewModel> announcementWithContentsModels = new ArrayList<>();

    for (Announcement announcement : pagedAnnouncementList.getAnnouncements()) {
      List<AnnouncementContentViewModel> contentModels = announcement.getContents().stream()
          .map(AnnouncementViewModelTranslator::createContentViewModel).toList();
      AnnouncementViewModel announcementModel =
          AnnouncementViewModelTranslator.createAnnouncementViewModel(announcement);
      announcementWithContentsModels
          .add(new AnnouncementWithContentsViewModel(announcementModel, contentModels));
    }
    AnnouncementListViewModel viewModel = new AnnouncementListViewModel(
        pagedAnnouncementList.getPageNumber(),
        pagedAnnouncementList.getPageSize(),
        pagedAnnouncementList.getTotalCount(),
        announcementWithContentsModels,
        pagedAnnouncementList.getLastPageNumber(),
        (pagedAnnouncementList.getPageNumber() - 1) * pagedAnnouncementList.getPageSize() + 1,
        Math.min(pagedAnnouncementList.getPageNumber() * pagedAnnouncementList.getPageSize(),
            pagedAnnouncementList.getTotalCount()));
    model.addAttribute("viewModel", viewModel);
    return "announcement/index";
  }



  /**
   * お知らせメッセージ登録画面を表示します。
   * 
   * @param model モデル。
   * @return お知らせメッセージ登録画面。
   */
  @GetMapping("create")
  public String create(Model model) {
    if (createSession.getAnnouncement() == null) {
      AnnouncementContent blankContent = new AnnouncementContent();
      blankContent.setLanguageCode("ja");
      Announcement blankAnnouncement = new Announcement();
      blankAnnouncement.setContents(List.of(blankContent));
      createSession.setAnnouncement(blankAnnouncement);
    }

    Announcement announcement = createSession.getAnnouncement();
    List<AnnouncementContent> contents = announcement.getContents();

    AnnouncementViewModel announcementViewModel =
        AnnouncementViewModelTranslator.createAnnouncementViewModel(announcement);

    List<AnnouncementContentViewModel> contentViewModels =
        contents.stream().map(AnnouncementViewModelTranslator::createContentViewModel).toList();

    AnnouncementCreateViewModel viewModel =
        new AnnouncementCreateViewModel(announcementViewModel, contentViewModels);

    model.addAttribute("viewModel", viewModel);
    model.addAttribute("languageCodeMap", LanguageCodeConstants.LANGUAGE_CODE_MAP);
    return "announcement/create";
  }

  /**
   * お知らせメッセージを登録します。
   * 
   * @return
   */
  @PostMapping("create")
  public String store(
      @Validated(AnnouncementStoreGroup.class) @ModelAttribute("viewModel") AnnouncementCreateViewModel viewModel,
      BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "announcement/create";
    }
    AnnouncementViewModel announcementViewModel = viewModel.getAnnouncement();
    List<AnnouncementContentViewModel> contentViewModels = viewModel.getContents();

    Announcement announcement =
        AnnouncementViewModelTranslator.createAnnouncement(announcementViewModel);
    List<AnnouncementContent> contents =
        contentViewModels.stream().map(AnnouncementViewModelTranslator::createContent).toList();
    UUID id = null;
    try {
      id = announcementApplicationService.addAnnouncementAndHistory(announcement, contents);
    } catch (AnnouncementValidationException e) {
      return "announcement/content";
    }
    createSession.clear();
    return "redirect:/announcements/" + id + "/edit";
  }

  /**
   * お知らせメッセージ登録画面上で言語を追加します。
   *
   * @param announcementId お知らせメッセージの ID。
   * @return お知らせメッセージの登録画面。
   */
  @PostMapping(path = "create", params = "addLanguageToCreate")
  public String addLanguageToCreate() {
    final Announcement announcement = createSession.getAnnouncement();
    final List<AnnouncementContent> contents = new ArrayList<>(announcement.getContents());

    final Set<String> existingLanguageCodeSet = contents.stream()
        .map(AnnouncementContent::getLanguageCode).collect(Collectors.toSet());

    for (String languageCode : LanguageCodeConstants.SUPPORTED_LANGUAGE_CODES) {
      if (!existingLanguageCodeSet.contains(languageCode)) {
        final AnnouncementContent content = new AnnouncementContent();
        content.setAnnouncementId(announcement.getId());
        content.setLanguageCode(languageCode);
        contents.add(content);
        break;
      }
      announcement.setContents(contents);
      createSession.setAnnouncement(announcement);
    }
    return "redirect:/announcements/create";
  }

  /**
   * お知らせメッセージ登録画面上で言語を削除します。
   *
   * @param announcementId お知らせメッセージの ID。
   * @param deleteLanguageCode 言語コード。
   * @return お知らせメッセージの登録画面。
   */
  @PostMapping(path = "create", params = "deleteLanguageFromCreate")
  public String deleteLanguageFromCreate(
      @RequestParam("deleteLanguageFromCreate") String languageCode) {

    createSession.getAnnouncement().getContents()
        .removeIf(entity -> entity.getLanguageCode().equals(languageCode));

    return "redirect:/announcements/create";
  }


  /**
   * お知らせメッセージ編集画面を表示します。
   * 
   * @param announcementId お知らせメッセージの ID 。
   * @return お知らせメッセージ編集画面。
   */
  @GetMapping("{announcementId}/edit")
  public String edit(@PathVariable("announcementId") UUID announcementId) {
    return "announcement/edit";
  }

}
