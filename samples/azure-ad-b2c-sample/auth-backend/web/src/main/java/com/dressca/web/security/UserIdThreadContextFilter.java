package com.dressca.web.security;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import com.dressca.systemcommon.constant.SystemPropertyConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ユーザー ID を threadLocal に格納するためのフィルタークラスです。
 */
public class UserIdThreadContextFilter extends OncePerRequestFilter {

  // ログ出力用（本番では消す）
  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);
  public static final ThreadLocal<String> threadLocalUserId = new ThreadLocal<>();

  /**
   * フィルターの実行クラスです。
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

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
