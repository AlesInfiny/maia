package com.dressca.cms.announcement.infrastructure.repository.mybatis.config;

import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.dressca.cms.announcement.infrastructure.repository.mybatis.handler.UuidTypeHandler;
import java.util.UUID;

/**
 * MyBatis 用の設定クラスです。
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.dressca.cms.announcement.infrastructure.repository.mybatis" })
public class AnnouncementMyBatisConfig {
  /**
   * MyBatis の設定をカスタマイズする {@link ConfigurationCustomizer} オブジェクトを Bean 登録します。
   *
   * @return MyBatis の設定をカスタマイズする {@link ConfigurationCustomizer} オブジェクト。
   */
  @Bean
  ConfigurationCustomizer announcementMybatisConfigurationCustomizer() {
    return configuration -> {
      configuration.setMapUnderscoreToCamelCase(true);
      configuration.getTypeHandlerRegistry().register(UUID.class, JdbcType.VARCHAR,
          new UuidTypeHandler());
    };
  }
}
