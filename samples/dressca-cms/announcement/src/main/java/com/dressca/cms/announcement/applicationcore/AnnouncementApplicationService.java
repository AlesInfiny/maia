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
import com.dressca.cms.announcement.applicationcore.constants.FieldNameConstants;
import com.dressca.cms.announcement.applicationcore.constants.LanguageCodeConstants;
import com.dressca.cms.announcement.applicationcore.constants.MessageIdConstants;
import com.dressca.cms.announcement.applicationcore.constants.OperationTypeConstants;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementValidationException;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentHistoryRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementHistoryRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.systemcommon.exception.ValidationError;
import com.dressca.cms.systemcommon.log.AbstractStructuredLogger;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import lombok.AllArgsConstructor;

/**
 * お知らせメッセージのアプリケーションサービスです。
 */
@Service
@AllArgsConstructor
public class AnnouncementApplicationService {

  private MessageSource messageSource;
  private AbstractStructuredLogger apLog;
  private AnnouncementRepository announcementRepository;
  private AnnouncementContentRepository contentRepository;
  private AnnouncementHistoryRepository historyRepository;
  private AnnouncementContentHistoryRepository contentHistoryRepository;
  private final TimeBasedEpochGenerator uuidGenerator = Generators.timeBasedEpochGenerator();

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

    apLog.debug(messageSource.getMessage(MessageIdConstants.D_ANNOUNCEMENT_GET_LIST_START, null,
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

    apLog.debug(messageSource.getMessage(MessageIdConstants.D_ANNOUNCEMENT_GET_LIST_END, null,
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
   * @throws AnnouncementValidationException 非宣言的なバリデーションエラーが発生した場合。
   */
  public UUID addAnnouncementAndHistory(Announcement announcement,
      List<AnnouncementContent> contents, String userName)
      throws AnnouncementValidationException {
    List<ValidationError> validationErrors = new ArrayList<>();

    for (AnnouncementContent content : contents) {
      if (!LanguageCodeConstants.SUPPORTED_LANGUAGE_CODES.contains(content.getLanguageCode())) {
        validationErrors
            .add(new ValidationError(FieldNameConstants.GLOBAL,
                ExceptionIdConstants.E_INVALID_LANGUAGE_CODE));
      }
    }

    OffsetDateTime postDateTime = announcement.getPostDateTime();
    OffsetDateTime expireDateTime = announcement.getExpireDateTime();
    if (postDateTime != null && expireDateTime != null && postDateTime.isAfter(expireDateTime)) {
      validationErrors.add(
          new ValidationError(FieldNameConstants.EXPIRE_DATE,
              ExceptionIdConstants.E_INVALID_EXPIRE_DATE));
    }

    if (contents.isEmpty()) {
      validationErrors.add(
          new ValidationError(FieldNameConstants.GLOBAL, ExceptionIdConstants.E_NULL_ANNOUNCEMENT));
    }

    // 言語コードの重複チェックを行います。
    Set<String> languageCodes = new HashSet<>();
    for (AnnouncementContent content : contents) {
      String languageCode = content.getLanguageCode();
      if (!languageCodes.add(languageCode)) {
        validationErrors.add(new ValidationError(FieldNameConstants.GLOBAL,
            ExceptionIdConstants.E_DUPLICATE_LANGUAGE_CODE));
        break;
      }
    }

    if (!validationErrors.isEmpty()) {
      throw new AnnouncementValidationException(validationErrors);
    }

    apLog.debug(messageSource.getMessage(
        MessageIdConstants.D_ANNOUNCEMENT_ADD_ANNOUNCEMENT_AND_HISTORY_START, null,
        Locale.getDefault()));

    UUID announcementId = uuidGenerator.generate();
    announcement.setId(announcementId);
    announcement.setCreatedAt(OffsetDateTime.now());
    announcement.setChangedAt(OffsetDateTime.now());
    announcement.setIsDeleted(false);
    announcementRepository.add(announcement);

    for (AnnouncementContent content : contents) {
      content.setId(uuidGenerator.generate());
      content.setAnnouncementId(announcementId);
      contentRepository.add(content);
    }

    UUID historyId = uuidGenerator.generate();
    AnnouncementHistory history = createAnnouncementHistory(announcement, historyId, userName,
        OperationTypeConstants.CREATE);
    historyRepository.add(history);

    for (AnnouncementContent content : contents) {
      AnnouncementContentHistory contentHistory =
          createAnnouncementContentHistory(content, uuidGenerator.generate(), historyId);
      contentHistoryRepository.add(contentHistory);
    }

    apLog.debug(
        messageSource.getMessage(MessageIdConstants.D_ANNOUNCEMENT_ADD_ANNOUNCEMENT_AND_HISTORY_END,
            null, Locale.getDefault()));

    return announcementId;
  }

  /**
   * {@link Announcement} を {@link AnnouncementHistory} に変換します。
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
   * {@link AnnouncementContent} を {@link AnnouncementContentHistory} に変換します。
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
