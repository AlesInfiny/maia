---
title: バッチアプリケーション編
description: バッチアプリケーションのテスト方針を説明します。
---

# バッチアプリケーションのテスト方針 {#top}

バッチアプリケーションのテスト方針について解説します。

## テストレベル {#test-level}

テストで確認する内容に合わせて、テストレベルを定義することが一般的です。
本稿では、以下のテストレベルについて取り扱います。
これ以外のテストレベルは、実行方法などを特に解説しません。

1. 単体テスト (UT0)

    アプリケーション内のクラスやメソッドが、仕様通りに実装されているか確認するテストです。
    バッチアプリケーションの単体テストについては、[バックエンドアプリケーションの単体テスト :material-open-in-new:](../../client-side-rendering/test/backend-application/unit-test.md){ target=_blank } の構成と違いはありません。
    ただし、 Spring Batch の単体テスト用のライブラリとして以下の `Spring Batch Test` が提供されています。

    - [Spring Batch Test :material-open-in-new:](org.springframework.batch:spring-batch-test){ target=_blank }
  
    [バックエンドアプリケーションの単体テストで利用するツール :material-open-in-new:](../../app-architecture/client-side-rendering/test/backend-application/unit-test/#ut0-dynamic-testing-tools){ target=_blank } と組み合わせて、バッチアプリケーションの単体テストを実装し

1. [結合テスト (ITa)](./integration-test.md)

    アプリケーションが、外部仕様に従った機能性を有しているか確認するテストです。
    JUnit 、 Spring Test 、 H2 を用いて、ジョブの起動からデータの変換、データベース / ファイルの書き込み処理、テスト結果の確認までエンドツーエンドの動的テストを実現します。