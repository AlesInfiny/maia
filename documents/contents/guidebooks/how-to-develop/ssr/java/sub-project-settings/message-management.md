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
   │  └ a-function/ ---------------------------------- a 機能のプロパティファイルを管理するフォルダー
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
# システムエラー
error.system.unexpected=想定外のシステムエラーが発生しました。
error.system.database=データベースへのアクセスに失敗しました。
error.system.external=外部システムとの連携に失敗しました。

# 業務エラー
error.business.data.notfound=指定されたデータが見つかりません。
error.business.data.duplicate=データが重複しています。
error.business.operation.invalid=この操作は実行できません。

# バリデーションエラー
validation.required={0}は必須です。
validation.length={0}は{1}文字以内で入力してください。
validation.format={0}の形式が正しくありません。
```

```properties title="messages_en.properties"
# System errors
error.system.unexpected=An unexpected system error has occurred.
error.system.database=Failed to access the database.
error.system.external=Failed to integrate with external system.

# Business errors
error.business.data.notfound=The specified data was not found.
error.business.data.duplicate=Data is duplicated.
error.business.operation.invalid=This operation cannot be performed.

# Validation errors
validation.required={0} is required.
validation.length={0} must be within {1} characters.
validation.format=The format of {0} is incorrect.
```

#### UI メッセージ {#ui-messages}

UI メッセージのプロパティファイルは `web` サブプロジェクトの `/src/main/resources/i18n` フォルダー配下に、画面ごとにフォルダーを作成して管理します。
メッセージコードは、アプリケーション内で重複しないように設定する必要があるため、以下のように `<機能名>.<画面名>.<項目名>` で設定します。

```properties title="register_ja.properties"
announcement.register.title=お知らせ登録
announcement.register.message1=お知らせ登録画面のメッセージ文字列
```

```properties title="register_en.properties"
announcement.register.title=Register Announcement
announcement.register.message1=Message string for announcement registration screen
```

### プロパティファイルの読込 {#reading-property-files}

<!-- textlint-disable ja-technical-writing/sentence-length -->

Spring Framework で提供されている [`#!java PathMatchingResourcePatternResolver` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html){ target=_blank } を利用して、プロパティファイルを読込みます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

また、 [`#!java MessageSource` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html){ target=_blank } で提供されている機能を利用して、プロパティファイルの末尾に `_ja` や `_en` のような接尾辞を付与します。
これにより、ブラウザーの言語設定に応じて読み込むプロパティファイルを切り替えます。

以下のように、 web プロジェクトなどエントリーポイントとなるサブプロジェクトの設定クラスにプロパティファイルを読み込む設定を記載します。

```java title="WebConfig.java"
@Bean
public MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(
        "classpath:i18n/messages",
        "classpath:i18n/announcement/register/register",
        "classpath:i18n/announcement/edit/edit"
    );
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
}
```

読み込むプロパティファイルは `classpath:` 配下の `i18n/<フォルダー名>/<ファイル名>` で指定します。
プロパティファイルが複数ある場合は、ファイルの間をカンマで区切ります。

### 多言語対応 {#localization}

<!-- textlint-disable ja-technical-writing/sentence-length -->

Spring Framework で提供されている [`#!java AcceptHeaderLocaleResolver` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/i18n/AcceptHeaderLocaleResolver.html){ target=_blank } を利用して、ブラウザーの言語設定を取得します。

<!-- textlint-enable ja-technical-writing/sentence-length -->

対応していない言語の場合は、 `#!java AcceptHeaderLocaleResolver` の `setDefaultLocale` メソッドを利用して日本語を使用するようにします。

```java title="WebConfig.java"
@Bean
public LocaleResolver localeResolver() {
    var resolver = new AcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(Locale.JAPANESE);
    return resolver;
}
```

### メッセージの取得 {#getting-messages}

読み込んだプロパティファイルのメッセージを取得するためには、 [`MessageSource` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html){ target=_blank } インターフェースを利用します。

以下は、プロパティファイルからメッセージを取得し、ログに出力するためのエラーメッセージを整形する `ErrorMessageBuilder` クラスの例です。

??? example "サンプルアプリケーションの ErrorMessageBuilder.java"

    ```java title="ErrorMessageBuilder.java" hl_lines="19 20 34"
    ```

<!-- textlint-disable ja-technical-writing/sentence-length -->
また、 `#!java @Service` や `#!java @Controller` 、 `#!java @Component` といった Bean 登録されたクラス内で `MessageSource` を利用する場合は、 `#!java @Autowired` による DI で実装します。
<!-- textlint-enable ja-technical-writing/sentence-length -->

以下は、プロパティファイルからエラーレスポンスに含めるメッセージを整形する `ProblemDetailsFactory.java` クラスの例です。

??? example "サンプルアプリケーションの ProblemDetailsFactory.java"

    ```java title="ProblemDetailsFactory.java" hl_lines="26 27 39 41"
    
    ```

### HTML とのバインディング {#binding}

テンプレートエンジンである [Thymeleaf :material-open-in-new:](https://www.thymeleaf.org/){ target=_blank } の機能を利用して、 `#{}` 構文によってメッセージを参照します。
以下のように、構文内にはプロパティファイルで定義したメッセージコードを記述します。

```html title="register.html"
<h1 th:text="#{announcement.register.title}"></h1>
<p th:text="#{announcement.register.message1}"></p>
```
