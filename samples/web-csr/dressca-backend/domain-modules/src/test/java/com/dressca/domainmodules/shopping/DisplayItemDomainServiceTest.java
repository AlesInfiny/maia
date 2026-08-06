package com.dressca.domainmodules.shopping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.domainmodules.common.config.DomainModulesTestConfig;
import com.dressca.domainmodules.shopping.internal.domain.DisplayItemDomainService;
import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemRepository;
import com.dressca.domainmodules.shopping.models.DisplayItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link DisplayItemDomainService}の動作をテストするクラスです。
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(DomainModulesTestConfig.class)
@TestPropertySource(properties = "spring.messages.basename=domainmodules.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class DisplayItemDomainServiceTest {
  @Mock
  private DisplayItemRepository displayItemRepository;
  @InjectMocks
  private DisplayItemDomainService displayItemDomainService;

  @Test
  void testGetExistDisplayItems_正常系_リポジトリのfindByDisplayItemIdInを1度だけ呼出す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> displayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = displayItemIds.stream().map(this::createDisplayItem).toList();
    when(this.displayItemRepository.findByDisplayItemIdIn(displayItemIds)).thenReturn(displayItems);

    // Act
    displayItemDomainService.getExistDisplayItems(displayItemIds);

    // Assert
    verify(this.displayItemRepository, times(1)).findByDisplayItemIdIn(displayItemIds);
  }

  @Test
  void testGetExistDisplayItems_正常系_リポジトリ内に存在するアイテムのリストを返す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> requestedDisplayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = List.of(createDisplayItem(secondDisplayItemId));
    when(this.displayItemRepository.findByDisplayItemIdIn(requestedDisplayItemIds))
        .thenReturn(displayItems);

    // Act
    List<DisplayItem> actualItems =
        displayItemDomainService.getExistDisplayItems(requestedDisplayItemIds);

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
    when(this.displayItemRepository.findByDisplayItemIdIn(displayItemIds)).thenReturn(displayItems);

    // Act
    displayItemDomainService.existAll(displayItemIds);

    // Assert
    verify(this.displayItemRepository, times(1)).findByDisplayItemIdIn(displayItemIds);
  }

  @Test
  void testExistAll_正常系_陳列品Idがすべて存在する場合trueを返す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> displayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = displayItemIds.stream().map(this::createDisplayItem).toList();
    when(this.displayItemRepository.findByDisplayItemIdIn(displayItemIds)).thenReturn(displayItems);

    // Act
    boolean existAll = displayItemDomainService.existAll(displayItemIds);

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
    when(this.displayItemRepository.findByDisplayItemIdIn(requestedDisplayItemIds))
        .thenReturn(displayItems);

    // Act
    boolean existAll = displayItemDomainService.existAll(requestedDisplayItemIds);

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistAll_正常系_陳列品Idが1件も存在しない場合falseを返す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> requestedDisplayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    when(this.displayItemRepository.findByDisplayItemIdIn(requestedDisplayItemIds))
        .thenReturn(List.of());

    // Act
    boolean existAll = displayItemDomainService.existAll(requestedDisplayItemIds);

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistDisplayItemIncludingDeleted_正常系_指定した陳列品が存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.displayItemRepository.findByDisplayItemIdInIncludingDeleted(List.of(targetId)))
        .thenReturn(List.of(createDisplayItem(targetId)));

    // Act
    boolean existDisplayItem = displayItemDomainService.existDisplayItemIncludingDeleted(targetId);

    // Assert
    assertThat(existDisplayItem).isTrue();
  }

  @Test
  void testExistDisplayItemIncludingDeleted_正常系_指定した陳列品が存在しない場合falseを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.displayItemRepository.findByDisplayItemIdInIncludingDeleted(List.of(targetId)))
        .thenReturn(List.of());

    // Act
    boolean existDisplayItem = displayItemDomainService.existDisplayItemIncludingDeleted(targetId);

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
