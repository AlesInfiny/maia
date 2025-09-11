package com.dressca.cms.announcement.applicationcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import com.dressca.cms.announcement.applicationcore.constants.ExceptionIdConstants;
import com.dressca.cms.announcement.applicationcore.constants.FieldNameConstants;
import com.dressca.cms.announcement.applicationcore.constants.LanguageCodeConstants;
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

/**
 * {@link AnnouncementApplicationService} の動作をテストするクラスです。
 */
@ExtendWith(MockitoExtension.class)
public class AnnouncementApplicationServiceTest {
  @Mock
  private MessageSource messageSource;
  @Mock
  private AbstractStructuredLogger apLog;
  @Mock
  private AnnouncementRepository announcementRepository;
  @Mock
  private AnnouncementContentRepository contentRepository;
  @Mock
  private AnnouncementHistoryRepository historyRepository;
  @Mock
  private AnnouncementContentHistoryRepository contentHistoryRepository;
  @InjectMocks
  private AnnouncementApplicationService service;

  private final TimeBasedEpochGenerator uuidGenerator = Generators.timeBasedEpochGenerator();

  @Test
  void testGetPagedAnnouncementList_正常系_ページ番号1とページサイズ10で1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageNumber = 1;
    Integer pageSize = 10;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(pageSize);
    PagedAnnouncementList expected = new PagedAnnouncementList(pageNumber, pageSize,
        (int) totalCount, announcements, lastPageNumber);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(pageNumber, pageSize))
        .thenReturn(announcements);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        pageNumber,
        pageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_最終ページ番号とページサイズ10で最終ページのお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageSize = 10;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(pageSize);
    PagedAnnouncementList expected = new PagedAnnouncementList(
        lastPageNumber, pageSize, (int) totalCount, announcements,
        lastPageNumber);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(lastPageNumber,
        pageSize))
            .thenReturn(announcements);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(lastPageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        lastPageNumber,
        pageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_nullのページ番号とページサイズ10で1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageNumber = null;
    Integer expectedPageNumber = 1;
    Integer pageSize = 10;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(pageSize);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(expectedPageNumber,
        pageSize))
            .thenReturn(announcements);

    PagedAnnouncementList expected =
        new PagedAnnouncementList(expectedPageNumber, pageSize,
            (int) totalCount, announcements, lastPageNumber);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        expectedPageNumber,
        pageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_ページ番号の最小値より小さいページ番号0とページサイズ10で1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageNumber = 0;
    Integer expectedPageNumber = 1;
    Integer pageSize = 10;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(pageSize);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(expectedPageNumber,
        pageSize))
            .thenReturn(announcements);

    PagedAnnouncementList expected =
        new PagedAnnouncementList(expectedPageNumber, pageSize,
            (int) totalCount, announcements, lastPageNumber);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        expectedPageNumber,
        pageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_最終ページ番号より大きいページ番号とページサイズ10で1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageSize = 10;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);
    Integer pageNumber = lastPageNumber + 1;
    Integer expectedPageNumber = 1;
    List<Announcement> announcements = createDefaultAnnouncementList(pageSize);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(expectedPageNumber,
        pageSize))
            .thenReturn(announcements);

    PagedAnnouncementList expected =
        new PagedAnnouncementList(expectedPageNumber, pageSize,
            (int) totalCount, announcements, lastPageNumber);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        expectedPageNumber,
        pageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_ページ番号1とページサイズの最大値200で1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageNumber = 1;
    Integer pageSize = 200;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / pageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(pageSize);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(pageNumber, pageSize))
        .thenReturn(announcements);

    PagedAnnouncementList expected = new PagedAnnouncementList(pageNumber, pageSize,
        (int) totalCount, announcements, lastPageNumber);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        pageNumber,
        pageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_ページ番号1とnullのページサイズで1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageNumber = 1;
    Integer pageSize = null;
    Integer expectedPageSize = 20;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / expectedPageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(expectedPageSize);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(pageNumber,
        expectedPageSize))
            .thenReturn(announcements);

    PagedAnnouncementList expected = new PagedAnnouncementList(pageNumber,
        expectedPageSize, (int) totalCount, announcements, lastPageNumber);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        pageNumber,
        expectedPageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_ページ番号1とページサイズの最小値より小さいページサイズ9で1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageNumber = 1;
    Integer pageSize = 9;
    Integer expectedPageSize = 20;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / expectedPageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(expectedPageSize);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(pageNumber,
        expectedPageSize))
            .thenReturn(announcements);

    PagedAnnouncementList expected = new PagedAnnouncementList(pageNumber,
        expectedPageSize, (int) totalCount, announcements, lastPageNumber);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        pageNumber,
        expectedPageSize);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_ページ番号1とページサイズの最大値より大きいページサイズ201で1ページ目のお知らせメッセージのリストを取得できる() {
    // Arrange
    Integer pageNumber = 1;
    Integer pageSize = 9;
    Integer expectedPageSize = 20;
    long totalCount = 30;
    int lastPageNumber = (int) Math.ceil((double) totalCount / expectedPageSize);
    List<Announcement> announcements = createDefaultAnnouncementList(expectedPageSize);

    when(this.announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(this.announcementRepository.findByPageNumberAndPageSize(pageNumber,
        expectedPageSize))
            .thenReturn(announcements);

    PagedAnnouncementList expected = new PagedAnnouncementList(pageNumber,
        expectedPageSize, (int) totalCount, announcements, lastPageNumber);

    // Act & Assert
    assertThat(service.getPagedAnnouncementList(pageNumber, pageSize))
        .isEqualTo(expected);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(this.announcementRepository, times(1)).findByPageNumberAndPageSize(
        pageNumber,
        expectedPageSize);
  }

  @Test
  void testAddAnnouncementAndHistory_正常系_お知らせメッセージとお知らせメッセージコンテンツを登録できる() {
    // Arrange
    Announcement announcement = createDefaultAnnouncement();
    List<AnnouncementContent> contents = announcement.getContents();
    String userName = "dummyName";
    when(announcementRepository.add(any(Announcement.class))).thenReturn(1);
    when(contentRepository.add(any(AnnouncementContent.class))).thenReturn(1);
    when(historyRepository.add(any(AnnouncementHistory.class))).thenReturn(1);
    when(contentHistoryRepository.add(any(AnnouncementContentHistory.class)))
        .thenReturn(1);

    // Act
    Executable action = () -> {
      service.addAnnouncementAndHistory(announcement, contents, userName);
    };

    // Assert
    assertDoesNotThrow(action);
    verify(announcementRepository, times(1)).add(any(Announcement.class));
    verify(contentRepository, times(contents.size()))
        .add(any(AnnouncementContent.class));
    verify(historyRepository, times(1)).add(any(AnnouncementHistory.class));
    verify(contentHistoryRepository, times(contents.size()))
        .add(any(AnnouncementContentHistory.class));
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_掲載開始日時より前に掲載終了日時が設定されている() {
    // Arrange
    Announcement announcement = createDefaultAnnouncement();
    OffsetDateTime invalidPostDateTime = OffsetDateTime.MAX;
    OffsetDateTime invalidExpireDateTime = OffsetDateTime.MIN;
    announcement.setPostDateTime(invalidPostDateTime);
    announcement.setExpireDateTime(invalidExpireDateTime);
    List<AnnouncementContent> contents = announcement.getContents();
    String userName = "dummyName";

    // Act
    Executable action = () -> {
      service.addAnnouncementAndHistory(announcement, contents, userName);
    };

    // Assert
    AnnouncementValidationException ex =
        assertThrows(AnnouncementValidationException.class, action);
    List<ValidationError> errors = ex.getValidationErrors();
    assertThat(errors).isNotEmpty();
    assertThat(errors).contains(new ValidationError(FieldNameConstants.EXPIRE_DATE,
        ExceptionIdConstants.E_INVALID_EXPIRE_DATE));
    verify(announcementRepository, times(0)).add(any(Announcement.class));
    verify(contentRepository, times(0)).add(any(AnnouncementContent.class));
    verify(historyRepository, times(0)).add(any(AnnouncementHistory.class));
    verify(contentHistoryRepository, times(0))
        .add(any(AnnouncementContentHistory.class));
  }


  @Test
  void testAddAnnouncementAndHistory_異常系_サポート外の言語コードが指定されている() {
    // Arrange
    Announcement announcement = createDefaultAnnouncement();
    String invalidLanguageCode = "invalidLanguageCode";
    UUID announcementId = announcement.getId();
    AnnouncementContent invalidLanguageCodeContent =
        createDefaultAnnouncementContent(announcementId,
            invalidLanguageCode);
    List<AnnouncementContent> contents = announcement.getContents();
    contents.add(invalidLanguageCodeContent);
    String userName = "dummyName";

    // Act
    Executable action = () -> {
      service.addAnnouncementAndHistory(announcement, contents, userName);
    };

    // Assert
    AnnouncementValidationException ex =
        assertThrows(AnnouncementValidationException.class, action);
    List<ValidationError> errors = ex.getValidationErrors();
    assertThat(errors).isNotEmpty();
    assertThat(errors).contains(new ValidationError(FieldNameConstants.GLOBAL,
        ExceptionIdConstants.E_INVALID_LANGUAGE_CODE));
    verify(announcementRepository, times(0)).add(any(Announcement.class));
    verify(contentRepository, times(0)).add(any(AnnouncementContent.class));
    verify(historyRepository, times(0)).add(any(AnnouncementHistory.class));
    verify(contentHistoryRepository, times(0))
        .add(any(AnnouncementContentHistory.class));
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_お知らせメッセージコンテンツが登録されていない() {
    // Arrange
    Announcement announcement = createDefaultAnnouncement();
    List<AnnouncementContent> contents = new ArrayList<>();
    String userName = "dummyName";

    // Act
    Executable action = () -> {
      service.addAnnouncementAndHistory(announcement, contents, userName);
    };

    // Assert
    AnnouncementValidationException ex =
        assertThrows(AnnouncementValidationException.class, action);
    List<ValidationError> errors = ex.getValidationErrors();
    assertThat(errors).isNotEmpty();
    assertThat(errors).contains(
        new ValidationError(FieldNameConstants.GLOBAL,
            ExceptionIdConstants.E_NULL_ANNOUNCEMENT));
    verify(announcementRepository, times(0)).add(any(Announcement.class));
    verify(contentRepository, times(0)).add(any(AnnouncementContent.class));
    verify(historyRepository, times(0)).add(any(AnnouncementHistory.class));
    verify(contentHistoryRepository, times(0))
        .add(any(AnnouncementContentHistory.class));
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_お知らせメッセージコンテンツの言語コードが重複している() {
    // Arrange
    Announcement announcement = createDefaultAnnouncement();
    String duplicateLanguageCode = LanguageCodeConstants.JA;
    UUID announcementId = announcement.getId();
    AnnouncementContent invalidLanguageCodeContent =
        createDefaultAnnouncementContent(announcementId,
            duplicateLanguageCode);
    List<AnnouncementContent> contents = announcement.getContents();
    contents.add(invalidLanguageCodeContent);
    String userName = "dummyName";

    // Act
    Executable action = () -> {
      service.addAnnouncementAndHistory(announcement, contents, userName);
    };

    // Assert
    AnnouncementValidationException ex =
        assertThrows(AnnouncementValidationException.class, action);
    List<ValidationError> errors = ex.getValidationErrors();
    assertThat(errors).isNotEmpty();
    assertThat(errors).contains(new ValidationError(FieldNameConstants.GLOBAL,
        ExceptionIdConstants.E_DUPLICATE_LANGUAGE_CODE));
    verify(announcementRepository, times(0)).add(any(Announcement.class));
    verify(contentRepository, times(0)).add(any(AnnouncementContent.class));
    verify(historyRepository, times(0)).add(any(AnnouncementHistory.class));
    verify(contentHistoryRepository, times(0))
        .add(any(AnnouncementContentHistory.class));
  }

  /**
   * お知らせメッセージコンテンツを生成します。
   * 
   * @param announcementId お知らせメッセージ ID 。
   * @return お知らせメッセージコンテンツ。
   */
  private AnnouncementContent createDefaultAnnouncementContent(UUID announcementId,
      String languageCode) {
    UUID defaultContentId = uuidGenerator.generate();
    String defaultTitle = "ダミータイトル";
    String defaultMessage = "ダミーメッセージ";
    String defaultLinkUrl = "https://dummy.com";
    AnnouncementContent content = new AnnouncementContent(defaultContentId,
        announcementId, languageCode, defaultTitle, defaultMessage,
        defaultLinkUrl);
    return content;
  }

  /**
   * お知らせメッセージを生成します。
   * 
   * @return お知らせメッセージ。
   */
  private Announcement createDefaultAnnouncement() {
    UUID defaultAnnouncementId = uuidGenerator.generate();
    String defaultCategory = "ダミーカテゴリー";
    OffsetDateTime defaultPostDateTime =
        OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0));
    OffsetDateTime defaultExpireDateTime =
        OffsetDateTime.of(2025, 1, 1, 23, 59, 0, 0, ZoneOffset.ofHours(0));
    Integer defaultDisplayPriority = 1;
    OffsetDateTime defaultCreatedAt =
        OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0));
    OffsetDateTime defaultChangedAt =
        OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0));
    Boolean defaultIsDeleted = false;
    List<String> languageCodes = LanguageCodeConstants.SUPPORTED_LANGUAGE_CODES;
    List<AnnouncementContent> contents = new ArrayList<>();
    for (String languageCode : languageCodes) {
      contents.add(createDefaultAnnouncementContent(defaultAnnouncementId,
          languageCode));
    }
    Announcement announcement = new Announcement(defaultAnnouncementId, defaultCategory,
        defaultPostDateTime, defaultExpireDateTime, defaultDisplayPriority,
        defaultCreatedAt,
        defaultChangedAt, defaultIsDeleted, contents);
    return announcement;
  }

  /**
   * お知らせメッセージのリストを生成します。
   * 
   * @param count お知らせメッセージの件数。
   * @return お知らせメッセージのリスト。
   */
  private List<Announcement> createDefaultAnnouncementList(long count) {
    List<Announcement> announcements = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      announcements.add(createDefaultAnnouncement());
    }
    return announcements;
  }
}
