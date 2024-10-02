package com.dressca.web.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import com.dressca.web.admin.filter.DummyUserFilter;
import java.util.Arrays;
import java.util.List;

/**
 * セキュリティ関連の実行クラス。
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Value("${cors.allowed.origins:}")
  private String allowedOrigins;

  /**
   * CORS設定、認可機能を実装。
   * 
   * @param http 認証認可の設定クラス
   * @return フィルターチェーン
   * @throws Exception 例外
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.securityMatcher("/api/**")
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        // .authorizeHttpRequests(authorize -> authorize
        // .anyRequest().hasRole("ADMIN"))
        .addFilterBefore(new DummyUserFilter(), AuthorizationFilter.class);

    return http.build();
  }

  /**
   * CORS設定を提供するメソッド。
   * 
   * @return CORS設定ソース
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
    configuration.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}