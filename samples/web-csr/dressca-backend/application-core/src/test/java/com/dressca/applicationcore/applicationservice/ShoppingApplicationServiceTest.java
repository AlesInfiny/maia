package com.dressca.applicationcore.applicationservice;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.CatalogItemOrdered;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;

/**
 * {@link ShoppingApplicationService}の動作をテストするクラスです。
 */
@ExtendWith(SpringExtension.class)
public class ShoppingApplicationServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private BasketRepository basketRepository;
  @Mock
  private CatalogRepository catalogRepository;

  @InjectMocks
  private ShoppingApplicationService service;

  @Test
  void testAddItemToBasket_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException {

    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);
    int quantity = 1;

    // 期待する戻り値
    // なし

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    service.addItemToBasket(basketId, catalogItemId, price, quantity);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findById(basketId);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testAddItemToBasket_正常系_商品追加処理後に数量が0となる場合買い物かごアイテムは削除される() throws BasketNotFoundException {
    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);
    int quantity = -1;

    // モックの設定
    Basket basket = new Basket("dummy");
    basket.addItem(catalogItemId, price, 1);
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    service.addItemToBasket(basketId, catalogItemId, price, quantity);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findById(basketId);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().size()).isEqualTo(0);
  }

  @Test
  void testAddItemToBasket_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);
    int quantity = 1;

    // モックの設定
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.empty());

    try {
      // テストメソッドの実行
      service.addItemToBasket(basketId, catalogItemId, price, quantity);
      fail("BasketNotFoundException が発生しなければ失敗");
    } catch (BasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findById(basketId);
      verify(this.basketRepository, times(0)).update(any());
    }
  }

  @Test
  void testDeleteBasket_正常系_リポジトリのremoveを1度だけ呼出す() throws BasketNotFoundException {

    // テスト用の入力データ
    long basketId = 1L;

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    service.deleteBasket(basketId);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findById(basketId);
    verify(this.basketRepository, times(1)).remove(basket);
  }

  @Test
  void testDeleteToBasket_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    long basketId = 1L;

    // モックの設定
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.empty());

    try {
      // テストメソッドの実行
      service.deleteBasket(basketId);
      fail("BasketNotFoundException が発生しなければ失敗");
    } catch (BasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findById(basketId);
      verify(this.basketRepository, times(0)).remove(any());
    }
  }

  @Test
  void testSetQuantities_正常系_リポジトリのupdateを1度だけ呼出す() throws BasketNotFoundException {

    // テスト用の入力データ
    long basketId = 1L;
    Map<Long, Integer> quantities = Map.of(1L, 5);

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    service.setQuantities(basketId, quantities);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findById(basketId);
    verify(this.basketRepository, times(1)).update(basket);
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在しない商品を指定しても買い物かごには追加されない() throws BasketNotFoundException {

    // テスト用の入力データ
    long basketId = 1L;
    Map<Long, Integer> quantities = Map.of(1L, 5);

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    service.setQuantities(basketId, quantities);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findById(basketId);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().size()).isEqualTo(0);
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在する商品を指定すると買い物かごの商品数が更新される() throws BasketNotFoundException {

    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    int newQuantity = 5;
    Map<Long, Integer> quantities = Map.of(catalogItemId, newQuantity);

    // モックの設定
    Basket basket = new Basket("dummy");
    basket.addItem(catalogItemId, BigDecimal.valueOf(1000), 100);
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    service.setQuantities(basketId, quantities);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findById(basketId);
    ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
    verify(this.basketRepository, times(1)).update(captor.capture());
    Basket argBasket = captor.getValue();
    assertThat(argBasket.getItems().get(0).getQuantity()).isEqualTo(newQuantity);
  }

  @Test
  void testSetQuantities_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    long basketId = 1L;
    Map<Long, Integer> quantities = Map.of(1L, 5);

    // モックの設定
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.empty());

    try {
      // テストメソッドの実行
      service.setQuantities(basketId, quantities);
      fail("BasketNotFoundException が発生しなければ失敗");
    } catch (BasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.basketRepository, times(1)).findById(basketId);
      verify(this.basketRepository, times(0)).update(any());
    }
  }

  @Test
  void testGetOrCreateBasketForUser_正常系_購入者Idに対応する買い物かご情報が存在しない場合は新規作成する() throws BasketNotFoundException {

    // テスト用の入力データ
    String dummyBuyerId = "dummyId";

    // モックの設定
    when(this.basketRepository.findByBuyerId(dummyBuyerId)).thenReturn(Optional.empty());
    when(this.basketRepository.add(any())).thenReturn(new Basket(dummyBuyerId));

    // テストメソッドの実行
    Basket basket = service.getOrCreateBasketForUser(dummyBuyerId);
    assertThat(basket.getBuyerId()).isEqualTo(dummyBuyerId);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(dummyBuyerId);
    verify(this.basketRepository, times(1)).add(basket);
  }

  @Test
  void testGetOrCreateBasketForUser_正常系_購入者Idに対応する買い物かご情報が存在する場合はその情報を返す() throws BasketNotFoundException {

    // テスト用の入力データ
    String dummyBuyerId = "dummyId";

    // モックの設定
    Basket basket = new Basket(dummyBuyerId);
    basket.addItem(1L, BigDecimal.valueOf(1000), 1);
    basket.addItem(2L, BigDecimal.valueOf(2000), 1);
    when(this.basketRepository.findByBuyerId(dummyBuyerId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    Basket actual = service.getOrCreateBasketForUser(dummyBuyerId);
    assertThat(actual.getBuyerId()).isEqualTo(dummyBuyerId);
    assertThat(actual.getItems().get(0).getCatalogItemId()).isEqualTo(1L);
    assertThat(actual.getItems().get(1).getCatalogItemId()).isEqualTo(2L);
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(1)).findByBuyerId(dummyBuyerId);
    verify(this.basketRepository, times(0)).add(any());
  }

  @ParameterizedTest
  @MethodSource("blankStringSource")
  void testGetOrCreateBasketForUser_異常系_購入者Idがnullまたは空白なら例外が発生する(String buyerId) throws IllegalArgumentException {
    // テストメソッドの実行
    try {
      service.getOrCreateBasketForUser(buyerId);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).startsWith("buyerIdがnullまたは空文字");
    }
    // モックが想定通り呼び出されていることの確認
    verify(this.basketRepository, times(0)).findByBuyerId(any());
    verify(this.basketRepository, times(0)).add(any());
  }

  @Test
  void testCheckout_正常系_注文リポジトリのAddを1回呼出す() throws Exception {
    // Arrange
    long basketId = 1L;
    String buyerId = UUID.randomUUID().toString();
    Basket basket = new Basket(buyerId);
    basket.addItem(10L, BigDecimal.valueOf(100_000_000), 1);
    ShipTo shipToAddress = createDefaultShipTo();
    List<CatalogItem> catalogItems = List.of(createCatalogItem(10L));
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));
    when(this.catalogRepository.findByCatalogItemIdIn(List.of(10L))).thenReturn(catalogItems);
    when(this.orderRepository.add(any())).thenReturn(order);

    // Act
    service.checkout(basketId, shipToAddress);

    // Assert
    verify(this.orderRepository, times(1)).add(any());
  }

  @Test
  void testCheckout_異常系_指定した買い物かごが存在しない場合は業務例外が発生する() {
    // Arrange
    long basketId = 999L;
    ShipTo shipToAddress = createDefaultShipTo();
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.empty());

    // Act
    Executable action = () -> service.checkout(basketId, shipToAddress);

    // Assert
    assertThrows(BasketNotFoundException.class, action);
  }

  @Test
  void testCheckout_異常系_指定した買い物かごが空の場合は業務例外が発生する() {
    // Arrange
    long basketId = 1L;
    String buyerId = UUID.randomUUID().toString();
    Basket basket = new Basket(buyerId);
    ShipTo shipToAddress = createDefaultShipTo();
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // Act
    Executable action = () -> service.checkout(basketId, shipToAddress);

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

    List<OrderItem> items = List.of(new OrderItem(new CatalogItemOrdered(1L, productName, productCode),
        BigDecimal.valueOf(100_000_000L), 1));

    return items;
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

  private static Stream<String> blankStringSource() {
    return Stream.of(null, "", " ");
  }
}
