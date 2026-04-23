package com.dressca.modules.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.dressca.modules.common.util.ApplicationContextWrapper;

/**
 * アプリケーションコア層のテスト用設定クラスです。
 * テスト用の Bean 定義などを記述します。
 */
@TestConfiguration
public class ApplicationCoreTestConfig {
  /**
   * テスト用に {@link ApplicationContextWrapper} を Bean として登録します。
   */
  @Bean
  public ApplicationContextWrapper applicationContextWrapper() {
    return new ApplicationContextWrapper();
  }
}
