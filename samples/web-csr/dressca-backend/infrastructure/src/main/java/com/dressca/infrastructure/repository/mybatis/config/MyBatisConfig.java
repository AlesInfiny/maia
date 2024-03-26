package com.dressca.infrastructure.repository.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis用の設定クラス。
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.dressca.infrastructure.repository.mybatis" })
public class MyBatisConfig {

    /**
     * MyBatis設定のカスタマイズ。
     * 
     * @return カスタマイズされたMyBatis設定
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
