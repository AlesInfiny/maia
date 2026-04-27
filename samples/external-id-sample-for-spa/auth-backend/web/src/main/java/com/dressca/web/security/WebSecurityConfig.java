package com.dressca.web.security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;

/**
 * セキュリティ関連の実行クラスです。
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(CorsAllowedOriginsProperties.class)
@SecurityScheme(name = "Bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT",
    scheme = "bearer")
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final CorsAllowedOriginsProperties corsAllowedOriginsProperties;

  /**
   * CORS 設定、JWT トークン検証を設定します。
   *
   * @param http http リクエスト。
   * @return フィルターチェーン。
   * @throws Exception 例外。
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      UserIdThreadContextFilter userIdThreadContextFilter) throws Exception {
    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny())
        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'none';")))
        .securityMatcher("/api/**").csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        .cors(Customizer.withDefaults())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .addFilterAfter(userIdThreadContextFilter, AuthorizationFilter.class);
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
    configuration.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }
}
