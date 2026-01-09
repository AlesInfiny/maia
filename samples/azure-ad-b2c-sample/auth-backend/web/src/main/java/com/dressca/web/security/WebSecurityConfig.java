package com.dressca.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.util.Arrays;
import java.util.List;

/**
 * セキュリティ関連の実行クラスです。
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
@SecurityScheme(name = "Bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT",
    scheme = "bearer")
public class WebSecurityConfig {

  @Value("${cors.allowed.origins:}")
  private String allowedOrigins;

  /**
   * CORS 設定、JWT トークン検証を設定します。
   *
   * @param http http リクエスト。
   * @return フィルターチェーン。
   * @throws Exception 例外。
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api/**").cors(cors -> cors.configurationSource(request -> {
      CorsConfiguration conf = new CorsConfiguration();
      conf.setAllowCredentials(true);
      conf.setAllowedOrigins(Arrays.asList(allowedOrigins));
      conf.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
      conf.setAllowedHeaders(List.of("*"));
      return conf;
    })).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .addFilterAfter(new UserIdThreadContextFilter(), AuthorizationFilter.class);
    return http.build();
  }
}
