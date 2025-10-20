---
title: バッチ アプリケーション編
description: バッチアプリケーションのテスト方針における 結合テストについて説明します。
---

# バッチアプリケーションの結合テスト {#top}

結合テスト (ITa) は、ジョブの起動からデータの変換、データベース / ファイルへの書き込み処理までを一気通貫にテストします。
特に、ジョブは複数のステップで構成されていることから、ステップの処理順序なども含めて動作を確認します。

## 結合テストの目的 {#purpose}

バッチアプリケーションで結合テストを行う目的は以下の通りです。

- UT0 でテストしたクラスやメソッドを組み合わせて、一連のバッチ処理が仕様通りに動作することを確認します

## 結合テストで利用するツール {#testing-tools}

上記の目的を達成するため、 AlesInfiny Maia OSS Edition ( 以降、 AlesInfiny Maia) では以下のテストフレームワークを用いて結合テストを行います。

- [JUnit :material-open-in-new:](https://junit.org/){ target=_blank }
    - Java のテストフレームワークです。
- [Spring Test :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/testing.html){ target=_blank }
    - Spring で構築したアプリケーションのテストを実行するためのツールセットです。
    - 主に Spring Boot アプリケーションのランタイムを結合テスト用に初期化するため使用します。
- [Spring Batch Test :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/testing.html){ target=_blank }
    - Spring Batch ベースのテストを実行するためのツールセットです。
    - ジョブ実行を作成または削除するためのテストセットアップで使用できます。
- [H2 :material-open-in-new:](https://www.h2database.com/){ target=_blank }
    - テスト用のデータベースエンジンとして使用します。
    - バッチ処理の対象がデータベースの場合に使用します。
    - インメモリデータベースであり、高速に動作する特徴があります。

## 結合テストのテスト対象 {#testing-targets}

前提として、結合テストでは Web API アプリケーションが正常に動作している必要があります。
[Web API アプリケーションの結合テスト](../../client-side-rendering/test/backend-application/integration-test.md) までが完了していること確認してください。

その上で、バッチアプリケーションの結合テストでは、以下の点に留意する必要があります。

- ジョブの入出力ファイルの設定

    ジョブ実行時に入出力ファイルが必要な場合には、本番環境と同一のものを用意する必要があります。

- エラーハンドリング

    エラー時に中断やスキップなどが仕様通りに実行されているかを確認する必要があります。

- ログ出力

    処理件数などのログが正常に出力され、無駄なログが出力されていないかを確認する必要があります。

## 結合テストの実行方法 {#testing-method}

AlesInfiny Maia のバッチアプリケーションにおける結合テストでは、 [JobLauncherTestUtils :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/test/JobLauncherTestUtils.html){ target=_blank } を利用します。

`JobLauncherTestUtils` は `Spring Batch Test` で提供されているクラスであり、単体のステップのテストだけでなく、ジョブのエンドツーエンドのテストに利用できます。
これにより、バッチ起動から正常終了までの間で複数のステップで構成されたジョブが正しい順序で実行されているかという結合テストの検証に利用できます。

単体テストで確認済みの使用は、結合テストで改めて確認しません。各ステップを結合した際のジョブの動作や、複数のジョブを実行したときのジョブの動作を重点的に確認するようにします。
