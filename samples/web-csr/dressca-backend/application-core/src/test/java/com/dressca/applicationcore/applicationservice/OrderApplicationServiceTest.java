package com.dressca.applicationcore.applicationservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.CatalogItemOrdered;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderNotFoundException;
import com.dressca.applicationcore.order.OrderRepository;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link OrderApplicationService}の動作をテストするクラスです。
 */
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@TestPropertySource(properties = "spring.messages.basename=applicationcore.messages")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class OrderApplicationServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Autowired
  private MessageSource messages;
  @Mock
  private AbstractStructuredLogger apLog;

  private OrderApplicationService service;

  @BeforeEach
  void setUp() {
    service = new OrderApplicationService(messages, orderRepository, apLog);
  }

  @Test
  void testGetOrder_正常系_注文リポジトリから取得した情報と指定した購入者IDが合致する場合注文情報を取得できる() throws Exception {
    // Arrange
    long orderId = 1L;
    String buyerId = UUID.randomUUID().toString();
    ShipTo shipToAddress = createDefaultShipTo();
    Order order = new Order(buyerId, shipToAddress, createDefaultOrderItems());

    when(this.orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    // Act
    Order actual = null;
    actual = service.getOrder(orderId, buyerId);

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
}