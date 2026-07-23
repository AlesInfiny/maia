package com.dressca.applicationcore.applicationservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.applicationcore.config.ApplicationCoreTestConfig;
import com.dressca.applicationcore.display.DisplayBrand;
import com.dressca.applicationcore.display.DisplayBrandRepository;
import com.dressca.applicationcore.display.DisplayCategory;
import com.dressca.applicationcore.display.DisplayCategoryRepository;
import com.dressca.applicationcore.display.DisplayItem;
import com.dressca.applicationcore.display.DisplayRepository;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link DisplayApplicationService}の動作をテストするクラスです。
 */
@Import(ApplicationCoreTestConfig.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(properties = "spring.messages.basename=applicationcore.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class DisplayApplicationServiceTest {
  @Mock
  private DisplayRepository displayRepository;
  @Mock
  private DisplayBrandRepository brandRepository;
  @Mock
  private DisplayCategoryRepository categoryRepository;
  @Mock
  private AbstractStructuredLogger apLog;

  @Autowired
  private MessageSource messages;

  private DisplayApplicationService service;

  @BeforeEach
  void setUp() {
    service = new DisplayApplicationService(messages, displayRepository, brandRepository,
        categoryRepository, apLog);
  }

  @Test
  void testGetDisplayItems_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 0;
    int pageSize = 20;

    // Act
    service.getDisplayItems(brandId, categoryId, page, pageSize);

    // Assert
    verify(this.displayRepository, times(1)).findByBrandIdAndCategoryId(brandId, categoryId, page,
        pageSize);
  }

  @Test
  void testGetDisplayItems_正常系_指定した条件の陳列品のリストが返却される() {
    // Arrange
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 0;
    int pageSize = 20;
    UUID targetId = UUID.randomUUID();
    DisplayItem displayItem = createDisplayItem(targetId);
    List<DisplayItem> expectedDisplayItemList = new ArrayList<>(Arrays.asList(displayItem));
    when(this.displayRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize))
        .thenReturn(expectedDisplayItemList);

    // Act
    List<DisplayItem> actualDisplayItemList =
        service.getDisplayItems(brandId, categoryId, page, pageSize);

    // Assert
    assertThat(actualDisplayItemList).isEqualTo(expectedDisplayItemList);
  }

  @Test
  void testCountDisplayItems_正常系_リポジトリのcountByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.displayRepository.countByBrandIdAndCategoryId(any(), any())).thenReturn(1);

    // Act
    service.countDisplayItems(brandId, categoryId);

    // Assert
    verify(this.displayRepository, times(1)).countByBrandIdAndCategoryId(any(), any());
  }

  @Test
  void testGetDisplayItemsByIds_正常系_リポジトリのfindByDisplayItemIdInを1回呼出す() {
    // Arrange
    UUID firstDisplayItemId = UUID.randomUUID();
    UUID secondDisplayItemId = UUID.randomUUID();
    List<UUID> displayItemIds = List.of(firstDisplayItemId, secondDisplayItemId);
    List<DisplayItem> displayItems = displayItemIds.stream().map(this::createDisplayItem).toList();
    when(this.displayRepository.findByDisplayItemIdIn(displayItemIds)).thenReturn(displayItems);

    // Act
    List<DisplayItem> actual = service.getDisplayItemsByIds(displayItemIds);

    // Assert
    assertThat(actual).isEqualTo(displayItems);
    verify(this.displayRepository, times(1)).findByDisplayItemIdIn(displayItemIds);
  }

  @Test
  void testGetBrands_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<DisplayBrand> brands = List.of(new DisplayBrand("dummy"));
    when(this.brandRepository.getAll()).thenReturn(brands);

    // Act
    service.getBrands();

    // Assert
    verify(this.brandRepository, times(1)).getAll();
  }

  @Test
  void testGetCategories_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<DisplayCategory> categories = List.of(new DisplayCategory("dummy"));
    when(this.categoryRepository.getAll()).thenReturn(categories);

    // Act
    service.getCategories();

    // Assert
    verify(this.categoryRepository, times(1)).getAll();
  }

  private DisplayItem createDisplayItem(UUID id) {
    UUID defaultDisplayBrandId = UUID.randomUUID();
    UUID defaultDisplayCategoryId = UUID.randomUUID();
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    return new DisplayItem(id, defaultName, defaultDescription, defaultPrice,
        defaultProductCode, defaultDisplayCategoryId, defaultDisplayBrandId, defaultIsDeleted);
  }
}
