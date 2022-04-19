package com.dressca.web.controller;

import com.dressca.web.controller.dto.OrderDto;
import com.dressca.web.controller.dto.PostOrderInputDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
  
  /**
   * 注文情報を取得します。
   * @param orderId 注文 Id
   * @return 注文情報
   */
  @GetMapping("{orderId}")
  public ResponseEntity<OrderDto> getById(@PathVariable("orderId") long orderId) {
    // TODO: 内容実装
    return null;
  }

  /**
   * 買い物かごに登録されている商品を注文します。
   * @param postOrderInput 注文に必要な配送先などの情報
   * @return なし
   */
  public ResponseEntity<?> postOrder(PostOrderInputDto postOrderInput) {
    // TODO: 内容実装
    return null;
  }
}
