package com.dressca.applicationcore.applicationservice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
  void testGetCatalogItems_正常系_ドメインサービスのgetCatalogItemsByConditionsを1回呼出す() {
    // Action
    service.getCatalogItems(1L, 1L, 0, 20);

    // Assert
    verify(this.catalogDomainService, times(1)).getCatalogItemsByConditions(anyLong(), anyLong(), anyInt(), anyInt());
  }

  @Test
  void testGetCatalogItem_正常系_ドメインサービスのgetCatalogItemByIdを1回呼出す() throws CatalogNotFoundException {
    // Arrange
    CatalogItem catalogItem = createCatalogItem(1L);
    when(this.catalogDomainService.getCatalogItemById(anyLong())).thenReturn(catalogItem);

    // Action
    service.getCatalogItem(1L);

    // Assert
    verify(this.catalogDomainService, times(1)).getCatalogItemById(anyLong());
  }

  @Test
  void testGetCatalogItem_異常系_対象のアイテムが存在しない() throws CatalogNotFoundException {
    // Arrange
    long targetId = 999L;
    doThrow(new CatalogNotFoundException(targetId)).when(this.catalogDomainService).getCatalogItemById(anyLong());

    // Action
    Executable action = () -> {
      this.service.getCatalogItem(targetId);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testAddItemToCatalog_正常系_ドメインサービスのaddCatalogItemを1回呼出す() throws PermissionDeniedException {
    // Arrange
    CatalogItem catalogItemAdded = createCatalogItem(1L);
    when(this.catalogDomainService.addCatalogItem(anyString(), anyString(), any(), anyString(), anyLong(), anyLong()))
        .thenReturn(catalogItemAdded);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    service.addItemToCatalog("テストアイテム", "テスト用のアイテムです。", new BigDecimal(123456), "TEST001", 1, 1);

    // Assert
    verify(this.catalogDomainService, times(1)).addCatalogItem(anyString(), anyString(), any(), anyString(), anyLong(),
        anyLong());
  }

  @Test
  void testAddItemToCatalog_異常系_カタログアイテムを追加する権限がない() {
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
  void testDeleteItemFromCatalog_正常系_ドメインサービスのremoveを1回呼出す()
      throws CatalogNotFoundException, PermissionDeniedException {
    // Arrange
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    this.service.deleteItemFromCatalog(1L);

    // Assert
    verify(this.catalogDomainService, times(1)).deleteCatalogItemById(anyLong());

  }

  @Test
  void testDeleteItemFromCatalog_異常系_対象のアイテムが存在しない() throws CatalogNotFoundException {
    // Arrange
    long targetId = 999L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    doThrow(new CatalogNotFoundException(targetId)).when(this.catalogDomainService).deleteCatalogItemById(anyLong());

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
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      this.service.deleteItemFromCatalog(1L);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_正常系_ドメインサービスのupdateCatalogItemを1回呼出す()
      throws CatalogNotFoundException, PermissionDeniedException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    // Arrange
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    // Action
    this.service.updateCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L, 1L);

    // Assert
    verify(this.catalogDomainService, times(1)).modifyCatalogItem(anyLong(), anyString(), anyString(), any(),
        anyString(), anyLong(), anyLong());
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のアイテムが存在しない() throws CatalogNotFoundException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    // Arrange
    long targetId = 999L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    doThrow(new CatalogNotFoundException(targetId)).when(this.catalogDomainService).modifyCatalogItem(anyLong(),
        anyString(), anyString(), any(), anyString(), anyLong(), anyLong());

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001",
          1L, 1L);
    };

    // Assert
    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のブランドが存在しない() throws CatalogNotFoundException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    // Arrange
    long targetId = 999L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    doThrow(new CatalogBrandNotFoundException(targetId)).when(this.catalogDomainService).modifyCatalogItem(anyLong(),
        anyString(), anyString(), any(), anyString(), anyLong(), anyLong());

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001", 1L,
          targetId);
    };

    // Assert
    assertThrows(CatalogBrandNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のカテゴリが存在しない() throws CatalogNotFoundException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    // Arrange
    long targetId = 999L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    doThrow(new CatalogCategoryNotFoundException(targetId)).when(this.catalogDomainService).modifyCatalogItem(anyLong(),
        anyString(), anyString(), any(), anyString(), anyLong(), anyLong());

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
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(1L, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001",
          1L, 1L);
    };

    // Assert
    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_楽観ロックエラーにより正常に更新ができない() throws CatalogNotFoundException,
      CatalogBrandNotFoundException, CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    // Arrange
    long targetId = 999L;
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    doThrow(new OptimisticLockingFailureException(targetId)).when(this.catalogDomainService).modifyCatalogItem(
        anyLong(), anyString(), anyString(), any(), anyString(), anyLong(), anyLong());

    // Action
    Executable action = () -> {
      this.service.updateCatalogItem(targetId, "Name", "Description.", BigDecimal.valueOf(100_000_000L), "C000000001",
          1L, 1L);
    };

    // Assert
    assertThrows(OptimisticLockingFailureException.class, action);
  }

  @Test
  void testCountCatalogItems_正常系_countByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    when(this.catalogRepository.countByBrandIdAndCategoryId(anyLong(), anyLong())).thenReturn(1);

    // Action
    this.service.countCatalogItems(1L, 1L);

    // Assert
    verify(this.catalogRepository, times(1)).countByBrandIdAndCategoryId(anyLong(), anyLong());
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
    return catalogItem;
  }

}