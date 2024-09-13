package com.dressca.web.filter;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 購入者IDにフィルターをかけるクラスです。
 */
public class BuyerIdFilter implements Filter {

  private static final String DEFAULT_BUYER_COOKIE_NAME = "Dressca-Bid";
  private static final String BUYER_ID_ATTRIBUTE_KEY = "buyerId";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
    String buyerId = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(DEFAULT_BUYER_COOKIE_NAME)) {
          buyerId = cookie.getValue();
        }
      }
    }
    if (StringUtils.isBlank(buyerId)) {
      buyerId = UUID.randomUUID().toString();
    }
    request.setAttribute(BUYER_ID_ATTRIBUTE_KEY, buyerId);

    chain.doFilter(request, response);

    buyerId = request.getAttribute(BUYER_ID_ATTRIBUTE_KEY).toString();
    Cookie cookie = new Cookie(DEFAULT_BUYER_COOKIE_NAME, buyerId);
    cookie.setMaxAge(60 * 60);
    ((HttpServletResponse) response).addCookie(cookie);
  }

}
