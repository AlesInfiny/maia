package com.dressca.applicationcore.baskets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BasketApplicationServiceTest {
  @Mock
  private BasketRepository repository;
  @InjectMocks
  private BasketApplicationService service;

  @Test
  void testAddItemToBasket_正常系_リポジトリのupdateを1度だけ呼出す() {

    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);
    int quantity = 1;

    // 期待する戻り値
    // なし

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.repository.findById(basketId)).thenReturn(Optional.of(basket));

    try {
      // テストメソッドの実行
      service.addItemToBasket(basketId, catalogItemId, price, quantity);
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      verify(this.repository, times(1)).update(basket);
    } catch (BasketNotFoundException e) {
      e.printStackTrace();
      fail(e);
    }
  }

  @Test
  void testAddItemToBasket_正常系_商品追加処理後に数量が0となる場合買い物かごアイテムは削除される() {
    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);
    int quantity = -1;

    // モックの設定
    Basket basket = new Basket("dummy");
    basket.addItem(catalogItemId, price, 1);
    when(this.repository.findById(basketId)).thenReturn(Optional.of(basket));

    try {
      // テストメソッドの実行
      service.addItemToBasket(basketId, catalogItemId, price, quantity);
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
      verify(this.repository, times(1)).update(captor.capture());
      Basket argBasket = captor.getValue();
      assertThat(argBasket.getItems().size()).isEqualTo(0);
    } catch (BasketNotFoundException e) {
      e.printStackTrace();
      fail(e);
    }
  }

  @Test
  void testAddItemToBasket_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    BigDecimal price = BigDecimal.valueOf(1000);
    int quantity = 1;

    // モックの設定
    when(this.repository.findById(basketId)).thenReturn(Optional.empty());

    try {
      // テストメソッドの実行
      service.addItemToBasket(basketId, catalogItemId, price, quantity);
      fail("BasketNotFoundException が発生しなければ失敗");
    } catch (BasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      verify(this.repository, times(0)).update(any());
    }
  }

  @Test
  void testDeleteBasket_正常系_リポジトリのremoveを1度だけ呼出す() {

    // テスト用の入力データ
    long basketId = 1L;

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.repository.findById(basketId)).thenReturn(Optional.of(basket));

    try {
      // テストメソッドの実行
      service.deleteBasket(basketId);
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      verify(this.repository, times(1)).remove(basket);
      ;
    } catch (BasketNotFoundException e) {
      e.printStackTrace();
      fail(e);
    }
  }

  @Test
  void testDeleteToBasket_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    long basketId = 1L;

    // モックの設定
    when(this.repository.findById(basketId)).thenReturn(Optional.empty());

    try {
      // テストメソッドの実行
      service.deleteBasket(basketId);
      fail("BasketNotFoundException が発生しなければ失敗");
    } catch (BasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      verify(this.repository, times(0)).remove(any());
    }
  }

  @Test
  void testSetQuantities_正常系_リポジトリのupdateを1度だけ呼出す() {

    // テスト用の入力データ
    long basketId = 1L;
    Map<Long, Integer> quantities = Map.of(1L, 5);

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.repository.findById(basketId)).thenReturn(Optional.of(basket));

    try {
      // テストメソッドの実行
      service.setQuantities(basketId, quantities);
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      verify(this.repository, times(1)).update(basket);
    } catch (BasketNotFoundException e) {
      e.printStackTrace();
      fail(e);
    }
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在しない商品を指定しても買い物かごには追加されない() {

    // テスト用の入力データ
    long basketId = 1L;
    Map<Long, Integer> quantities = Map.of(1L, 5);

    // モックの設定
    Basket basket = new Basket("dummy");
    when(this.repository.findById(basketId)).thenReturn(Optional.of(basket));

    try {
      // テストメソッドの実行
      service.setQuantities(basketId, quantities);
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
      verify(this.repository, times(1)).update(captor.capture());
      Basket argBasket = captor.getValue();
      assertThat(argBasket.getItems().size()).isEqualTo(0);
    } catch (BasketNotFoundException e) {
      e.printStackTrace();
      fail(e);
    }
  }

  @Test
  void testSetQuantities_正常系_買い物かごに存在する商品を指定すると買い物かごの商品数が更新される() {

    // テスト用の入力データ
    long basketId = 1L;
    long catalogItemId = 1L;
    int newQuantity = 5;
    Map<Long, Integer> quantities = Map.of(catalogItemId, newQuantity);

    // モックの設定
    Basket basket = new Basket("dummy");
    basket.addItem(catalogItemId, BigDecimal.valueOf(1000), 100);
    when(this.repository.findById(basketId)).thenReturn(Optional.of(basket));

    try {
      // テストメソッドの実行
      service.setQuantities(basketId, quantities);
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
      verify(this.repository, times(1)).update(captor.capture());
      Basket argBasket = captor.getValue();
      assertThat(argBasket.getItems().get(0).getQuantity()).isEqualTo(newQuantity);
    } catch (BasketNotFoundException e) {
      e.printStackTrace();
      fail(e);
    }
  }

  @Test
  void testSetQuantities_異常系_買い物かごが見つからない場合は例外が発生する() {
    // テスト用の入力データ
    long basketId = 1L;
    Map<Long, Integer> quantities = Map.of(1L, 5);

    // モックの設定
    when(this.repository.findById(basketId)).thenReturn(Optional.empty());

    try {
      // テストメソッドの実行
      service.setQuantities(basketId, quantities);
      fail("BasketNotFoundException が発生しなければ失敗");
    } catch (BasketNotFoundException e) {
      // モックが想定通り呼び出されていることの確認
      verify(this.repository, times(1)).findById(basketId);
      verify(this.repository, times(0)).update(any());
    }
  }

  @Test
  void testGetOrCreateBasketForUser_正常系_購入者Idに対応する買い物かご情報が存在しない場合は新規作成する() {

    // テスト用の入力データ
    String dummyBuyerId = "dummyId";

    // モックの設定
    when(this.repository.findByBuyerId(dummyBuyerId)).thenReturn(Optional.empty());
    when(this.repository.add(any())).thenReturn(new Basket(dummyBuyerId));

    // テストメソッドの実行
    Basket basket = service.getOrCreateBasketForUser(dummyBuyerId);
    assertThat(basket.getBuyerId()).isEqualTo(dummyBuyerId);
    // モックが想定通り呼び出されていることの確認
    verify(this.repository, times(1)).findByBuyerId(dummyBuyerId);
    verify(this.repository, times(1)).add(basket);
  }

  @Test
  void testGetOrCreateBasketForUser_正常系_購入者Idに対応する買い物かご情報が存在する場合はその情報を返す() {

    // テスト用の入力データ
    String dummyBuyerId = "dummyId";

    // モックの設定
    Basket basket = new Basket(dummyBuyerId);
    basket.addItem(1L, BigDecimal.valueOf(1000), 1);
    basket.addItem(2L, BigDecimal.valueOf(2000), 1);
    when(this.repository.findByBuyerId(dummyBuyerId)).thenReturn(Optional.of(basket));

    // テストメソッドの実行
    Basket actual = service.getOrCreateBasketForUser(dummyBuyerId);
    assertThat(actual.getBuyerId()).isEqualTo(dummyBuyerId);
    assertThat(actual.getItems().get(0).getCatalogItemId()).isEqualTo(1L);
    assertThat(actual.getItems().get(1).getCatalogItemId()).isEqualTo(2L);
    // モックが想定通り呼び出されていることの確認
    verify(this.repository, times(1)).findByBuyerId(dummyBuyerId);
    verify(this.repository, times(0)).add(any());
  }

  @ParameterizedTest
  @MethodSource("blankStringSource")
  void testGetOrCreateBasketForUser_異常系_購入者Idがnullまたは空白なら例外が発生する(String buyerId) {
    // テストメソッドの実行
    try {
      service.getOrCreateBasketForUser(buyerId);
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage()).startsWith("buyerIdがnullまたは空文字");
    }
    // // モックが想定通り呼び出されていることの確認
    verify(this.repository, times(0)).findByBuyerId(any());
    verify(this.repository, times(0)).add(any());
  }

  private static Stream<String> blankStringSource() {
    return Stream.of(null, "", " ");
  }
}
