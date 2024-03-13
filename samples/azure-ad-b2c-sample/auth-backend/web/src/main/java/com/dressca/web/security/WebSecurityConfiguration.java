package com.dressca.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

/**
 * セキュリティ関連の実行クラス。
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
public class WebSecurityConfiguration {

  @Value("${cors.allowed.origins}")
  private String allowedOrigins;

  /**
   * CORS設定、JWTトークン検証を実行。
   *
   * @param http httpリクエスト
   * @return フィルターチェーン
   * @throws Exception 例外
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    http.csrf(csrf -> csrf.disable());
    http.cors(cors -> cors.configurationSource(request -> {
      var conf = new CorsConfiguration();
      conf.setAllowedOrigins(List.of(allowedOrigins));
      conf.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      conf.setAllowedHeaders(List.of("*"));
      return conf;
    }));
    http.authorizeHttpRequests((requests) -> requests
        .requestMatchers("/api/auth/get").authenticated()
        .anyRequest().permitAll())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)))
        .addFilterAfter(new UserIdThreadContextFilter(), AuthorizationFilter.class);

    return http.build();
  }
}
