package com.dressca.web.controller;

import com.dressca.web.controller.dto.OrderDto;
import com.dressca.web.controller.dto.PostOrderInputDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * {@link Order} の情報にアクセスするAPIコントローラーです.
 */
@RestController
@Tag(name = "Order", description = "注文の情報にアクセスするAPI")
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

  /**
   * 注文情報を取得します。
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
  public ResponseEntity<OrderDto> getById(@PathVariable("orderId") long orderId) {
    // TODO: 内容実装
    return null;
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
  public ResponseEntity<?> postOrder(@RequestBody PostOrderInputDto postOrderInput) {
    // TODO: 内容実装
    return null;
  }
}
