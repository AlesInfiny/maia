package com.dressca.web.consumer.controller.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import com.dressca.web.consumer.controller.dto.accounting.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文情報を取得する際に用いるdtoクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  @NotNull
  private long id;
  @NotNull
  private String buyerId;
  @NotNull
  private LocalDateTime orderDate;
  @NotNull
  private String fullName;
  @NotNull
  private String postalCode;
  @NotNull
  private String todofuken;
  @NotNull
  private String shikuchoson;
  @NotNull
  private String azanaAndOthers;
  private AccountResponse account;
  private List<OrderItemResponse> orderItems;
}
