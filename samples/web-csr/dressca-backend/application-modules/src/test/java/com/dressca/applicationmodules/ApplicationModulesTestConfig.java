package com.dressca.applicationmodules;

import com.dressca.systemcommon.util.ApplicationContextWrapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * ドメインモジュールのテスト用設定クラスです。
 * テスト用の Bean 定義などを記述します。
 */
@TestConfiguration
public class ApplicationModulesTestConfig {
  /**
   * テスト用に {@link ApplicationContextWrapper} を Bean として登録します。
   */
  @Bean
  public ApplicationContextWrapper applicationContextWrapper() {
    return new ApplicationContextWrapper();
  }
}
