---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# 集約例外ハンドラーの設定 {#top}

集約例外ハンドラーの設定方法について解説します。実装方針については、[こちら](../../../app-architecture/client-side-rendering/backend-application/presentation.md#exception-handling) を参照してください。

## 集約例外ハンドラークラスの実装 {#global-error-handler-implementation}

集約例外ハンドラークラスは [ResponseEntityExceptionHandler クラス :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html) の拡張クラスとし、 `@ControllerAdvice` アノテーションを付与します。また、 `@Profile` アノテーションを付与することで起動環境ごとに集約例外ハンドラーを切り替えることができます。

以下は、開発環境で使用する集約例外ハンドラークラスの実装例です。

``` Java
@ControllerAdvice(basePackages = "プロジェクトの Group 名")
@Profile("local")
public class LocalExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler{
    // 例外をハンドリングするメソッド
}
```

## 例外ハンドリングのメソッドの実装 {#error-handling-method-implementation}

集約例外ハンドラークラスには、ハンドリングする例外と対応したメソッドを実装します。

<!-- textlint-disable ja-technical-writing/sentence-length -->
各メソッドには `@ExceptionHandler` アノテーションを付与し、引数としてハンドリングを行う例外クラスを指定します。
また、メソッドの返り値には、 [ProblemDetail :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ProblemDetail.html){ target=_blank } を型パラメータとして指定した [ResponseEntity :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html){ target=_blank } クラスを指定します。
これにより、 RESTful API のエラーレスポンスの標準的な仕様である [RFC9457 :material-open-in-new:](https://datatracker.ietf.org/doc/html/rfc9457){ target=_blank } に準拠したエラーレスポンスを返却できます。
<!-- textlint-enable ja-technical-writing/sentence-length -->

``` Java
@ExceptionHandler(Exception.class)
public ResponseEntity<ProblemDetail> handleLogicException(Exception e, HttpServletRequest req) {
    // 例外のハンドリングを行う処理
}
```

各メソッドでは、エラーログの出力やエラーレスポンスの生成をします。以下にそれらの実装方法を解説します。

### エラーログの出力 {#error-log-output}

メソッドではエラーログを出力します。
出力するログレベルは、[こちら](../../../app-architecture/overview/java-application-processing-system/logging-policy.md#log-level) を参照して適切なログレベルを指定してください。

### エラーレスポンスの生成 {#error-response}

先述したようにエラーレスポンスは、 [RFC9457 :material-open-in-new:](https://datatracker.ietf.org/doc/html/rfc9457){ target=_blank } に準拠させるため、メソッド内で ProblemDetail をインスタンス化して、返り値に詰め込みます。

AlesInfiny Maia では、例外メッセージをプロパティファイルから取得し、ログ出力するために ErrorMessageBuilder クラスを使用しています。
また、プロパティファイルからエラーレスポンスに含めるメッセージを整形するために ProblemDetailsFactory クラスを実装しています。
これらのクラスを活用して、エラーレスポンスを生成します。
ErrorMessageBuilder クラスおよび ProblemDetailsFactory クラスの実装例は [メッセージ管理機能の設定のメッセージの取得](./sub-project-settings/message-management.md#getting-messages) およびサンプルアプリケーションを参照ください。

ここまで示した実装を実装した集約例外ハンドラーの実装例を以下に示す。

``` Java
@ControllerAdvice(basePackages = "プロジェクトの Group 名")
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

    @Autowired
    private ProblemDetailsFactory problemDetailsFactory;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e, HttpServletRequest req) {
        ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, CommonExceptionIdConstants.E_SYSTEM, null, null);
        apLog.error(errorBuilder.createLogMessageStackTrace());
        ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(errorBuilder,
            CommonExceptionIdConstants.E_SYSTEM,
            HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problemDetail);
    }
}
```
