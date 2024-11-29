---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

<!-- cspell:ignore applicationcore systemcommon -->

# メッセージ管理機能の設定 {#top}

バックエンドのメッセージ管理方針に関するアーキテクチャについては、[こちら](../../../app-architecture/overview/java-application-processing-system/message-management-policy.md) をご確認ください。

## 設定方法 {#settings}

本設定で利用するフォルダーの構成は以下の通りです。

```terminal linenums="0"
root/ ------------------------------------------ root フォルダー
 ├ application-core/src/main/resource
 │ └ applicationcore --------------------------- 業務メッセージのプロパティファイルを一元管理するフォルダー
 │    └ message.properties --------------------- 業務メッセージのプロパティファイル
 └ system-common/src/main/resource
   └ systemcommon ------------------------------ 共通メッセージのプロパティファイルを一元管理するフォルダー
      └ message.properties --------------------- 共通メッセージのプロパティファイル
```

### プロパティファイルの作成 {#creating-property-file}

メッセージに関するプロパティファイルは各サブプロジェクトの `/src/main/resource/<サブプロジェクト名>` フォルダーに集約します。
以下のように、メッセージ本体を格納するプロパティファイルを作成します。

```properties title="message.properties"
systemError=想定外のシステムエラーが発生しました
businessError=想定外の業務エラーが発生しました
```

### プロパティファイルの読込 {#reading-property-files}

以下のように、 web プロジェクトなどエントリーポイントとなるサブプロジェクトの application.properties にプロパティファイルを読み込む設定を記載します。

``` properties title="application.properties"
spring.messages.basename=applicationcore.messages,systemcommon.messages
```

読み込むプロパティファイルは `src/main/resource` 配下の `<フォルダー名>.<ファイル名>` で指定します。
プロパティファイルが複数ある場合は、ファイルの間をカンマで区切ります。

### メッセージの取得 {#using-messages}

読み込んだプロパティファイルのメッセージを取得するためには、 [`MessageSource` :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/context/MessageSource.html){ target=_blank } クラスを利用します。

以下は、プロパティファイルからメッセージを取得し、ログに出力するためのエラーメッセージを整形する `ErrorMessageBuilder` クラスの例です。

```java title="ErrorMessageBuilder.java" hl_lines="5 14"
@Getter
@AllArgsConstructor
public class ErrorMessageBuilder {

  private static final MessageSource messageSource = (MessageSource) ApplicationContextWrapper.getBean(MessageSource.class);

  private Exception ex;
  private String exceptionId;
  private String[] logMessageValue;
  private String[] frontMessageValue;

  public String createLogMessageStackTrace() {
    StringBuilder builder = new StringBuilder();
    String exceptionMessage = messageSource.getMessage(exceptionId, logMessageValue, Locale.getDefault());
    builder.append(exceptionId).append(" ").append(exceptionMessage).append(SystemPropertyConstants.LINE_SEPARATOR);
    StringWriter writer = new StringWriter();
    ex.printStackTrace(new PrintWriter(writer));
    builder.append(writer.getBuffer().toString());
    return builder.toString();
  }
}
```
