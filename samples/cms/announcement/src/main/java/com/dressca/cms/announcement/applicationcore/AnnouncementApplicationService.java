package com.dressca.cms.announcement.applicationcore;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.dressca.cms.announcement.applicationcore.constants.LanguageCodeConstants;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.systemcommon.log.AbstractStructuredLogger;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnnouncementApplicationService {

  private final AbstractStructuredLogger apLog;

  private final AnnouncementRepository announcementRepository;

  /**
   * ページングされたお知らせメッセージを掲載開始日時の降順で取得します。
   * 
   * @param pageNumber ページ番号。
   * @param pageSize ページサイズ。
   * @return ページングされたお知らせメッセージ。
   */
  public PagedAnnouncementList getPagedAnnouncementList(Integer pageNumber, Integer pageSize) {
    if (pageNumber == null) {
      pageNumber = 1;
    }
    if (pageSize == null || pageSize < 10 || pageSize >= 201) {
      pageSize = 20;
    }

    apLog.info("ページングされたお知らせメッセージの取得を開始します。");

    int totalCount = (int) announcementRepository.countByIsDeletedFalse();
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);


    if (pageNumber <= 0 || pageNumber > lastPageNumber) {
      pageNumber = 1;
    }

    List<Announcement> announcements =
        announcementRepository.findByPageNumberAndPageSize(pageNumber, pageSize);


    // お知らせメッセージコンテンツを言語コード順に並び替え、最優先の言語コードのコンテンツのみ持つようにします。
    for (Announcement announcement : announcements) {
      List<AnnouncementContent> contents = announcement.getContents();
      contents.sort(Comparator.comparingInt(content -> {
        int index =
            LanguageCodeConstants.SUPPORTED_LANGUAGE_CODES.indexOf(content.getLanguageCode());
        return index >= 0 ? index : Integer.MAX_VALUE;
      }));
      announcement.setContents(List.of(contents.get(0)));
    }
    apLog.info("ページングされたお知らせメッセージの取得を終了します。");

    return new PagedAnnouncementList(pageNumber, pageSize, totalCount, announcements,
        lastPageNumber);
  }


  /**
   * お知らせメッセージおよびお知らせメッセージ履歴を登録します。
   * 
   * @param announcement お知らせメッセージ。
   * @param contents お知らせメッセージコンテンツのリスト。
   * @return 登録したお知らせメッセージの Id 。
   */
  public UUID addAnnouncementAndHistory(Announcement announcement,
      List<AnnouncementContent> contents) {

    return null;
  }
}
