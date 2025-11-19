package com.dressca.cms.announcement.applicationcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.cms.announcement.applicationcore.constant.LanguageCodeConstants;
import com.dressca.cms.announcement.applicationcore.dto.Announcement;
import com.dressca.cms.announcement.applicationcore.dto.AnnouncementContent;
import com.dressca.cms.announcement.applicationcore.dto.PagedAnnouncementList;
import com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
import com.dressca.cms.systemcommon.config.I18nConfig;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link AnnouncementApplicationService} の単体テストクラスです。
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = I18nConfig.class)
public class AnnouncementApplicationServiceTest {

  @Mock
  private AnnouncementRepository announcementRepository;

  @Autowired
  private MessageSource messages;

  private AnnouncementApplicationService service;

  @BeforeEach
  void setUp() {
    service = new AnnouncementApplicationService(announcementRepository, messages);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberとpageSizeが指定された場合ページングされたお知らせメッセージを取得できる() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 20;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageNumber()).isEqualTo(1);
    assertThat(result.getPageSize()).isEqualTo(20);
    assertThat(result.getTotalCount()).isEqualTo(50L);
    assertThat(result.getAnnouncements()).hasSize(20);
    assertThat(result.getLastPageNumber()).isEqualTo(3);
    verify(this.announcementRepository, times(1)).countByIsDeletedFalse();
    verify(announcementRepository, times(1)).findByOffsetAndLimit(0, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberがnullの場合デフォルト値1が使用される() {
    // Arrange
    Integer pageNumber = null;
    int pageSize = 20;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageNumber()).isEqualTo(1);
    assertThat(result.getPageSize()).isEqualTo(20);

    verify(announcementRepository, times(1)).findByOffsetAndLimit(0, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageSizeがnullの場合デフォルト値20が使用される() {
    // Arrange
    int pageNumber = 1;
    Integer pageSize = null;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageNumber()).isEqualTo(1);
    assertThat(result.getPageSize()).isEqualTo(20);

    verify(announcementRepository, times(1)).findByOffsetAndLimit(0, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageSizeが10未満の場合デフォルト値20が使用される() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 9;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageSize()).isEqualTo(20);

    verify(announcementRepository, times(1)).findByOffsetAndLimit(0, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageSizeが200より大きい場合デフォルト値20が使用される() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 201;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageSize()).isEqualTo(20);

    verify(announcementRepository, times(1)).findByOffsetAndLimit(0, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberが0以下の場合デフォルト値1が使用される() {
    // Arrange
    int pageNumber = 0;
    int pageSize = 20;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageNumber()).isEqualTo(1);

    verify(announcementRepository, times(1)).findByOffsetAndLimit(0, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_pageNumberが最後のページ番号より大きい場合1にリセットされる() {
    // Arrange
    int pageNumber = 4;
    int pageSize = 20;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageNumber()).isEqualTo(1);

    verify(announcementRepository, times(1)).findByOffsetAndLimit(0, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_総件数が0件の場合最後のページ番号は1となる() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 20;
    long totalCount = 0L;

    List<Announcement> announcements = new ArrayList<>();

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageNumber()).isEqualTo(1);
    assertThat(result.getLastPageNumber()).isEqualTo(1);
    assertThat(result.getAnnouncements()).isEmpty();
  }

  @Test
  void testGetPagedAnnouncementList_正常系_2ページ目を取得する場合正しいオフセットが計算される() {
    // Arrange
    int pageNumber = 2;
    int pageSize = 20;
    long totalCount = 50L;

    List<Announcement> announcements = createAnnouncementList(20);

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(20, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getPageNumber()).isEqualTo(2);

    verify(announcementRepository, times(1)).findByOffsetAndLimit(20, 20);
  }

  @Test
  void testGetPagedAnnouncementList_正常系_言語コード優先順に従って日本語が選択される() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 20;
    long totalCount = 1L;

    List<Announcement> announcements = createAnnouncementListWithMultipleLanguages();

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getAnnouncements()).hasSize(1);
    assertThat(result.getAnnouncements().get(0).getContents()).hasSize(1);
    assertThat(result.getAnnouncements().get(0).getContents().get(0).getLanguageCode())
        .isEqualTo("ja");
  }

  @Test
  void testGetPagedAnnouncementList_正常系_日本語がない場合英語が選択される() {
    // Arrange
    int pageNumber = 1;
    int pageSize = 20;
    long totalCount = 1L;

    List<Announcement> announcements = createAnnouncementListWithoutJapanese();

    when(announcementRepository.countByIsDeletedFalse()).thenReturn(totalCount);
    when(announcementRepository.findByOffsetAndLimit(0, 20)).thenReturn(announcements);

    // Act
    PagedAnnouncementList result = service.getPagedAnnouncementList(pageNumber, pageSize);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getAnnouncements()).hasSize(1);
    assertThat(result.getAnnouncements().get(0).getContents()).hasSize(1);
    assertThat(result.getAnnouncements().get(0).getContents().get(0).getLanguageCode())
        .isEqualTo("en");
  }

  /**
   * 指定された件数のお知らせメッセージリストを作成します。
   *
   * @param count 件数。
   * @return お知らせメッセージリスト。
   */
  private List<Announcement> createAnnouncementList(int count) {
    List<Announcement> announcements = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      Announcement announcement =
          new Announcement(UUID.randomUUID(), "INFO", OffsetDateTime.now(), null, 1,
              OffsetDateTime.now(), OffsetDateTime.now(), false, new ArrayList<>());
      List<AnnouncementContent> contents = new ArrayList<>();
      AnnouncementContent content = new AnnouncementContent(UUID.randomUUID(), announcement.getId(),
          LanguageCodeConstants.JA, "お知らせ " + (i + 1), "お知らせメッセージ " + (i + 1), null);
      contents.add(content);
      announcement.setContents(contents);
      announcements.add(announcement);
    }
    return announcements;
  }

  /**
   * 複数言語のコンテンツを持つお知らせメッセージリストを作成します。
   *
   * @return お知らせメッセージリスト。
   */
  private List<Announcement> createAnnouncementListWithMultipleLanguages() {
    UUID announcementId = UUID.randomUUID();
    List<AnnouncementContent> contents = new ArrayList<>();

    // 英語コンテンツ
    AnnouncementContent enContent =
        new AnnouncementContent(UUID.randomUUID(), announcementId, LanguageCodeConstants.EN,
            "Announcement", "Announcement message", null);
    contents.add(enContent);

    // 日本語コンテンツ
    AnnouncementContent jaContent =
        new AnnouncementContent(UUID.randomUUID(), announcementId, LanguageCodeConstants.JA,
            "お知らせ", "お知らせメッセージ", null);
    contents.add(jaContent);

    // 中国語コンテンツ
    AnnouncementContent zhContent =
        new AnnouncementContent(UUID.randomUUID(), announcementId, LanguageCodeConstants.ZH,
            "通知", "通知消息", null);
    contents.add(zhContent);

    Announcement announcement =
        new Announcement(announcementId, "INFO", OffsetDateTime.now(), null, 1,
            OffsetDateTime.now(), OffsetDateTime.now(), false, contents);

    return List.of(announcement);
  }

  /**
   * 日本語コンテンツがないお知らせメッセージリストを作成します。
   *
   * @return お知らせメッセージリスト。
   */
  private List<Announcement> createAnnouncementListWithoutJapanese() {
    UUID announcementId = UUID.randomUUID();
    List<AnnouncementContent> contents = new ArrayList<>();

    // 英語コンテンツ
    AnnouncementContent enContent =
        new AnnouncementContent(UUID.randomUUID(), announcementId, LanguageCodeConstants.EN,
            "Announcement", "Announcement message", null);
    contents.add(enContent);

    // スペイン語コンテンツ
    AnnouncementContent esContent =
        new AnnouncementContent(UUID.randomUUID(), announcementId, LanguageCodeConstants.ES,
            "Anuncio", "Mensaje de anuncio", null);
    contents.add(esContent);

    Announcement announcement =
        new Announcement(announcementId, "INFO", OffsetDateTime.now(), null, 1,
            OffsetDateTime.now(), OffsetDateTime.now(), false, contents);
    return List.of(announcement);
  }
}
