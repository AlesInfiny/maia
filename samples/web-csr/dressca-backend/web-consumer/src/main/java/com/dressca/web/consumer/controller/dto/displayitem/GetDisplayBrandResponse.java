package com.dressca.web.consumer.controller.dto.displayitem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 掲載ブランドの情報を取得する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDisplayBrandResponse {
  @NotNull
  private long id;
  @NotNull
  private String name;
}
