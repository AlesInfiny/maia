package com.dressca.cms.announcement.applicationcore;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.dressca.cms.announcement.applicationcore.constants.ExceptionIdConstants;
import com.dressca.cms.announcement.applicationcore.constants.LanguageCodeConstants;
import com.dressca.cms.announcement.applicationcore.constants.MessageIdConstants;
import com.dressca.cms.announcement.applicationcore.constants.OperationTypeConstants;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementValidationError;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementValidationException;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentHistoryRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementHistoryRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.systemcommon.log.AbstractStructuredLogger;
import com.fasterxml.uuid.Generators;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnnouncementApplicationService {

  private final MessageSource messageSource;
  private final AbstractStructuredLogger apLog;
  private final AnnouncementRepository announcementRepository;
  private final AnnouncementContentRepository contentRepository;
  private final AnnouncementHistoryRepository historyRepository;
  private final AnnouncementContentHistoryRepository contentHistoryRepository;

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

    apLog.info(messageSource.getMessage(MessageIdConstants.D_ANNOUNCEMENT_GET_LIST_START, null,
        Locale.getDefault()));

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

    apLog.info(messageSource.getMessage(MessageIdConstants.D_ANNOUNCEMENT_GET_LIST_END, null,
        Locale.getDefault()));

    return new PagedAnnouncementList(pageNumber, pageSize, totalCount, announcements,
        lastPageNumber);
  }


  /**
   * お知らせメッセージおよびお知らせメッセージ履歴を登録します。
   * 
   * @param announcement お知らせメッセージ。
   * @param contents お知らせメッセージコンテンツのリスト。
   * @return 登録したお知らせメッセージの Id 。
   * @throws AnnouncementValidationException
   */
  public UUID addAnnouncementAndHistory(Announcement announcement,
      List<AnnouncementContent> contents) throws AnnouncementValidationException {
    List<AnnouncementValidationError> errors = new ArrayList<>();

    for (AnnouncementContent content : contents) {
      if (!LanguageCodeConstants.SUPPORTED_LANGUAGE_CODES.contains(content.getLanguageCode())) {
        errors.add(new AnnouncementValidationError(ExceptionIdConstants.E_INVALID_LANGUAGE_CODE));
      }
    }
    OffsetDateTime postDateTime = announcement.getPostDateTime();
    OffsetDateTime expireDateTime = announcement.getExpireDateTime();
    if (postDateTime != null && expireDateTime != null) {
      if (postDateTime.isAfter(expireDateTime)) {
        errors.add(
            new AnnouncementValidationError(ExceptionIdConstants.E_POST_DATE_AFTER_EXPIRE_DATE));
      }
    }
    if (contents.isEmpty()) {
      errors.add(new AnnouncementValidationError(
          ExceptionIdConstants.E_ANNOUNCEMENT_CONTENTS_LIST_IS_EMPTY));
    }

    Set<String> seenCodes = new HashSet<>();
    for (String code : contents.stream().map(AnnouncementContent::getLanguageCode).toList()) {
      if (!seenCodes.add(code)) {
        errors.add(new AnnouncementValidationError(ExceptionIdConstants.E_DUPLICATE_LANGUAGE_CODE));
        break;
      }
    }

    if (!errors.isEmpty()) {
      throw new AnnouncementValidationException(errors);
    }

    apLog.info(messageSource.getMessage(
        MessageIdConstants.D_ANNOUNCEMENT_ADD_ANNOUNCEMENT_AND_HISTORY_START, null,
        Locale.getDefault()));

    UUID announcementId = Generators.timeBasedEpochGenerator().generate();
    announcement.setId(announcementId);
    announcement.setCreatedAt(OffsetDateTime.now());
    announcement.setChangedAt(OffsetDateTime.now());
    announcement.setIsDeleted(false);
    announcementRepository.add(announcement);

    for (AnnouncementContent content : contents) {
      content.setId(Generators.timeBasedEpochGenerator().generate());
      content.setAnnouncementId(announcementId);
      contentRepository.add(content);
    }

    UUID historyId = Generators.timeBasedEpochGenerator().generate();
    AnnouncementHistory history =
        createAnnouncementHistory(announcement, historyId, "admin", OperationTypeConstants.CREATE);
    historyRepository.add(history);

    for (AnnouncementContent content : contents) {
      AnnouncementContentHistory contentHistory = createAnnouncementContentHistory(content,
          Generators.timeBasedEpochGenerator().generate(), historyId);
      contentHistoryRepository.add(contentHistory);
    }

    apLog.info(messageSource.getMessage(
        MessageIdConstants.D_ANNOUNCEMENT_ADD_ANNOUNCEMENT_AND_HISTORY_START, null,
        Locale.getDefault()));

    return announcementId;
  }

  /**
   * {@link Announcement} を {@link AnnouncementHistory} に変換する。
   * 
   * @param announcement {@link Announcement} オブジェクト。
   * @param id お知らせメッセージ履歴の ID 。
   * @param userName ユーザーの名前。
   * @param operationType 操作タイプ。
   * @return {@link AnnouncementHistory} オブジェクト。
   */
  private AnnouncementHistory createAnnouncementHistory(Announcement announcement,
      UUID id, String userName, Integer operationType) {
    AnnouncementHistory history = new AnnouncementHistory();
    Optional.ofNullable(id).ifPresent(history::setId);
    Optional.ofNullable(announcement.getId()).ifPresent(history::setAnnouncementId);
    Optional.ofNullable(announcement.getCategory()).ifPresent(history::setCategory);
    Optional.ofNullable(announcement.getPostDateTime()).ifPresent(history::setPostDateTime);
    Optional.ofNullable(announcement.getExpireDateTime()).ifPresent(history::setExpireDateTime);
    Optional.ofNullable(announcement.getDisplayPriority()).ifPresent(history::setDisplayPriority);
    Optional.ofNullable(announcement.getCreatedAt()).ifPresent(history::setCreatedAt);
    Optional.ofNullable(userName).ifPresent(history::setChangedBy);
    Optional.ofNullable(operationType).ifPresent(history::setOperationType);
    return history;
  }

  /**
   * {@link AnnouncementContent} を {@link AnnouncementContentHistory} に変換する。
   * 
   * @param content @param content {@link AnnouncementContent} オブジェクト。
   * @param id お知らせメッセージコンテンツ履歴の ID 。
   * @param historyId お知らせメッセージ履歴の ID 。
   * @return {@link AnnouncementContentHistory} オブジェクト。
   */
  private AnnouncementContentHistory createAnnouncementContentHistory(AnnouncementContent content,
      UUID id, UUID historyId) {
    AnnouncementContentHistory contentHistory = new AnnouncementContentHistory();
    Optional.ofNullable(id).ifPresent(contentHistory::setId);
    Optional.ofNullable(historyId).ifPresent(contentHistory::setAnnouncementHistoryId);
    Optional.ofNullable(content.getLanguageCode()).ifPresent(contentHistory::setLanguageCode);
    Optional.ofNullable(content.getTitle()).ifPresent(contentHistory::setTitle);
    Optional.ofNullable(content.getMessage()).ifPresent(contentHistory::setMessage);
    Optional.ofNullable(content.getLinkUrl()).ifPresent(contentHistory::setLinkUrl);
    return contentHistory;
  }

}
