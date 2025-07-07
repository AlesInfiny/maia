package com.dressca.mpa.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfig {

    /**
     * Accept-Language ヘッダをそのまま使うロケールリゾルバ。
     * クッキーや URL パラメータによる切り替えは不要。
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.JAPANESE);
        return resolver;
    }
}