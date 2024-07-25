package com.dressca.web.security;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

/**
 * セキュリティ関連の実行クラス。
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class WebSecurityConfig {

  @Value("${cors.allowed.origins:}")
  private String[] allowedOrigins;

  /**
   * CORS 設定を実行。
   * 
   * @param http http リクエスト
   * @return フィルターチェーン
   * @throws Exception 例外
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**")
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration conf = new CorsConfiguration();
          conf.setAllowCredentials(true);
          conf.setAllowedOrigins(Arrays.asList(allowedOrigins));
          conf.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
          conf.setAllowedHeaders(List.of("*"));
          // 注文情報の確定にLocationを利用するため公開ヘッダーとして設定
          conf.addExposedHeader("Location");
          return conf;
        }));

    return http.build();
  }
}
