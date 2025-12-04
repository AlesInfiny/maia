---
title: Java アプリケーションの 処理方式
description: アプリケーションの形態によらず、 Java アプリケーションで 考慮すべき関心事について、実装方針を説明します。
---

<!-- cspell:ignore applicationcore systemcommon -->

# メッセージ管理方針 {#top}

メッセージ文字列は、表記の統一を図ることを目的にプロパティファイルで管理します。
プロパティファイルの読み込みは、 [`#!java MessageSource` :material-open-in-new:](https://docs.spring.io/spring-boot/reference/features/internationalization.html){ target=_blank } を利用します。

## プロパティファイルの管理 {#property-file-management}

プロパティファイルでは、以下のようにメッセージ文字列を識別するメッセージコードとメッセージ文字列本体をペアで管理します。

```properties title="messages.properties の例"
errorOccurred=エラーが発生しました。
...
```

## メッセージ内の埋め込みパラメータ {#parameter-of-messages}

以下のように、メッセージの中にはパラメータを含むものがあります。

```properties title="messages.properties の例"
errorOccurred=エラーが発生しました。（エラーコード：{0}）
...
```

以下のように、 `#!java MessageSource` の `getMessage` メソッドを利用してメッセージを整形し取得します。

```java title="埋め込みパラメータによるメッセージの取得"
string message = messageSource.getMessage("errorOccurred", new Object[] { "ErrorCode" }, Locale.getDefault());
```
