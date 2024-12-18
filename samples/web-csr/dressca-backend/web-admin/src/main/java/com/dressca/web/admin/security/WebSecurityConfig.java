package com.dressca.web.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import com.dressca.web.admin.filter.DummyUserInjectionFilter;
import java.util.Arrays;
import java.util.List;

/**
 * セキュリティ関連の実行クラス。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

  @Value("${cors.allowed.origins:}")
  private String allowedOrigins;

  @Autowired(required = false)
  private DummyUserInjectionFilter dummyUserInjectionFilter;

  /**
   * CORS設定、認可機能を実装。
   * 
   * @param http 認証認可の設定クラス
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
          return conf;
        }))
        .anonymous(anon -> anon.disable());

    // 開発環境においてはダミーユーザを注入する
    if (dummyUserInjectionFilter != null) {
      http.addFilterBefore(dummyUserInjectionFilter, UsernamePasswordAuthenticationFilter.class);
    }

    return http.build();
  }
}