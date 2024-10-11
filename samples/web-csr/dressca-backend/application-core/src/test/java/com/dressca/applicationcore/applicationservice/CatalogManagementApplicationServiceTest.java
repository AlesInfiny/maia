package com.dressca.applicationcore.applicationservice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.dressca.applicationcore.authorization.UserStore;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogItemUpdateCommand;
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
  void testUpdateCatalogItem_異常系_カタログアイテムの更新中に他の更新が入った場合例外になる() {

    // Arrange
    CatalogItem catalogItem = createCatalogItem(1L);
    CatalogCategory catalogCategory = createCatalogCategory();
    CatalogBrand catalogBrand = createCatalogBrand();
    CatalogItemUpdateCommand command = this.createCatalogItemUpdateCommand(1L);

    when(this.userStore.isInRole(anyString())).thenReturn(true);
    when(this.catalogRepository.findById(anyLong())).thenReturn(catalogItem);
    when(this.catalogCategoryRepository.findById(anyLong())).thenReturn(catalogCategory);
    when(this.catalogBrandRepository.findById(anyLong())).thenReturn(catalogBrand);
    when(this.catalogRepository.update(any())).thenReturn(0);

    // Act
    Executable action = () -> {
      service.updateCatalogItem(command);
    };

    // Assert
    assertThrows(OptimisticLockingFailureException.class, action);
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

  private CatalogItemUpdateCommand createCatalogItemUpdateCommand(long id) {
    Random random = new Random();
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    CatalogItemUpdateCommand catalogItemUpdateCommand = new CatalogItemUpdateCommand(id, defaultName,
        defaultDescription,
        defaultPrice, defaultProductCode, defaultCatalogCategoryId, defaultCatalogBrandId);
    return catalogItemUpdateCommand;
  }
}
