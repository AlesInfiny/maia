---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# 集約例外ハンドラーの設定 {#top}

集約例外ハンドラーの設定方法について解説します。
<!-- textlint-disable ja-technical-writing/sentence-length -->
AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）では、 RESTful API のエラーレスポンスの標準的な仕様である [RFC9457 :material-open-in-new:](https://datatracker.ietf.org/doc/html/rfc9457){ target=_blank } に準拠した形式でエラーレスポンスを返却します。その他の実装方針については、[こちら](../../../app-architecture/client-side-rendering/backend-application/presentation.md#exception-handling) を参照してください。
<!-- textlint-enable ja-technical-writing/sentence-length -->

## 集約例外ハンドラークラスの実装 {#exception-handler-class}

<!-- textlint-disable ja-technical-writing/sentence-length -->
集約例外ハンドラークラスは [ResponseEntityExceptionHandler クラス :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html) の拡張クラスとして実装し、 `@ControllerAdvice` および `@Profile` を付与します。
<!-- textlint-enable ja-technical-writing/sentence-length -->

`@ControllerAdvice` により、付与されたクラス内で定義された機能を全てのコントローラーに適用できます。
また、 `@Profile` により、アプリケーションの動作環境ごとに集約例外ハンドラーを切り替えることができます。

以下は、ローカル環境で使用する集約例外ハンドラークラスの実装例です。

``` Java
@ControllerAdvice(basePackages = "プロジェクトの Group 名")
@Profile("local")
public class LocalExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler{
  // 例外をハンドリングするメソッド
}
```

## 例外ハンドリングのメソッドの実装 {#exception-handling-method}

集約例外ハンドラークラスには、ハンドリングする例外と対応したメソッドを実装します。

実装する各メソッドには `@ExceptionHandler` アノテーションを付与し、引数としてハンドリングを行う例外クラスを指定します。
指定した例外クラスとプレゼンテーション層まででハンドリングされなかった例外が合致した際に、メソッド内の処理が実行されます。

<!-- textlint-disable ja-technical-writing/sentence-length -->
メソッドの返り値には、 [ProblemDetail :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ProblemDetail.html){ target=_blank } を型パラメータとして指定した [ResponseEntity :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html){ target=_blank } クラスを指定します。
これにより、 RFC9457 に準拠したエラーレスポンスが返却されます。
<!-- textlint-enable ja-technical-writing/sentence-length -->

以下は、 Exception クラスをハンドリングするメソッドの実装例です。

``` Java
@ExceptionHandler(Exception.class)
public ResponseEntity<ProblemDetail> handleLogicException(Exception e, HttpServletRequest req) {
  // 例外のハンドリングを行う処理
}
```

各メソッドでは、エラーログの出力やエラーレスポンスの生成を実装します。以下にそれらの実装方法を解説します。

### エラーログの出力 {#error-log-output}

メソッドではエラーに関するアプリケーションログを出力します。
ログに含める標準的なデータやログレベルは、[ログ出力方針](../../../app-architecture/overview/java-application-processing-system/logging-policy.md) を参照してください。

### エラーレスポンスの生成 {#error-response}

先述したようにエラーレスポンスは RFC9457 に準拠させるため、メソッド内で ProblemDetail をインスタンス化し、 ResponseEntity の body に含めます。

AlesInfiny Maia では、例外メッセージをプロパティファイルから取得し、ログ出力するために ErrorMessageBuilder クラスを実装しています。
また、プロパティファイルからエラーレスポンスに含めるメッセージを整形し ProblemDetail クラスを生成する ProblemDetailsFactory クラスを実装しています。
これらを用いてエラーレスポンスを生成を実装しています。
ErrorMessageBuilder クラスおよび ProblemDetailsFactory クラスの実装例は [メッセージ管理機能の設定のメッセージの取得](./sub-project-settings/message-management.md#getting-messages) およびサンプルアプリケーションを参照ください。

以下は、これらのクラスを用いた集約例外ハンドラーの実装例です。

``` Java
@ControllerAdvice(basePackages = "プロジェクトの Group 名")
@Profile("local")
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

  @Autowired
  private ProblemDetailsFactory problemDetailsFactory;

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleException(Exception e, HttpServletRequest req) {
    ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, CommonExceptionIdConstants.E_SYSTEM, null, null);
    apLog.error(errorBuilder.createLogMessageStackTrace());
    ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
        CommonExceptionIdConstants.E_SYSTEM, HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problemDetail);
  }
}
```
