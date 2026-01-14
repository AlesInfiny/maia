package com.dressca.cms.web.models.base;

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
  @NotBlank(message = "{authentication.login.emailIsRequired}")
  @Email(message = "{authentication.login.emailIsInvalid}")
  private String email;

  /**
   * パスワード。
   */
  @NotBlank(message = "{authentication.login.passwordIsRequired}")
  private String password;
}
