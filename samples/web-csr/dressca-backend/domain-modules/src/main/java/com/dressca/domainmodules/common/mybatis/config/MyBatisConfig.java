package com.dressca.domainmodules.common.mybatis.config;

import com.dressca.domainmodules.common.mybatis.handler.UuidTypeHandler;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.type.JdbcType;
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
// annotationClass を指定しないと、基底パッケージ配下の全インターフェース
// （UserStore やリポジトリのアウトバウンドポート）までマッパーとして登録されてしまう。
@MapperScan(basePackages = "com.dressca.domainmodules", annotationClass = Mapper.class)
public class MyBatisConfig {

  /**
   * MyBatis の設定をカスタマイズします。
   * 
   * @return カスタマイズされた MyBatis 設定 。
   */
  @Bean
  ConfigurationCustomizer mybatisConfigurationCustomizer() {
    return configuration -> {
      configuration.setMapUnderscoreToCamelCase(true);
      configuration.getTypeHandlerRegistry().register(UUID.class, JdbcType.OTHER,
          new UuidTypeHandler());
    };
  }
}
