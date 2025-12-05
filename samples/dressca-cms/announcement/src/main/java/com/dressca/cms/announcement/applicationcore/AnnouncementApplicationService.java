package com.dressca.cms.announcement.applicationcore;

import com.dressca.cms.announcement.applicationcore.constant.MessageIdConstants;
import com.dressca.cms.announcement.applicationcore.constant.OperationTypeConstants;
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
import com.dressca.cms.systemcommon.constant.LanguageCodeConstants;
import com.dressca.cms.systemcommon.constant.SystemPropertyConstants;
import com.dressca.cms.systemcommon.exception.ValidationError;
import com.dressca.cms.systemcommon.util.UuidGenerator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
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

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);
  private final AnnouncementRepository announcementRepository;
  private final AnnouncementContentRepository announcementContentRepository;
  private final AnnouncementHistoryRepository announcementHistoryRepository;
  private final AnnouncementContentHistoryRepository announcementContentHistoryRepository;
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

    apLog.info(messages.getMessage(MessageIdConstants.D_START_GET_PAGED_ANNOUNCEMENT_LIST,
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
    apLog.info(messages.getMessage(MessageIdConstants.D_END_GET_PAGED_ANNOUNCEMENT_LIST,
        new Object[] { pageNumber, pageSize }, Locale.getDefault()));

    return new PagedAnnouncementList(pageNumber, pageSize, totalCount,
        announcements, lastPageNumber);
  }

  /**
   * お知らせメッセージおよびお知らせメッセージ履歴を登録します。
   *
   * @param announcement お知らせメッセージ。
   * @param username     ユーザー名。
   * @return 登録したお知らせメッセージの ID。
   * @throws AnnouncementValidationException バリデーションエラーが発生した場合。
   */
  public UUID addAnnouncementAndHistory(Announcement announcement, String username)
      throws AnnouncementValidationException {
    // 業務開始処理
    List<ValidationError> validationErrors = new ArrayList<>();

    // 言語コードのチェック
    if (announcement.getContents() != null) {
      for (AnnouncementContent content : announcement.getContents()) {
        if (!LanguageCodeConstants.LANGUAGE_CODE_PRIORITY.containsKey(content.getLanguageCode())) {
          validationErrors.add(new ValidationError("global", "announcement.create.invalidLanguageCode"));
        }
      }
    }

    // 掲載終了日時のチェック
    if (announcement.getExpireDateTime() != null && announcement.getPostDateTime() != null
        && announcement.getExpireDateTime().isBefore(announcement.getPostDateTime())) {
      validationErrors
          .add(new ValidationError("announcement.expireDate", "announcement.create.expireDateBeforePostDate"));
    }

    // お知らせコンテンツが 1 件以上あることをチェック
    if (announcement.getContents() == null || announcement.getContents().isEmpty()) {
      validationErrors.add(new ValidationError("global", "announcement.create.noLanguageContent"));
    }

    // 言語コードの重複チェック
    if (announcement.getContents() != null) {
      Set<String> languageCodes = new HashSet<>();
      for (AnnouncementContent content : announcement.getContents()) {
        if (!languageCodes.add(content.getLanguageCode())) {
          validationErrors.add(new ValidationError("global", "announcement.create.duplicateLanguageCode"));
          break;
        }
      }
    }

    if (!validationErrors.isEmpty()) {
      throw new AnnouncementValidationException(validationErrors);
    }

    apLog.info(messages.getMessage(MessageIdConstants.D_START_ADD_ANNOUNCEMENT_AND_HISTORY, null, Locale.getDefault()));

    // 業務メイン処理
    // お知らせメッセージの ID を生成
    UUID announcementId = UuidGenerator.generate();
    OffsetDateTime createdAt = OffsetDateTime.now();

    // お知らせメッセージを追加
    announcement.setId(announcementId);
    announcement.setCreatedAt(createdAt);
    announcement.setChangedAt(createdAt);
    announcement.setIsDeleted(false);
    announcementRepository.add(announcement);

    // お知らせコンテンツを追加
    for (AnnouncementContent content : announcement.getContents()) {
      content.setAnnouncementId(announcementId);
      announcementContentRepository.add(content);
    }

    // お知らせメッセージ履歴を追加
    AnnouncementHistory announcementHistory = createAnnouncementHistory(announcement, createdAt, username,
        OperationTypeConstants.CREATE);
    announcementHistoryRepository.add(announcementHistory);

    // お知らせコンテンツ履歴を追加
    for (AnnouncementContent content : announcement.getContents()) {
      AnnouncementContentHistory contentHistory = createAnnouncementContentHistory(content,
          announcementHistory.getId());
      announcementContentHistoryRepository.add(contentHistory);
    }

    // 業務終了処理
    apLog.info(messages.getMessage(MessageIdConstants.D_END_ADD_ANNOUNCEMENT_AND_HISTORY, null, Locale.getDefault()));

    return announcementId;
  }

  /**
   * お知らせメッセージからお知らせメッセージ履歴を生成します。
   *
   * @param announcement  お知らせメッセージ。
   * @param createdAt     作成日時。
   * @param username      ユーザー名。
   * @param operationType 操作種別。
   * @return お知らせメッセージ履歴。
   */
  private AnnouncementHistory createAnnouncementHistory(Announcement announcement,
      OffsetDateTime createdAt, String username, int operationType) {
    return new AnnouncementHistory(
        UuidGenerator.generate(),
        announcement.getId(),
        announcement.getCategory(),
        announcement.getPostDateTime(),
        announcement.getExpireDateTime(),
        announcement.getDisplayPriority(),
        createdAt,
        username,
        operationType,
        null);
  }

  /**
   * お知らせコンテンツからお知らせコンテンツ履歴を生成します。
   *
   * @param content               お知らせコンテンツ。
   * @param announcementHistoryId お知らせメッセージ履歴 ID。
   * @return お知らせコンテンツ履歴。
   */
  private AnnouncementContentHistory createAnnouncementContentHistory(
      AnnouncementContent content, UUID announcementHistoryId) {
    return new AnnouncementContentHistory(
        UuidGenerator.generate(),
        announcementHistoryId,
        content.getLanguageCode(),
        content.getTitle(),
        content.getMessage(),
        content.getLinkUrl());
  }

  /**
   * 言語コードの優先順（ja > en > zh > es）に従って、
   * 各お知らせメッセージの代表コンテンツを選択します。
   *
   * @param announcements お知らせメッセージのリスト。
   */
  private void selectPriorityContent(List<Announcement> announcements) {
    for (Announcement announcement : announcements) {
      List<AnnouncementContent> contents = announcement.getContents();
      if (contents == null || contents.isEmpty()) {
        continue;
      }
      // 優先順位が最も高いコンテンツを選択
      AnnouncementContent priorityContent = contents.stream()
          .min(Comparator.comparingInt(content -> LanguageCodeConstants.LANGUAGE_CODE_PRIORITY
              .getOrDefault(content.getLanguageCode(), 999)))
          .orElse(contents.get(0));

      // 選択したコンテンツのみの新しいリストを作成してセット
      announcement.setContents(Collections.singletonList(priorityContent));
    }
  }
}
