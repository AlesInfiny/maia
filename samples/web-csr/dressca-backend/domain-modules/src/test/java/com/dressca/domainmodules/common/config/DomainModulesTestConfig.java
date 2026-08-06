package com.dressca.domainmodules.common.config;

import com.dressca.domainmodules.common.util.ApplicationContextWrapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * ドメインモジュールのテスト用設定クラスです。
 * テスト用の Bean 定義などを記述します。
 */
@TestConfiguration
public class DomainModulesTestConfig {
  /**
   * テスト用に {@link ApplicationContextWrapper} を Bean として登録します。
   */
  @Bean
  public ApplicationContextWrapper applicationContextWrapper() {
    return new ApplicationContextWrapper();
  }
}
