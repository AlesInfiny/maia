package com.dressca.applicationcore.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.dressca.applicationcore.config.ApplicationCoreTestConfig;

/**
 * {@link CatalogDomainService}の動作をテストするクラスです。
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(ApplicationCoreTestConfig.class)
public class CatalogDomainServiceTest {
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogBrandRepository catalogBrandRepository;
  @Mock
  private CatalogCategoryRepository catalogCategoryRepository;
  @InjectMocks
  private CatalogDomainService service;

  @Test
  void testGetExistCatalogItems_正常系_リポジトリのfindByCategoryIdInを1度だけ呼出す() {
    // Arrange
    UUID firstCatalogItemId = UUID.randomUUID();
    UUID secondCatalogItemId = UUID.randomUUID();
    List<UUID> catalogItemIds = List.of(firstCatalogItemId, secondCatalogItemId);
    List<CatalogItem> catalogItems = catalogItemIds.stream().map(this::createCatalogItem).toList();
    when(this.catalogRepository.findByCatalogItemIdIn(catalogItemIds)).thenReturn(catalogItems);

    // Act
    service.getExistCatalogItems(catalogItemIds);

    // Assert
    verify(this.catalogRepository, times(1)).findByCatalogItemIdIn(catalogItemIds);
  }

  @Test
  void testExistAll_正常系_リポジトリ内に存在するアイテムのリストを返す() {
    // Arrange
    UUID firstCatalogItemId = UUID.randomUUID();
    UUID secondCatalogItemId = UUID.randomUUID();
    List<UUID> requestedCatalogItemIds = List.of(firstCatalogItemId, secondCatalogItemId);
    List<CatalogItem> catalogItems = List.of(createCatalogItem(secondCatalogItemId));
    when(this.catalogRepository.findByCatalogItemIdIn(requestedCatalogItemIds))
        .thenReturn(catalogItems);

    // Act
    List<CatalogItem> actualItems = service.getExistCatalogItems(requestedCatalogItemIds);

    // Assert
    assertThat(actualItems).hasSize(1);
    assertThat(actualItems.get(0).getId()).isEqualTo(secondCatalogItemId);
  }

  @Test
  void testExistAll_正常系_リポジトリのfindByCategoryIdInを1度だけ呼出す() {
    // Arrange
    UUID firstCatalogItemId = UUID.randomUUID();
    UUID secondCatalogItemId = UUID.randomUUID();
    List<UUID> catalogItemIds = List.of(firstCatalogItemId, secondCatalogItemId);
    List<CatalogItem> catalogItems = catalogItemIds.stream().map(this::createCatalogItem).toList();
    when(this.catalogRepository.findByCatalogItemIdIn(catalogItemIds)).thenReturn(catalogItems);

    // Act
    service.existAll(catalogItemIds);

    // Assert
    verify(this.catalogRepository, times(1)).findByCatalogItemIdIn(catalogItemIds);
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdがすべて存在する場合trueを返す() {
    // Arrange
    UUID firstCatalogItemId = UUID.randomUUID();
    UUID secondCatalogItemId = UUID.randomUUID();
    List<UUID> catalogItemIds = List.of(firstCatalogItemId, secondCatalogItemId);
    List<CatalogItem> catalogItems = catalogItemIds.stream().map(this::createCatalogItem).toList();
    when(this.catalogRepository.findByCatalogItemIdIn(catalogItemIds)).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(catalogItemIds);

    // Assert
    assertThat(existAll).isTrue();
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdが一部だけ存在する場合falseを返す() {
    // Arrange
    UUID firstCatalogItemId = UUID.randomUUID();
    UUID secondCatalogItemId = UUID.randomUUID();
    List<UUID> requestedCatalogItemIds = List.of(firstCatalogItemId, secondCatalogItemId);
    List<CatalogItem> catalogItems = List.of(createCatalogItem(secondCatalogItemId));
    when(this.catalogRepository.findByCatalogItemIdIn(requestedCatalogItemIds))
        .thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(requestedCatalogItemIds);

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistAll_正常系_カタログアイテムIdが1件も存在しない場合falseを返す() {
    // Arrange
    UUID firstCatalogItemId = UUID.randomUUID();
    UUID secondCatalogItemId = UUID.randomUUID();
    List<UUID> requestedCatalogItemIds = List.of(firstCatalogItemId, secondCatalogItemId);
    when(this.catalogRepository.findByCatalogItemIdIn(requestedCatalogItemIds))
        .thenReturn(List.of());

    // Act
    boolean existAll = service.existAll(requestedCatalogItemIds);

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testExistCatalogBrand_正常系_指定したカタログブランドが存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    CatalogBrand catalogBrand = this.createCatalogBrand(targetId);
    when(this.catalogBrandRepository.findById(targetId)).thenReturn(catalogBrand);

    // Act
    boolean existCatalogBrand = service.existCatalogBrand(targetId);

    // Assert
    assertThat(existCatalogBrand).isTrue();
  }

  @Test
  void testExistCatalogBrand_正常系_指定したカタログブランドが存在しない場合falseを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.catalogBrandRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogBrand = service.existCatalogBrand(targetId);

    // Assert
    assertThat(existCatalogBrand).isFalse();
  }

  @Test
  void testExistCatalogCategory_正常系_指定したカタログカテゴリが存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    CatalogCategory catalogCategory = this.createCatalogCategory(targetId);
    when(this.catalogCategoryRepository.findById(targetId)).thenReturn(catalogCategory);

    // Act
    boolean existCatalogCategory = service.existCatalogCategory(targetId);

    // Assert
    assertThat(existCatalogCategory).isTrue();
  }

  @Test
  void testExistCatalogCategory_正常系_指定したカタログカテゴリが存在しない場合falseを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
    when(this.catalogCategoryRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogCategory = service.existCatalogCategory(targetId);

    // Assert
    assertThat(existCatalogCategory).isFalse();
  }

  @Test
  void testExistCatalogItem_正常系_指定したカタログアイテムが存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
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
    UUID targetId = UUID.randomUUID();
    when(this.catalogRepository.findById(targetId)).thenReturn(null);

    // Act
    boolean existCatalogItem = service.existCatalogItem(targetId);

    // Assert
    assertThat(existCatalogItem).isFalse();
  }

  @Test
  void testExistCatalogItemIncludingDeleted_正常系_指定したカタログアイテムが存在する場合trueを返す() {
    // Arrange
    UUID targetId = UUID.randomUUID();
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
    UUID targetId = UUID.randomUUID();
    when(this.catalogRepository.findByIdIncludingDeleted(targetId)).thenReturn(null);

    // Act
    boolean existCatalogItem = service.existCatalogItemIncludingDeleted(targetId);

    // Assert
    assertThat(existCatalogItem).isFalse();
  }

  private CatalogItem createCatalogItem(UUID id) {
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    CatalogItem catalogItem = new CatalogItem(id, defaultName, defaultDescription, defaultPrice,
        defaultProductCode, UUID.randomUUID(), UUID.randomUUID(), defaultIsDeleted);
    // catalogItem.setId(id);
    return catalogItem;
  }

  private CatalogBrand createCatalogBrand(UUID id) {
    CatalogBrand catalogBrand = new CatalogBrand("Name");
    catalogBrand.setId(id);
    return catalogBrand;
  }

  private CatalogCategory createCatalogCategory(UUID id) {
    CatalogCategory catalogCategory = new CatalogCategory("Name");
    catalogCategory.setId(id);
    return catalogCategory;
  }
}
