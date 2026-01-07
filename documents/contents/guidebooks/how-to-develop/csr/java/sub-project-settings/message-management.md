---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

<!-- cspell:ignore applicationcore systemcommon -->

# メッセージ管理機能の設定 {#top}

バックエンドのメッセージ管理方針に関するアーキテクチャについては、[こちら](../../../../../app-architecture/client-side-rendering/global-function/message-management-policy.md#backend-application) をご確認ください。

## 設定方法 {#settings}

本設定で利用するフォルダーの構成は以下の通りです。

```text linenums="0"
root/ ------------------------------------------- root フォルダー
 ├ application-core/src/main/resource
 │ └ applicationcore ---------------------------- 業務メッセージのプロパティファイルを一元管理するフォルダー
 │    └ messages.properties --------------------- 業務メッセージのプロパティファイル
 └ system-common/src/main/resource
   └ systemcommon ------------------------------- 共通メッセージのプロパティファイルを一元管理するフォルダー
      └ messages.properties --------------------- 共通メッセージのプロパティファイル
```

### プロパティファイルの作成 {#creating-property-file}

メッセージに関するプロパティファイルは各サブプロジェクトの `/src/main/resource/<サブプロジェクト名>` フォルダーに集約します。
以下のように、メッセージ本体を格納するプロパティファイルを作成します。

```properties title="message.properties"
systemError=想定外のシステムエラーが発生しました
businessError=想定外の業務エラーが発生しました
```

### プロパティファイルの読み込み {#reading-property-files}

以下のように、 web プロジェクトなどエントリーポイントとなるサブプロジェクトの application.properties にプロパティファイルを読み込む設定を記載します。

```properties title="application.properties"
spring.messages.basename=applicationcore.messages,systemcommon.messages
```

読み込むプロパティファイルは `src/main/resource` 配下の `<フォルダー名>.<ファイル名>` で指定します。
プロパティファイルが複数ある場合は、ファイルの間をカンマで区切ります。

### メッセージの取得 {#getting-messages}

読み込んだプロパティファイルのメッセージを取得するためには、 [`MessageSource` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html){ target=_blank } インターフェースを利用します。

以下は、プロパティファイルからメッセージを取得し、ログに出力するためのエラーメッセージを整形する `ErrorMessageBuilder` クラスの例です。

??? example "サンプルアプリケーションの ErrorMessageBuilder.java"

    ```java title="ErrorMessageBuilder.java" hl_lines="19 20 34"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web/src/main/java/com/dressca/web/log/ErrorMessageBuilder.java
    ```

<!-- textlint-disable ja-technical-writing/sentence-length -->
また、 `#!java @Service` や `#!java @Controller` 、 `#!java @Component` といった Bean 登録されたクラス内で `MessageSource` を利用する場合は、 `#!java @Autowired` による DI で実装します。
<!-- textlint-enable ja-technical-writing/sentence-length -->

以下は、プロパティファイルからエラーレスポンスに含めるメッセージを整形する `ProblemDetailsFactory.java` クラスの例です。

??? example "サンプルアプリケーションの ProblemDetailsFactory.java"

    ```java title="ProblemDetailsFactory.java" hl_lines="26 27 39 41"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web/src/main/java/com/dressca/web/controller/advice/ProblemDetailsFactory.java
    ```
