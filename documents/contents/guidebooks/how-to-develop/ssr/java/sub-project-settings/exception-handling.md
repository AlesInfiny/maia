---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# 集約例外ハンドラーの設定 {#top}

集約例外ハンドラーの設定方法について解説します。集約例外ハンドラーを実装することで、複数のコントローラーで必要となる、プレゼンテーション層までで処理されなかった業務例外やシステム例外を一元的にハンドリングする機能を提供できます。

<!-- textlint-disable ja-technical-writing/sentence-length -->
AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）では、 RESTful API のエラーレスポンスの標準的な仕様である [RFC9457 :material-open-in-new:](https://datatracker.ietf.org/doc/html/rfc9457){ target=_blank } に準拠した形式でエラーレスポンスを返却します。その他の実装方針については、[こちら](../../../../../app-architecture/client-side-rendering/backend-application/presentation.md#exception-handling) を参照してください。
<!-- textlint-enable ja-technical-writing/sentence-length -->

集約例外ハンドラーは web プロジェクトに実装します。本設定で利用するフォルダーの構成は以下の通りです。

```text linenums="0"
root/ --------------------------------------------------- root フォルダー
└ web/src/main/java/{ プロジェクトのグループ名 }/web/controller/advice
  └ ExceptionHandlerControllerAdvice.java --------------- 集約例外ハンドラークラス
```

本手順では、初めに集約例外ハンドラークラスを定義し、次にそれぞれの例外をハンドリングするメソッドを定義します。
最後に、定義したメソッドに AlesInfiny Maia の実装方針に沿った、例外を適切にハンドリングする機能を実装します。

## 集約例外ハンドラークラスの定義 {#exception-handler-class}

<!-- textlint-disable ja-technical-writing/sentence-length -->
集約例外ハンドラークラスは [ResponseEntityExceptionHandler クラス :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html){ target=_blank } の拡張クラスとして定義し、 `@ControllerAdvice` を付与します。
<!-- textlint-enable ja-technical-writing/sentence-length -->

`@ControllerAdvice` により、付与されたクラス内で実装された機能を全てのコントローラーに追加で適用できます。

```java title="集約例外ハンドラークラスの例"
@ControllerAdvice(basePackages = "プロジェクトのグループ名")
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler{
  // 例外をハンドリングするメソッド
}
```

## 例外ハンドリングするメソッドの定義 {#exception-handling-method}

集約例外ハンドラークラスには、ハンドリングする例外と対応したメソッドを定義します。

定義する各メソッドには `@ExceptionHandler` アノテーションを付与し、引数としてハンドリングする例外クラスを指定します。
指定した例外クラスとプレゼンテーション層まででハンドリングされなかった例外が合致した際に、メソッド内の処理が実行されます。

<!-- textlint-disable ja-technical-writing/sentence-length -->
メソッドの返り値には、 [ProblemDetail :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ProblemDetail.html){ target=_blank } を型パラメータとして指定した [ResponseEntity :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html){ target=_blank } クラスを指定します。
これにより、 RFC9457 に準拠したエラーレスポンスが返却されます。
<!-- textlint-enable ja-technical-writing/sentence-length -->

```java title="Exception クラスをハンドリングするメソッドの例"
@ExceptionHandler(Exception.class)
public ResponseEntity<ProblemDetail> handleException(Exception e, HttpServletRequest req) {
  // 例外のハンドリングを行う処理
}
```

## 例外ハンドリングするメソッドの実装 {#exception-handling-method-implementation}

各メソッドでは、エラーログの出力やエラーレスポンスの生成を実装します。以下にそれらの実装方法を解説します。

### エラーログの出力 {#error-log-output}

エラーに関するアプリケーションログを出力する処理を実装します。
ログに含める標準的なデータやログレベルは、[ログ出力方針](../../../../../app-architecture/overview/java-application-processing-system/logging-policy.md) を参照してください。

### エラーレスポンスの生成 {#error-response}

エラーレスポンスを生成する処理を実装します。
エラーレスポンスは RFC9457 に準拠させるため、メソッド内で ProblemDetail をインスタンス化し、 ResponseEntity の body に含めます。

AlesInfiny Maia では、例外メッセージをプロパティファイルから取得し、ログ出力するために ErrorMessageBuilder クラスを実装しています。
また、プロパティファイルからエラーレスポンスに含めるメッセージを整形し ProblemDetail クラスを生成する ProblemDetailsFactory クラスを実装しています。
そして、これらを用いてエラーレスポンスを生成を実装しています。

ErrorMessageBuilder クラスおよび ProblemDetailsFactory クラスの実装例は [メッセージ管理機能の設定 - メッセージの取得](./message-management.md#getting-messages) およびサンプルアプリケーションを参照ください。

??? example "ProblemDetail および ErrorMessageBuilder を用いた集約例外ハンドラーの実装例"

    ```java title="ExceptionHandlerControllerAdvice.java" hl_lines="32-33 84-95 104-115 124-134"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/web/src/main/java/com/dressca/web/controller/advice/ExceptionHandlerControllerAdvice.java
    ```
