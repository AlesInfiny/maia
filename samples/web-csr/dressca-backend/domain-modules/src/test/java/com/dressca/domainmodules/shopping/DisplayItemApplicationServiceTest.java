package com.dressca.domainmodules.shopping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.domainmodules.common.config.DomainModulesTestConfig;
import com.dressca.domainmodules.common.log.AbstractStructuredLogger;
import com.dressca.domainmodules.shopping.displayitem.DisplayItem;
import com.dressca.domainmodules.shopping.displayitem.DisplayItemBrand;
import com.dressca.domainmodules.shopping.displayitem.DisplayItemCategory;
import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemBrandRepository;
import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemCategoryRepository;
import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemRepository;
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
 * {@link DisplayItemApplicationService}の動作をテストするクラスです。
 */
@Import(DomainModulesTestConfig.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(properties = "spring.messages.basename=domainmodules.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class DisplayItemApplicationServiceTest {
  @Mock
  private DisplayItemRepository displayItemRepository;
  @Mock
  private DisplayItemBrandRepository displayItemBrandRepository;
  @Mock
  private DisplayItemCategoryRepository displayItemCategoryRepository;
  @Mock
  private AbstractStructuredLogger apLog;

  @Autowired
  private MessageSource messages;

  private DisplayItemApplicationService service;

  @BeforeEach
  void setUp() {
    service = new DisplayItemApplicationService(messages, displayItemRepository,
        displayItemBrandRepository, displayItemCategoryRepository, apLog);
  }

  @Test
  void testGetDisplayItems_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 1;
    int pageSize = 20;

    // Act
    service.getDisplayItems(brandId, categoryId, page, pageSize);

    // Assert
    verify(this.displayItemRepository, times(1)).findByBrandIdAndCategoryId(brandId, categoryId,
        page, pageSize);
  }

  @Test
  void testGetDisplayItems_正常系_指定した条件の陳列品のリストが返却される() {
    // Arrange
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 1;
    int pageSize = 20;
    UUID targetId = UUID.randomUUID();
    DisplayItem displayItem = createDisplayItem(targetId);
    List<DisplayItem> expectedDisplayItemList = new ArrayList<>(Arrays.asList(displayItem));
    when(this.displayItemRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize))
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
    when(this.displayItemRepository.countByBrandIdAndCategoryId(any(), any())).thenReturn(1);

    // Act
    service.countDisplayItems(brandId, categoryId);

    // Assert
    verify(this.displayItemRepository, times(1)).countByBrandIdAndCategoryId(any(), any());
  }

  @Test
  void testGetBrands_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<DisplayItemBrand> brands = List.of(new DisplayItemBrand("dummy"));
    when(this.displayItemBrandRepository.getAll()).thenReturn(brands);

    // Act
    service.getBrands();

    // Assert
    verify(this.displayItemBrandRepository, times(1)).getAll();
  }

  @Test
  void testGetCategories_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<DisplayItemCategory> categories = List.of(new DisplayItemCategory("dummy"));
    when(this.displayItemCategoryRepository.getAll()).thenReturn(categories);

    // Act
    service.getCategories();

    // Assert
    verify(this.displayItemCategoryRepository, times(1)).getAll();
  }

  private DisplayItem createDisplayItem(UUID id) {
    UUID defaultDisplayItemBrandId = UUID.randomUUID();
    UUID defaultDisplayItemCategoryId = UUID.randomUUID();
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    return new DisplayItem(id, defaultName, defaultDescription, defaultPrice, defaultProductCode,
        defaultDisplayItemCategoryId, defaultDisplayItemBrandId, defaultIsDeleted);
  }
}
