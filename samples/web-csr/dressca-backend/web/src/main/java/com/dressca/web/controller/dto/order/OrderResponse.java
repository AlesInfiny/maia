package com.dressca.web.controller.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import com.dressca.web.controller.dto.accounting.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  private long id;
  private String buyerId;
  private LocalDateTime orderDate;
  private String fullName;
  private String postalCode;
  private String todofuken;
  private String shikuchoson;
  private String azanaAndOthers;
  private AccountResponse account;
  private List<OrderItemResponse> orderItems;
}
