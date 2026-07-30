package com.dressca.applicationcore.display;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.applicationcore.config.ApplicationCoreTestConfig;
import com.dressca.applicationcore.displayitem.DisplayDomainService;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link DisplayDomainService}の動作をテストするクラスです。
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(ApplicationCoreTestConfig.class)
public class DisplayDomainServiceTest {
  @Mock
  private DisplayRepository displayRepository;
  @InjectMocks
  private DisplayDomainService service;

  @Test
  void testGetExistDisplayItems_正常系_リポジトリのfindByDisplayItemIdInを1度だけ呼出す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> displayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = displayItemIds.stream().map(this::createDisplayItem).toList();
    when(this.displayRepository.findByDisplayItemIdIn(displayItemIds)).thenReturn(displayItems);

    // Act
    service.getExistDisplayItems(displayItemIds);

    // Assert
    verify(this.displayRepository, times(1)).findByDisplayItemIdIn(displayItemIds);
  }

  @Test
  void testGetExistDisplayItems_正常系_リポジトリ内に存在するアイテムのリストを返す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> requestedDisplayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = List.of(createDisplayItem(secondDisplayItemId));
    when(this.displayRepository.findByDisplayItemIdIn(requestedDisplayItemIds))
        .thenReturn(displayItems);

    // Act
    List<DisplayItem> actualItems = service.getExistDisplayItems(requestedDisplayItemIds);

    // Assert
    assertThat(actualItems).hasSize(1);
    assertThat(actualItems.get(0).getId()).isEqualTo(secondDisplayItemId);
  }

  @Test
  void testExistAll_正常系_リポジトリのfindByDisplayItemIdInを1度だけ呼出す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> displayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = displayItemIds.stream().map(this::createDisplayItem).toList();
    when(this.displayRepository.findByDisplayItemIdIn(displayItemIds)).thenReturn(displayItems);

    // Act
    service.existAll(displayItemIds);

    // Assert
    verify(this.displayRepository, times(1)).findByDisplayItemIdIn(displayItemIds);
  }

  @Test
  void testExistAll_正常系_陳列品Idがすべて存在する場合trueを返す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> displayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = displayItemIds.stream().map(this::createDisplayItem).toList();
    when(this.displayRepository.findByDisplayItemIdIn(displayItemIds)).thenReturn(displayItems);

    // Act
    boolean existAll = service.existAll(displayItemIds);

    // Assert
    assertThat(existAll).isTrue();
  }

  @Test
  void testExistAll_正常系_陳列品Idが一部だけ存在する場合falseを返す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> requestedDisplayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = List.of(createDisplayItem(secondDisplayItemId));
    when(this.displayRepository.findByDisplayItemIdIn(requestedDisplayItemIds))
        .thenReturn(displayItems);

    // Act
    boolean existAll = service.existAll(requestedDisplayItemIds);

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistAll_正常系_陳列品Idが1件も存在しない場合falseを返す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> requestedDisplayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    when(this.displayRepository.findByDisplayItemIdIn(requestedDisplayItemIds))
        .thenReturn(List.of());

    // Act
    boolean existAll = service.existAll(requestedDisplayItemIds);

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistDisplayItemIncludingDeleted_正常系_指定した陳列品が存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.displayRepository.findByDisplayItemIdInIncludingDeleted(List.of(targetId)))
        .thenReturn(List.of(createDisplayItem(targetId)));

    // Act
    boolean existDisplayItem = service.existDisplayItemIncludingDeleted(targetId);

    // Assert
    assertThat(existDisplayItem).isTrue();
  }

  @Test
  void testExistDisplayItemIncludingDeleted_正常系_指定した陳列品が存在しない場合falseを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.displayRepository.findByDisplayItemIdInIncludingDeleted(List.of(targetId)))
        .thenReturn(List.of());

    // Act
    boolean existDisplayItem = service.existDisplayItemIncludingDeleted(targetId);

    // Assert
    assertThat(existDisplayItem).isFalse();
  }

  private DisplayItem createDisplayItem(UUID id) {
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    return new DisplayItem(id, defaultName, defaultDescription, defaultPrice, defaultProductCode,
        UUID.randomUUID(), UUID.randomUUID(), defaultIsDeleted);
  }
}
