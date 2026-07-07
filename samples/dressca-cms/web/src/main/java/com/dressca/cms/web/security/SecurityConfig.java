package com.dressca.cms.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security の構成クラスです。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Spring Security の構成をします。
   * 
   * @param http HttpSecurity。
   * @return SecurityFilterChain。
   * @throws Exception 例外。
   */
  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny())
        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'none';")))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/bootstrap/**", "/css/**", "/scss/**", "/images/**").permitAll()
            .anyRequest().authenticated())
        .oauth2Login(Customizer.withDefaults());

    return http.build();
  }
}
