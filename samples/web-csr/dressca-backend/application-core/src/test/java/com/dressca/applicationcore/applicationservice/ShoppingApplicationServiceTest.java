package com.dressca.applicationcore.applicationservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.baskets.CatalogItemInBasketNotFoundException;
import com.dressca.applicationcore.catalog.CatalogDomainService;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogNotFoundException;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.config.ApplicationCoreTestConfig;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.CatalogItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link ShoppingApplicationService}の動作をテストするクラスです。
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(ApplicationCoreTestConfig.class)
@TestPropertySource(properties = "spring.messages.basename=applicationcore.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class ShoppingApplicationServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private BasketRepository basketRepository;
  @Mock
  private CatalogRepository catalogRepository;
  @Mock
  private CatalogDomainService catalogDomainService;

  @Autowired
  private MessageSource messages;

  @Mock
  private AbstractStructuredLogger apLog;

  private ShoppingApplicationService service;

  @BeforeEach
  void setUp() {
    service = new ShoppingApplicationService(messages, basketRepository, catalogRepository,
        orderRepository, catalogDomainService, apLog);
  }

  @Test
  void testAddItemToBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws CatalogNotFoundException {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // 期待する戻り値
    // なし

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    CatalogItem catalogItem = createCatalogItem(catalogItemId);
    List<UUID> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);
    when(this.catalogDomainService.getExistCatalogItems(catalogItemIds))
        .thenReturn(List.of(catalogItem));

    // テストメソッドの実行
    service.addItemToBasket(buyerId, catalogItemId, 1);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
    verify(this.catalogDomainService, times(1)).getExistCatalogItems(catalogItemIds);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testAddItemToBasket_正常系_商品追加処理後に数量が0となる場合買い物かごアイテムは削除される() throws CatalogNotFoundException {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();
    BigDecimal price = BigDecimal.valueOf(1000);

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(catalogItemId, price, 1);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    CatalogItem catalogItem = createCatalogItem(catalogItemId);
    List<UUID> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);
    when(this.catalogDomainService.getExistCatalogItems(catalogItemIds))
        .thenReturn(List.of(catalogItem));

    // テストメソッドの実行
    service.addItemToBasket(buyerId, catalogItemId, -1);

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
  void testAddItemToBasket_異常系_カタログに存在しない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(false);

    try {
      // テストメソッドの実行
      service.addItemToBasket(buyerId, catalogItemId, 1);
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
  void testSetQuantities_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException,
      CatalogNotFoundException, CatalogItemInBasketNotFoundException {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(catalogItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);

    // テストメソッドの実行
    int newQuantity = 5;
    Map<UUID, Integer> quantities = Map.of(catalogItemId, newQuantity);
    service.setQuantities(buyerId, quantities);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在する商品を指定すると買い物かごの商品数が更新される() throws BasketNotFoundException,
      CatalogNotFoundException, CatalogItemInBasketNotFoundException {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(catalogItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);

    // テストメソッドの実行
    int newQuantity = 5;
    Map<UUID, Integer> quantities = Map.of(catalogItemId, newQuantity);
    service.setQuantities(buyerId, quantities);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().get(0).getQuantity()).isEqualTo(newQuantity);
  }

  @Test
  void testSetQuantities_異常系_カタログリポジトリに存在しない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID deletedCatalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    CatalogItem deletedCatalogItem = createCatalogItem(deletedCatalogItemId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> catalogItemIds = List.of(deletedCatalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(false);
    when(this.catalogRepository.findDeletedItemsByCatalogItemIdIn(catalogItemIds))
        .thenReturn(List.of(deletedCatalogItem));

    try {
      // テストメソッドの実行
      Map<UUID, Integer> quantities = Map.of(deletedCatalogItemId, 5);
      service.setQuantities(buyerId, quantities);
      fail("CatalogNotFoundException が発生しなければ失敗");
    } catch (CatalogNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
      verify(this.catalogRepository, times(1)).findDeletedItemsByCatalogItemIdIn(catalogItemIds);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("CatalogNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testSetQuantities_異常系_買い物かごに入っていない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(UUID.randomUUID(), BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> catalogItemIds = List.of(catalogItemId);
    when(this.catalogDomainService.existAll(catalogItemIds)).thenReturn(true);

    try {
      // テストメソッドの実行
      Map<UUID, Integer> quantities = Map.of(catalogItemId, 5);
      service.setQuantities(buyerId, quantities);
      fail("CatalogItemInBasketNotFoundException が発生しなければ失敗");
    } catch (CatalogItemInBasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.catalogDomainService, times(1)).existAll(catalogItemIds);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("CatalogItemInBasketNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testDeleteItemFromBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException,
      CatalogNotFoundException, CatalogItemInBasketNotFoundException {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
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
  void testDeleteItemFromBasket_正常系_買い物かごから指定の商品が削除されている() throws BasketNotFoundException,
      CatalogNotFoundException, CatalogItemInBasketNotFoundException {
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
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
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogDomainService.existCatalogItemIncludingDeleted(catalogItemId))
        .thenReturn(false);

    try {
      // テストメソッドの実行
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
    UUID buyerId = UUID.randomUUID();
    UUID catalogItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
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
    UUID dummyBuyerId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(dummyBuyerId);
    UUID itemId1 = UUID.randomUUID();
    UUID itemId2 = UUID.randomUUID();
    basket.addItem(itemId1, BigDecimal.valueOf(1000), 1);
    basket.addItem(itemId2, BigDecimal.valueOf(2000), 1);
    when(this.basketRepository.findByBuyerId(dummyBuyerId)).thenReturn(Optional.of(basket));
    List<CatalogItem> items = List.of(
        new CatalogItem(itemId1, "name1", "desc1", BigDecimal.valueOf(1000), "code1",
            UUID.randomUUID(), UUID.randomUUID(), false),
        new CatalogItem(itemId2, "name2", "desc2", BigDecimal.valueOf(2000), "code2",
            UUID.randomUUID(), UUID.randomUUID(), false));
    List<UUID> catalogItemIds = List.of(itemId1, itemId2);
    when(this.catalogRepository.findByCatalogItemIdInIncludingDeleted(catalogItemIds))
        .thenReturn(items);

    // テストメソッドの実行
    BasketDetail actual = service.getBasketDetail(dummyBuyerId);
    assertThat(actual.catalogItems.size()).isEqualTo(2);
    assertThat(actual.catalogItems.get(0).getId()).isEqualTo(itemId1);
    assertThat(actual.catalogItems.get(1).getId()).isEqualTo(itemId2);

    // モックが想定通り呼び出されていることの確認
    verify(this.catalogRepository, times(1)).findByCatalogItemIdInIncludingDeleted(catalogItemIds);
  }

  @ParameterizedTest
  @MethodSource("blankBuyerIdSource")
  void testGetBasketDetail_異常系_購入者IDがnullまたは空白なら例外が発生する(UUID buyerId)
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
    UUID buyerId = UUID.randomUUID();
    Basket basket = new Basket(buyerId);
    UUID catalogItemId = UUID.randomUUID();
    basket.addItem(catalogItemId, BigDecimal.valueOf(100_000_000), 1);
    ShipTo shipToAddress = createDefaultShipTo();
    List<CatalogItem> catalogItems = List.of(createCatalogItem(catalogItemId));
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.catalogRepository.findByCatalogItemIdIn(List.of(catalogItemId)))
        .thenReturn(catalogItems);
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
    UUID buyerId = UUID.randomUUID();
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

    return List.of(new OrderItem(
        new CatalogItemOrdered(UUID.randomUUID(), productName, productCode),
        BigDecimal.valueOf(100_000_000L), 1));
  }

  private CatalogItem createCatalogItem(UUID id) {
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    return new CatalogItem(id, defaultName, defaultDescription, defaultPrice, defaultProductCode,
        UUID.randomUUID(), UUID.randomUUID(), defaultIsDeleted);
  }

  private static Stream<UUID> blankBuyerIdSource() {
    return Stream.<UUID>of((UUID) null);
  }
}
