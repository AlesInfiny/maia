package com.dressca.applicationcore.applicationservice;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;

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
  private CatalogCategoryRepository catalogCategoryRepository;

  @Autowired
  private MessageSource messages;

  private CatalogApplicationService service;

  @BeforeEach
  void setUp() {
    service = new CatalogApplicationService(messages, catalogRepository, brandRepository, catalogCategoryRepository);
  }

  @Test
  void testGetCatalogItems_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    List<CatalogItem> catalogItems = List.of(createCatalogItem(1L));
    when(this.catalogRepository.findByBrandIdAndCategoryId(anyLong(), anyLong(), anyInt(), anyInt()))
        .thenReturn(catalogItems);

    // Act
    service.getCatalogItems(1L, 1L, 1, 10);

    // Assert
    verify(this.catalogRepository, times(1)).findByBrandIdAndCategoryId(anyLong(), anyLong(),
        anyInt(), anyInt());

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
    when(this.catalogCategoryRepository.getAll()).thenReturn(catalogCategories);

    // Act
    service.getCategories();

    // Assert
    verify(this.catalogCategoryRepository, times(1)).getAll();

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
