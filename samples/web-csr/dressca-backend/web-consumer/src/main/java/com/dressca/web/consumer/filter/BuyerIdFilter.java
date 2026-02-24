package com.dressca.web.consumer.filter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import com.dressca.web.constant.WebConstants;
import com.dressca.web.consumer.security.CookieSettings;

/**
 * 購入者 ID にフィルターをかけるクラスです。
 */
@AllArgsConstructor
public class BuyerIdFilter implements Filter {

  private static final String DEFAULT_BUYER_COOKIE_NAME = "Dressca-Bid";
  private static final Pattern UUID_PATTERN = Pattern
      .compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
  private final CookieSettings cookieSettings;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
    String buyerId = null;

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (DEFAULT_BUYER_COOKIE_NAME.equals(cookie.getName())) {
          buyerId = cookie.getValue();
          break;
        }
      }
    }

    if (StringUtils.isBlank(buyerId) || !isValidUuid(buyerId)) {
      buyerId = UUID.randomUUID().toString();
    }

    request.setAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID, buyerId);

    chain.doFilter(request, response);

    buyerId = request.getAttribute(WebConstants.ATTRIBUTE_KEY_BUYER_ID).toString();
    ResponseCookie responseCookie = ResponseCookie.from(DEFAULT_BUYER_COOKIE_NAME, buyerId)
        .path("/").httpOnly(cookieSettings.isHttpOnly()).secure(cookieSettings.isSecure())
        .maxAge((long) cookieSettings.getExpiredDays() * 60 * 60 * 24)
        .sameSite(cookieSettings.getSameSite()).build();

    ((HttpServletResponse) response).addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
  }

  private static boolean isValidUuid(String value) {
    return UUID_PATTERN.matcher(value).matches();
  }
}
