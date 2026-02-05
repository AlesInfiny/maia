package com.dressca.cms.authentication.infrastructure.repository.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis 用の設定クラスです。
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.dressca.cms.authentication.infrastructure.repository.mybatis"})
public class AuthenticationMyBatisConfig {
  /**
   * MyBatis の設定をカスタマイズする {@link ConfigurationCustomizer} オブジェクトを Bean 登録します。
   *
   * @return MyBatis の設定をカスタマイズする {@link ConfigurationCustomizer} オブジェクト。
   */
  @Bean
  ConfigurationCustomizer authenticationMybatisConfigurationCustomizer() {
    return configuration -> {
      configuration.setMapUnderscoreToCamelCase(true);
    };
  }
}
