package com.dressca.infrastructure.repository.mybatis.config;

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
@MapperScan(basePackages = {"com.dressca.infrastructure.repository.mybatis"})
public class MyBatisConfig {

  /**
   * MyBatis の設定をカスタマイズします。
   * 
   * @return カスタマイズされた MyBatis 設定 。
   */
  @Bean
  ConfigurationCustomizer mybatisConfigurationCustomizer() {
    return new ConfigurationCustomizer() {
      @Override
      public void customize(org.apache.ibatis.session.Configuration configuration) {
        configuration.setMapUnderscoreToCamelCase(true);
      }
    };
  }
}
