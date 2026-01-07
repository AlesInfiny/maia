---
title: Java 編 （SSR 編）
description: SSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# 集約例外ハンドラーの設定 {#top}

集約例外ハンドラーの設定方法について解説します。集約例外ハンドラーを実装することで、複数のコントローラーで必要となる、プレゼンテーション層までで処理されなかった業務例外やシステム例外を一元的にハンドリングする機能を提供できます。

SSR アプリケーションでは、エラー発生時にエラー情報を含んだ HTML ページをレンダリングしてユーザーに提示します。

集約例外ハンドラーは web プロジェクトに実装します。本設定で利用するフォルダーの構成は以下の通りです。

```text linenums="0"
root/ --------------------------------------------------- root フォルダー
└ web/src/main/java/{ プロジェクトのグループ名 }/web/controller/advice
  └ ExceptionHandlerControllerAdvice.java --------------- 集約例外ハンドラークラス
```

本手順では、初めに集約例外ハンドラークラスを定義し、次にそれぞれの例外をハンドリングするメソッドを定義します。
最後に、定義したメソッドに AlesInfiny Maia の実装方針に沿った、例外を適切にハンドリングする機能を実装します。

## 集約例外ハンドラークラスの定義 {#exception-handler-class}

集約例外ハンドラークラスの定義は CSR 編と同様です。
[こちら](../../../csr/java/sub-project-settings/exception-handling.md#exception-handler-class) を参照してください。

## 例外ハンドリングするメソッドの定義 {#exception-handling-method}

集約例外ハンドラークラスには、ハンドリングする例外と対応したメソッドを定義します。

定義する各メソッドには `@ExceptionHandler` アノテーションを付与し、引数としてハンドリングする例外クラスを指定します。
指定した例外クラスとプレゼンテーション層まででハンドリングされなかった例外が合致した際に、メソッド内の処理が実行されます。

<!-- textlint-disable ja-technical-writing/sentence-length -->
メソッドの引数には、例外オブジェクトと [Model :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/ui/Model.html){ target=_blank } を指定し、返り値にはビュー名を表す String を指定します。
Model にエラー情報を設定し、ビュー名を返すことで、エラー情報を含んだ HTML ページがレンダリングされてユーザーに表示されます。
<!-- textlint-enable ja-technical-writing/sentence-length -->

```java title="Exception クラスをハンドリングするメソッドの例"
@ExceptionHandler(Exception.class)
public String handleException(Exception e, Model model) {
  // 例外のハンドリングを行う処理
  return "error"; // エラーページのビュー名
}
```

## 例外ハンドリングするメソッドの実装 {#exception-handling-method-implementation}

各メソッドでは、エラーログの出力やエラーレスポンスの生成を実装します。以下にそれらの実装方法を解説します。

### エラーログの出力 {#error-log-output}

エラーに関するアプリケーションログを出力する処理を実装します。
ログに含める標準的なデータやログレベルは、[ログ出力方針](../../../../../app-architecture/overview/java-application-processing-system/logging-policy.md) を参照してください。

### エラーレスポンスの生成 {#error-response}

エラーレスポンスを生成する処理を実装します。
SSR アプリケーションでは、エラー情報を Model に設定し、エラーページのビュー名を返すことで、エラーページを HTML としてレンダリングします。

例外の種類に応じて、エラーコードや発生日時などの情報を Model に追加し、適切なビュー名（例：`error`、`not_found`）を返します。
ログ出力も合わせて行い、デバッグ時にはスタックトレースも出力することで、問題の原因を特定しやすくします。
