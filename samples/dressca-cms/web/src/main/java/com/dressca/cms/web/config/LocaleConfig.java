package com.dressca.cms.web.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * Locale の設定クラスです。
 */
@Configuration
public class LocaleConfig {

  /**
   * Accept-Language ヘッダをそのまま使う {@link LocaleResolver} オブジェクトを Bean 登録します。
   * クッキーや URL パラメータによる切り替えは不要です。
   * 
   * @return {@link LocaleResolver} オブジェクト。
   */
  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(Locale.JAPANESE);
    return resolver;
  }
}