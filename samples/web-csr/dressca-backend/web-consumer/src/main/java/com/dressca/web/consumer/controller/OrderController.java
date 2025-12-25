package com.dressca.web.consumer.controller;

import com.dressca.applicationcore.applicationservice.ShoppingApplicationService;
import com.dressca.applicationcore.applicationservice.OrderApplicationService;
import com.dressca.applicationcore.order.Address;
import com.dressca.applicationcore.order.EmptyBasketOnCheckoutException;
import com.dressca.applicationcore.order.Order;
import com.dressca.applicationcore.order.OrderNotFoundException;
import com.dressca.applicationcore.order.ShipTo;
import com.dressca.systemcommon.constant.CommonExceptionIdConstants;
import com.dressca.systemcommon.exception.SystemException;
import com.dressca.systemcommon.log.AbstractStructuredLogger;
import com.dressca.web.controller.advice.ProblemDetailsFactory;
import com.dressca.web.constant.WebConstants;
import com.dressca.web.consumer.controller.dto.order.OrderResponse;
import com.dressca.web.consumer.controller.dto.order.PostOrderRequest;
import com.dressca.web.log.ErrorMessageBuilder;
import com.dressca.web.consumer.mapper.OrderMapper;
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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link Order} の情報にアクセスする API コントローラーです。
 */
@RestController
@Tag(
    name = "Orders",
    description = "注文の情報にアクセスする API です。")
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
  private OrderApplicationService orderApplicationService;
  @Autowired
  private ShoppingApplicationService shoppingApplicationService;

  @Autowired
  private ProblemDetailsFactory problemDetailsFactory;

  @Autowired
  private AbstractStructuredLogger apLog;

  /**
   * 注文情報を取得します。
   * 
   * @param orderId 注文 ID 。
   * @return 注文情報。
   */
  @Operation(
      summary = "注文情報を取得します。",
      description = "注文情報を取得します。")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "成功。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = OrderResponse.class))),
          @ApiResponse(
              responseCode = "404",
              description = "注文 ID が存在しません。",
              content = @Content(
                  mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class)))
      })
  @GetMapping("{orderId}")
  public ResponseEntity<?> getById(@PathVariable("orderId") long orderId,
      HttpServletRequest req) {
    String buyerId = req.getAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID).toString();

    try {
      Order order = orderApplicationService.getOrder(orderId, buyerId);
      OrderResponse orderDto = OrderMapper.convert(order);
      return ResponseEntity.ok().body(orderDto);
    } catch (OrderNotFoundException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e,
          e.getExceptionId(),
          e.getLogMessageValue(), e.getFrontMessageValue());
      ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(
          errorBuilder,
          CommonExceptionIdConstants.E_BUSINESS,
          HttpStatus.NOT_FOUND);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON)
          .body(problemDetail);
    }
  }

  /**
   * 買い物かごに登録されている商品を注文します。
   * 
   * @param postOrderInput 注文に必要な配送先などの情報。
   * @return なし。
   */
  @Operation(
      summary = "買い物かごに登録されている商品を注文します。",
      description = "買い物かごに登録されている商品を注文します。")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "201", description = "成功。", content = @Content),
          @ApiResponse(
              responseCode = "400",
              description = "リクエストエラー。",
              content = @Content(
                  mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))),
          @ApiResponse(
              responseCode = "500",
              description = "サーバーエラー。",
              content = @Content(
                  mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class)))})
  @PostMapping
  public ResponseEntity<?> postOrder(@RequestBody @Valid PostOrderRequest postOrderInput,
      HttpServletRequest req) {
    String buyerId = req.getAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID).toString();
    Address address = new Address(postOrderInput.getPostalCode(), postOrderInput.getTodofuken(),
        postOrderInput.getShikuchoson(), postOrderInput.getAzanaAndOthers());
    ShipTo shipToAddress = new ShipTo(postOrderInput.getFullName(), address);
    Order order;
    try {
      order = shoppingApplicationService.checkout(buyerId, shipToAddress);
    } catch (EmptyBasketOnCheckoutException e) {
      // ここでは発生しえないので、システムエラーとする
      throw new SystemException(e, CommonExceptionIdConstants.E_SYSTEM, null, null);
    }

    String requestUri = req.getRequestURL().toString();
    return ResponseEntity.created(URI.create(requestUri + "/" + order.getId())).build();
  }
}
