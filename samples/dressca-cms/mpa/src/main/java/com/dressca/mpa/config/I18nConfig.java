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
        Resource[] resources = resolver.getResources("classpath:/i18n/*/messages.properties");

        // フォルダ名（ja, en, ...） を取り出して baseNames に整形
        String[] baseNames = Arrays.stream(resources)
            .map(resource -> {
                try {
                    return resource.getURI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .map(uri -> {
                // URI: .../i18n/ja/messages.properties → extract "i18n/ja/messages"
                String path = uri.getPath();
                return path
                    .replaceFirst(".*(/i18n/.+)\\.properties$", "$1");
            })
            .map(s -> "classpath:" + s)
            .toArray(String[]::new);

        ms.setBasenames(baseNames);
        return ms;
    }
}