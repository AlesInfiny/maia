package com.dressca.applicationcore.applicationservice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.applicationcore.authorization.PermissionDeniedException;
import com.dressca.applicationcore.authorization.UserStore;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandNotFoundException;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryNotFoundException;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.systemcommon.exception.OptimisticLockingFailureException;

/**
 * {@link CatalogApplicationService}の動作をテストするクラスです。
 */
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class CatalogApplicationServiceTest {
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogBrandRepository brandRepository;
  @Mock
  private CatalogCategoryRepository categoryRepository;
  @Mock
  private UserStore userStore;

  @Autowired
  private MessageSource messages;

  private CatalogApplicationService service;

  @BeforeEach
  void setUp() {
    service = new CatalogApplicationService(messages, catalogRepository, brandRepository, categoryRepository);
    service.setUserStore(this.userStore);
  }

  @Test
  void testGetCatalogItemsByAdmin_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す() throws PermissionDeniedException {
    // Arrange
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    service.getCatalogItemsByAdmin(1L, 1L, 0, 20);

    // Assert
    verify(this.catalogRepository, times(1)).findByBrandIdAndCategoryId(anyLong(), anyLong(), anyInt(), anyInt());
  }

  @Test
  void testGetCatalogItemsByAdmin_異常系_カタログアイテムの一覧を取得する権限がない() {
    // Arrange
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      service.getCatalogItemsByAdmin(1L, 1L, 0, 20);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testGetCatalogItem_正常系_リポジトリのfindByIdを1回呼出す() throws CatalogNotFoundException, PermissionDeniedException {
    // Arrange
    long targetId = 1L;
    CatalogItem catalogItem = createCatalogItem(targetId);
    when(this.catalogRepository.findById(anyLong())).thenReturn(catalogItem);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    service.getCatalogItem(1L);

    // Assert
    verify(this.catalogRepository, times(1)).findById(anyLong());
  }

  @Test
  void testGetCatalogItem_異常系_対象のアイテムが存在しない() {
    // Arrange
    when(this.catalogRepository.findById(anyLong())).thenReturn(null);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    Executable action = () -> {
      this.service.getCatalogItem(anyLong());
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testGetCatalogItem_異常系_カタログアイテムを取得する権限がない() {
    // Arrange
    long targetId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      this.service.getCatalogItem(anyLong());
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testAddItemToCatalog_正常系_リポジトリのaddCatalogItemを1回呼出す() throws PermissionDeniedException {
    // Arrange
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    service.addItemToCatalog("テストアイテム", "テスト用のアイテムです。", new BigDecimal(123456), "TEST001", 1, 1);

    // Assert
    verify(this.catalogRepository, times(1)).add(any());
  }

  @Test
  void testAddItemToCatalog_異常系_カタログアイテムを追加する権限がない() {
    // Arrange
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      service.addItemToCatalog("テストアイテム", "テスト用のアイテムです。", new BigDecimal(123456), "TEST001", 1, 1);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_正常系_リポジトリのremoveを1回呼出す()
      throws CatalogNotFoundException, PermissionDeniedException {
    // Arrange
    long targetId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);

    // Action
    this.service.deleteItemFromCatalog(1L);

    // Assert
    verify(this.catalogRepository, times(1)).remove(any());

  }

  @Test
  void testDeleteItemFromCatalog_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(null);

    // Action
    Executable action = () -> {
      this.service.deleteItemFromCatalog(targetId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_異常系_カタログアイテムを削除する権限がない() {
    // Arrange
    long targetId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    when(this.userStore.isInRole(anyString())).thenReturn(false);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);

    // Action
    Executable action = () -> {
      this.service.deleteItemFromCatalog(1L);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_正常系_リポジトリのupdateを1回呼出す() throws CatalogNotFoundException, PermissionDeniedException,
      CatalogBrandNotFoundException, CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    // Arrange
    long targetId = 1L;
    long brandId = 1L;
    long categoryId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    CatalogBrand brand = createCatalogBrand(brandId);
    CatalogCategory category = createCatalogCategory(categoryId);
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.brandRepository.findById(anyLong())).thenReturn(brand);
    when(this.categoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    this.service.updateCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L, 1L);

    // Assert
    verify(this.catalogRepository, times(1)).update(any());
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;
    long brandId = 1L;
    long categoryId = 1L;
    CatalogBrand brand = createCatalogBrand(brandId);
    CatalogCategory category = createCatalogCategory(categoryId);
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(null);
    when(this.brandRepository.findById(anyLong())).thenReturn(brand);
    when(this.categoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001",
          1L, 1L);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のブランドが存在しない() {
    // Arrange
    long targetId = 1L;
    long categoryId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    CatalogCategory category = createCatalogCategory(categoryId);
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.brandRepository.findById(anyLong())).thenReturn(null);
    when(this.categoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L,
          targetId);
    };

    // Assert
    assertThrows(CatalogBrandNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のカテゴリが存在しない() {
    // Arrange
    long targetId = 1L;
    long brandId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    CatalogBrand brand = createCatalogBrand(brandId);
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.brandRepository.findById(anyLong())).thenReturn(brand);
    when(this.categoryRepository.findById(anyLong())).thenReturn(null);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001",
          targetId, 1L);
    };

    // Assert
    assertThrows(CatalogCategoryNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_カタログアイテムを更新する権限がない() {
    // Arrange
    long targetId = 1L;
    long brandId = 1L;
    long categoryId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    CatalogBrand brand = createCatalogBrand(brandId);
    CatalogCategory category = createCatalogCategory(categoryId);
    when(this.userStore.isInRole(anyString())).thenReturn(false);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.brandRepository.findById(anyLong())).thenReturn(brand);
    when(this.categoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogRepository.update(any())).thenReturn(1);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001",
          1L, 1L);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_楽観ロックエラーにより正常に更新ができない() {
    // Arrange
    long targetId = 1L;
    long brandId = 1L;
    long categoryId = 1L;
    CatalogItem item = createCatalogItem(targetId);
    CatalogBrand brand = createCatalogBrand(brandId);
    CatalogCategory category = createCatalogCategory(categoryId);
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(item);
    when(this.brandRepository.findById(anyLong())).thenReturn(brand);
    when(this.categoryRepository.findById(anyLong())).thenReturn(category);
    when(this.catalogRepository.update(any())).thenReturn(0);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001",
          1L, 1L);
    };

    // Assert
    assertThrows(OptimisticLockingFailureException.class, action);
  }

  @Test
  void testCountCatalogItems_正常系_リポジトリのcountByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    when(this.catalogRepository.countByBrandIdAndCategoryId(anyLong(), anyLong())).thenReturn(1);

    // Act
    service.countCatalogItems(1L, 1L);

    // Assert
    verify(this.catalogRepository, times(1)).countByBrandIdAndCategoryId(anyLong(), anyLong());

  }

  @Test
  void testGetBrands_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<CatalogBrand> brands = List.of(new CatalogBrand("dummy"));
    when(this.brandRepository.getAll()).thenReturn(brands);

    // Act
    service.getBrands();

    // Assert
    verify(this.brandRepository, times(1)).getAll();

  }

  @Test
  void testGetCategories_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<CatalogCategory> catalogCategories = List.of(new CatalogCategory("dummy"));
    when(this.categoryRepository.getAll()).thenReturn(catalogCategories);

    // Act
    service.getCategories();

    // Assert
    verify(this.categoryRepository, times(1)).getAll();

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

  private CatalogBrand createCatalogBrand(long id) {
    String defaultName = "Name";
    CatalogBrand catalogBrand = new CatalogBrand(defaultName);
    return catalogBrand;
  }

  private CatalogCategory createCatalogCategory(long id) {
    String defaultName = "Name";
    CatalogCategory catalogCategory = new CatalogCategory(defaultName);
    return catalogCategory;
  }
}
