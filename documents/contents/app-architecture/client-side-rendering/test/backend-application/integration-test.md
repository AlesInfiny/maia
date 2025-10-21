---
title: CSR 編 - テスト
description: バックエンドアプリケーションのテスト方針について解説します。
---

# 結合テスト ( ITa ) {#top}

結合テスト ( ITa ) は、プレゼンテーション層からデータベースまでを一気通貫にテストします。

## 結合テストの目的 {#purpose}

- [UT0](unit-test.md) でテストしたクラスやメソッドを組み合わせて、単一の業務機能が仕様通りに動作することを確認します
- プレゼンテーション層からデータベースまでを結合し、 Web API ごとの動作を検証します。

## 結合テストで利用するツール {#testing-tools}

上記の目的を達成するため、 AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）では以下のテストフレームワークを用いて結合テストを行います。

- [JUnit :material-open-in-new:](https://junit.org/){ target=_blank }
    - Java のテストフレームワークです。
- [Spring Test :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/testing.html){ target=_blank }
    - Spring で構築したアプリケーションのテストを実行するためのツールセットです。
    - 主に Spring Boot アプリケーションのランタイムを結合テスト用に初期化するため使用します。
- [H2 :material-open-in-new:](https://www.h2database.com/html/main.html){ target=_blank }
    - テスト用のデータベースエンジンとして使用します。
    - インメモリデータベースであり、高速に動作する特徴があります。

## 結合テストのテスト対象 {#testing-targets}

結合テストでは、プレゼンテーション層からデータベースまで、すべての層を対象とした機能性を確認します。
開発したアプリケーションは原則モック化せず、完成済みのものを使用してテストします。

!!! note "結合テストでモックを利用する場合"
    以下のパターンに該当する箇所は、結合テストであってもモック化することを検討してください。

    - テストモードの存在しない外部のサービスを呼び出す個所
    - メールサービスや帳票の紙出力など、繰り返し何度も実行することがコスト上・運用上の問題となる箇所

## 結合テストの実行方法 {#testing-method}

AlesInfiny Maia の CSR 方式のバックエンドアプリケーションは、 Spring Boot を用いた Web API のアプリケーションです。
結合テストでは、 Web API のリクエストを疑似的に再現し、アプリケーションの実行とレスポンスの検証します。
Web API のリクエスト送信は、 Spring Test の [MockMvc :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/testing/mockmvc.html){ target=_blank } を活用します。
テストフレームワークは JUnit を利用します。

テストが実行環境に依存しないように、データベースはインメモリの H2 を利用します。
各テストケースの処理開始時に、そのテストで利用するデータベースを構築します。

単体テストで確認済みの仕様は、結合テストで改めて確認しません。
層の間で結合をしたときのアプリケーションの動作を重点的に確認するようにします。
