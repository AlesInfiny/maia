package com.dressca.cms.web.controller;

import com.dressca.cms.announcement.applicationcore.AnnouncementApplicationService;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.web.constant.DisplayPriorityConstants;
import com.dressca.cms.web.models.AnnouncementListViewModel;
import com.dressca.cms.web.models.AnnouncementWithContentsViewModel;
import com.dressca.cms.web.translator.AnnouncementViewModelTranslator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * お知らせメッセージ管理画面のコントローラークラスです。
 */
@Controller
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

  private final AnnouncementApplicationService announcementApplicationService;

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
    PagedAnnouncementList pagedList = announcementApplicationService
        .getPagedAnnouncementList(pageNum, pageSz);

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
    model.addAttribute("announcementList", viewModel);
    model.addAttribute("displayPriorityLabels", DisplayPriorityConstants.DISPLAY_PRIORITY_LABELS);

    // お知らせメッセージ管理画面に遷移
    return "announcement/index";
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
}
