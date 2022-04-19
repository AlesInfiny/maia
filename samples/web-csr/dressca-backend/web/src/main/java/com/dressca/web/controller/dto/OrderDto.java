package com.dressca.web.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDto {
  private long Id;
  private String buyerId;
  private LocalDateTime orderDate;
  private String fullName;
  private String postalCode;
  private String todofuken;
  private String shikuchoson;
  private String azanaAndOthers;
  private AccountDto account;
  private List<OrderItemDto> orderItems;
}
