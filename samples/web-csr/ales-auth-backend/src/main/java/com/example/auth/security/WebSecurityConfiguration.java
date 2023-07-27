package com.example.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * WebSecurityConfigurationクラス：セキュリティ関連の実行クラス
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug=true)
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    /**
     * CORS設定、JWTトークン検証を実行
     *
     * @param http
     * @return http
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        http.csrf().disable();
        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of(allowedOrigins));
            cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        });

        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(converter);
        return http.build();
    }
}