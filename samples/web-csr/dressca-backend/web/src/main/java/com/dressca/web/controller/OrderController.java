package com.dressca.web.controller;

import com.dressca.applicationcore.baskets.Basket;
import com.dressca.applicationcore.baskets.BasketApplicationService;
import com.dressca.applicationcore.baskets.BasketNotFoundException;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderApplicationService;
import com.dressca.applicationcore.order.OrderItem;
import com.dressca.applicationcore.order.OrderItemAsset;
import com.dressca.applicationcore.order.OrderNotFoundException;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.web.controller.dto.AccountDto;
import com.dressca.web.controller.dto.CatalogItemSummaryDto;
import com.dressca.web.controller.dto.OrderDto;
import com.dressca.web.controller.dto.OrderItemDto;
import com.dressca.web.controller.dto.PostOrderInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link Order} の情報にアクセスするAPIコントローラーです.
 */
@RestController
@Tag(name = "Order", description = "注文の情報にアクセスするAPI")
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
  private OrderApplicationService orderApplicationService;
  @Autowired
  private BasketApplicationService basketApplicationService;

  /**
   * 注文情報を取得します.
   * 
   * @param orderId 注文 Id
   * @return 注文情報
   */
  @Operation(summary = "注文情報を取得します.", description = "注文情報を取得します.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = OrderDto.class))),
      @ApiResponse(responseCode = "404", description = "注文IDが存在しない.", content = @Content)})
  @GetMapping("{orderId}")
  public ResponseEntity<OrderDto> getById(@PathVariable("orderId") long orderId,
      HttpServletRequest req) {
    String buyerId = req.getAttribute("buyerId").toString();

    try {
      Order order = orderApplicationService.getOrder(orderId, buyerId);
      OrderDto orderDto = toOrderDto(order);
      return ResponseEntity.ok().body(orderDto);
    } catch (OrderNotFoundException e) {
      // TODO 警告ログの出力
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * 買い物かごに登録されている商品を注文します.
   * 
   * @param postOrderInput 注文に必要な配送先などの情報
   * @return なし
   */
  @Operation(summary = "買い物かごに登録されている商品を注文します.", description = "買い物かごに登録されている商品を注文します.")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "201", description = "成功.", content = @Content),
          @ApiResponse(responseCode = "400", description = "リクエストエラー.", content = @Content),
          @ApiResponse(responseCode = "500", description = "サーバーエラー.", content = @Content)})
  @PostMapping
  public ResponseEntity<?> postOrder(@RequestBody PostOrderInputDto postOrderInput,
      HttpServletRequest req) {
    String buyerId = req.getAttribute("buyerId").toString();
    Basket basket = basketApplicationService.getOrCreateBasketForUser(buyerId);

    Address address = new Address(postOrderInput.getPostalCode(), postOrderInput.getTodofuken(),
        postOrderInput.getShikuchoson(), postOrderInput.getAzanaAndOthers());
    ShipTo shipToAddress = new ShipTo(postOrderInput.getFullName(), address);
    Order order;
    try {
      order = orderApplicationService.createOrder(basket.getId(), shipToAddress);
      // 買い物かごを削除
      basketApplicationService.deleteBasket(basket.getId());
    } catch (BasketNotFoundException | EmptyBasketOnCheckoutException e) {
      // ここでは発生しえないので、システムエラーとする
      throw new SystemException(e, ExceptionIdConstant.E_SHAR0000, null, null);
    }

    String requestUri = req.getRequestURL().toString();
    return ResponseEntity.created(URI.create(requestUri + "/" + order.getId())).build();
  }

  private OrderDto toOrderDto(Order order) {
    return new OrderDto(
      order.getId(), 
      order.getBuyerId(), 
      order.getOrderDate(), 
      order.getShipToAddress().getFullName(), 
      order.getShipToAddress().getAddress().getPostalCode(), 
      order.getShipToAddress().getAddress().getTodofuken(), 
      order.getShipToAddress().getAddress().getShikuchoson(),
      order.getShipToAddress().getAddress().getAzanaAndOthers(), 
      new AccountDto(
        order.getConsumptionTaxRate(), 
        order.getTotalItemsPrice(), 
        order.getDeliveryCharge(), 
        order.getConsumptionTax(), 
        order.getTotalPrice()),
      order.getOrderItems().stream()
        .map(this::toOrderItemDto)
        .collect(Collectors.toList()));
  }

  private OrderItemDto toOrderItemDto(OrderItem item) {
    return new OrderItemDto(
      item.getId(), 
      new CatalogItemSummaryDto(
        item.getItemOrdered().getCatalogItemId(), 
        item.getItemOrdered().getProductName(), 
        item.getItemOrdered().getProductCode(),
        item.getAssets().stream()
          .map(OrderItemAsset::getAssetCode)
          .collect(Collectors.toList())), 
      item.getQuantity(),
      item.getUnitPrice(), 
      item.getSubTotal());
  }
}
