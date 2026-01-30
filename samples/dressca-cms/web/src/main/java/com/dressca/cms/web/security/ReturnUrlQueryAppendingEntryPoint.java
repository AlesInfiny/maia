package com.dressca.cms.web.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link LoginUrlAuthenticationEntryPoint} を継承するクラス。 未認証ユーザーが認証が必要な画面にアクセスした場合に、
 * ログイン画面にリダイレクトする前にクエリ文字列に return-url を追加します。
 */
public class ReturnUrlQueryAppendingEntryPoint extends LoginUrlAuthenticationEntryPoint {

  /**
   * コンストラクタ。
   * 
   * @param loginFormUrl ログインフォームの URL。
   */
  public ReturnUrlQueryAppendingEntryPoint(String loginFormUrl) {
    super(loginFormUrl);
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    String requestUri = request.getRequestURI();
    String queryString = request.getQueryString();

    String returnUrl = requestUri;
    if (queryString != null && !queryString.isEmpty()) {
      returnUrl = requestUri + "?" + queryString;
    }

    String redirectUrl = UriComponentsBuilder.fromPath(getLoginFormUrl())
        .queryParam("return-url", returnUrl).build().toUriString();

    response.sendRedirect(redirectUrl);
  }
}
