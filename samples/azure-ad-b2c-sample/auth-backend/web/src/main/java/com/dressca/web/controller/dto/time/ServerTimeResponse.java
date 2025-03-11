package com.dressca.web.controller.dto.time;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * サーバーの現在時刻を格納するクラスです。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerTimeResponse {
  @NotNull
  private String serverTime;
}
