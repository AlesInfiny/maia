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
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.baskets.DisplayItemInBasketNotFoundException;
import com.dressca.applicationcore.config.ApplicationCoreTestConfig;
import com.dressca.applicationcore.displayitem.DisplayDomainService;
import com.dressca.applicationcore.displayitem.DisplayItem;
import com.dressca.applicationcore.displayitem.DisplayItemAsset;
import com.dressca.applicationcore.displayitem.DisplayItemNotFoundException;
import com.dressca.applicationcore.displayitem.DisplayRepository;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.DisplayItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.log.AbstractStructuredLogger;

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
  private DisplayRepository displayRepository;
  @Mock
  private DisplayDomainService displayDomainService;

  @Autowired
  private MessageSource messages;

  @Mock
  private AbstractStructuredLogger apLog;

  private ShoppingApplicationService service;

  private static final Random random = new Random();

  @BeforeEach
  void setUp() {
    service = new ShoppingApplicationService(messages, basketRepository, displayRepository,
        orderRepository, displayDomainService, apLog);
  }

  @Test
  void testAddItemToBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws DisplayItemNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long displayItemId = 1L;

    // 期待する戻り値
    // なし

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    DisplayItem displayItem = createDisplayItem(displayItemId);
    List<Long> displayItemIds = List.of(displayItemId);
    when(this.displayDomainService.existAll(displayItemIds)).thenReturn(true);
    when(this.displayDomainService.getExistDisplayItems(displayItemIds))
        .thenReturn(List.of(displayItem));

    // テストメソッドの実行
    int quantity = 1;
    service.addItemToBasket(buyerId, displayItemId, quantity);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayDomainService, times(1)).existAll(displayItemIds);
    verify(this.displayDomainService, times(1)).getExistDisplayItems(displayItemIds);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testAddItemToBasket_正常系_商品追加処理後に数量が0となる場合買い物かごアイテムは削除される()
      throws DisplayItemNotFoundException {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long displayItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(displayItemId, price, 1);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    DisplayItem displayItem = createDisplayItem(displayItemId);
    List<Long> displayItemIds = List.of(displayItemId);
    when(this.displayDomainService.existAll(displayItemIds)).thenReturn(true);
    when(this.displayDomainService.getExistDisplayItems(displayItemIds))
        .thenReturn(List.of(displayItem));

    // テストメソッドの実行
    int quantity = -1;
    service.addItemToBasket(buyerId, displayItemId, quantity);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayDomainService, times(1)).existAll(displayItemIds);
    verify(this.displayDomainService, times(1)).getExistDisplayItems(displayItemIds);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().size()).isEqualTo(0);
  }

  @Test
  void testAddItemToBasket_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long displayItemId = 1L;

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<Long> displayItemIds = List.of(displayItemId);
    when(this.displayDomainService.existAll(displayItemIds)).thenReturn(false);

    try {
      // テストメソッドの実行
      int quantity = 1;
      service.addItemToBasket(buyerId, displayItemId, quantity);
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    } catch (DisplayItemNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.displayDomainService, times(1)).existAll(displayItemIds);
      verify(this.displayDomainService, times(0)).getExistDisplayItems(any());
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testSetQuantities_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    List<Long> displayItemIds = List.of(1L);

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(displayItemIds.get(0), BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existAll(displayItemIds)).thenReturn(true);

    // テストメソッドの実行
    int newQuantity = 5;
    Map<Long, Integer> quantities = Map.of(displayItemIds.get(0), newQuantity);
    service.setQuantities(buyerId, quantities);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayDomainService, times(1)).existAll(displayItemIds);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在する商品を指定すると買い物かごの商品数が更新される() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    List<Long> displayItemIds = List.of(1L);

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(displayItemIds.get(0), BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existAll(displayItemIds)).thenReturn(true);

    // テストメソッドの実行
    int newQuantity = 5;
    Map<Long, Integer> quantities = Map.of(displayItemIds.get(0), newQuantity);
    service.setQuantities(buyerId, quantities);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayDomainService, times(1)).existAll(displayItemIds);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().get(0).getQuantity()).isEqualTo(newQuantity);
  }

  @Test
  void testSetQuantities_異常系_カタログリポジトリに存在しない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long deletedDisplayItemId = 1L;
    List<Long> displayItemIds = List.of(deletedDisplayItemId);

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    DisplayItem deletedDisplayItem = createDisplayItem(deletedDisplayItemId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existAll(displayItemIds)).thenReturn(false);
    when(this.displayRepository.findDeletedItemsByDisplayItemIdIn(displayItemIds))
        .thenReturn(List.of(deletedDisplayItem));

    try {
      // テストメソッドの実行
      int newQuantity = 5;
      Map<Long, Integer> quantities = Map.of(displayItemIds.get(0), newQuantity);
      service.setQuantities(buyerId, quantities);
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    } catch (DisplayItemNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.displayDomainService, times(1)).existAll(displayItemIds);
      verify(this.displayRepository, times(1)).findDeletedItemsByDisplayItemIdIn(displayItemIds);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testSetQuantities_異常系_買い物かごに入っていない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    List<Long> displayItemIds = List.of(1L);

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(2L, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existAll(displayItemIds)).thenReturn(true);

    try {
      // テストメソッドの実行
      int newQuantity = 5;
      Map<Long, Integer> quantities = Map.of(displayItemIds.get(0), newQuantity);
      service.setQuantities(buyerId, quantities);
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
    } catch (DisplayItemInBasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.displayDomainService, times(1)).existAll(displayItemIds);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testDeleteItemFromBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long displayItemId = 1L;

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(displayItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(true);

    // テストメソッドの実行
    service.deleteItemFromBasket(buyerId, displayItemId);

    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testDeleteItemFromBasket_正常系_買い物かごから指定の商品が削除されている() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {

    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long displayItemId = 1L;

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    basket.addItem(displayItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(true);

    // テストメソッドの実行
    service.deleteItemFromBasket(buyerId, displayItemId);

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
    long displayItemId = 1L;

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(false);

    try {
      service.deleteItemFromBasket(buyerId, displayItemId);
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    } catch (DisplayItemNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testDeleteItemFromBasket_異常系_買い物かごに入っていない商品が指定された場合は例外が発生する() {
    // テスト用の入力データ
    String buyerId = UUID.randomUUID().toString();
    long displayItemId = 1L;

    // モックの設定
    long basketId = 1L;
    Basket basket = new Basket(basketId, buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(true);

    try {
      // テストメソッドの実行
      service.deleteItemFromBasket(buyerId, displayItemId);
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
    } catch (DisplayItemInBasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
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
    List<DisplayItem> items = List.of(
        new DisplayItem(1L, 1L, new ArrayList<>(), "name1", "desc1", BigDecimal.valueOf(1000),
            "code1", 1L, 1L, false),
        new DisplayItem(2L, 2L, new ArrayList<>(), "name2", "desc2", BigDecimal.valueOf(2000),
            "code2", 2L, 2L, false));
    List<Long> displayItemIds = List.of(1L, 2L);
    when(this.displayRepository.findByDisplayItemIdInIncludingDeleted(displayItemIds))
        .thenReturn(items);

    // テストメソッドの実行
    BasketDetail actual = service.getBasketDetail(dummyBuyerId);
    assertThat(actual.displayItems.size()).isEqualTo(2);
    assertThat(actual.displayItems.get(0).getId()).isEqualTo(1L);
    assertThat(actual.displayItems.get(1).getId()).isEqualTo(2L);
    // モックが想定通り呼び出されていることの確認
    verify(this.displayRepository, times(1)).findByDisplayItemIdInIncludingDeleted(displayItemIds);
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
    verify(this.displayRepository, times(0)).findByDisplayItemIdInIncludingDeleted(any());
  }

  @Test
  void testCheckout_正常系_注文リポジトリのAddを1回呼出す() throws Exception {
    // Arrange
    String buyerId = UUID.randomUUID().toString();
    Basket basket = new Basket(buyerId);
    basket.addItem(10L, BigDecimal.valueOf(100_000_000), 1);
    ShipTo shipToAddress = createDefaultShipTo();
    List<DisplayItem> displayItems = List.of(createDisplayItem(10L));
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayRepository.findByDisplayItemIdIn(List.of(10L))).thenReturn(displayItems);
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
        List.of(new OrderItem(new DisplayItemOrdered(1L, productName, productCode),
            BigDecimal.valueOf(100_000_000L), 1));

    return items;
  }

  private DisplayItem createDisplayItem(long id) {
    long defaultCatalogItemId = random.nextInt(1000);
    List<DisplayItemAsset> defaultAssets = new ArrayList<>();
    long defaultDisplayCategoryId = random.nextInt(1000);
    long defaultDisplayBrandId = random.nextInt(1000);
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    DisplayItem displayItem = new DisplayItem(id, defaultCatalogItemId, defaultAssets, defaultName,
        defaultDescription, defaultPrice, defaultProductCode, defaultDisplayCategoryId,
        defaultDisplayBrandId, defaultIsDeleted);
    // displayItem.setId(id);
    return displayItem;
  }

  private static Stream<String> blankStringSource() {
    return Stream.of(null, "", " ");
  }
}
