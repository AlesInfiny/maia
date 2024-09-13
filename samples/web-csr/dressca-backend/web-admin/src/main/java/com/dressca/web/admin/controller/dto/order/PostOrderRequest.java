package com.dressca.web.admin.controller.dto.order;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注文情報における個人データを追加・変更する際に用いるdtoクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOrderRequest {
  @NotNull
  @Length(min = 4, max = 15)
  private String fullName;
  @NotNull
  private String postalCode;
  @NotNull
  private String todofuken;
  @NotNull
  private String shikuchoson;
  @NotNull
  private String azanaAndOthers;
}
