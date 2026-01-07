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
import org.apache.commons.lang3.ArrayUtils;
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
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogBrandRepository catalogBrandRepository;
  @Mock
  private CatalogCategoryRepository catalogCategoryRepository;
  @InjectMocks
  private CatalogDomainService service;

  private static final Random random = new Random();

  @Test
  void testGetExistCatalogItems_正常系_リポジトリのfindByCategoryIdInを1度だけ呼出す() {
    // Arrange
    long[] catalogItemIds = {1L, 2L};
    List<Long> catalogItemIdsList = Arrays.asList(ArrayUtils.toObject(catalogItemIds));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds)
        .mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.catalogRepository.findByCatalogItemIdIn(catalogItemIdsList)).thenReturn(catalogItems);

    // Act
    service.getExistCatalogItems(catalogItemIdsList);

    // Assert
    verify(this.catalogRepository, times(1)).findByCatalogItemIdIn(catalogItemIdsList);
  }

  @Test
  void testExistAll_正常系_リポジトリ内に存在するアイテムのリストを返す() {
    // Arrange
    long[] catalogItemIds = {2L};
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds)
        .mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.catalogRepository.findByCatalogItemIdIn(List.of(1L, 2L))).thenReturn(catalogItems);

    // Act
    List<CatalogItem> actualItems = service.getExistCatalogItems(List.of(1L, 2L));

    // Assert
    assertThat(actualItems.size()).isEqualTo(1);
    assertThat(actualItems.get(0).getId()).isEqualTo(2L);
  }

  @Test
  void testExistAll_正常系_リポジトリのfindByCategoryIdInを1度だけ呼出す() {
    // Arrange
    long[] catalogItemIds = {1L, 2L};
    List<Long> catalogItemIdsList = Arrays.asList(ArrayUtils.toObject(catalogItemIds));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds)
        .mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.catalogRepository.findByCatalogItemIdIn(catalogItemIdsList)).thenReturn(catalogItems);

    // Act
    service.existAll(catalogItemIdsList);

    // Assert
    verify(this.catalogRepository, times(1)).findByCatalogItemIdIn(catalogItemIdsList);
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdがすべて存在する場合trueを返す() {
    // Arrange
    long[] catalogItemIds = {1L, 2L};
    List<Long> catalogItemIdsList = Arrays.asList(ArrayUtils.toObject(catalogItemIds));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds)
        .mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.catalogRepository.findByCatalogItemIdIn(catalogItemIdsList)).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(List.of(1L, 2L));

    // Assert
    assertThat(existAll).isTrue();
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdが一部だけ存在する場合falseを返す() {
    // Arrange
    long[] catalogItemIds = {2L};
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds)
        .mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.catalogRepository.findByCatalogItemIdIn(List.of(1L, 2L))).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(List.of(1L, 2L));

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdが1件も存在しない場合falseを返す() {
    // Arrange
    long[] catalogItemIds = {};
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds)
        .mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.catalogRepository.findByCatalogItemIdIn(List.of(1L, 2L))).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(List.of(1L, 2L));

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistCatalogBrand_正常系_指定したカタログブランドが存在する場合trueを返す() {
    // Arrange
    long targetId = 1L;
    CatalogBrand catalogBrand = this.createCatalogBrand();
    when(this.catalogBrandRepository.findById(targetId)).thenReturn(catalogBrand);

    // Act
    boolean existCatalogBrand = service.existCatalogBrand(targetId);

    // Assert
    assertThat(existCatalogBrand).isTrue();
  }

  @Test
  void testExistCatalogBrand_正常系_指定したカタログブランドが存在しない場合falseを返す() {
    // Arrange
    long targetId = 1L;
    when(this.catalogBrandRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogBrand = service.existCatalogBrand(targetId);

    // Assert
    assertThat(existCatalogBrand).isFalse();
  }

  @Test
  void testExistCatalogCategory_正常系_指定したカタログカテゴリが存在する場合trueを返す() {
    // Arrange
    long targetId = 1L;
    CatalogCategory catalogCategory = this.createCatalogCategory();
    when(this.catalogCategoryRepository.findById(targetId)).thenReturn(catalogCategory);

    // Act
    boolean existCatalogCategory = service.existCatalogCategory(targetId);

    // Assert
    assertThat(existCatalogCategory).isTrue();
  }

  @Test
  void testExistCatalogCategory_正常系_指定したカタログカテゴリが存在しない場合falseを返す() {
    // Arrange
    long targetId = 1L;
    when(this.catalogCategoryRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogCategory = service.existCatalogCategory(targetId);

    // Assert
    assertThat(existCatalogCategory).isFalse();
  }

  @Test
  void testExistCatalogItem_正常系_指定したカタログアイテムが存在する場合trueを返す() {
    // Arrange
    long targetId = 1L;
    CatalogItem catalogItem = this.createCatalogItem(targetId);
    when(this.catalogRepository.findById(targetId)).thenReturn(catalogItem);

    // Act
    boolean existCatalogItem = service.existCatalogItem(targetId);

    // Assert
    assertThat(existCatalogItem).isTrue();
  }

  @Test
  void testExistCatalogItem_正常系_指定したカタログアイテムが存在しない場合falseを返す() {
    // Arrange
    long targetId = 1L;
    when(this.catalogRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogItem = service.existCatalogItem(targetId);

    // Assert
    assertThat(existCatalogItem).isFalse();
  }

  @Test
  void testExistCatalogItemIncludingDeleted_正常系_指定したカタログアイテムが存在する場合trueを返す() {
    // Arrange
    long targetId = 1L;
    CatalogItem catalogItem = this.createCatalogItem(targetId);
    when(this.catalogRepository.findByIdIncludingDeleted(targetId)).thenReturn(catalogItem);

    // Act
    boolean existCatalogItem = service.existCatalogItemIncludingDeleted(targetId);

    // Assert
    assertThat(existCatalogItem).isTrue();
  }

  @Test
  void testExistCatalogItemIncludingDeleted_正常系_指定したカタログアイテムが存在しない場合falseを返す() {
    // Arrange
    long targetId = 1L;
    when(this.catalogRepository.findByIdIncludingDeleted(targetId)).thenReturn(null);

    // Act
    boolean existCatalogItem = service.existCatalogItemIncludingDeleted(targetId);

    // Assert
    assertThat(existCatalogItem).isFalse();
  }

  private CatalogItem createCatalogItem(long id) {
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    CatalogItem catalogItem = new CatalogItem(id, defaultName, defaultDescription, defaultPrice,
        defaultProductCode, defaultCatalogCategoryId, defaultCatalogBrandId, defaultIsDeleted);
    // catalogItem.setId(id);
    return catalogItem;
  }

  private CatalogBrand createCatalogBrand() {
    String defaultName = "Name";
    CatalogBrand catalogBrand = new CatalogBrand(defaultName);
    return catalogBrand;
  }

  private CatalogCategory createCatalogCategory() {
    String defaultName = "Name";
    CatalogCategory catalogCategory = new CatalogCategory(defaultName);
    return catalogCategory;
  }
}
