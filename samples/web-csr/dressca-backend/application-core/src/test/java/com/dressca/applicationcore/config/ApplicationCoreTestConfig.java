package com.dressca.applicationcore.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.dressca.systemcommon.util.ApplicationContextWrapper;

/**
 * アプリケーションコア層のテスト用設定クラスです。
 * テスト用の Bean 定義などを記述します。
 */
@TestConfiguration
public class ApplicationCoreTestConfig {
  @Bean
  ApplicationContextWrapper applicationContextWrapper() {
    return new ApplicationContextWrapper();
  }
}
