package com.dressca.cms.announcement.applicationcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContentHistory;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementHistory;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementWithHistory;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementNotFoundException;
import com.dressca.cms.announcement.applicationcore.exception.AnnouncementValidationException;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentHistoryRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementHistoryRepository;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.systemcommon.util.UuidGenerator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.cms.systemcommon.util.ApplicationContextWrapper;

/**
 * {@link AnnouncementApplicationService} の単体テストクラスです。
 */
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@TestPropertySource(properties = "spring.messages.basename=i18n/messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class AnnouncementApplicationServiceTest {

  @Mock
  private AnnouncementRepository announcementRepository;
  @Mock
  private AnnouncementContentRepository announcementContentRepository;
  @Mock
  private AnnouncementHistoryRepository announcementHistoryRepository;
  @Mock
  private AnnouncementContentHistoryRepository announcementContentHistoryRepository;
  @Autowired
  private MessageSource messages;
  @Autowired
  private ApplicationContext applicationContext;
  private AnnouncementApplicationService service;

  @BeforeEach
  void setUp() {
    // ApplicationContextWrapper を初期化
    ApplicationContextWrapper wrapper = new ApplicationContextWrapper();
    wrapper.setApplicationContext(applicationContext);

    service = new AnnouncementApplicationService(announcementRepository, announcementContentRepository,
        announcementHistoryRepository, announcementContentHistoryRepository, messages);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberとpageSizeが指定された場合ページングされたお知らせメッセージを取得できる() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(createAnnouncementList(20));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageNumber()).isEqualTo(1);
    assertThat(result.getPageSize()).isEqualTo(20);
    assertThat(result.getTotalCount()).isEqualTo(50L);
    assertThat(result.getAnnouncements()).hasSize(20);
    assertThat(result.getLastPageNumber()).isEqualTo(3);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberがnullの場合デフォルト値1が使用される() {
    // Arrange
    Integer pageNumber = null;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(createAnnouncementList(20));
    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageNumber()).isEqualTo(1);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageSizeがnullの場合デフォルト値20が使用される() {
    // Arrange
    int pageNumber = 1;
    Integer pageSize = null;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(createAnnouncementList(20));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageSize()).isEqualTo(20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageSizeが10未満の場合デフォルト値20が使用される() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 9;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(createAnnouncementList(20));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageSize()).isEqualTo(20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageSizeが200より大きい場合デフォルト値20が使用される() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 201;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(createAnnouncementList(20));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageSize()).isEqualTo(20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberが0以下の場合デフォルト値1が使用される() {
    // Arrange
    int pageNumber = 0;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(createAnnouncementList(20));
    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageNumber()).isEqualTo(1);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberが最後のページ番号より大きい場合1にリセットされる() {
    // Arrange
    int pageNumber = 4;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(createAnnouncementList(20));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageNumber()).isEqualTo(1);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_総件数が0件の場合最後のページ番号は1となる() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(0L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(new ArrayList<>());

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getLastPageNumber()).isEqualTo(1);
    assertThat(result.getAnnouncements()).isEmpty();
  }

  @Test
  void testGetPagedAnnouncementList_正常系_2ページ目を取得する場合正しいオフセットが計算される() {
    // Arrange
    int pageNumber = 2;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(50L);
    when(announcementRepository.findByOffsetAndLimit(20, 20)).thenReturn(createAnnouncementList(20));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getPageNumber()).isEqualTo(2);
    verify(announcementRepository, times(1)).findByOffsetAndLimit(20, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_言語コード優先順に従って日本語が選択される() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent jaContent = createContent(announcement.getId(), "ja");
    AnnouncementContent enContent = createContent(announcement.getId(), "en");
    announcement.setContents(List.of(enContent, jaContent));

    int pageNumber = 1;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(1L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(List.of(announcement));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getAnnouncements().get(0).getContents().get(0).getLanguageCode()).isEqualTo("ja");
  }

  @Test
  void testGetPagedAnnouncementList_正常系_日本語がない場合英語が選択される() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent enContent = createContent(announcement.getId(), "en");
    announcement.setContents(List.of(enContent));
    int pageNumber = 1;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(1L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(List.of(announcement));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getAnnouncements().get(0).getContents().get(0).getLanguageCode()).isEqualTo("en");
  }

  @Test
  void testSelectPriorityContent_正常系_コンテンツがnullの場合スキップされる() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    int pageNumber = 1;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(1L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(List.of(announcement));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getAnnouncements().get(0).getContents()).isNull();
  }

  @Test
  void testSelectPriorityContent_正常系_コンテンツが空の場合スキップされる() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    announcement.setContents(new ArrayList<>());
    int pageNumber = 1;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(1L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(List.of(announcement));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getAnnouncements().get(0).getContents()).isEmpty();
  }

  @Test
  void testSelectPriorityContent_正常系_未知の言語コードの場合最初のコンテンツが選択される() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent unknownContent = createContent(announcement.getId(), "unknown");
    announcement.setContents(List.of(unknownContent));
    int pageNumber = 1;
    int pageSize = 20;
    when(announcementRepository.countByIsDeletedFalse()).thenReturn(1L);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(List.of(announcement));

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result.getAnnouncements().get(0).getContents().get(0).getLanguageCode()).isEqualTo("unknown");
  }

  @Test
  void testAddAnnouncementAndHistory_正常系_お知らせメッセージと履歴が正しく登録される() throws AnnouncementValidationException {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent content = createContent(announcement.getId(), "ja");
    announcement.setContents(List.of(content));
    String username = "dummyUser";

    // Act
    UUID result = service.addAnnouncementAndHistory(announcement, username);

    // Assert
    assertThat(result).isNotNull();
    verify(announcementRepository, times(1)).add(any());
    verify(announcementContentRepository, times(1)).add(any());
    verify(announcementHistoryRepository, times(1)).add(any());
    verify(announcementContentHistoryRepository, times(1)).add(any());
  }

  @Test
  void testAddAnnouncementAndHistory_正常系_複数言語のコンテンツが正しく登録される()
      throws AnnouncementValidationException {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent jaContent = createContent(announcement.getId(), "ja");
    AnnouncementContent enContent = createContent(announcement.getId(), "en");
    announcement.setContents(List.of(jaContent, enContent));
    String username = "dummyUser";

    // Act
    UUID result = service.addAnnouncementAndHistory(announcement, username);

    // Assert
    assertThat(result).isNotNull();
    verify(announcementContentRepository, times(2)).add(any());
    verify(announcementHistoryRepository, times(1)).add(any());
    verify(announcementContentHistoryRepository, times(2)).add(any());
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_無効な言語コードの場合例外が発生する() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent invalidContent = createContent(announcement.getId(), "invalid");
    announcement.setContents(List.of(invalidContent));
    String username = "dummyUser";

    // Act & Assert
    assertThrows(AnnouncementValidationException.class,
        () -> service.addAnnouncementAndHistory(announcement, username));
    verify(announcementRepository, times(0)).add(any());
    verify(announcementHistoryRepository, times(0)).add(any());
  }

  @Test
  void testAddAnnouncementAndHistory_正常系_掲載開始日時がnullで掲載終了日時が指定されている場合正常に登録される()
      throws AnnouncementValidationException {
    // Arrange
    OffsetDateTime expireDateTime = OffsetDateTime.now().plusDays(7);
    Announcement announcement = new Announcement(UuidGenerator.generate(), "INFO", null, expireDateTime, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, null);
    AnnouncementContent content = createContent(announcement.getId(), "ja");
    announcement.setContents(List.of(content));
    String username = "dummyUser";

    // Act
    UUID result = service.addAnnouncementAndHistory(announcement, username);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_コンテンツがnullの場合例外が発生する() {
    // Arrange
    Announcement announcement = new Announcement(UuidGenerator.generate(), "INFO", OffsetDateTime.now(), null, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, null);
    String username = "dummyUser";

    // Act & Assert
    assertThrows(AnnouncementValidationException.class,
        () -> service.addAnnouncementAndHistory(announcement, username));
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_コンテンツが空の場合例外が発生する() {
    // Arrange
    Announcement announcement = new Announcement(UuidGenerator.generate(), "INFO", OffsetDateTime.now(), null, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, new ArrayList<>());
    String username = "dummyUser";

    // Act & Assert
    assertThrows(AnnouncementValidationException.class,
        () -> service.addAnnouncementAndHistory(announcement, username));
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_言語コードが重複している場合例外が発生する() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent jaContent1 = createContent(announcement.getId(), "ja");
    AnnouncementContent jaContent2 = createContent(announcement.getId(), "ja");
    announcement.setContents(List.of(jaContent1, jaContent2));
    String username = "dummyUser";

    // Act & Assert
    AnnouncementValidationException exception = assertThrows(AnnouncementValidationException.class,
        () -> service.addAnnouncementAndHistory(announcement, username));
    assertThat(exception.getValidationErrors()).hasSize(1);
  }

  @Test
  void testAddAnnouncementAndHistory_正常系_掲載開始日時と掲載終了日時が同じ場合正常に登録される() throws AnnouncementValidationException {
    // Arrange
    OffsetDateTime sameDateTime = OffsetDateTime.now();
    Announcement announcement = new Announcement(UuidGenerator.generate(), "INFO", sameDateTime, sameDateTime, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, null);
    AnnouncementContent content = createContent(announcement.getId(), "ja");
    announcement.setContents(List.of(content));
    String username = "dummyUser";

    // Act
    UUID result = service.addAnnouncementAndHistory(announcement, username);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  void testAddAnnouncementAndHistory_正常系_掲載終了日時がnullの場合正常に登録される() throws AnnouncementValidationException {
    // Arrange
    Announcement announcement = new Announcement(UuidGenerator.generate(), "INFO", OffsetDateTime.now(), null, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, null);
    AnnouncementContent content = createContent(announcement.getId(), "ja");
    announcement.setContents(List.of(content));
    String username = "dummyUser";

    // Act
    UUID result = service.addAnnouncementAndHistory(announcement, username);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  void testAddAnnouncementAndHistory_異常系_複数の無効な言語コードがある場合複数のエラーが発生する() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent invalidContent1 = createContent(announcement.getId(), "invalid1");
    AnnouncementContent invalidContent2 = createContent(announcement.getId(), "invalid2");
    announcement.setContents(List.of(invalidContent1, invalidContent2));
    String username = "dummyUser";

    // Act & Assert
    AnnouncementValidationException exception = assertThrows(AnnouncementValidationException.class,
        () -> service.addAnnouncementAndHistory(announcement, username));
    assertThat(exception.getValidationErrors()).hasSize(2);
  }

  @Test
  void testGetAnnouncementAndHistoriesById_正常系_お知らせメッセージと履歴を取得できる() throws AnnouncementNotFoundException {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    AnnouncementContent content = createContent(announcementId, "ja");
    Announcement announcement = new Announcement(announcementId, "INFO", OffsetDateTime.now(), null, 3,
        OffsetDateTime.now(), OffsetDateTime.now(), false, List.of(content));

    UUID announcementHistoryId = UuidGenerator.generate();
    AnnouncementContentHistory contentHistory = createContentHistory(announcementHistoryId, "ja");
    AnnouncementHistory history = new AnnouncementHistory(announcementHistoryId, announcementId, "INFO",
        OffsetDateTime.now(), null, 3, OffsetDateTime.now(), "dummyUser", 1, List.of(contentHistory));

    when(announcementRepository.findByIdWithContents(announcementId)).thenReturn(Optional.ofNullable(announcement));
    when(announcementHistoryRepository.findByAnnouncementIdWithContents(announcementId)).thenReturn(List.of(history));

    // Act
    AnnouncementWithHistory result = service.getAnnouncementAndHistoriesById(announcementId);

    // Assert
    assertThat(result.getAnnouncement()).isEqualTo(announcement);
    assertThat(result.getHistories()).hasSize(1);
    assertThat(result.getHistories().get(0)).isEqualTo(history);
  }

  @Test
  void testGetAnnouncementAndHistoriesById_異常系_お知らせメッセージが存在しない場合例外が発生する() {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    when(announcementRepository.findByIdWithContents(announcementId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(AnnouncementNotFoundException.class, () -> service.getAnnouncementAndHistoriesById(announcementId));
    verify(announcementRepository, times(1)).findByIdWithContents(announcementId);
    verify(announcementHistoryRepository, times(0)).findByAnnouncementIdWithContents(any());
  }

  @Test
  void testUpdateAnnouncement_正常系_お知らせメッセージと履歴が正しく更新される() throws AnnouncementValidationException {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    UUID contentId = UuidGenerator.generate();
    AnnouncementContent content = new AnnouncementContent(contentId, announcementId, "ja", "日本語タイトル", "日本語メッセージ",
        "https://example.com");
    Announcement announcement = new Announcement(announcementId, "WARN", OffsetDateTime.now(), null,
        2, OffsetDateTime.now(), OffsetDateTime.now(), false, List.of(content));

    AnnouncementContent existingContent = new AnnouncementContent(contentId, announcementId, "ja", "日本語タイトル",
        "日本語メッセージ", "https://example.com");
    when(announcementContentRepository.findByAnnouncementId(announcementId))
        .thenReturn(List.of(existingContent));

    // Act
    service.updateAnnouncement(announcement, "dummyUser");

    // Assert
    verify(announcementRepository, times(1)).update(any());
    verify(announcementContentRepository, times(1)).update(any());
    verify(announcementHistoryRepository, times(1)).add(any());
    verify(announcementContentHistoryRepository, times(1)).add(any());
  }

  @Test
  void testUpdateAnnouncement_正常系_新しい言語のコンテンツを追加できる() throws AnnouncementValidationException {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    UUID jaContentId = UuidGenerator.generate();
    AnnouncementContent jaContent = new AnnouncementContent(jaContentId, announcementId, "ja", "日本語タイトル", "日本語メッセージ",
        "https://example.com");
    AnnouncementContent enContent = createContent(announcementId, "en");
    Announcement announcement = new Announcement(announcementId, "INFO", OffsetDateTime.now(), null,
        3, OffsetDateTime.now(), OffsetDateTime.now(), false,
        List.of(jaContent, enContent));

    AnnouncementContent existingContent = new AnnouncementContent(jaContentId, announcementId, "ja", "日本語タイトル",
        "日本語メッセージ", "https://example.com");
    when(announcementContentRepository.findByAnnouncementId(announcementId))
        .thenReturn(List.of(existingContent));

    // Act
    service.updateAnnouncement(announcement, "dummyUser");

    // Assert
    verify(announcementRepository, times(1)).update(any());
    verify(announcementContentRepository, times(1)).update(any());
    verify(announcementContentRepository, times(1)).add(any());
    verify(announcementHistoryRepository, times(1)).add(any());
    verify(announcementContentHistoryRepository, times(2)).add(any());
  }

  @Test
  void testUpdateAnnouncement_正常系_言語のコンテンツを削除できる() throws AnnouncementValidationException {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    UUID jaContentId = UuidGenerator.generate();
    UUID enContentId = UuidGenerator.generate();
    AnnouncementContent jaContent = new AnnouncementContent(jaContentId, announcementId, "ja", "日本語タイトル", "日本語メッセージ",
        "https://example.com");
    Announcement announcement = new Announcement(announcementId, "INFO", OffsetDateTime.now(), null,
        3, OffsetDateTime.now(), OffsetDateTime.now(), false, List.of(jaContent));

    AnnouncementContent existingJaContent = new AnnouncementContent(jaContentId, announcementId, "ja", "日本語タイトル",
        "日本語メッセージ", "https://example.com");
    AnnouncementContent existingEnContent = new AnnouncementContent(enContentId, announcementId, "en", "English Title",
        "English Message", "https://example.com");
    when(announcementContentRepository.findByAnnouncementId(announcementId))
        .thenReturn(List.of(existingJaContent, existingEnContent));

    // Act
    service.updateAnnouncement(announcement, "dummyUser");

    // Assert
    verify(announcementRepository, times(1)).update(any());
    verify(announcementContentRepository, times(1)).update(any());
    verify(announcementContentRepository, times(1)).deleteById(enContentId);
    verify(announcementHistoryRepository, times(1)).add(any());
    verify(announcementContentHistoryRepository, times(1)).add(any());
  }

  @Test
  void testUpdateAnnouncement_異常系_無効な言語コードの場合例外が発生する() {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    AnnouncementContent invalidContent = createContent(announcementId, "invalid");
    Announcement announcement = new Announcement(announcementId, "INFO", OffsetDateTime.now(), null, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, List.of(invalidContent));

    // Act & Assert
    assertThrows(AnnouncementValidationException.class,
        () -> service.updateAnnouncement(announcement, "dummyUser"));
    verify(announcementRepository, times(0)).update(any());
    verify(announcementHistoryRepository, times(0)).add(any());
  }

  @Test
  void testUpdateAnnouncement_異常系_コンテンツがnullの場合例外が発生する() {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    Announcement announcement = new Announcement(announcementId, "INFO", OffsetDateTime.now(), null, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, null);

    // Act & Assert
    assertThrows(AnnouncementValidationException.class, () -> service.updateAnnouncement(announcement, "dummyUser"));
    verify(announcementRepository, times(0)).update(any());
  }

  @Test
  void testUpdateAnnouncement_異常系_コンテンツが空の場合例外が発生する() {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    Announcement announcement = new Announcement(announcementId, "INFO", OffsetDateTime.now(), null, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, new ArrayList<>());

    // Act & Assert
    assertThrows(AnnouncementValidationException.class, () -> service.updateAnnouncement(announcement, "dummyUser"));
    verify(announcementRepository, times(0)).update(any());
  }

  @Test
  void testUpdateAnnouncement_異常系_言語コードが重複している場合例外が発生する() {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent jaContent1 = createContent(announcement.getId(), "ja");
    AnnouncementContent jaContent2 = createContent(announcement.getId(), "ja");
    announcement.setContents(List.of(jaContent1, jaContent2));

    // Act & Assert
    AnnouncementValidationException exception = assertThrows(AnnouncementValidationException.class,
        () -> service.updateAnnouncement(announcement, "dummyUser"));
    assertThat(exception.getValidationErrors()).hasSize(1);
    verify(announcementRepository, times(0)).update(any());
  }

  @Test
  void testUpdateAnnouncement_正常系_掲載開始日時がnullで掲載終了日時が指定されている場合正常に更新される()
      throws AnnouncementValidationException {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    UUID contentId = UuidGenerator.generate();
    AnnouncementContent jaContent = new AnnouncementContent(contentId, announcementId, "ja", "日本語タイトル", "日本語メッセージ",
        "https://example.com");
    Announcement announcement = new Announcement(announcementId, "INFO", null, OffsetDateTime.now().plusDays(7), 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, List.of(jaContent));

    AnnouncementContent existingContent = new AnnouncementContent(contentId, announcementId, "ja", "日本語タイトル",
        "日本語メッセージ", "https://example.com");
    when(announcementContentRepository.findByAnnouncementId(announcementId)).thenReturn(List.of(existingContent));

    // Act
    service.updateAnnouncement(announcement, "dummyUser");

    // Assert
    verify(announcementRepository, times(1)).update(any());
    verify(announcementContentRepository, times(1)).update(any());
  }

  @Test
  void testUpdateAnnouncement_正常系_掲載開始日時と掲載終了日時が同じ場合正常に更新される()
      throws AnnouncementValidationException {
    // Arrange
    UUID announcementId = UuidGenerator.generate();
    OffsetDateTime sameDateTime = OffsetDateTime.now();
    UUID contentId = UuidGenerator.generate();
    AnnouncementContent jaContent = new AnnouncementContent(contentId, announcementId, "ja", "日本語タイトル", "日本語メッセージ",
        "https://example.com");
    Announcement announcement = new Announcement(announcementId, "INFO", sameDateTime, sameDateTime, 1,
        OffsetDateTime.now(), OffsetDateTime.now(), false, List.of(jaContent));

    AnnouncementContent existingContent = new AnnouncementContent(contentId, announcementId, "ja", "日本語タイトル",
        "日本語メッセージ", "https://example.com");
    when(announcementContentRepository.findByAnnouncementId(announcementId)).thenReturn(List.of(existingContent));

    // Act
    service.updateAnnouncement(announcement, "dummyUser");

    // Assert
    verify(announcementRepository, times(1)).update(any());
    verify(announcementContentRepository, times(1)).update(any());
  }

  @Test
  void testDeleteAnnouncementAndRecordHistory_正常系_お知らせメッセージと履歴が正しく削除および追加される()
      throws AnnouncementNotFoundException {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent content = createContent(announcement.getId(), "ja");
    announcement.setContents(List.of(content));
    List<AnnouncementHistory> histories = new ArrayList<>();
    String username = "dummyUser";

    when(announcementRepository.delete(announcement.getId())).thenReturn(Optional.ofNullable(announcement));
    when(announcementHistoryRepository.findByAnnouncementIdWithContents(announcement.getId())).thenReturn(histories);

    // Act
    AnnouncementWithHistory result = service.deleteAnnouncementAndRecordHistory(announcement.getId(), username);
    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getAnnouncement()).isNotNull();
    assertThat(result.getHistories()).isNotNull();
    verify(announcementRepository, times(1)).delete(announcement.getId());
    verify(announcementHistoryRepository, times(1)).add(any());
    verify(announcementContentHistoryRepository, times(1)).add(any());
  }

  @Test
  void testDeleteAnnouncementAndRecordHistory_正常系_複数言語のコンテンツを持つお知らせメッセージが削除される() throws AnnouncementNotFoundException {
    // Arrange
    Announcement announcement = createAnnouncementWithoutContent();
    AnnouncementContent jaContent = createContent(announcement.getId(), "ja");
    AnnouncementContent enContent = createContent(announcement.getId(), "en");
    announcement.setContents(List.of(jaContent, enContent));
    List<AnnouncementHistory> histories = new ArrayList<>();
    String username = "dummyUser";

    when(announcementRepository.delete(announcement.getId())).thenReturn(Optional.ofNullable(announcement));
    when(announcementHistoryRepository.findByAnnouncementIdWithContents(announcement.getId())).thenReturn(histories);

    // Act
    AnnouncementWithHistory result = service.deleteAnnouncementAndRecordHistory(announcement.getId(), username);

    // Assert
    assertThat(result).isNotNull();
    verify(announcementRepository, times(1)).delete(announcement.getId());
    verify(announcementHistoryRepository, times(1)).add(any());
    verify(announcementContentHistoryRepository, times(2)).add(any());
  }

  @Test
  void testDeleteAnnouncementAndRecordHistory_異常系_存在しないお知らせメッセージIDの場合例外が発生する() {
    // Arrange
    UUID nonExistentId = UuidGenerator.generate();
    String username = "dummyUser";
    when(announcementRepository.delete(nonExistentId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(AnnouncementNotFoundException.class,
        () -> service.deleteAnnouncementAndRecordHistory(nonExistentId, username));
    verify(announcementRepository, times(1)).delete(nonExistentId);
    verify(announcementRepository, times(0)).findByIdWithContents(any());
    verify(announcementHistoryRepository, times(0)).add(any());
  }

  /**
   * お知らせコンテンツを作成します。
   * 
   * @param announcementId お知らせメッセージ ID 。
   * @param languageCode   言語コード。
   * @return お知らせコンテンツ。
   */
  private AnnouncementContent createContent(UUID announcementId, String languageCode) {
    return new AnnouncementContent(UuidGenerator.generate(), announcementId, languageCode, "dummy title",
        "dummy message", "https://example.com");
  }

  /**
   * お知らせコンテンツが空のお知らせメッセージを作成します。
   * お知らせコンテンツは本メソッドを呼び出した後にセッター経由で設定してください。
   * 
   * @return お知らせコンテンツが空のお知らせメッセージ。
   */
  private Announcement createAnnouncementWithoutContent() {
    int displayPriority = 3; // 表示優先度中
    return new Announcement(UuidGenerator.generate(), "INFO", OffsetDateTime.now(), null,
        displayPriority, OffsetDateTime.now(), OffsetDateTime.now(), false, null);
  }

  /**
   * お知らせメッセージのリストを作成します。
   * 
   * @param count 作成するお知らせメッセージの件数。
   * @return 日本語のお知らせコンテンツを持つお知らせメッセージのリスト。
   */
  private List<Announcement> createAnnouncementList(int count) {
    List<Announcement> announcements = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      Announcement announcement = createAnnouncementWithoutContent();
      List<AnnouncementContent> content = List.of(createContent(announcement.getId(), "ja"));
      announcement.setContents(content);
      announcements.add(announcement);
    }
    return announcements;
  }

  /**
   * お知らせコンテンツ履歴を作成します。
   * 
   * @param announcementHistoryId お知らせ履歴 ID 。
   * @param languageCode          言語コード。
   * @return お知らせコンテンツ履歴。
   */
  private AnnouncementContentHistory createContentHistory(UUID announcementHistoryId, String languageCode) {
    return new AnnouncementContentHistory(UuidGenerator.generate(), announcementHistoryId, languageCode, "dummy title",
        "dummy message", "https://example.com");
  }
}