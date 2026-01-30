package com.dressca.cms.web.security;

import com.dressca.cms.authentication.applicationcore.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security の構成クラスです。
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;

  /**
   * パスワードをエンコードするための {@link BCryptPasswordEncoder} クラスの Bean を返します。
   * 
   * @return パスワードエンコーダー。
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 認証処理を行う Bean を返します。 認証処理はデータベースのアカウント・パスワード情報に基づいて行うため、{@link DaoAuthenticationProvider} を用いる。
   * 
   * @param authenticationConfiguration 認証設定。
   * @return 認証マネージャー。
   * @throws Exception 例外。
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * DaoAuthenticationProvider を構成します。
   * 
   * @return DaoAuthenticationProvider。
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /**
   * Spring Security の構成をします。
   * 
   * @param http HttpSecurity。
   * @return SecurityFilterChain。
   * @throws Exception 例外。
   */
  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(authorize -> authorize.requestMatchers("/h2-console/**",
            "/account/login", "/bootstrap/**", "/css/**", "/scss/**", "/images/**").permitAll()
            .anyRequest().authenticated())
        .formLogin(form -> form.loginPage("/account/login").permitAll().disable())
        // ログアウト機能を有効にする場合は、以下のコメントアウトを外してください。
        // .logout(logout -> logout
        // .logoutUrl("/account/logout")
        // .logoutSuccessUrl("/account/login")
        // .permitAll())
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new ReturnUrlQueryAppendingEntryPoint("/account/login")))
        // csrf 無効化は本番環境では削除してください。
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

    return http.build();
  }
}
