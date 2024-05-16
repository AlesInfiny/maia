package com.dressca.applicationcore.applicationservice;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.applicationcore.catalog.CatalogBrand;
import com.dressca.applicationcore.catalog.CatalogBrandRepository;
import com.dressca.applicationcore.catalog.CatalogCategory;
import com.dressca.applicationcore.catalog.CatalogCategoryRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.systemcommon.constant.MessageIdConstant;
import com.dressca.systemcommon.util.MessageUtil;

/**
 * {@link CatalogApplicationService}の動作をテストするクラスです。
 */
@ExtendWith(SpringExtension.class)
public class CatalogApplicationServiceTest {
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogBrandRepository brandRepository;
  @Mock
  private CatalogCategoryRepository catalogCategoryRepository;
  @InjectMocks
  private CatalogApplicationService service;

  @Test
  void testGetCatalogItems_正常系_リポジトリのfindByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    List<CatalogItem> catalogItems = List.of(createCatalogItem(1L));
    when(this.catalogRepository.findByBrandIdAndCategoryId(anyLong(), anyLong(), anyInt(), anyInt()))
        .thenReturn(catalogItems);
    // Debugログ出力時のmessages.propertiesに代わるモックの設定
    MockedStatic<MessageUtil> messageUtil = Mockito.mockStatic(MessageUtil.class);
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "getCatalogItems" }))
        .thenReturn("メソッド getCatalogItems を開始しました。");
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "getCatalogItems" }))
        .thenReturn("メソッド getCatalogItems を終了しました。");

    // Act
    service.getCatalogItems(1L, 1L, 1, 10);

    // Assert
    verify(this.catalogRepository, times(1)).findByBrandIdAndCategoryId(anyLong(), anyLong(),
        anyInt(), anyInt());

    // staticなモックのクローズ
    messageUtil.close();

  }

  @Test
  void testCountCatalogItems_正常系_リポジトリのcountByBrandIdAndCategoryIdを1回呼出す() {
    // Arrange
    when(this.catalogRepository.countByBrandIdAndCategoryId(anyLong(), anyLong())).thenReturn(1);
    // Debugログ出力時のmessages.propertiesに代わるモックの設定
    MockedStatic<MessageUtil> messageUtil = Mockito.mockStatic(MessageUtil.class);
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "countCatalogItems" }))
        .thenReturn("メソッド countCatalogItems を開始しました。");
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "countCatalogItems" }))
        .thenReturn("メソッド countCatalogItems を終了しました。");

    // Act
    service.countCatalogItems(1L, 1L);

    // Assert
    verify(this.catalogRepository, times(1)).countByBrandIdAndCategoryId(anyLong(), anyLong());

    // staticなモックのクローズ
    messageUtil.close();

  }

  @Test
  void testGetBrands_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<CatalogBrand> brands = List.of(new CatalogBrand("dummy"));
    when(this.brandRepository.getAll()).thenReturn(brands);
    // Debugログ出力時のmessages.propertiesに代わるモックの設定
    MockedStatic<MessageUtil> messageUtil = Mockito.mockStatic(MessageUtil.class);
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "getBrands" }))
        .thenReturn("メソッド getBrands を開始しました。");
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "getBrands" }))
        .thenReturn("メソッド getBrands を終了しました。");

    // Act
    service.getBrands();

    // Assert
    verify(this.brandRepository, times(1)).getAll();

    // staticなモックのクローズ
    messageUtil.close();

  }

  @Test
  void testGetCategories_正常系_リポジトリのgetAllを1回呼出す() {
    // Arrange
    List<CatalogCategory> catalogCategories = List.of(new CatalogCategory("dummy"));
    when(this.catalogCategoryRepository.getAll()).thenReturn(catalogCategories);
    // Debugログ出力時のmessages.propertiesに代わるモックの設定
    MockedStatic<MessageUtil> messageUtil = Mockito.mockStatic(MessageUtil.class);
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0000_LOG, new String[] { "getCategories" }))
        .thenReturn("メソッド getCategories を開始しました。");
    messageUtil
        .when(() -> MessageUtil.getMessage(MessageIdConstant.D_METHOD0001_LOG, new String[] { "getCategories" }))
        .thenReturn("メソッド getCategories を終了しました。");

    // Act
    service.getCategories();

    // Assert
    verify(this.catalogCategoryRepository, times(1)).getAll();

    // staticなモックのクローズ
    messageUtil.close();

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
