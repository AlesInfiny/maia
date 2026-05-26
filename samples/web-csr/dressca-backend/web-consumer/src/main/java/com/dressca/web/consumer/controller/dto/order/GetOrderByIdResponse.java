package com.dressca.web.consumer.controller.dto.order;

import com.dressca.web.consumer.controller.dto.accounting.AccountApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文情報を取得する際に用いる dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderByIdResponse {
  @NotNull
  @Schema(type = "string", format = "uuid")
  private UUID id;
  @NotNull
  @Schema(type = "string", format = "uuid")
  private UUID buyerId;
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
  private AccountApiModel account;
  private List<OrderItemApiModel> orderItems;
}
