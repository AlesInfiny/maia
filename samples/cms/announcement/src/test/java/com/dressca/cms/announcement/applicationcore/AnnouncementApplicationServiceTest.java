package com.dressca.cms.announcement.applicationcore;
// package com.dressca.cms.announcement.applicationcore.applicationservice;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.atLeastOnce;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import
// com.dressca.cms.announcement.applicationcore.AnnouncementApplicationService;
// import
// com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentEntity;
// import
// com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementEntity;
// import
// com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementHistoryEntity;
// import
// com.dressca.cms.announcement.infrastructure.repository.mybatis.generated.entity.AnnouncementContentHistoryEntity;
// import com.dressca.cms.announcement.applicationcore.AnnouncementWithHistory;
// import
// com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentHistoryRepository;
// import
// com.dressca.cms.announcement.applicationcore.repository.AnnouncementContentRepository;
// import
// com.dressca.cms.announcement.applicationcore.repository.AnnouncementHistoryRepository;
// import
// com.dressca.cms.announcement.applicationcore.repository.AnnouncementRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import java.time.OffsetDateTime;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.UUID;

// class AnnouncementApplicationServiceTest {

// @Mock
// private AnnouncementRepository announcementRepository;
// @Mock
// private AnnouncementContentRepository contentRepository;
// @Mock
// private AnnouncementHistoryRepository historyRepository;
// @Mock
// private AnnouncementContentHistoryRepository contentHistoryRepository;

// @InjectMocks
// private AnnouncementApplicationService service;

// @BeforeEach
// void setUp() {
// MockitoAnnotations.openMocks(this);
// service = new AnnouncementApplicationService(announcementRepository,
// contentRepository, historyRepository,
// contentHistoryRepository);
// }

// @Test
// void getAnnouncementAndHistoryById_正常系_取得できる() throws Exception {
// // Arrange
// UUID announcementId = UUID.randomUUID();
// var announcement = new AnnouncementEntity();
// announcement.setId(announcementId.toString());
// announcement.setIsDeleted(false);

// AnnouncementContentEntity contentJa = new AnnouncementContentEntity();
// contentJa.setId(UUID.randomUUID().toString());
// contentJa.setAnnouncementId(announcementId.toString());
// contentJa.setLanguageCode("ja");
// contentJa.setMessage("お知らせ本文");

// final List<AnnouncementContentEntity> contents = new
// ArrayList<>(List.of(contentJa));

// final AnnouncementHistoryEntity historyEntity = new
// AnnouncementHistoryEntity();
// historyEntity.setId(UUID.randomUUID().toString());
// final List<AnnouncementHistoryEntity> histories = new
// ArrayList<>(List.of(historyEntity));

// final AnnouncementContentHistoryEntity contentHistoryEntity = new
// AnnouncementContentHistoryEntity();
// contentHistoryEntity.setId(UUID.randomUUID().toString());
// contentHistoryEntity.setAnnouncementHistoryId(historyEntity.getId());
// contentHistoryEntity.setLanguageCode("ja");
// contentHistoryEntity.setMessage("履歴本文");
// List<AnnouncementContentHistoryEntity> contentHistories = new
// ArrayList<>(List.of(contentHistoryEntity));

// when(announcementRepository.findByAnnouncementId(announcementId)).thenReturn(announcement);
// when(contentRepository.findByAnnouncementId(announcementId)).thenReturn(contents);
// when(historyRepository.findByAnnouncementId(announcementId)).thenReturn(histories);
// when(contentHistoryRepository.findByAnnouncementHistoryId(UUID.fromString(historyEntity.getId())))
// .thenReturn(contentHistories);

// // Act
// var result = service.getAnnouncementAndHistoryById(announcementId);

// // Assert
// assertNotNull(result);
// assertEquals(announcement, result.getAnnouncement());
// assertEquals(1, result.getContents().size());
// assertEquals(1, result.getHistoryWithContentHistories().size());
// assertEquals(1,
// result.getHistoryWithContentHistories().get(0).getContentHistories().size());
// }

// @Test
// void getAnnouncementAndHistoryById_例外_存在しない場合() {
// // Arrange
// UUID announcementId = UUID.randomUUID();
// when(announcementRepository.findByAnnouncementId(announcementId)).thenReturn(null);

// // Act & Assert
// assertThrows(
// com.dressca.cms.announcement.applicationcore.AnnouncementNotFoundException.class,
// () -> service.getAnnouncementAndHistoryById(announcementId));
// }

// @Test
// void getAnnouncementAndHistoryById_例外_削除済みの場合() {
// // Arrange
// UUID announcementId = UUID.randomUUID();
// var announcement = new AnnouncementEntity();
// announcement.setId(announcementId.toString());
// announcement.setIsDeleted(true);
// when(announcementRepository.findByAnnouncementId(announcementId)).thenReturn(announcement);

// // Act & Assert
// assertThrows(
// com.dressca.cms.announcement.applicationcore.AnnouncementNotFoundException.class,
// () -> service.getAnnouncementAndHistoryById(announcementId));
// }

// @Test
// void updateAnnouncement_正常系_更新と履歴追加() {
// // Arrange
// String announcementId = UUID.randomUUID().toString();
// AnnouncementEntity announcement = new AnnouncementEntity();
// announcement.setId(announcementId);
// announcement.setCategory("INFO");
// OffsetDateTime now = OffsetDateTime.now();
// announcement.setPostDateTime(now);
// announcement.setExpireDateTime(now.plusDays(1));
// announcement.setDisplayPriority(1);
// announcement.setCreatedAt(now);

// AnnouncementEntity updatedAnnouncement = new AnnouncementEntity();
// updatedAnnouncement.setId(announcementId);
// updatedAnnouncement.setCategory("INFO");
// updatedAnnouncement.setPostDateTime(announcement.getPostDateTime());
// updatedAnnouncement.setExpireDateTime(announcement.getExpireDateTime());
// updatedAnnouncement.setDisplayPriority(1);
// updatedAnnouncement.setCreatedAt(announcement.getCreatedAt());

// AnnouncementContentEntity contentJa = new AnnouncementContentEntity();
// contentJa.setId(UUID.randomUUID().toString());
// contentJa.setAnnouncementId(announcementId);
// contentJa.setLanguageCode("ja");
// try {
// var contentField =
// AnnouncementContentEntity.class.getDeclaredField("content");
// contentField.setAccessible(true);
// contentField.set(contentJa, "お知らせ本文");
// } catch (Exception e) {
// // ignore for test
// }

// AnnouncementContentEntity contentEn = new AnnouncementContentEntity();
// contentEn.setId(UUID.randomUUID().toString());
// contentEn.setAnnouncementId(announcementId);
// contentEn.setLanguageCode("en");
// try {
// var contentField =
// AnnouncementContentEntity.class.getDeclaredField("content");
// contentField.setAccessible(true);
// contentField.set(contentEn, "Announcement body");
// } catch (Exception e) {
// // ignore for test
// }

// List<AnnouncementContentEntity> existContents = new ArrayList<>();
// existContents.add(contentJa);

// List<AnnouncementContentEntity> updateContents = new ArrayList<>();
// updateContents.add(contentJa);
// updateContents.add(contentEn);

// when(announcementRepository.update(any())).thenReturn(updatedAnnouncement);
// when(contentRepository.findByAnnouncementId(UUID.fromString(announcementId))).thenReturn(existContents)
// .thenReturn(updateContents);

// AnnouncementHistoryEntity history = new AnnouncementHistoryEntity(
// UUID.randomUUID().toString(),
// announcementId,
// "INFO",
// announcement.getPostDateTime(),
// announcement.getExpireDateTime(),
// 1,
// announcement.getCreatedAt(),
// "ADMIN",
// 1);
// List<AnnouncementHistoryEntity> updatedHistories = List.of(history);
// when(historyRepository.findByAnnouncementId(UUID.fromString(announcementId))).thenReturn(updatedHistories);
// when(contentHistoryRepository.findByAnnouncementHistoryId(any())).thenReturn(Collections.emptyList());

// // Act
// AnnouncementWithHistory result = service.updateAnnouncement(announcement,
// updateContents);

// // Assert
// assertNotNull(result);
// assertEquals(updatedAnnouncement, result.getAnnouncement());
// assertEquals(2, result.getContents().size());
// verify(announcementRepository).update(announcement);
// verify(contentRepository, times(1)).update(contentJa);
// verify(contentRepository,
// times(1)).add(any(AnnouncementContentEntity.class));
// verify(historyRepository).add(any(AnnouncementHistoryEntity.class));
// verify(contentHistoryRepository,
// atLeastOnce()).add(any(AnnouncementContentHistoryEntity.class));
// }

// @Test
// void updateAnnouncement_異常系_updateがnullを返す場合() {
// // Arrange
// String announcementId = UUID.randomUUID().toString();
// AnnouncementEntity announcement = new AnnouncementEntity();
// announcement.setId(announcementId);
// List<AnnouncementContentEntity> contents = new ArrayList<>();
// when(announcementRepository.update(any())).thenReturn(null);

// // Act & Assert
// assertThrows(NullPointerException.class, () ->
// service.updateAnnouncement(announcement, contents));
// }

// @Test
// void updateAnnouncement_削除ロジック_存在しない言語コードのコンテンツが削除される() {
// // Arrange
// String announcementId = UUID.randomUUID().toString();
// AnnouncementEntity announcement = new AnnouncementEntity();
// announcement.setId(announcementId);
// AnnouncementEntity updatedAnnouncement = new AnnouncementEntity();
// updatedAnnouncement.setId(announcementId);

// AnnouncementContentEntity contentJa = new AnnouncementContentEntity();
// contentJa.setId(UUID.randomUUID().toString());
// contentJa.setAnnouncementId(announcementId);
// contentJa.setLanguageCode("ja");
// AnnouncementContentEntity contentEn = new AnnouncementContentEntity();
// contentEn.setId(UUID.randomUUID().toString());
// contentEn.setAnnouncementId(announcementId);
// contentEn.setLanguageCode("en");

// List<AnnouncementContentEntity> existContents = new ArrayList<>();
// existContents.add(contentJa);
// existContents.add(contentEn);

// List<AnnouncementContentEntity> updateContents = new ArrayList<>();
// updateContents.add(contentJa); // enは削除対象

// when(announcementRepository.update(any())).thenReturn(updatedAnnouncement);
// when(contentRepository.findByAnnouncementId(UUID.fromString(announcementId))).thenReturn(existContents)
// .thenReturn(updateContents);
// when(historyRepository.findByAnnouncementId(UUID.fromString(announcementId))).thenReturn(new
// ArrayList<>());
// when(contentHistoryRepository.findByAnnouncementHistoryId(any())).thenReturn(new
// ArrayList<>());

// // Act
// service.updateAnnouncement(announcement, updateContents);

// // Assert
// verify(contentRepository).remove(contentEn);
// }

// @Test
// void getAnnouncementAndHistoryById_正常系_多言語複数履歴() throws Exception {
// // Arrange
// UUID announcementId = UUID.randomUUID();
// var announcement = new AnnouncementEntity();
// announcement.setId(announcementId.toString());
// announcement.setIsDeleted(false);

// AnnouncementContentEntity contentJa = new AnnouncementContentEntity();
// contentJa.setId(UUID.randomUUID().toString());
// contentJa.setAnnouncementId(announcementId.toString());
// contentJa.setLanguageCode("ja");
// contentJa.setMessage("お知らせ本文");
// AnnouncementContentEntity contentEn = new AnnouncementContentEntity();
// contentEn.setId(UUID.randomUUID().toString());
// contentEn.setAnnouncementId(announcementId.toString());
// contentEn.setLanguageCode("en");
// contentEn.setMessage("Announcement body");
// final List<AnnouncementContentEntity> contents = new
// ArrayList<>(List.of(contentJa, contentEn));

// AnnouncementHistoryEntity history1 = new AnnouncementHistoryEntity();
// history1.setId(UUID.randomUUID().toString());
// AnnouncementHistoryEntity history2 = new AnnouncementHistoryEntity();
// history2.setId(UUID.randomUUID().toString());
// final List<AnnouncementHistoryEntity> histories = List.of(history1,
// history2);

// AnnouncementContentHistoryEntity contentHistoryJa1 = new
// AnnouncementContentHistoryEntity();
// contentHistoryJa1.setId(UUID.randomUUID().toString());
// contentHistoryJa1.setAnnouncementHistoryId(history1.getId());
// contentHistoryJa1.setLanguageCode("ja");
// contentHistoryJa1.setMessage("履歴本文1");
// AnnouncementContentHistoryEntity contentHistoryEn1 = new
// AnnouncementContentHistoryEntity();
// contentHistoryEn1.setId(UUID.randomUUID().toString());
// contentHistoryEn1.setAnnouncementHistoryId(history1.getId());
// contentHistoryEn1.setLanguageCode("en");
// contentHistoryEn1.setMessage("履歴本文2");
// AnnouncementContentHistoryEntity contentHistoryJa2 = new
// AnnouncementContentHistoryEntity();
// contentHistoryJa2.setId(UUID.randomUUID().toString());
// contentHistoryJa2.setAnnouncementHistoryId(history2.getId());
// contentHistoryJa2.setLanguageCode("ja");
// contentHistoryJa2.setMessage("履歴本文3");
// List<AnnouncementContentHistoryEntity> contentHistories1 = new ArrayList<>(
// List.of(contentHistoryJa1, contentHistoryEn1));
// List<AnnouncementContentHistoryEntity> contentHistories2 = new
// ArrayList<>(List.of(contentHistoryJa2));

// when(announcementRepository.findByAnnouncementId(announcementId)).thenReturn(announcement);
// when(contentRepository.findByAnnouncementId(announcementId)).thenReturn(contents);
// when(historyRepository.findByAnnouncementId(announcementId)).thenReturn(histories);
// when(contentHistoryRepository.findByAnnouncementHistoryId(UUID.fromString(history1.getId())))
// .thenReturn(contentHistories1);
// when(contentHistoryRepository.findByAnnouncementHistoryId(UUID.fromString(history2.getId())))
// .thenReturn(contentHistories2);

// // Act
// var result = service.getAnnouncementAndHistoryById(announcementId);

// // Assert
// assertNotNull(result);
// assertEquals(announcement, result.getAnnouncement());
// assertEquals(2, result.getContents().size());
// assertEquals(2, result.getHistoryWithContentHistories().size());
// assertEquals(2,
// result.getHistoryWithContentHistories().get(0).getContentHistories().size());
// assertEquals(1,
// result.getHistoryWithContentHistories().get(1).getContentHistories().size());
// }
// }
