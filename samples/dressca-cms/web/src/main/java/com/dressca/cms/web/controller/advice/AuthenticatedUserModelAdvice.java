package com.dressca.cms.web.controller.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 全コントローラ共通で 認証済みユーザー情報を Model に追加するための ControllerAdvice です。 Thymeleaf
 * のヘッダーなど、全画面共通部品でログインユーザー情報を参照できるようにする目的で使用します。
 */
@ControllerAdvice
public class AuthenticatedUserModelAdvice {

  /**
   * 認証済みユーザーのユーザー名をモデルに追加します。
   * 
   * @param loginUser 認証済みユーザーの詳細情報。
   * @return ユーザー名。
   */
  @ModelAttribute("userName")
  public String exposeLoginUserToModel(@AuthenticationPrincipal UserDetails loginUser) {
    if (loginUser == null) {
      return null;
    }
    return loginUser.getUsername();
  }
}
