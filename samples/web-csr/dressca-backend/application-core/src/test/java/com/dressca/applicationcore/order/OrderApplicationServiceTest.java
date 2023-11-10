package com.dressca.applicationcore.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.baskets.BasketRepository;
import com.dressca.applicationcore.catalog.CatalogItem;
import com.dressca.applicationcore.catalog.CatalogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class OrderApplicationServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private BasketRepository basketRepository;
  @Mock
  private CatalogRepository catalogRepository;
  @InjectMocks
  private OrderApplicationService service;

  @Test
  void testCreateOrder_正常系_注文リポジトリのAddを1回呼出す() {
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
    try {
      service.createOrder(basketId, shipToAddress);
    } catch (Exception e) {
      fail("例外は発生しないはず", e);
    }

    // Assert
    verify(this.orderRepository, times(1)).add(any());
  }

  @Test
  void testCreateOrder_異常系_指定した買い物かごが存在しない場合は業務例外が発生する() {
    // Arrange
    long basketId = 999L;
    ShipTo shipToAddress = createDefaultShipTo();
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.empty());

    // Act
    Executable action = () -> service.createOrder(basketId, shipToAddress);

    // Assert
    assertThrows(BasketNotFoundException.class, action);
  }

  @Test
  void testCreateOrder_異常系_指定した買い物かごが空の場合は業務例外が発生する() {
    // Arrange
    long basketId = 1L;
    String buyerId = UUID.randomUUID().toString();
    Basket basket = new Basket(buyerId);
    ShipTo shipToAddress = createDefaultShipTo();
    when(this.basketRepository.findById(basketId)).thenReturn(Optional.of(basket));

    // Act
    Executable action = () -> service.createOrder(basketId, shipToAddress);

    // Assert
    assertThrows(EmptyBasketOnCheckoutException.class, action);
  }

  @Test
  void testGetOrder_正常系_注文リポジトリから取得した情報と指定した購入者IDが合致する場合注文情報を取得できる() {
    // Arrange
    long orderId = 1L;
    String buyerId = UUID.randomUUID().toString();
    ShipTo shipToAddress = createDefaultShipTo();
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    // Act
    Order actual = null;
    try {
      actual = service.getOrder(orderId, buyerId);
    } catch (Exception e) {
      fail("例外は発生しないはず", e);
    }

    // Assert
    assertThat(actual).isEqualTo(order);
  }

  @Test
  void testGetOrder_異常系_注文リポジトリから取得した情報と指定した購入者IDが異なる場合例外になる() {
    // Arrange
    long orderId = 1L;
    String buyerId = UUID.randomUUID().toString();
    ShipTo shipToAddress = createDefaultShipTo();
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    // Act
    Executable action = () -> service.getOrder(orderId, "dummy");

    // Assert
    assertThrows(OrderNotFoundException.class, action);
  }

  @Test
  void testGetOrder_異常系_注文リポジトリから注文情報を取得できない場合例外になる() {
    // Arrange
    long orderId = 1L;
    String buyerId = UUID.randomUUID().toString();

    when(this.orderRepository.findById(orderId)).thenReturn(Optional.empty());

    // Act
    Executable action = () -> service.getOrder(orderId, buyerId);

    // Assert
    assertThrows(OrderNotFoundException.class, action);
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
}
