package com.dressca.web.controller.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログインに成功した際のユーザIDを格納するクラス。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  @NotNull
  private String userId;
}