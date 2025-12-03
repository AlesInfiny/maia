package com.dressca.cms.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

/**
 * Thymeleaf Layout Dialect の設定クラスです。
 */
@Configuration
public class LayoutConfig {

  /**
   * Thymeleaf Layout Dialect の {@link LayoutDialect} オブジェクトを Bean 登録します。
   * 
   * @return {@link LayoutDialect} オブジェクト。
   */
  @Bean
  public LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }
}
