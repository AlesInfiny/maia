package com.dressca.applicationcore.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.systemcommon.exception.OptimisticLockingFailureException;

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

  @Test
  void testGetExistCatalogItems_正常系_リポジトリのfindByCategoryIdInを1度だけ呼出す() {
    // Arrange
    long[] catalogItemIds = { 1L, 2L };
    List<Long> catalogItemIdsList = Arrays.asList(ArrayUtils.toObject(catalogItemIds));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
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
    long[] catalogItemIds = { 2L };
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
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
    long[] catalogItemIds = { 1L, 2L };
    List<Long> catalogItemIdsList = Arrays.asList(ArrayUtils.toObject(catalogItemIds));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
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
    long[] catalogItemIds = { 1L, 2L };
    List<Long> catalogItemIdsList = Arrays.asList(ArrayUtils.toObject(catalogItemIds));
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
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
    long[] catalogItemIds = { 2L };
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
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
    List<CatalogItem> catalogItems = Arrays.stream(catalogItemIds).mapToObj(this::createCatalogItem)
        .collect(Collectors.toList());
    when(this.catalogRepository.findByCatalogItemIdIn(List.of(1L, 2L))).thenReturn(catalogItems);

    // Act
    boolean existAll = service.existAll(List.of(1L, 2L));

    // Assert
    assertThat(existAll).isFalse();
  }

  @Test
  void testGetCatalogItemById_正常系_リポジトリのfindByIdを1回呼出す() throws CatalogNotFoundException {
    // Arrange
    CatalogItem item = createCatalogItem(1L);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);

    // Act
    this.service.getCatalogItemById(1L);

    // Assert
    verify(this.catalogRepository, times(1)).findById(anyLong());
  }

  @Test
  void testGetCatalogItemById_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;
    when(this.catalogRepository.findById(targetId)).thenReturn(null);

    // Act
    Executable action = () -> {
      this.service.getCatalogItemById(targetId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testGetCatalogItemsByConditions_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す() {
    // Action
    this.service.getCatalogItemsByConditions(1L, 1L, 0, 20);

    // Assert
    verify(this.catalogRepository, times(1)).findByBrandIdAndCategoryId(anyLong(), anyLong(), anyInt(), anyInt());
  }

  @Test
  void testAddCatalogItem_正常系_リポジトリのaddを1回呼出す() {

    // Action
    this.service.addCatalogItem("Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L, 1L);

    // Assert
    verify(this.catalogRepository, times(1)).add(any());
  }

  @Test
  void testDeleteCatalogItemById_正常系_リポジトリのremoveを1回呼出す() throws CatalogNotFoundException {
    // Arrange
    CatalogItem item = createCatalogItem(1L);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);

    // Action
    this.service.deleteCatalogItemById(1L);

    // Assert
    verify(this.catalogRepository, times(1)).remove(any());
  }

  @Test
  void testDeleteCatalogItemById_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;
    when(this.catalogRepository.findById(targetId)).thenReturn(null);

    // Action
    Executable action = () -> {
      this.service.deleteCatalogItemById(targetId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_正常系_リポジトリのupdateを1回呼出す() throws CatalogNotFoundException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    // Arrange
    CatalogItem item = createCatalogItem(1L);
    CatalogCategory category = createCatalogCategory();
    CatalogBrand brand = createCatalogBrand();
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(brand);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    this.service.modifyCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L, 1L);

    // Assert
    verify(this.catalogRepository, times(1)).update(any());
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のアイテムが存在しない() {
    // Arrange
    CatalogCategory category = createCatalogCategory();
    CatalogBrand brand = createCatalogBrand();
    when(this.catalogRepository.findById(anyLong())).thenReturn(null);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(brand);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    Executable action = () -> {
      this.service.modifyCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L,
          1L);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のカテゴリが存在しない() {
    // Arrange
    CatalogItem item = createCatalogItem(1L);
    CatalogBrand brand = createCatalogBrand();
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(null);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(brand);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    Executable action = () -> {
      this.service.modifyCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L,
          1L);
    };

    // Assert
    assertThrows(CatalogCategoryNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のブランドが存在しない() {
    // Arrange
    CatalogItem item = createCatalogItem(1L);
    CatalogCategory category = createCatalogCategory();
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(null);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    Executable action = () -> {
      this.service.modifyCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L,
          1L);
    };

    // Assert
    assertThrows(CatalogBrandNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_楽観ロックエラーが発生() {
    // Arrange
    CatalogItem item = createCatalogItem(1L);
    CatalogCategory category = createCatalogCategory();
    CatalogBrand brand = createCatalogBrand();
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(brand);
    when(this.catalogRepository.update(any())).thenReturn(0);

    // Action
    Executable action = () -> {
      this.service.modifyCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L,
          1L);
    };

    // Assert
    assertThrows(OptimisticLockingFailureException.class, action);
  }

  private CatalogItem createCatalogItem(long id) {
    long defaultCatalogCategoryId = 1L;
    long defaultCatalogBrandId = 1L;
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";

    CatalogItem catalogItem = new CatalogItem(id, defaultName, defaultDescription, defaultPrice,
        defaultProductCode, defaultCatalogCategoryId, defaultCatalogBrandId);
    // catalogItem.setId(id);
    return catalogItem;
  }

  private CatalogCategory createCatalogCategory() {
    String defaultName = "Name";
    CatalogCategory catalogCategory = new CatalogCategory(defaultName);
    return catalogCategory;
  }

  private CatalogBrand createCatalogBrand() {
    String defaultName = "Name";
    CatalogBrand catalogBrand = new CatalogBrand(defaultName);
    return catalogBrand;
  }

}
