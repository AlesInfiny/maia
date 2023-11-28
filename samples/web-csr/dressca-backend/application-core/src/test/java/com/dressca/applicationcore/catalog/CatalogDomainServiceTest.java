package com.dressca.applicationcore.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link CatalogDomainService}の動作をテストするクラスです。
 */
@ExtendWith(SpringExtension.class)
public class CatalogDomainServiceTest {
  @Mock
  private CatalogRepository repository;
  @InjectMocks
  private CatalogDomainService service;

  @Test
  void testGetExistCatalogItems_正常系_リポジトリのfindByCategoryIdInを1度だけ呼出す() {
    // Arrange
    long[] catalogItemIds = { 1L, 2L };
    List<Long> catalogItemIdsList = Arrays.asList(Arrays.stream(catalogItemIds).boxed().toArray(Long[]::new));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.repository.findByCatalogItemIdIn(catalogItemIdsList)).thenReturn(catalogItems);

    // Act
    service.getExistCatalogItems(catalogItemIdsList);

    // Assert
    verify(this.repository, times(1)).findByCatalogItemIdIn(catalogItemIdsList);
  }

  @Test
  void testExistAll_正常系_リポジトリ内に存在するアイテムのリストを返す() {
    // Arrange
    long[] catalogItemIds = { 2L };
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.repository.findByCatalogItemIdIn(List.of(1L, 2L))).thenReturn(catalogItems);

    // Act
    List<CatalogItem> actualItems = service.getExistCatalogItems(List.of(1L, 2L));

    // Assert
    assertThat(actualItems.size()).isEqualTo(1);
    assertThat(actualItems.get(0).getId()).isEqualTo(2L);
  }

  @Test
  void testExistAll_正常系_リポジトリのfindByCategoryIdInを1度だけ呼出す() {
    // Arrange
    long[] catalogItemIds = { 1L, 2L };
    List<Long> catalogItemIdsList = Arrays.asList(Arrays.stream(catalogItemIds).boxed().toArray(Long[]::new));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.repository.findByCatalogItemIdIn(catalogItemIdsList)).thenReturn(catalogItems);

    // Act
    service.existAll(catalogItemIdsList);

    // Assert
    verify(this.repository, times(1)).findByCatalogItemIdIn(catalogItemIdsList);
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdがすべて存在する場合trueを返す() {
    // Arrange
    long[] catalogItemIds = { 1L, 2L };
    List<Long> catalogItemIdsList = Arrays.asList(Arrays.stream(catalogItemIds).boxed().toArray(Long[]::new));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.repository.findByCatalogItemIdIn(catalogItemIdsList)).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(List.of(1L, 2L));

    // Assert
    assertThat(existAll).isTrue();
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdが一部だけ存在する場合falseを返す() {
    // Arrange
    long[] catalogItemIds = { 2L };
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.repository.findByCatalogItemIdIn(List.of(1L, 2L))).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(List.of(1L, 2L));

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdが1件も存在しない場合falseを返す() {
    // Arrange
    long[] catalogItemIds = {};
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.repository.findByCatalogItemIdIn(List.of(1L, 2L))).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(List.of(1L, 2L));

    // Assert
    assertThat(existAll).isFalse();
  }

  private CatalogItem createCatalogItem(long id) {
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";

    CatalogItem catalogItem = new CatalogItem(id, defaultName, defaultDescription, defaultPrice,
        defaultProductCode, defaultCatalogCategoryId, defaultCatalogBrandId);
    // catalogItem.setId(id);
    return catalogItem;
  }
}
