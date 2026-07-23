package com.dressca.web.consumer.controller.dto.display;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陳列ブランドの情報を取得する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDisplayBrandsResponse {
  @NotNull
  private UUID id;
  @NotNull
  private String name;
}
