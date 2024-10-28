package com.dressca.applicationcore.applicationservice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
 * {@link CatalogManagementApplicationService} の動作をテストするクラスです。
 */
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class CatalogManagementApplicationServiceTest {
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogBrandRepository catalogBrandRepository;
  @Mock
  private CatalogCategoryRepository catalogCategoryRepository;
  @Mock
  private UserStore userStore;

  @Autowired
  private MessageSource messages;

  private CatalogManagementApplicationService service;

  @BeforeEach
  void setUp() {
    service = new CatalogManagementApplicationService(messages, catalogRepository, catalogBrandRepository,
        catalogCategoryRepository);
    service.setUserStore(this.userStore);
  }

  @Test
  void testAddItemToCatalog_01_正常系_リポジトリのaddを1回呼出す() {
    // Arrange
    CatalogItem catalogItem = createCatalogItem(1L);
    when(this.catalogRepository.add(any())).thenReturn(catalogItem);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    try {
      service.addItemToCatalog("テストアイテム", "テスト用のアイテムです。", new BigDecimal(123456), "TEST001", 1, 1);
    } catch (PermissionDeniedException e) {
      fail(e.getMessage());
    }

    // Assert
    verify(this.catalogRepository, times(1)).add(any());
  }

  @Test
  void testAddItemToCatalog_02_異常系_カタログアイテムを追加する権限がない() {
    // Arrange
    CatalogItem catalogItem = createCatalogItem(1L);
    when(this.catalogRepository.add(any())).thenReturn(catalogItem);
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      service.addItemToCatalog("テストアイテム", "テスト用のアイテムです。", new BigDecimal(123456), "TEST001", 1, 1);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_03_正常系_リポジトリのremoveを1回呼出す() {
    // Arrange
    long targetId = 1L;
    CatalogItem targetItem = this.createCatalogItem(targetId);
    when(this.catalogRepository.findById(targetId)).thenReturn(targetItem);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    try {
      this.service.deleteItemFromCatalog(targetId);
    } catch (PermissionDeniedException | CatalogNotFoundException e) {
      fail(e.getMessage());
    }

    // Assert
    verify(this.catalogRepository, times(1)).remove(any());
  }

  @Test
  void testDeleteItemFromCatalog_04_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;
    CatalogItem targetItem = null;
    when(this.catalogRepository.findById(targetId)).thenReturn(targetItem);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    Executable action = () -> {
      this.service.deleteItemFromCatalog(targetId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_05_異常系_カタログアイテムを削除する権限がない() {
    // Arrange
    long targetId = 1L;
    CatalogItem targetItem = this.createCatalogItem(targetId);
    when(this.catalogRepository.findById(targetId)).thenReturn(targetItem);
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      this.service.deleteItemFromCatalog(targetId);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_06_正常系_リポジトリのupdateを1回呼出す() {
    // Arrange
    long targetId = 1L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";

    CatalogItem catalogItem = createCatalogItem(targetId);
    CatalogCategory catalogCategory = createCatalogCategory();
    CatalogBrand catalogBrand = createCatalogBrand();

    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(catalogItem);
    when(this.catalogRepository.update(any())).thenReturn(1);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(catalogCategory);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(catalogBrand);

    // Action
    try {
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    // Assert
    verify(this.catalogRepository, times(1)).update(any());
  }

  @Test
  void testUpdateCatalogItem_07_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 1L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    CatalogCategory catalogCategory = createCatalogCategory();
    CatalogBrand catalogBrand = createCatalogBrand();

    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(null);
    when(this.catalogRepository.update(any())).thenReturn(1);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(catalogCategory);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(catalogBrand);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_08_異常系_対象のブランドが存在しない() {
    // Arrange
    long targetId = 1L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    CatalogItem catalogItem = createCatalogItem(targetId);
    CatalogCategory catalogCategory = createCatalogCategory();

    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(catalogItem);
    when(this.catalogRepository.update(any())).thenReturn(1);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(catalogCategory);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(null);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(CatalogBrandNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_09_異常系_対象のカテゴリが存在しない() {
    // Arrange
    long targetId = 1L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    CatalogItem catalogItem = createCatalogItem(targetId);
    CatalogBrand catalogBrand = createCatalogBrand();

    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(catalogItem);
    when(this.catalogRepository.update(any())).thenReturn(1);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(null);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(catalogBrand);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(CatalogCategoryNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_10_異常系_カタログアイテムを更新する権限がない() {
    // Arrange
    long targetId = 1L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    CatalogItem catalogItem = createCatalogItem(targetId);
    CatalogCategory catalogCategory = createCatalogCategory();
    CatalogBrand catalogBrand = createCatalogBrand();

    when(this.userStore.isInRole(anyString())).thenReturn(false);
    when(this.catalogRepository.findById(anyLong())).thenReturn(catalogItem);
    when(this.catalogRepository.update(any())).thenReturn(1);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(catalogCategory);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(catalogBrand);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_11_異常系_楽観ロックエラーにより正常に更新ができない() {
    // Arrange
    long targetId = 1L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    CatalogItem catalogItem = createCatalogItem(targetId);
    CatalogCategory catalogCategory = createCatalogCategory();
    CatalogBrand catalogBrand = createCatalogBrand();

    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(catalogItem);
    when(this.catalogRepository.update(any())).thenReturn(0);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(catalogCategory);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(catalogBrand);

    // Act
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(OptimisticLockingFailureException.class, action);
  }

  @Test
  void testGetCatalogItems_12_正常系_リポジトリのFindAsyncを1回呼出す() {
    long brandId = 1L;
    long categoryId = 1L;
    int page = 0;
    int pageSize = 20;

    service.getCatalogItems(brandId, categoryId, page, pageSize);
    verify(this.catalogRepository, times(1)).findByBrandIdAndCategoryId(anyLong(), anyLong(), anyInt(), anyInt());
  }

  @Test
  void testGetCatalogItem_13_正常系_リポジトリのfindByIdを1回呼出す() {
    // Arrange
    long targetId = 1L;
    CatalogItem catalogItem = createCatalogItem(targetId);
    when(this.catalogRepository.findById(targetId)).thenReturn(catalogItem);

    // Action
    try {
      service.getCatalogItem(targetId);
    } catch (CatalogNotFoundException e) {
      fail(e.getMessage());
    }

    // Assert
    verify(this.catalogRepository, times(1)).findById(anyLong());
  }

  @Test
  void testGetCatalogItem_14_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 1L;
    when(this.catalogRepository.findById(targetId)).thenReturn(null);

    // Action
    Executable action = () -> {
      this.service.getCatalogItem(targetId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testCountCatalogItems_15_正常系_countByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    long brandId = 1L;
    long categoryId = 1L;
    int totalCount = 1;
    when(this.catalogRepository.countByBrandIdAndCategoryId(brandId, categoryId)).thenReturn(totalCount);

    // Action
    this.service.countCatalogItems(brandId, categoryId);

    // Assert
    verify(this.catalogRepository, times(1)).countByBrandIdAndCategoryId(anyLong(), anyLong());
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