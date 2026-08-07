package com.dressca.domainmodules.shopping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dressca.domainmodules.common.config.DomainModulesTestConfig;
import com.dressca.domainmodules.common.log.AbstractStructuredLogger;
import com.dressca.domainmodules.shopping.dto.BasketDetail;
import com.dressca.domainmodules.shopping.exception.BasketNotFoundException;
import com.dressca.domainmodules.shopping.exception.DisplayItemInBasketNotFoundException;
import com.dressca.domainmodules.shopping.exception.DisplayItemNotFoundException;
import com.dressca.domainmodules.shopping.exception.EmptyBasketOnCheckoutException;
import com.dressca.domainmodules.shopping.internal.domain.DisplayItemDomainService;
import com.dressca.domainmodules.shopping.internal.domain.repository.BasketRepository;
import com.dressca.domainmodules.shopping.internal.domain.repository.DisplayItemRepository;
import com.dressca.domainmodules.shopping.internal.domain.repository.OrderRepository;
import com.dressca.domainmodules.shopping.model.Basket;
import com.dressca.domainmodules.shopping.model.DisplayItem;
import com.dressca.domainmodules.shopping.model.Order;
import com.dressca.domainmodules.shopping.model.OrderItem;
import com.dressca.domainmodules.shopping.valueobject.Address;
import com.dressca.domainmodules.shopping.valueobject.DisplayItemOrdered;
import com.dressca.domainmodules.shopping.valueobject.ShipTo;
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
@Import(DomainModulesTestConfig.class)
@TestPropertySource(properties = "spring.messages.basename=domainmodules.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class ShoppingApplicationServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private BasketRepository basketRepository;
  @Mock
  private DisplayItemRepository displayItemRepository;
  @Mock
  private DisplayItemDomainService displayItemDomainService;

  @Autowired
  private MessageSource messages;

  @Mock
  private AbstractStructuredLogger apLog;

  private ShoppingApplicationService service;

  @BeforeEach
  void setUp() {
    service = new ShoppingApplicationService(messages, basketRepository, displayItemRepository,
        orderRepository, displayItemDomainService, apLog);
  }

  @Test
  void testAddItemToBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws DisplayItemNotFoundException {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // 期待する戻り値
    // なし

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    DisplayItem displayItem = createDisplayItem(displayItemId);
    List<UUID> displayItemIds = List.of(displayItemId);
    when(this.displayItemDomainService.existAll(displayItemIds)).thenReturn(true);
    when(this.displayItemDomainService.getExistDisplayItems(displayItemIds))
        .thenReturn(List.of(displayItem));

    // Act
    // テストメソッドの実行
    service.addItemToBasket(buyerId, displayItemId, 1);

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayItemDomainService, times(1)).existAll(displayItemIds);
    verify(this.displayItemDomainService, times(1)).getExistDisplayItems(displayItemIds);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testAddItemToBasket_正常系_商品追加処理後に数量が0となる場合買い物かごアイテムは削除される()
      throws DisplayItemNotFoundException {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();
    BigDecimal price = BigDecimal.valueOf(1000);

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(displayItemId, price, 1);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    DisplayItem displayItem = createDisplayItem(displayItemId);
    List<UUID> displayItemIds = List.of(displayItemId);
    when(this.displayItemDomainService.existAll(displayItemIds)).thenReturn(true);
    when(this.displayItemDomainService.getExistDisplayItems(displayItemIds))
        .thenReturn(List.of(displayItem));

    // Act
    // テストメソッドの実行
    service.addItemToBasket(buyerId, displayItemId, -1);

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayItemDomainService, times(1)).existAll(displayItemIds);
    verify(this.displayItemDomainService, times(1)).getExistDisplayItems(displayItemIds);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().size()).isEqualTo(0);
  }

  @Test
  void testAddItemToBasket_異常系_陳列品に存在しない商品が指定された場合は例外が発生する() {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> displayItemIds = List.of(displayItemId);
    when(this.displayItemDomainService.existAll(displayItemIds)).thenReturn(false);

    try {
      // Act
      // テストメソッドの実行
      service.addItemToBasket(buyerId, displayItemId, 1);
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    } catch (DisplayItemNotFoundException e) {
      // Assert
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.displayItemDomainService, times(1)).existAll(displayItemIds);
      verify(this.displayItemDomainService, times(0)).getExistDisplayItems(any());
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testSetQuantities_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(displayItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> displayItemIds = List.of(displayItemId);
    when(this.displayItemDomainService.existAll(displayItemIds)).thenReturn(true);

    // Act
    // テストメソッドの実行
    int newQuantity = 5;
    Map<UUID, Integer> quantities = Map.of(displayItemId, newQuantity);
    service.setQuantities(buyerId, quantities);

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayItemDomainService, times(1)).existAll(displayItemIds);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在する商品を指定すると買い物かごの商品数が更新される() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(displayItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> displayItemIds = List.of(displayItemId);
    when(this.displayItemDomainService.existAll(displayItemIds)).thenReturn(true);

    // Act
    // テストメソッドの実行
    int newQuantity = 5;
    Map<UUID, Integer> quantities = Map.of(displayItemId, newQuantity);
    service.setQuantities(buyerId, quantities);

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.displayItemDomainService, times(1)).existAll(displayItemIds);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().get(0).getQuantity()).isEqualTo(newQuantity);
  }

  @Test
  void testSetQuantities_異常系_陳列品リポジトリに存在しない商品が指定された場合は例外が発生する() {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID deletedDisplayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    DisplayItem deletedDisplayItem = createDisplayItem(deletedDisplayItemId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> displayItemIds = List.of(deletedDisplayItemId);
    when(this.displayItemDomainService.existAll(displayItemIds)).thenReturn(false);
    when(this.displayItemRepository.findDeletedItemsByDisplayItemIdIn(displayItemIds))
        .thenReturn(List.of(deletedDisplayItem));

    try {
      // Act
      // テストメソッドの実行
      Map<UUID, Integer> quantities = Map.of(deletedDisplayItemId, 5);
      service.setQuantities(buyerId, quantities);
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    } catch (DisplayItemNotFoundException e) {
      // Assert
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.displayItemDomainService, times(1)).existAll(displayItemIds);
      verify(this.displayItemRepository, times(1))
          .findDeletedItemsByDisplayItemIdIn(displayItemIds);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testSetQuantities_異常系_買い物かごに入っていない商品が指定された場合は例外が発生する() {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(UUID.randomUUID(), BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    List<UUID> displayItemIds = List.of(displayItemId);
    when(this.displayItemDomainService.existAll(displayItemIds)).thenReturn(true);

    try {
      // Act
      // テストメソッドの実行
      Map<UUID, Integer> quantities = Map.of(displayItemId, 5);
      service.setQuantities(buyerId, quantities);
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
    } catch (DisplayItemInBasketNotFoundException e) {
      // Assert
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.displayItemDomainService, times(1)).existAll(displayItemIds);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testDeleteItemFromBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(displayItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayItemDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(true);

    // Act
    // テストメソッドの実行
    service.deleteItemFromBasket(buyerId, displayItemId);

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testDeleteItemFromBasket_正常系_買い物かごから指定の商品が削除されている() throws BasketNotFoundException,
      DisplayItemNotFoundException, DisplayItemInBasketNotFoundException {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    basket.addItem(displayItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayItemDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(true);

    // Act
    // テストメソッドの実行
    service.deleteItemFromBasket(buyerId, displayItemId);

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertEquals(0, argBasket.getItems().size());
  }

  @Test
  void testDeleteItemFromBasket_異常系_陳列品リポジトリに存在しない商品が指定された場合は例外が発生する() {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayItemDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(false);

    try {
      // Act
      // テストメソッドの実行
      service.deleteItemFromBasket(buyerId, displayItemId);
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    } catch (DisplayItemNotFoundException e) {
      // Assert
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testDeleteItemFromBasket_異常系_買い物かごに入っていない商品が指定された場合は例外が発生する() {
    // Arrange
    // テスト用の入力データ
    UUID buyerId = UUID.randomUUID();
    UUID displayItemId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(UUID.randomUUID(), buyerId);
    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayItemDomainService.existDisplayItemIncludingDeleted(displayItemId))
        .thenReturn(true);

    try {
      // Act
      // テストメソッドの実行
      service.deleteItemFromBasket(buyerId, displayItemId);
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
    } catch (DisplayItemInBasketNotFoundException e) {
      // Assert
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findByBuyerId(buyerId);
      verify(this.basketRepository, times(0)).update(any());
    } catch (Exception e) {
      fail("DisplayItemInBasketNotFoundException が発生しなければ失敗");
    }
  }

  @Test
  void testGetBasketDetail_正常系_陳列品IDに対応する陳列品情報が取得されること() throws BasketNotFoundException {
    // Arrange
    // テスト用の入力データ
    UUID dummyBuyerId = UUID.randomUUID();

    // モックの設定
    Basket basket = new Basket(dummyBuyerId);
    UUID itemId1 = UUID.randomUUID();
    UUID itemId2 = UUID.randomUUID();
    basket.addItem(itemId1, BigDecimal.valueOf(1000), 1);
    basket.addItem(itemId2, BigDecimal.valueOf(2000), 1);
    when(this.basketRepository.findByBuyerId(dummyBuyerId)).thenReturn(Optional.of(basket));
    List<DisplayItem> items = List.of(
        new DisplayItem(itemId1, "name1", "desc1", BigDecimal.valueOf(1000), "code1",
            UUID.randomUUID(), UUID.randomUUID(), false),
        new DisplayItem(itemId2, "name2", "desc2", BigDecimal.valueOf(2000), "code2",
            UUID.randomUUID(), UUID.randomUUID(), false));
    List<UUID> displayItemIds = List.of(itemId1, itemId2);
    when(this.displayItemRepository.findByDisplayItemIdInIncludingDeleted(displayItemIds))
        .thenReturn(items);

    // Act
    // テストメソッドの実行
    BasketDetail actual = service.getBasketDetail(dummyBuyerId);
    assertThat(actual.getDisplayItems().size()).isEqualTo(2);
    assertThat(actual.getDisplayItems().get(0).getId()).isEqualTo(itemId1);
    assertThat(actual.getDisplayItems().get(1).getId()).isEqualTo(itemId2);

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.displayItemRepository, times(1))
        .findByDisplayItemIdInIncludingDeleted(displayItemIds);
  }

  @ParameterizedTest
  @MethodSource("blankBuyerIdSource")
  void testGetBasketDetail_異常系_購入者IDがnullまたは空白なら例外が発生する(UUID buyerId)
      throws IllegalArgumentException {
    // Act
    // テストメソッドの実行
    try {
      service.getBasketDetail(buyerId);
    } catch (IllegalArgumentException e) {
      // Assert
      assertThat(e.getMessage()).startsWith("buyerIdがnullまたは空文字");
    }

    // Assert
    // モックが想定通り呼び出されていることの確認
    verify(this.displayItemRepository, times(0)).findByDisplayItemIdInIncludingDeleted(any());
  }

  @Test
  void testCheckout_正常系_注文リポジトリのAddを1回呼出す() throws Exception {
    // Arrange
    UUID buyerId = UUID.randomUUID();
    Basket basket = new Basket(buyerId);
    UUID displayItemId = UUID.randomUUID();
    basket.addItem(displayItemId, BigDecimal.valueOf(100_000_000), 1);
    ShipTo shipToAddress = createDefaultShipTo();
    List<DisplayItem> displayItems = List.of(createDisplayItem(displayItemId));
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.basketRepository.findByBuyerId(buyerId)).thenReturn(Optional.of(basket));
    when(this.displayItemRepository.findByDisplayItemIdIn(List.of(displayItemId)))
        .thenReturn(displayItems);
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

    return List
        .of(new OrderItem(new DisplayItemOrdered(UUID.randomUUID(), productName, productCode),
            BigDecimal.valueOf(100_000_000L), 1));
  }

  private DisplayItem createDisplayItem(UUID id) {
    String defaultDescription = "Description.";
    String defaultName = "Name";
    BigDecimal defaultPrice = BigDecimal.valueOf(100_000_000L);
    String defaultProductCode = "C000000001";
    boolean defaultIsDeleted = false;

    return new DisplayItem(id, defaultName, defaultDescription, defaultPrice, defaultProductCode,
        UUID.randomUUID(), UUID.randomUUID(), defaultIsDeleted);
  }

  private static Stream<UUID> blankBuyerIdSource() {
    return Stream.<UUID>of((UUID) null);
  }
}
