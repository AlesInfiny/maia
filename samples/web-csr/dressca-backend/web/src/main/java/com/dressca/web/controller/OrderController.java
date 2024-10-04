package com.dressca.web.controller;

import com.dressca.applicationcore.applicationservice.ShoppingApplicationService;
import com.dressca.applicationcore.applicationservice.OrderApplicationService;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderNotFoundException;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.constant.CommonExceptionIdConstant;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.web.controller.dto.order.OrderResponse;
import com.dressca.web.controller.dto.order.PostOrderRequest;
import com.dressca.web.mapper.OrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link Order} の情報にアクセスするAPIコントローラーです。
 */
@RestController
@Tag(name = "Orders", description = "注文の情報にアクセスするAPI")
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
  private OrderApplicationService orderApplicationService;
  @Autowired
  private ShoppingApplicationService shoppingApplicationService;

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  /**
   * 注文情報を取得します。
   * 
   * @param orderId 注文 Id
   * @return 注文情報
   */
  @Operation(summary = "注文情報を取得します.", description = "注文情報を取得します.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
      @ApiResponse(responseCode = "404", description = "注文IDが存在しない.", content = @Content) })
  @GetMapping("{orderId}")
  public ResponseEntity<OrderResponse> getById(@PathVariable("orderId") long orderId,
      HttpServletRequest req) {
    String buyerId = req.getAttribute("buyerId").toString();

    try {
      Order order = orderApplicationService.getOrder(orderId, buyerId);
      OrderResponse orderDto = OrderMapper.convert(order);
      return ResponseEntity.ok().body(orderDto);
    } catch (OrderNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * 買い物かごに登録されている商品を注文します。
   * 
   * @param postOrderInput 注文に必要な配送先などの情報
   * @return なし
   */
  @Operation(summary = "買い物かごに登録されている商品を注文します.", description = "買い物かごに登録されている商品を注文します.")
  @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "成功.", content = @Content),
      @ApiResponse(responseCode = "400", description = "リクエストエラー.", content = @Content),
      @ApiResponse(responseCode = "500", description = "サーバーエラー.", content = @Content) })
  @PostMapping
  public ResponseEntity<?> postOrder(@RequestBody @Valid PostOrderRequest postOrderInput,
      HttpServletRequest req) {
    String buyerId = req.getAttribute("buyerId").toString();
    Address address = new Address(postOrderInput.getPostalCode(), postOrderInput.getTodofuken(),
        postOrderInput.getShikuchoson(), postOrderInput.getAzanaAndOthers());
    ShipTo shipToAddress = new ShipTo(postOrderInput.getFullName(), address);
    Order order;
    try {
      order = shoppingApplicationService.checkout(buyerId, shipToAddress);
    } catch (EmptyBasketOnCheckoutException e) {
      // ここでは発生しえないので、システムエラーとする
      throw new SystemException(e, CommonExceptionIdConstant.E_SYSTEM, null, null);
    }

    String requestUri = req.getRequestURL().toString();
    return ResponseEntity.created(URI.create(requestUri + "/" + order.getId())).build();
  }
}
