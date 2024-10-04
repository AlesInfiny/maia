package com.dressca.web.admin.filter;

import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ダミーユーザーをSecurityContextHolderに詰めるためのフィルタークラス。
 */
@Profile("dev")
@Component
public class DummyUserInjectionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    UserDetails dummyUser = new User(
        "admin@example.com",
        "",
        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        dummyUser,
        dummyUser.getPassword(),
        dummyUser.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }
}