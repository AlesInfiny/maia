package com.dressca.web.consumer.controller.dto.baskets;

import com.dressca.web.consumer.controller.dto.accounting.AccountApiModel;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 買い物かごアイテムの一覧を格納するための dto クラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBasketItemsResponse {
  @NotNull
  @Schema(type = "string", format = "uuid")
  private UUID buyerId;
  private AccountApiModel account;
  private List<BasketItemApiModel> basketItems;
  @ArraySchema(schema = @Schema(type = "string", format = "uuid"))
  private List<UUID> deletedItemIds;
}
