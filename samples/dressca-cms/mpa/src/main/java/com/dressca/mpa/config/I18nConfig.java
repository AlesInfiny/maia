package com.dressca.mpa.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class I18nConfig {

    @Bean
    public MessageSource messageSource() throws IOException {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setDefaultEncoding("UTF-8");
        // キャッシュ時間（開発時は 0、運用時は適宜）
        ms.setCacheSeconds(3600);

        // ── i18n 以下をスキャン ──
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // /i18n/ja/messages.properties や /i18n/en/messages.properties を拾う
        Resource[] resources = resolver.getResources("classpath:/i18n/**/*.properties");

        String[] baseNames = Arrays.stream(resources)
            .map(Resource::getFilename)
            .map(fn -> fn.replaceAll("(_[a-z]{2})?\\.properties$", ""))
            .distinct()
            .map(bn -> "classpath:/i18n/" + bn)
            .toArray(String[]::new);

        ms.setBasenames(baseNames);
        return ms;
    }
}