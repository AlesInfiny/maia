package com.dressca.cms.announcement.applicationcore;

import com.dressca.cms.announcement.applicationcore.constant.MessageIdConstants;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.systemcommon.constant.SystemPropertiesConstants;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * お知らせメッセージのアプリケーションサービスクラスです。
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AnnouncementApplicationService {

  private static final Logger log = LoggerFactory.getLogger(SystemPropertiesConstants.APPLICATION_LOG_LOGGER);

  private final AnnouncementRepository announcementRepository;

  private final MessageSource messages;

  /**
   * 指定されたページ番号とページサイズから、論理削除されていないページングされたお知らせメッセージを
   * 掲載開始日時の降順で取得します。
   *
   * @param pageNumber ページ番号（null の場合は 1）。
   * @param pageSize   ページサイズ（null の場合は 20、10 未満または 201 以上の場合は 20）。
   * @return ページングされたお知らせメッセージ。
   */
  public PagedAnnouncementList getPagedAnnouncementList(Integer pageNumber, Integer pageSize) {
    // 業務開始処理
    // pageNumber が未指定の場合は 1 とする
    if (pageNumber == null) {
      pageNumber = 1;
    }

    // pageSize が未指定、10 未満または 201 以上の場合は 20 とする
    if (pageSize == null || pageSize < 10 || pageSize > 200) {
      pageSize = 20;
    }

    log.info(messages.getMessage(MessageIdConstants.D_START_GET_PAGED_ANNOUNCEMENT_LIST,
        new Object[] { pageNumber, pageSize }, Locale.getDefault()));

    // 業務メイン処理
    // お知らせメッセージの総件数を取得
    long totalCount = announcementRepository.countByIsDeletedFalse();

    // 最後のページ番号を計算
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);
    if (lastPageNumber == 0) {
      lastPageNumber = 1;
    }

    // pageNumber が 0 以下または最後のページ番号より大きい場合は 1 とする
    if (pageNumber <= 0 || pageNumber > lastPageNumber) {
      pageNumber = 1;
    }

    // オフセットを計算
    int offset = (pageNumber - 1) * pageSize;

    // お知らせメッセージリストを取得
    List<Announcement> announcements = announcementRepository
        .findByOffsetAndLimit(offset, pageSize);

    // 言語コードの優先順に従って、各お知らせメッセージの代表コンテンツを選択
    selectPriorityContent(announcements);

    // 業務終了処理
    log.info(messages.getMessage(MessageIdConstants.D_END_GET_PAGED_ANNOUNCEMENT_LIST,
        new Object[] { pageNumber, pageSize }, Locale.getDefault()));

    return new PagedAnnouncementList(pageNumber, pageSize, totalCount,
        announcements, lastPageNumber);
  }

  /**
   * 言語コードの優先順（ja &gt; en &gt; zh &gt; es）に従って、
   * 各お知らせメッセージの代表コンテンツを選択します。
   *
   * @param announcements お知らせメッセージのリスト。
   */
  private void selectPriorityContent(List<Announcement> announcements) {
    Map<String, Integer> languagePriority = new HashMap<>();
    languagePriority.put("ja", 1);
    languagePriority.put("en", 2);
    languagePriority.put("zh", 3);
    languagePriority.put("es", 4);

    for (Announcement announcement : announcements) {
      if (announcement.getContents() != null && !announcement.getContents().isEmpty()) {
        // 優先順位が最も高いコンテンツを選択
        AnnouncementContent priorityContent = announcement.getContents().stream()
            .min(Comparator.comparingInt(content -> languagePriority.getOrDefault(content.getLanguageCode(), 999)))
            .orElse(announcement.getContents().get(0));

        // 選択したコンテンツのみを残す
        announcement.getContents().clear();
        announcement.getContents().add(priorityContent);
      }
    }
  }
}
