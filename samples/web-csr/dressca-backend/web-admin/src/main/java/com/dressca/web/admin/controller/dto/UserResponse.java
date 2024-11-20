package com.dressca.web.admin.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ユーザー情報を取得する際に用いるdtoクラスです。
 */
@Data
@AllArgsConstructor
public class UserResponse {
  @NotNull
  private String userName = "";
  @NotNull
  private String role = "";
}
