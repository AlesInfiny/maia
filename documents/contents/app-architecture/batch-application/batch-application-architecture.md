---
title: バッチ アプリケーション編
description: バッチアプリケーションを実装するための OSS ライブラリについて説明しています。
---

# バッチアプリケーションのアーキテクチャ {#top}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）において、バッチアプリケーションを構築する際に想定しているアーキテクチャの概要について説明します。

## 技術スタック {#technology-stack}

本アーキテクチャを構成する主なライブラリを以下に示します。

![アーキテクチャ概要図](../../images/app-architecture/batch-application/batch-library-light.png#only-light){ loading=lazy }
![アーキテクチャ概要図](../../images/app-architecture/batch-application/batch-library-dark.png#only-dark){ loading=lazy }

なお、本アーキテクチャでは、図の通り [クライアントサイドレンダリング方式のバックエンドアプリケーション](../client-side-rendering/csr-architecture-overview.md) の一部を参照しています。
具体的には、バックエンドアプリケーションのアプリケーションコア層およびインフラストラクチャ層を参照することでロジックを再利用しているほか、プレゼンテーション層にあたる部分をバッチアプリケーションで設けたバッチ層が置換している形です。
Web アプリケーションの利用ライブラリについては、[こちら](../client-side-rendering/csr-architecture-overview.md#technology-stack) をご覧ください。

### 利用ライブラリ {#oss-library}

バッチアプリケーションに必要なライブラリについて説明します。

- [Spring Batch :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/){ target=_blank }

    トランザクション管理、ジョブ処理統計、開始 / 停止 / スキップ、エラーハンドリング、フロー制御、リソース管理、大量のレコード処理といったバッチ処理機能を提供します。

- [Spring Batch Test :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/testing.html){ target=_blank }

    Spring Batch ベースのテストを実行するライブラリです。
    ジョブを起動・実行するためのテストセットアップを使用できます。

## Spring Batch のアーキテクチャ {#spring-batch-architecture}

### Spring Batch のバッチ処理方式 {#spring-batch-process-type}

Spring Batch 上でバッチ処理を実行する際の流れを以下に示します。

![アーキテクチャ概要図](../../images/app-architecture/batch-application/spring-batch-architecture-light.png#only-light){ loading=lazy }
![アーキテクチャ概要図](../../images/app-architecture/batch-application/spring-batch-architecture-dark.png#only-dark){ loading=lazy }

上図で記載されている各クラスの説明は以下の通りです。

- [BatchAutoConfiguration :material-open-in-new:](https://spring.pleiades.io/spring-boot/api/java/org/springframework/boot/batch/autoconfigure/BatchAutoConfiguration.html){ target=_blank }

    起動時にコンテキストで `Job` が見つかった場合に、自動的に実行されます。
    複数の `Job` がある場合には、起動時に実行する `Job` 名を `application.properties` の `spring.batch.job.name` で指定できます。
    その後、`Job` を起動するための `JobLauncherApplicationRunner` を呼び出します。
    このクラスは、 `#!java @SpringBootApplication` を使用している場合に自動的に有効となります。

- [JobLauncherApplicationRunner :material-open-in-new:](https://spring.pleiades.io/spring-boot/api/java/org/springframework/boot/batch/autoconfigure/JobLauncherApplicationRunner.html){ target=_blank }

    Spring Batch アプリケーションの `Job` を起動するための `ApplicationRunner` クラスです。
    `BatchAutoConfiguration` によって自動的に実行されます。

- [JobLauncher :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/launch/JobLauncher.html){ target=_blank }

    `Job` を起動するためのインターフェースです。
    指定されたジョブを呼び出し、バッチ処理を開始します。

- [JobRepository :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/repository/JobRepository.html){ target=_blank }

    `JobExecution` や `StepExecution` 等のバッチアプリケーションのジョブ実行履歴やジョブの実行状態を表したメタデータの管理・永続化を実現する機能を提供します。

- [JobExecution :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/JobExecution.html){ target=_blank }

    `Job` を管理する実行単位です。
    失敗した `Job` を再実行すると、新しい `JobExecution` が作成されます。

- [Job :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/Job.html){ target=_blank }

    `Step` を管理する実行単位です。
    `Job` 自身に登録されている `Step` を起動し、 `StepExecution` を生成します。

- [Step :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/Step.html){ target=_blank }

    `Step` は、 `Job` を構成する処理の単位であり、 1 つの `Job` に複数の `Step` を指定可能です。
    コミット間隔などの設定に基づき、ビジネスロジックを初期化してトランザクション管理を実現します。

- [Chunk :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/item/Chunk.html){ target=_blank }

    `Chunk` とは、 1 つずつデータを読み取り、中間コミットを発行して動作状態を保存しながらバッチ処理を行う仕組みを提供するクラスです。
    `Chunk` モデルでは、以下の 3 段階の処理からなります。

    - ItemReader

        DB やファイルからデータを取得し、 ItemProcessor に渡します。

    - ItemProcessor

        ItemReader から受け取ったデータを変換します。

    - ItemWriter

        変換したデータを DB やファイルに書き込みます。

    Spring Batch における `Chunk` の詳細は [チャンク指向の処理 :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/step/chunk-oriented-processing.html){ target=_blank } をご覧ください。

- [Tasklet :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/step/tasklet/Tasklet.html){ target=_blank }

    `Job` を全体で 1 回のコミットで処理する仕組みを提供するクラスです。
    `Chunk` モデルのように `ItemReader` や `ItemProcessor` 、 `ItemWriter` と処理を分ける必要がないため、実装に慣れていない開発者にとっては実装しやすい特徴があります。
    また、バッチ処理の実装が 1 クラスにまとまり見通しがよくなります。

!!! note "Chunk モデルと Tasklet モデルの使い分け"

    Spring Batch では、バッチ処理の性質によって、 `Chunk` モデルと `Tasklet` モデルを使い分けることが推奨されています。
    以下に示す観点に基づき、各バッチ処理に応じてどちらのモデルを使用するかを考慮することで、状況に応じたモデルの併用を実現しましょう。

    - データ処理量

        `Chunk` モデルは、一定件数で中間コミットを発行しながら処理を実施します。これに対し、 `Tasklet` モデルは一括コミットにて処理することが基本です。
        処理するデータが大量になると、一括コミットする `Tasklet` モデルではリソースが枯渇し正常に処理できない可能性があります。
        大量のデータを安定して処理する必要がある場合には、`Chunk` モデルを使用するとよいでしょう。

    - ジョブの再開能力
        
        `Chunk` モデルは、件数ベースのリスタートを実現できます。これにより、エラーとなったデータ以降からバッチ処理を再開することができます。
        これに対し `Tasklet` モデルは、 1 つの `Tasklet` が完全に成功するか失敗するかのどちらかとなり、失敗した場合は処理を再実行することになります。
        エラーとなったジョブを再実行するのみで復旧する場合は `Tasklet` モデルを、バッチジョブを完了する必要がある時間枠（バッチウィンドウ）が厳密でエラーとなったデータ以降の復旧が必要な場合は `Chunk` モデルを使用するとよいでしょう。

### Spring Batch のメタデータスキーマ {#spring-batch-meta-data-schema}

Spring Batch では、バッチの実行履歴や実行状態をデータベースに保存するため、 Spring Batch で規定しているメタデータテーブルを作成する必要があります。
作成されるメタデータテーブルの詳細については、[Spring Batch ドキュメントのメタデータスキーマ :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/schema-appendix.html){ target=_blank } をご覧ください。
