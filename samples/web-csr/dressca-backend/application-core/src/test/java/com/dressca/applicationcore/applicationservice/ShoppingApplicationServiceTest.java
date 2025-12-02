package com.dressca.applicationcore.applicationservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.baskets.CatalogItemInBasketNotFoundException;
import com.dressca.applicationcore.catalog.CatalogDomainService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.CatalogItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.log.AbstractStructuredLogger;

/**
 * {@link ShoppingApplicationService}の動作をテストするクラスです。
 */
@ExtendWith(MockitoExtension.class)
public class ShoppingApplicationServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private BasketRepository basketRepository;
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogDomainService catalogDomainService;
  @Mock
  private AbstractStructuredLogger apLog;

  private ShoppingApplicationService service;

  private static final Random random = new Random();

  @BeforeEach
  void setUp() {
    ResourceBundleMessageSource messages = new ResourceBundleMessageSource();
    messages.setBasename("applicationcore.messages");
    messages.setDefaultEncoding("UTF-8");
    service = new ShoppingApplicationService(messages, basketRepository, catalogRepository,
        orderRepository, catalogDomainService, apLog);
  }

  @Test
  void testAddItemToBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws CatalogNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long catalogItemId = 1L;

    // 期待する戻り値
    // なし

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    CatalogItem catalogItem = createCatalogItem(catalogItemId);
    List<Long> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);
    when(this.catalogDomainService.getExistCatalogItems(catalogItemIds))
        .thenReturn(List.of(catalogItem));

    // テストメソッドの実行
    int quantity = 1;
    service.addItemToBasket(buyerId, catalogItemId, quantity);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
    verify(this.catalogDomainService, times(1)).getExistCatalogItems(catalogItemIds);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testAddItemToBasket_正常系_商品追加処理後に数量が0となる場合買い物かごアイテムは削除される() throws CatalogNotFoundException {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long catalogItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(catalogItemId, price, 1);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    CatalogItem catalogItem = createCatalogItem(catalogItemId);
    List<Long> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);
    when(this.catalogDomainService.getExistCatalogItems(catalogItemIds))
        .thenReturn(List.of(catalogItem));

    // テストメソッドの実行
    int quantity = -1;
    service.addItemToBasket(buyerId, catalogItemId, quantity);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
    verify(this.catalogDomainService, times(1)).getExistCatalogItems(catalogItemIds);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().size()).isEqualTo(0);
  }

  @Test
  void testAddItemToBasket_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long catalogItemId = 1L;

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<Long> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(false);

    try {
      // テストメソッドの実行
      int quantity = 1;
      service.addItemToBasket(buyerId, catalogItemId, quantity);
      fail("CatalogNotFoundException が発生しなければ失敗");
    } catch (CatalogNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
      verify(this.catalogDomainService, times(0)).getExistCatalogItems(any());
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("CatalogNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testSetQuantities_正常系_リポジトリのupdateを1度だけ呼出す()
      throws BasketNotFoundException, CatalogNotFoundException,
      CatalogItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    List<Long> catalogItemIds = List.of(1L);

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(catalogItemIds.get(0), BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);

    // テストメソッドの実行
    int newQuantity = 5;
    Map<Long, Integer> quantities = Map.of(catalogItemIds.get(0), newQuantity);
    service.setQuantities(buyerId, quantities);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在する商品を指定すると買い物かごの商品数が更新される()
      throws BasketNotFoundException, CatalogNotFoundException,
      CatalogItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    List<Long> catalogItemIds = List.of(1L);

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(catalogItemIds.get(0), BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);

    // テストメソッドの実行
    int newQuantity = 5;
    Map<Long, Integer> quantities = Map.of(catalogItemIds.get(0), newQuantity);
    service.setQuantities(buyerId, quantities);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().get(0).getQuantity()).isEqualTo(newQuantity);
  }

  @Test
  void testSetQuantities_異常系_カタログリポジトリに存在しない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    List<Long> catalogItemIds = List.of(1L);

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(false);

    try {
      // テストメソッドの実行
      int newQuantity = 5;
      Map<Long, Integer> quantities = Map.of(catalogItemIds.get(0), newQuantity);
      service.setQuantities(buyerId, quantities);
      fail("CatalogNotFoundException が発生しなければ失敗");
    } catch (CatalogNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("CatalogNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testSetQuantities_異常系_買い物かごに入っていない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    List<Long> catalogItemIds = List.of(1L);

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(2L, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);

    try {
      // テストメソッドの実行
      int newQuantity = 5;
      Map<Long, Integer> quantities = Map.of(catalogItemIds.get(0), newQuantity);
      service.setQuantities(buyerId, quantities);
      fail("CatalogItemInBasketNotFoundException が発生しなければ失敗");
    } catch (CatalogItemInBasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("CatalogItemInBasketNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testDeleteItemFromBasket_正常系_リポジトリのupdateを1度だけ呼出す()
      throws BasketNotFoundException, CatalogNotFoundException,
      CatalogItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long catalogItemId = 1L;

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(catalogItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existCatalogItemIncludingDeleted(catalogItemId))
        .thenReturn(true);

    // テストメソッドの実行
    service.deleteItemFromBasket(buyerId, catalogItemId);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testDeleteItemFromBasket_正常系_買い物かごから指定の商品が削除されている()
      throws BasketNotFoundException, CatalogNotFoundException,
      CatalogItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long catalogItemId = 1L;

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(catalogItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existCatalogItemIncludingDeleted(catalogItemId))
        .thenReturn(true);

    // テストメソッドの実行
    service.deleteItemFromBasket(buyerId, catalogItemId);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertEquals(0, argBasket.getItems().size());
  }

  @Test
  void testDeleteItemFromBasket_異常系_カタログリポジトリに存在しない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long catalogItemId = 1L;

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existCatalogItemIncludingDeleted(catalogItemId))
        .thenReturn(false);

    try {
      service.deleteItemFromBasket(buyerId, catalogItemId);
      fail("CatalogNotFoundException が発生しなければ失敗");
    } catch (CatalogNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("CatalogNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testDeleteItemFromBasket_異常系_買い物かごに入っていない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long catalogItemId = 1L;

    // モックの設定
    Long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existCatalogItemIncludingDeleted(catalogItemId))
        .thenReturn(true);

    try {
      // テストメソッドの実行
      service.deleteItemFromBasket(buyerId, catalogItemId);
      fail("CatalogItemInBasketNotFoundException が発生しなければ失敗");
    } catch (CatalogItemInBasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("CatalogItemInBasketNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testGetBasketDetail_正常系_カタログIDに対応するカタログ情報が取得されること() throws BasketNotFoundException {

    // テスト用の入力データ
    String dummyBuyerId = "dummyId";

    // モックの設定
    Basket basket = new Basket(dummyBuyerId);
    basket.addItem(1L, BigDecimal.valueOf(1000), 1);
    basket.addItem(2L, BigDecimal.valueOf(2000), 1);
    when(this.basketRepository.findByBuyerId(dummyBuyerId)).thenReturn(Optional.of(basket));
    List<CatalogItem> items = List.of(
        new CatalogItem(1L, "name1", "desc1", BigDecimal.valueOf(1000), "code1", 1L, 1L, false),
        new CatalogItem(2L, "name2", "desc2", BigDecimal.valueOf(2000), "code2", 2L, 2L, false));
    List<Long> catalogItemIds = List.of(1L, 2L);
    when(this.catalogRepository.findByCatalogItemIdInIncludingDeleted(catalogItemIds))
        .thenReturn(items);

    // テストメソッドの実行
    BasketDetail actual = service.getBasketDetail(dummyBuyerId);
    assertThat(actual.catalogItems.size()).isEqualTo(2);
    assertThat(actual.catalogItems.get(0).getId()).isEqualTo(1L);
    assertThat(actual.catalogItems.get(1).getId()).isEqualTo(2L);
    // モックが想定通り呼び出されていることの確認
    verify(this.catalogRepository, times(1)).findByCatalogItemIdInIncludingDeleted(catalogItemIds);
  }

  @ParameterizedTest
  @MethodSource("blankStringSource")
  void testGetBasketDetail_異常系_購入者IDがnullまたは空白なら例外が発生する(String buyerId)
      throws IllegalArgumentException {

    // テストメソッドの実行
    try {
      service.getBasketDetail(buyerId);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).startsWith("buyerIdがnullまたは空文字");
    }
    // モックが想定通り呼び出されていることの確認
    verify(this.catalogRepository, times(0)).findByCatalogItemIdInIncludingDeleted(any());
  }

  @Test
  void testCheckout_正常系_注文リポジトリのAddを1回呼出す() throws Exception {
    // Arrange
    String buyerId = UUID.randomUUID().toString();
    Basket basket = new Basket(buyerId);
    basket.addItem(10L, BigDecimal.valueOf(100_000_000), 1);
    ShipTo shipToAddress = createDefaultShipTo();
    List<CatalogItem> catalogItems = List.of(createCatalogItem(10L));
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogRepository.findByCatalogItemIdIn(List.of(10L))).thenReturn(catalogItems);
    when(this.orderRepository.add(any())).thenReturn(order);

    // Act
    service.checkout(buyerId, shipToAddress);

    // Assert
    verify(this.orderRepository, times(1)).add(any());
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.basketRepository, times(1)).remove(basket);
  }

  @Test
  void testCheckout_異常系_指定した買い物かごが空の場合は業務例外が発生する() {
    // Arrange
    String buyerId = UUID.randomUUID().toString();
    Basket basket = new Basket(buyerId);
    ShipTo shipToAddress = createDefaultShipTo();
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));

    // Act
    Executable action = () -> service.checkout(buyerId, shipToAddress);

    // Assert
    assertThrows(EmptyBasketOnCheckoutException.class, action);
  }

  private ShipTo createDefaultShipTo() {
    String defaultFullName = "国会　太郎";
    Address address = createDefaultAddress();
    return new ShipTo(defaultFullName, address);
  }

  private static Address createDefaultAddress() {
    String defaultPostalCode = "100-8924";
    String defaultTodofuken = "東京都";
    String defaultShikuchoson = "千代田区";
    String defaultAzanaAndOthers = "永田町1-10-1";
    return new Address(defaultPostalCode, defaultTodofuken, defaultShikuchoson,
        defaultAzanaAndOthers);
  }

  private List<OrderItem> createDefaultOrderItems() {
    String productName = "ダミー商品1";
    String productCode = "C000000001";

    List<OrderItem> items =
        List.of(new OrderItem(new CatalogItemOrdered(1L, productName, productCode),
            BigDecimal.valueOf(100_000_000L), 1));

    return items;
  }

  private CatalogItem createCatalogItem(long id) {
    long defaultCatalogCategoryId = random.nextInt(1000);
    long defaultCatalogBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    CatalogItem catalogItem = new CatalogItem(id, defaultName, defaultDescription, defaultPrice,
        defaultProductCode, defaultCatalogCategoryId, defaultCatalogBrandId, defaultIsDeleted);
    // catalogItem.setId(id);
    return catalogItem;
  }

  private static Stream<String> blankStringSource() {
    return Stream.of(null, "", " ");
  }
}
