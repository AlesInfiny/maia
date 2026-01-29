---
title: SSR 編 - 全体処理方式
description: SSR アプリケーション全体で考慮すべきアーキテクチャについて、 その実装方針を説明します。
---

<!-- cspell:ignore applicationcore -->

# メッセージ管理方針 {#top}

メッセージ文字列は、表記の統一を図ることを目的にプロパティファイルで管理します。
メッセージは用途に応じて以下の 2 種類に分類します。

- 業務メッセージ

    業務ロジックで使用されるエラーメッセージや警告文など。

- UI メッセージ

    ラベル、ボタン名、画面固有の文言といった画面表示用のメッセージ。

## メッセージの管理単位 {#management-unit}

以下のように、業務メッセージと UI メッセージを分割して各サブプロジェクトで管理します。
業務メッセージに関しては、複数の処理で共通に使用することが考えられるため、 1 つのプロパティファイルで一括管理します。
UI メッセージに関しては、プロパティファイルと HTML ファイルについてはフォルダー構造で 1 対 1 に対応させ、画面単位でのメッセージ定義および管理を容易にします。

```txt
root/ -------------------------------------------------- root フォルダー
 ├ applicationcore/src/main/resources
 │ └ i18n/applicationcore ------------------------------ 業務メッセージのプロパティファイルを一括管理するフォルダー
 │    ├ messages_en.properties ------------------------- 業務メッセージのプロパティファイル（英語）
 │    └ messages_ja.properties ------------------------- 業務メッセージのプロパティファイル（日本語）
 └ web/src/main/resources
   ├ i18n/ --------------------------------------------- UI メッセージのプロパティファイルを一括管理するフォルダー
   │  └ announcement/ ---------------------------------- お知らせメッセージ機能のプロパティファイルを管理するフォルダー
   |     ├ register/
   |     |  ├ register_en.properties ------------------- UI メッセージ（お知らせメッセージ登録）のプロパティファイル（英語）
   |     |  └ register_ja.properties ------------------- UI メッセージ（お知らせメッセージ登録）のプロパティファイル（日本語）
   │     └ edit/
   |        ├ edit_en.properties ----------------------- UI メッセージ（お知らせメッセージ編集）のプロパティファイル（英語）
   |        └ edit_ja.properties ----------------------- UI メッセージ（お知らせメッセージ編集）のプロパティファイル（日本語）
   └ templates/ ---------------------------------------- HTML ファイルを配置するフォルダー
      └ announcement/
         ├ register/
         |  └ register.html ---------------------------- お知らせメッセージ登録画面の HTML ファイル
         └ edit/
            └ edit.html -------------------------------- お知らせメッセージ編集画面の HTML ファイル
```

## プロパティファイルの管理 {#property-file-management}

プロパティファイルでは、以下のようにメッセージ文字列を識別するメッセージコードとメッセージ文字列本体をペアで管理します。

### 業務メッセージ {#business-messages}

業務メッセージの管理方針については、[こちら](../../overview/java-application-processing-system/message-management-policy.md) を参照してください。

### UI メッセージ {#ui-messages}

メッセージコードは、アプリケーション内で重複しないように設定する必要があるため、以下のように `<機能名>.<画面名>.<項目名>` で設定します。

```properties title="register_ja.properties"
announcement.register.message1=お知らせ登録画面のメッセージ文字列
```

```properties title="edit_ja.properties"
announcement.edit.message1=お知らせ編集画面のメッセージ文字列
```

## 多言語対応 {#localization}

多言語対応するために利用する機能は以下の通りです。

### ブラウザーの言語取得 {#getting-browser-language}

<!-- textlint-disable ja-technical-writing/sentence-length -->

Spring Framework で提供されている [`#!java AcceptHeaderLocaleResolver` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/i18n/AcceptHeaderLocaleResolver.html){ target=_blank } を利用して、ブラウザーの言語設定を取得します。

<!-- textlint-enable ja-technical-writing/sentence-length -->

対応していない言語の場合は、 `#!java AcceptHeaderLocaleResolver` の `setDefaultLocale` メソッドを利用して日本語を使用するようにします。

### プロパティファイルの読み込み {#reading-property-files}

<!-- textlint-disable ja-technical-writing/sentence-length -->

Spring Framework で提供されている [`#!java PathMatchingResourcePatternResolver` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html){ target=_blank } を利用して、プロパティファイルを読み込みます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

また、 [`#!java MessageSource` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html){ target=_blank } で提供されている機能を利用して、プロパティファイルの末尾に `_ja` や `_en` のような接尾辞を付与します。
これにより、ブラウザーの言語設定に応じて読み込むプロパティファイルを切り替えます。

#### HTML とのバインディング {#binding}

テンプレートエンジンである [Thymeleaf :material-open-in-new:](https://www.thymeleaf.org/){ target=_blank } の機能を利用して、 `#{}` 構文によってメッセージを参照します。
以下のように、構文内にはプロパティファイルで定義したメッセージコードを記述します。

```html
<h1 th:text="#{announcement.register.message1}"></h1>
```
