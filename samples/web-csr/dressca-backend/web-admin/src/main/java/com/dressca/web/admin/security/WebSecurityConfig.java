package com.dressca.web.admin.security;

import com.dressca.web.admin.filter.DummyUserInjectionFilter;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;

/**
 * セキュリティ関連の実行クラスです。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(CorsAllowedOriginsProperties.class)
@RequiredArgsConstructor
public class WebSecurityConfig {

  private static final List<String> ALLOWED_METHODS =
      List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS");

  private final CorsAllowedOriginsProperties corsAllowedOriginsProperties;

  private final Environment environment;

  /**
   * CORS 設定、認可機能を設定します。
   *
   * @param http 認証認可の設定クラス。
   * @return フィルターチェーン。
   * @throws Exception 例外。
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny())
        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'none';")))
        .securityMatcher("/api/**")
        // CSRF トークンを利用したリクエストの検証を無効化（ OAuth2.0 による認証認可を利用する前提のため）
        // OAuth2.0 によるリクエストの検証を利用しない場合は、有効化して CSRF 対策を施す
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")).cors(Customizer.withDefaults())
        .anonymous(anon -> anon.disable());

    // 開発環境においてはダミーユーザを注入する
    if (environment.acceptsProfiles(Profiles.of("local"))) {
      http.addFilterBefore(new DummyUserInjectionFilter(),
          UsernamePasswordAuthenticationFilter.class);
    }

    return http.build();
  }

  /**
   * CORS 設定を Spring Security へ提供します。
   *
   * @return CORS 設定ソース。
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(corsAllowedOriginsProperties.getOrigins());
    configuration.setAllowedMethods(ALLOWED_METHODS);
    configuration.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }
}
