---
title: Java 編 （SSR 編）
description: SSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

<!-- cspell:ignore applicationcore systemcommon Reloadable Basenames -->

# メッセージ管理機能の設定 {#top}

SSR アプリケーションのメッセージ管理方針に関するアーキテクチャについては、[こちら](../../../../../app-architecture/server-side-rendering/global-function/message-management-policy.md) をご確認ください。

メッセージは用途に応じて以下の 2 種類に分類します。

- 業務メッセージ

    業務ロジックで使用されるエラーメッセージや警告文など。

- UI メッセージ

    ラベル、ボタン名、画面固有の文言といった画面表示用のメッセージ。

## 設定方法 {#settings}

本設定で利用するフォルダーの構成は以下の通りです。

```text linenums="0"
root/ -------------------------------------------------- root フォルダー
 ├ a-function/src/main/resources
 │ └ i18n/ --------------------------------------------- a 機能の業務メッセージのプロパティファイルを一括管理するフォルダー
 │    ├ messages_en.properties ------------------------- a 機能の業務メッセージのプロパティファイル（英語）
 │    └ messages_ja.properties ------------------------- a 機能の業務メッセージのプロパティファイル（日本語）
 └ web/src/main/resources
   ├ i18n/ --------------------------------------------- UI メッセージのプロパティファイルを一括管理するフォルダー
   │  └ a-function/ ------------------------------------ a 機能 の UI メッセージのプロパティファイルを管理するフォルダー
   |     └ register/
   |        ├ register_en.properties ------------------- UI メッセージ（登録画面）のプロパティファイル（英語）
   |        └ register_ja.properties ------------------- UI メッセージ（登録画面）のプロパティファイル（日本語）
   │     
   └ templates/ ---------------------------------------- HTML ファイルを配置するフォルダー
      └ a-function/
         └ register/
            └ register.html ---------------------------- 登録画面の HTML ファイル
         
```

### プロパティファイルの作成 {#creating-property-file}

#### 業務メッセージ {#business-messages}

業務メッセージのプロパティファイルは機能モジュールのプロジェクトの `/src/main/resources/i18n` フォルダーに作成します。
以下のように、メッセージコードとメッセージ文字列本体を格納するプロパティファイルを言語ごとに作成します。

```properties title="messages_ja.properties"
systemError=想定外のシステムエラーが発生しました
businessError=想定外の業務エラーが発生しました
```

#### UI メッセージ {#ui-messages}

UI メッセージのプロパティファイルは `web` サブプロジェクトの `/src/main/resources/i18n` フォルダー配下に、画面ごとにフォルダーを作成して管理します。
メッセージコードは、アプリケーション内で重複しないように設定する必要があるため、以下のように `<機能名>.<画面名>.<項目名>` で設定します。

```properties title="register_ja.properties"
announcement.register.message1=お知らせ登録画面のメッセージ文字列
```

### プロパティファイルの読込 {#reading-property-files}

<!-- textlint-disable ja-technical-writing/sentence-length -->

Spring Framework で提供されている [`#!java PathMatchingResourcePatternResolver` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html){ target=_blank } を利用して、プロパティファイルを読込みます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

また、 [`#!java MessageSource` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html){ target=_blank } で提供されている機能を利用して、プロパティファイルの末尾に `_ja` や `_en` のような接尾辞を付与します。
これにより、ブラウザーの言語設定に応じて読み込むプロパティファイルを切り替えます。

??? example "サンプルアプリケーションの I18nConfig.java"

    以下のように、 web プロジェクトなどエントリーポイントとなるサブプロジェクトの設定クラスにプロパティファイルを読み込む設定を記載します。

    ```java title="I18nConfig.java"
    package com.dressca.cms.systemcommon.config;

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
    ```

    読み込むプロパティファイルは `classpath:` 配下の `i18n/<フォルダー名>/<ファイル名>` で指定します。
    プロパティファイルが複数ある場合は、ファイルの間をカンマで区切ります。

### 多言語対応 {#localization}

<!-- textlint-disable ja-technical-writing/sentence-length -->

Spring Framework で提供されている [`#!java AcceptHeaderLocaleResolver` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/i18n/AcceptHeaderLocaleResolver.html){ target=_blank } を利用して、ブラウザーの言語設定を取得します。

<!-- textlint-enable ja-technical-writing/sentence-length -->

対応していない言語の場合は、 `#!java AcceptHeaderLocaleResolver` の `setDefaultLocale` メソッドを利用して日本語を使用するようにします。

??? example "サンプルアプリケーションの LocaleConfig.java"

    ```java title="LocaleConfig.java"
    package com.dressca.cms.web.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.LocaleResolver;
    import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
    import com.dressca.cms.systemcommon.constant.LanguageCodeConstants;

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
        resolver.setDefaultLocale(LanguageCodeConstants.LOCALE_JA);
        return resolver;
      }
    }
    ```

### メッセージの取得 {#getting-messages}

読み込んだプロパティファイルのメッセージを取得するためには、 [`MessageSource` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html){ target=_blank } インターフェースを利用します。

<!-- textlint-disable ja-technical-writing/sentence-length -->
また、 `#!java @Service` や `#!java @Controller` 、 `#!java @Component` といった Bean 登録されたクラス内で `MessageSource` を利用する場合は、 `#!java @Autowired` による DI で実装します。
<!-- textlint-enable ja-technical-writing/sentence-length -->

### HTML とのバインディング {#binding}

テンプレートエンジンである [Thymeleaf :material-open-in-new:](https://www.thymeleaf.org/){ target=_blank } の機能を利用して、 `#{}` 構文によってメッセージを参照します。
以下のように、構文内にはプロパティファイルで定義したメッセージコードを記述します。

```html title="register.html"
<h1 th:text="#{announcement.register.message1}"></h1>
```
