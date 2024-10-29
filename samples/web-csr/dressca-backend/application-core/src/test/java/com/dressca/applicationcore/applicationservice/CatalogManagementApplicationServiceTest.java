package com.dressca.applicationcore.applicationservice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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
import com.dressca.applicationcore.catalog.CatalogBrandNotFoundException;
import com.dressca.applicationcore.catalog.CatalogCategoryNotFoundException;
import com.dressca.applicationcore.catalog.CatalogDomainService;
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
  private CatalogDomainService catalogDomainService;
  @Mock
  private UserStore userStore;

  @Autowired
  private MessageSource messages;

  private CatalogManagementApplicationService service;

  @BeforeEach
  void setUp() {
    service = new CatalogManagementApplicationService(messages, catalogRepository, catalogDomainService);
    service.setUserStore(this.userStore);
  }

  @Test
  void testAddItemToCatalog_01_正常系_ドメインサービスのaddCatalogItemを1回呼出す() {
    // Arrange
    CatalogItem catalogItemAdded = createCatalogItem(1L);
    when(this.catalogDomainService.addCatalogItem(anyString(), anyString(), any(), anyString(), anyLong(), anyLong()))
        .thenReturn(catalogItemAdded);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    try {
      service.addItemToCatalog("テストアイテム", "テスト用のアイテムです。", new BigDecimal(123456), "TEST001", 1, 1);
    } catch (PermissionDeniedException e) {
      fail(e.getMessage());
    }

    // Assert
    verify(this.catalogDomainService, times(1)).addCatalogItem(anyString(), anyString(), any(), anyString(), anyLong(),
        anyLong());
  }

  @Test
  void testAddItemToCatalog_02_異常系_カタログアイテムを追加する権限がない() {
    // Arrange
    CatalogItem catalogItemAdded = createCatalogItem(1L);
    when(this.catalogDomainService.addCatalogItem(anyString(), anyString(), any(), anyString(), anyLong(), anyLong()))
        .thenReturn(catalogItemAdded);
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      service.addItemToCatalog("テストアイテム", "テスト用のアイテムです。", new BigDecimal(123456), "TEST001", 1, 1);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_03_正常系_ドメインサービスのremoveを1回呼出す() {
    // Arrange
    long targetId = 1L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action & Assert
    try {
      this.service.deleteItemFromCatalog(targetId);
      verify(this.catalogDomainService, times(1)).deleteCatalogItemById(anyLong());
    } catch (PermissionDeniedException | CatalogNotFoundException e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testDeleteItemFromCatalog_04_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    Executable action = () -> {
      doThrow(new CatalogNotFoundException(targetId)).when(this.catalogDomainService).deleteCatalogItemById(targetId);
      this.service.deleteItemFromCatalog(targetId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_05_異常系_カタログアイテムを削除する権限がない() {
    // Arrange
    long targetId = 1L;
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      this.service.deleteItemFromCatalog(targetId);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_06_正常系_ドメインサービスのupdateCatalogItemを1回呼出す() {
    // Arrange
    long targetId = 1L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action & Assert
    try {
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
      verify(this.catalogDomainService, times(1)).updateCatalogItem(anyLong(), anyString(), anyString(), any(),
          anyString(), anyLong(), anyLong());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testUpdateCatalogItem_07_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";

    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    Executable action = () -> {
      doThrow(new CatalogNotFoundException(targetId)).when(this.catalogDomainService).updateCatalogItem(targetId,
          defaultName, defaultDescription, defaultPrice, defaultProductCode, defaultCatalogCategoryId,
          defaultCatalogBrandId);
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_08_異常系_対象のブランドが存在しない() {
    // Arrange
    long targetId = 999L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    Executable action = () -> {
      doThrow(new CatalogBrandNotFoundException(targetId)).when(this.catalogDomainService).updateCatalogItem(targetId,
          defaultName, defaultDescription, defaultPrice, defaultProductCode, defaultCatalogCategoryId,
          defaultCatalogBrandId);
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(CatalogBrandNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_09_異常系_対象のカテゴリが存在しない() {
    // Arrange
    long targetId = 999L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";

    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    Executable action = () -> {
      doThrow(new CatalogCategoryNotFoundException(targetId)).when(this.catalogDomainService).updateCatalogItem(
          targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode, defaultCatalogCategoryId,
          defaultCatalogBrandId);
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
    when(this.userStore.isInRole(anyString())).thenReturn(false);
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
    long targetId = 999L;
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";

    when(this.userStore.isInRole(anyString())).thenReturn(true);
    // Act
    Executable action = () -> {
      doThrow(new OptimisticLockingFailureException(targetId)).when(this.catalogDomainService).updateCatalogItem(
          targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode, defaultCatalogCategoryId,
          defaultCatalogBrandId);
      this.service.updateCatalogItem(targetId, defaultName, defaultDescription, defaultPrice, defaultProductCode,
          defaultCatalogCategoryId, defaultCatalogBrandId);
    };

    // Assert
    assertThrows(OptimisticLockingFailureException.class, action);
  }

  @Test
  void testGetCatalogItems_12_正常系_ドメインサービスのgetCatalogItemsByConditionsを1回呼出す() {
    // Arrange
    long brandId = 1L;
    long categoryId = 1L;
    int page = 0;
    int pageSize = 20;

    // Action
    service.getCatalogItems(brandId, categoryId, page, pageSize);

    // Assert
    verify(this.catalogDomainService, times(1)).getCatalogItemsByConditions(anyLong(), anyLong(), anyInt(), anyInt());
  }

  @Test
  void testGetCatalogItem_13_正常系_ドメインサービスのgetCatalogItemByIdを1回呼出す() {
    // Arrange
    long targetId = 1L;
    CatalogItem catalogItem = createCatalogItem(targetId);

    // Action & Assert
    try {
      when(this.catalogDomainService.getCatalogItemById(targetId)).thenReturn(catalogItem);
      service.getCatalogItem(targetId);
      verify(this.catalogDomainService, times(1)).getCatalogItemById(anyLong());
    } catch (CatalogNotFoundException e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testGetCatalogItem_14_異常系_対象のアイテムが存在しない() {
    // Arrange
    long targetId = 999L;

    // Action
    Executable action = () -> {
      doThrow(new CatalogNotFoundException(targetId)).when(this.catalogDomainService).getCatalogItemById(targetId);
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

}