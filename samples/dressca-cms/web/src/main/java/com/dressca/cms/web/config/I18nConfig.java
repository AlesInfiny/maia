package com.dressca.cms.web.config;

import java.io.IOException;
import java.util.Arrays;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * i18n の設定クラスです。
 */
@Configuration
public class I18nConfig {

  /**
   * メッセージプロパティファイルが読み込まれた、 {@link MessageSource} オブジェクトを Bean 登録します。
   * 
   * @return {@link MessageSource} オブジェクト。
   * @throws IOException 正常にプロパティファイルを読み込めなかった場合。
   */
  @Bean
  public MessageSource messageSource() throws IOException {
    ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setDefaultEncoding("UTF-8");
    // キャッシュ時間（開発時は 0、運用時は適宜）
    ms.setCacheSeconds(3600);
    // ── i18n 以下をスキャン ──
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    // /i18n/messages_ja.properties や
    // /i18n/announcement/register/register_en.properties を拾う
    Resource[] resources = resolver.getResources("classpath*:i18n/**/*.properties");
    String[] baseNames = Arrays.stream(resources)
        .map(resource -> {
          try {
            String uriStr = resource.getURI().toString();
            // "classpath:"付きのベース名を抽出
            return "classpath:" + uriStr
                .replaceAll("^.*?/i18n/", "i18n/")
                .replaceAll("(_[a-z]{2}(_[A-Z]{2})?)?\\.properties$", "");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .distinct()
        .toArray(String[]::new);
    ms.setBasenames(baseNames);
    return ms;
  }
}
