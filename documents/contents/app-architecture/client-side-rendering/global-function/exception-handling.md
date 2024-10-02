---
title: CSR 編
description: クライアントサイドレンダリングを行う Web アプリケーションの アーキテクチャについて解説します。
---

# 例外処理方針 {#top}

バックエンドアプリケーションで発生するシステム例外や業務例外は、例外フィルターによって捕捉します。
例外フィルターでは、ログ出力方針に従ってアプリケーションログを出力します。

システム例外を例外フィルターで捕捉した場合は、 HTTP 500 のエラーレスポンスをフロントエンドアプリケーションに返却します。
フロントエンドアプリケーションは、システムエラー画面に遷移し、 Sorry メッセージを出力します。
フロントエンドアプリケーション内で例外が発生した場合も、同様にシステムエラー画面に遷移します。

業務例外を例外フィルターで補足した場合は、 HTTP 400 のエラーレスポンスをフロントエンドアプリケーションに返却します。
フロントエンドアプリケーションは、その画面内のメッセージ領域にエラーメッセージを表示します。

![例外処理方針](../../../images/app-architecture/client-side-rendering/exception-handling-policy-light.png#only-light){ loading=lazy }
![例外処理方針](../../../images/app-architecture/client-side-rendering/exception-handling-policy-dark.png#only-dark){ loading=lazy }
