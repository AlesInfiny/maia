package com.dressca.cms.web.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログイン画面のビューモデル。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginViewModel {

  /**
   * メールアドレス。
   */
  @NotBlank(message = "{validation.NotBlank}")
  @Email(message = "{validation.Email}")
  private String email;

  /**
   * パスワード。
   */
  @NotBlank(message = "{validation.NotBlank}")
  private String password;

  /**
   * 認証エラーメッセージ。
   */
  private String authenticationError;
}
