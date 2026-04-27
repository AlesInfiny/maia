package com.dressca.web.consumer.security;

import com.dressca.web.security.CorsAllowedOriginsProperties;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;

/**
 * セキュリティ関連の実行クラスです。
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableConfigurationProperties(CorsAllowedOriginsProperties.class)
@RequiredArgsConstructor
public class WebSecurityConfig {

  private static final List<String> ALLOWED_METHODS =
      List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS");

  private final CorsAllowedOriginsProperties corsAllowedOriginsProperties;

  /**
   * CORS の設定をします。
   *
   * @param http http リクエスト。
   * @return フィルターチェーン。
   * @throws Exception 例外。
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny())
        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'none';")))
        .securityMatcher("/api/**")
        // CSRF トークンを利用したリクエストの検証を無効化（ OAuth2.0 による認証認可を利用する前提のため）
        // OAuth2.0 によるリクエストの検証を利用しない場合は、有効化して CSRF 対策を施す
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        .cors(Customizer.withDefaults());

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
    // 注文情報の確定にLocationを利用するため公開ヘッダーとして設定
    configuration.addExposedHeader("Location");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }
}
