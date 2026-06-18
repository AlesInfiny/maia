package com.dressca.web.admin.filter;

import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import com.dressca.applicationcore.constant.UserRoleConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ダミーユーザーを {@link SecurityContextHolder} に詰めるためのフィルタークラスです。
 * 
 * <p>開発環境（local プロファイル）においてユーザー名が admin@example.com
 * 、権限が管理者（{@link UserRoleConstants#ADMIN}）のユーザーでアクセスしたことにして認証プロセスをスキップするために使用します。
 * {@link com.dressca.web.admin.security.WebSecurityConfig} にて、セキュリティフィルターチェーンの
 * {@link org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter}
 * の前に挿入します。</p>
 */
public class DummyUserInjectionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    UserDetails dummyUser = new User("admin@example.com", "",
        List.of(new SimpleGrantedAuthority(UserRoleConstants.ADMIN)));

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        dummyUser, dummyUser.getPassword(), dummyUser.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }
}
