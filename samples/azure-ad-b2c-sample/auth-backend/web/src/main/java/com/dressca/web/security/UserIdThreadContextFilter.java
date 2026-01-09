package com.dressca.web.security;

import java.io.IOException;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import com.dressca.web.log.DresscaStructuredLoggerImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ユーザー ID を threadLocal に格納するためのフィルタークラスです。
 */
public class UserIdThreadContextFilter extends OncePerRequestFilter {

  // ログ出力用（本番では消す）
  private static final DresscaStructuredLoggerImpl apLog = new DresscaStructuredLoggerImpl();
  public static final ThreadLocal<String> threadLocalUserId = new ThreadLocal<>();

  /**
   * フィルターの実行クラスです。
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
      Map<String, Object> token = jwtAuthentication.getToken().getClaims();
      // ログ出力用（本番では消す）
      token.forEach((s, p) -> apLog.info(s + " : " + p.toString()));
      String userId = jwtAuthentication.getName();
      threadLocalUserId.set(userId);
    }

    filterChain.doFilter(request, response);
  }

}
