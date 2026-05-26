package com.dressca.applicationcore.applicationservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.applicationcore.authorization.PermissionDeniedException;
import com.dressca.applicationcore.authorization.UserStore;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandNotFoundException;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryNotFoundException;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogDomainService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.catalog.OptimisticLockingFailureException;
import com.dressca.applicationcore.config.ApplicationCoreTestConfig;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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
 * {@link CatalogApplicationService}の動作をテストするクラスです。
 */
@Import(ApplicationCoreTestConfig.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(properties = "spring.messages.basename=applicationcore.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class CatalogApplicationServiceTest {
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogBrandRepository brandRepository;
  @Mock
  private CatalogCategoryRepository categoryRepository;
  @Mock
  private CatalogDomainService catalogDomainService;
  @Mock
  private UserStore userStore;
  @Mock
  private AbstractStructuredLogger apLog;

  @Autowired
  private MessageSource messages;

  private CatalogApplicationService service;

  @BeforeEach
  void setUp() {
    service = new CatalogApplicationService(messages, catalogRepository, brandRepository,
        categoryRepository, catalogDomainService, apLog, userStore);
  }

  @Test
  void testGetCatalogItem_正常系_リポジトリのfindByIdIncludingDeletedを1回呼出す()
      throws CatalogNotFoundException, PermissionDeniedException {
    UUID targetId = UUID.randomUUID();
    CatalogItem catalogItem = createCatalogItem(targetId);
    when(this.catalogRepository.findByIdIncludingDeleted(targetId)).thenReturn(catalogItem);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    service.getCatalogItem(targetId);

    verify(this.catalogRepository, times(1)).findByIdIncludingDeleted(targetId);
  }

  @Test
  void testGetCatalogItem_正常系_指定したidのカタログアイテムが返却される()
      throws CatalogNotFoundException, PermissionDeniedException {
    UUID targetId = UUID.randomUUID();
    CatalogItem expectedCatalogItem = createCatalogItem(targetId);
    when(this.catalogRepository.findByIdIncludingDeleted(targetId)).thenReturn(expectedCatalogItem);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    CatalogItem actualCatalogItem = service.getCatalogItem(targetId);

    assertThat(actualCatalogItem).isEqualTo(expectedCatalogItem);
  }

  @Test
  void testGetCatalogItem_異常系_対象のアイテムが存在しない() {
    UUID targetId = UUID.randomUUID();
    when(this.catalogRepository.findByIdIncludingDeleted(targetId)).thenReturn(null);
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    Executable action = () -> this.service.getCatalogItem(targetId);

    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testGetCatalogItem_異常系_カタログアイテムを取得する権限がない() {
    UUID targetId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    Executable action = () -> this.service.getCatalogItem(targetId);

    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testGetCatalogItemsForConsumer_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 0;
    int pageSize = 20;

    service.getCatalogItemsForConsumer(brandId, categoryId, page, pageSize);

    verify(this.catalogRepository, times(1)).findByBrandIdAndCategoryId(brandId, categoryId, page,
        pageSize);
  }

  @Test
  void testGetCatalogItemsForConsumer_正常系_指定した条件のカタログアイテムのリストが返却される() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 0;
    int pageSize = 20;
    UUID targetId = UUID.randomUUID();
    CatalogItem catalogItem = createCatalogItem(targetId);
    List<CatalogItem> expectedCatalogItemList = new ArrayList<>(Arrays.asList(catalogItem));
    when(this.catalogRepository.findByBrandIdAndCategoryId(brandId, categoryId, page, pageSize))
        .thenReturn(expectedCatalogItemList);

    List<CatalogItem> actualCatalogItemList =
        service.getCatalogItemsForConsumer(brandId, categoryId, page, pageSize);

    assertThat(actualCatalogItemList).isEqualTo(expectedCatalogItemList);
  }

  @Test
  void testGetCatalogItemsForAdmin_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す()
      throws PermissionDeniedException {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 0;
    int pageSize = 20;
    when(this.userStore.isInRole(anyString())).thenReturn(true);

    service.getCatalogItemsForAdmin(brandId, categoryId, page, pageSize);

    verify(this.catalogRepository, times(1)).findByBrandIdAndCategoryIdIncludingDeleted(brandId,
        categoryId, page, pageSize);
  }

  @Test
  void testGetCatalogItemsForAdmin_正常系_指定した条件のカタログアイテムのリストが返却される()
      throws PermissionDeniedException {
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 0;
    int pageSize = 20;
    UUID targetId = UUID.randomUUID();
    CatalogItem catalogItem = createCatalogItem(targetId);
    List<CatalogItem> expectedCatalogItemList = new ArrayList<>(Arrays.asList(catalogItem));
    when(this.catalogRepository.findByBrandIdAndCategoryIdIncludingDeleted(brandId, categoryId,
        page, pageSize)).thenReturn(expectedCatalogItemList);

    List<CatalogItem> actualCatalogItemList =
        service.getCatalogItemsForAdmin(brandId, categoryId, page, pageSize);

    assertThat(actualCatalogItemList).isEqualTo(expectedCatalogItemList);
  }

  @Test
  void testGetCatalogItemsForAdmin_異常系_カタログアイテムの一覧を取得する権限がない() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    int page = 0;
    int pageSize = 20;
    when(this.userStore.isInRole(anyString())).thenReturn(false);

    Executable action = () -> service.getCatalogItemsForAdmin(brandId, categoryId, page, pageSize);

    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testAddItemToCatalog_正常系_リポジトリのaddCatalogItemを1回呼出す() throws PermissionDeniedException,
      CatalogCategoryNotFoundException, CatalogBrandNotFoundException {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogBrand(brandId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(true);
    String name = "テストアイテム";
    String description = "テスト用のアイテムです。";
    BigDecimal price = new BigDecimal(123456);
    String productCode = "TEST001";

    service.addItemToCatalog(name, description, price, productCode, categoryId, brandId);

    verify(this.catalogRepository, times(1)).add(any());
  }

  @Test
  void testAddItemToCatalog_正常系_追加したカタログアイテムが返却される() throws PermissionDeniedException,
      CatalogCategoryNotFoundException, CatalogBrandNotFoundException {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogBrand(brandId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(true);
    String name = "テストアイテム";
    String description = "テスト用のアイテムです。";
    BigDecimal price = new BigDecimal(123456);
    String productCode = "TEST001";
    UUID targetId = UUID.randomUUID();
    CatalogItem expectedCatalogItem = createCatalogItem(targetId);
    when(this.catalogRepository.add(any())).thenReturn(expectedCatalogItem);

    CatalogItem actualCatalogItem =
        service.addItemToCatalog(name, description, price, productCode, categoryId, brandId);

    assertThat(actualCatalogItem).isEqualTo(expectedCatalogItem);
  }

  @Test
  void testAddItemToCatalog_異常系_カタログアイテムを追加する権限がない() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(false);
    String name = "テストアイテム";
    String description = "テスト用のアイテムです。";
    BigDecimal price = new BigDecimal(123456);
    String productCode = "TEST001";

    Executable action =
        () -> service.addItemToCatalog(name, description, price, productCode, categoryId, brandId);

    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testAddItemToCatalog_異常系_追加対象のカタログカテゴリが存在しない() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(false);
    String name = "テストアイテム";
    String description = "テスト用のアイテムです。";
    BigDecimal price = new BigDecimal(123456);
    String productCode = "TEST001";

    Executable action =
        () -> service.addItemToCatalog(name, description, price, productCode, categoryId, brandId);

    assertThrows(CatalogCategoryNotFoundException.class, action);
  }

  @Test
  void testAddItemToCatalog_異常系_追加対象のカタログブランドが存在しない() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogBrand(brandId)).thenReturn(false);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(true);
    String name = "テストアイテム";
    String description = "テスト用のアイテムです。";
    BigDecimal price = new BigDecimal(123456);
    String productCode = "TEST001";

    Executable action =
        () -> service.addItemToCatalog(name, description, price, productCode, categoryId, brandId);

    assertThrows(CatalogBrandNotFoundException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_正常系_リポジトリのremoveを1回呼出す() throws CatalogNotFoundException,
      PermissionDeniedException, OptimisticLockingFailureException {
    UUID targetId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(true);
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    when(this.catalogRepository.remove(targetId, rowVersion)).thenReturn(1);

    this.service.deleteItemFromCatalog(targetId, rowVersion);

    verify(this.catalogRepository, times(1)).remove(targetId, rowVersion);
  }

  @Test
  void testDeleteItemFromCatalog_異常系_対象のアイテムが存在しない() {
    UUID targetId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(false);
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    Executable action = () -> this.service.deleteItemFromCatalog(targetId, rowVersion);

    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_異常系_カタログアイテムを削除する権限がない() {
    UUID targetId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(false);
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    Executable action = () -> this.service.deleteItemFromCatalog(targetId, rowVersion);

    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testDeleteItemFromCatalog_異常系_楽観ロックエラー() {
    UUID targetId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(true);
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    when(this.catalogRepository.remove(targetId, rowVersion)).thenReturn(0);

    Executable action = () -> this.service.deleteItemFromCatalog(targetId, rowVersion);

    assertThrows(OptimisticLockingFailureException.class, action);
  }

  @Test
  void testUpdateCatalogItem_正常系_リポジトリのupdateを1回呼出す()
      throws CatalogNotFoundException, PermissionDeniedException, CatalogBrandNotFoundException,
      CatalogCategoryNotFoundException, OptimisticLockingFailureException {
    UUID targetId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    UUID brandId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogBrand(brandId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(true);
    when(this.catalogRepository.update(any())).thenReturn(1);
    String name = "name";
    String description = "Description.";
    BigDecimal price = BigDecimal.valueOf(100_000_000L);
    String productCode = "C000000001";
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    boolean isDeleted = false;

    this.service.updateCatalogItem(targetId, name, description, price, productCode, categoryId,
        brandId, rowVersion, isDeleted);

    verify(this.catalogRepository, times(1)).update(any());
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のアイテムが存在しない() {
    UUID targetId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    UUID brandId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(false);
    String name = "name";
    String description = "Description.";
    BigDecimal price = BigDecimal.valueOf(100_000_000L);
    String productCode = "C000000001";
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    boolean isDeleted = false;

    Executable action = () -> this.service.updateCatalogItem(targetId, name, description, price,
        productCode, categoryId, brandId, rowVersion, isDeleted);

    assertThrows(CatalogNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のカテゴリが存在しない() {
    UUID targetId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    UUID brandId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(false);
    String name = "name";
    String description = "Description.";
    BigDecimal price = BigDecimal.valueOf(100_000_000L);
    String productCode = "C000000001";
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    boolean isDeleted = false;

    Executable action = () -> this.service.updateCatalogItem(targetId, name, description, price,
        productCode, categoryId, brandId, rowVersion, isDeleted);

    assertThrows(CatalogCategoryNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_対象のブランドが存在しない() {
    UUID targetId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    UUID brandId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogBrand(brandId)).thenReturn(false);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(true);
    String name = "name";
    String description = "Description.";
    BigDecimal price = BigDecimal.valueOf(100_000_000L);
    String productCode = "C000000001";
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    boolean isDeleted = false;

    Executable action = () -> this.service.updateCatalogItem(targetId, name, description, price,
        productCode, categoryId, brandId, rowVersion, isDeleted);

    assertThrows(CatalogBrandNotFoundException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_カタログアイテムを更新する権限がない() {
    UUID targetId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    UUID brandId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(false);
    String name = "name";
    String description = "Description.";
    BigDecimal price = BigDecimal.valueOf(100_000_000L);
    String productCode = "C000000001";
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    boolean isDeleted = false;

    Executable action = () -> this.service.updateCatalogItem(targetId, name, description, price,
        productCode, categoryId, brandId, rowVersion, isDeleted);

    assertThrows(PermissionDeniedException.class, action);
  }

  @Test
  void testUpdateCatalogItem_異常系_楽観ロックエラーにより正常に更新ができない() {
    UUID targetId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    UUID brandId = UUID.randomUUID();
    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogDomainService.existCatalogItem(targetId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogBrand(brandId)).thenReturn(true);
    when(this.catalogDomainService.existCatalogCategory(categoryId)).thenReturn(true);
    when(this.catalogRepository.update(any())).thenReturn(0);
    String name = "name";
    String description = "Description.";
    BigDecimal price = BigDecimal.valueOf(100_000_000L);
    String productCode = "C000000001";
    OffsetDateTime rowVersion = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    boolean isDeleted = false;

    Executable action = () -> this.service.updateCatalogItem(targetId, name, description, price,
        productCode, categoryId, brandId, rowVersion, isDeleted);

    assertThrows(OptimisticLockingFailureException.class, action);
  }

  @Test
  void countCatalogItemsForConsumer_正常系_リポジトリのcountByBrandIdAndCategoryIdを1回呼出す() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.catalogRepository.countByBrandIdAndCategoryId(any(), any())).thenReturn(1);

    service.countCatalogItemsForConsumer(brandId, categoryId);

    verify(this.catalogRepository, times(1)).countByBrandIdAndCategoryId(any(), any());
  }

  @Test
  void countCatalogItemsForAdmin_正常系_リポジトリのcountByBrandIdAndCategoryIdを1回呼出す() {
    UUID brandId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    when(this.catalogRepository.countByBrandIdAndCategoryIdIncludingDeleted(any(), any()))
        .thenReturn(1);

    service.countCatalogItemsForAdmin(brandId, categoryId);

    verify(this.catalogRepository, times(1)).countByBrandIdAndCategoryIdIncludingDeleted(any(),
        any());
  }

  @Test
  void testGetBrands_正常系_リポジトリのgetAllを1回呼出す() {
    List<CatalogBrand> brands = List.of(new CatalogBrand("dummy"));
    when(this.brandRepository.getAll()).thenReturn(brands);

    service.getBrands();

    verify(this.brandRepository, times(1)).getAll();
  }

  @Test
  void testGetCategories_正常系_リポジトリのgetAllを1回呼出す() {
    List<CatalogCategory> catalogCategories = List.of(new CatalogCategory("dummy"));
    when(this.categoryRepository.getAll()).thenReturn(catalogCategories);

    service.getCategories();

    verify(this.categoryRepository, times(1)).getAll();
  }

  private CatalogItem createCatalogItem(UUID id) {
    UUID defaultCatalogBrandId = UUID.randomUUID();
    UUID defaultCatalogCategoryId = UUID.randomUUID();
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    return new CatalogItem(id, defaultName, defaultDescription, defaultPrice, defaultProductCode,
        defaultCatalogBrandId, defaultCatalogCategoryId, defaultIsDeleted);
  }
}
