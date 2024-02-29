package com.dressca.web.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserIdThreadContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    ThreadLocal<String> threadLocalUserId = new ThreadLocal<>();

    JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
    if (authentication != null) {
      String userId = (String) authentication.getToken().getClaims().get("UserId");
      System.err.println("userId= " + userId);
      threadLocalUserId.set(userId);
    }

    filterChain.doFilter(request, response);
  }

}
