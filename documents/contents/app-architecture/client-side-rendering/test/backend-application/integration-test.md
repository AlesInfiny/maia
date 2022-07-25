# 結合テスト ( ITa )

結合テスト ( ITa ) は、プレゼンテーション層からデータベースまでを一気通貫にテストします。

## 結合テストの目的 {: #purpose }

- [UT0](unit-test.md) でテストしたクラスやメソッドを組み合わせて、単一の業務機能が仕様通りに動作することを確認します
- プレゼンテーション層からデータベースまでを結合し、 Web API ごとの動作を検証します。

## 結合テストで利用するツール {: #testing-tools }

上記の目的を達成するため、 Maia OSS 版では以下のテストフレームワークを用いて結合テストを実施します。

- [JUnit :material-open-in-new:](https://junit.org/){ target=_blank }
    - Java のテストフレームワークです。
- [Spring Test :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/reference/html/testing.html){ target=_blank }
    - Spring で構築したアプリケーションのテストを実施するためのツールセットです。
    - 主に Spring Boot アプリケーションのランタイムを結合テスト用に初期化する目的に使用します。
- [H2 :material-open-in-new:](https://www.h2database.com/){ target=_blank }
    - テスト用のデータベースエンジンとして使用します。
    - インメモリデータベースであり、高速に動作する特徴があります。

## 結合テストのテスト対象 {: #testing-targets }

結合テストでは、プレゼンテーション層からデータベースまで、すべてのレイヤーを対象とした機能性の確認を行います。
開発したアプリケーションは原則モック化せず、完成済みのものを使用してテストします。

!!! note "結合テストでモックを利用するケース"
    以下のパターンに該当する箇所は、結合テストであってもモック化することを検討してください。

    - テストモードの存在しない外部のサービスを呼び出す個所
    - メールサービスや帳票の紙出力など、繰り返し何度も実行することがコスト上・運用上の問題となる箇所

## 結合テストの実施方法 {: #testing-method }

Maia OSS 版の CSR 方式のバックエンドアプリケーションは、 Spring Boot を用いた Web API のアプリケーションです。
結合テストでは、 Web API のリクエストとレスポンスを用いて、アプリケーションの実行と検証を行います。
Web API の実行は、 Spring Test の [MockMvc :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework){ target=_blank } を活用し JUnit 上でテストを行います。

テストが実行環境に依存しないように、データベースはインメモリの H2 を利用します。
テストの開始時にテスト用のデータベースを構築します。

単体テストで実施済みの仕様確認は、原則結合テストで改めて実施しません。
レイヤー間の結合を行ったときのアプリケーションの動作を重点的に確認するようにします。
