---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# 例外ハンドリングの設定 {#top}

例外ハンドリングの設定方法について解説します。例外ハンドリングの実装方針については、[こちら](../../../app-architecture/client-side-rendering/backend-application/presentation.md#exception-handling) を参照してください。

## エラーレスポンスの実装方法 {#error-response}

[RFC9457 :material-open-in-new:](https://datatracker.ietf.org/doc/html/rfc9457){ target=_blank } に準拠したエラーレスポンスを返すクラスとして、 Spring Boot には ProblemDetails クラスが用意されています。

例外をハンドリングする際の実装は以下の通りです。

``` Java
    try {
        ・・・
    } catch (ExampleException e) {
      apLog.info(e.getMessage());
      apLog.debug(ExceptionUtils.getStackTrace(e));
      ErrorMessageBuilder errorBuilder = new ErrorMessageBuilder(e, e.getExceptionId(),
          e.getLogMessageValue(), e.getFrontMessageValue());
      ProblemDetail problemDetail = problemDetailsFactory.createProblemDetail(
          errorBuilder,
          CommonExceptionIdConstants.E_BUSINESS,
          HttpStatus.NOT_FOUND);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .contentType(MediaType.APPLICATION_PROBLEM_JSON)
          .body(problemDetail);
    }
```

プレゼンテーション層まででハンドリングされなかった業務例外やシステム例外は、集約例外ハンドラーで処理します。

集約例外ハンドラークラスは [ResponseEntityExceptionHandler クラス :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html)　の拡張クラスとします。
集約例外ハンドラークラスには `@ControllerAdvice` アノテーションを付与する。
これにより、コントローラーの共通処理として、例外ハンドリング機能を付与できる。

``` Java
@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {}
```

``` Java
@ExceptionHandler(Exception.class)
public ResponseEntity<ProblemDetail> handleException(Exception e, HttpServletRequest req){}
```

``` Java
@ControllerAdvice
@Profile("production | test")
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler{}
@ControllerAdvice
@Profile("local")
public class LocalExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler{}
```
