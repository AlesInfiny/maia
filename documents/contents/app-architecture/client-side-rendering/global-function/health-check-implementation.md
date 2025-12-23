---
title: CSR 編
description: クライアントサイドレンダリングを行う Web アプリケーションの アーキテクチャについて解説します。
---

# ヘルスチェック機能の実装 {#top}

AlesInfiny Maia において定義しているヘルスチェックの実装例について説明します。

ヘルスチェック機能の概要については、[ヘルスチェックの必要性](../../overview/java-application-processing-system/health-check-necessity.md) を参照してください。

## ヘルスチェック機能の 2 種類のプローブ {#health-check-probe}

ヘルスチェック機能を実装する際、以下に示すいずれかの要素を確認することで正常に動作しているとみなすことができます。

- 活動性

    アプリがクラッシュしていたり、再起動の必要性が発生したりしていないこと。

- 対応性

    アプリが正常に動作していて、かつリクエストを受信する準備ができていること。

これら 2 つの要素を満たすため、 AlesInfiny Maia のサンプルアプリケーションでは活動性と対応性の状態をユーザーに渡す 2 種類のプローブを定義します。
詳細については [実装方針](#implementation-policy) に示します。

## 実装方針 {#implementation-policy}

<!-- textlint-disable ja-technical-writing/sentence-length -->

Spring Boot を用いた Web アプリケーションでは、 [Spring Boot Actuator :material-open-in-new:](https://spring.pleiades.io/spring-boot/reference/actuator/){ target=_blank } を利用することで比較適容易にヘルスチェック機能を実装可能です。
Spring Boot Actuator はアプリケーションレベルでサーバーを監視、管理する追加機能を提供するモジュールです。

<!-- textlint-enable ja-technical-writing/sentence-length -->

AlesInfiny Maia ではこのモジュールを利用してヘルスチェック用のエンドポイントを作成することを推奨しています。
設定内容の詳細に関しては、サンプルアプリケーション内のプレゼンテーション層の application.properties を参照してください。

AlesInfiny Maia のサンプルアプリケーションでは、[前節](#health-check-probe) で述べた通り活動性と対応性をそれぞれ満たす 2 つのプローブを実装しており、
以下のアドレスでヘルスチェックの状態を確認できます。

- 活動性のプローブ（<http://localhost:8080/api/health/check>）

    アプリが正常に動作していることを確認するためのプローブ。

- 対応性のプローブ（<http://localhost:8080/api/health/datasource>）

    データベースの正常動作によって、リクエストを受信する準備ができていることを確認するためのプローブ。

本ドキュメントにおける [ヘルスチェックのレスポンス形式](../../overview/java-application-processing-system/health-check-necessity.md#health-check-response) に基づき、アクセスしたアドレスからの HTTP レスポンスは以下のように定義します。これにより、簡潔な形でサーバーの状態を把握できます。

| ステータス     | ステータスコード | レスポンスボディ              | 詳細                                     |
| -------------- | ---------------- | ----------------------------- | ---------------------------------------- |
| UP             | 200              | { "status": "UP"}             | サーバーが正常に起動している             |
| DOWN           | 503              | { "status": "DOWN"}           | サーバーが異常を起こし停止している       |
| OUT_OF_SERVICE | 503              | { "status": "OUT_OF_SERVICE"} | サーバーが意図的にシャットダウンしている |
| UNKNOWN        | 200              | { "status": "UNKNOWN"}        | サーバーが定義していないステータスである |
