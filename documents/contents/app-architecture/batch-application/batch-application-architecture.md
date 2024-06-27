---
title: バッチアプリケーション編
description: バッチアプリケーションを実装するための OSS ライブラリについて説明しています。
---

# バッチアプリケーションのアーキテクチャ {#top}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）において、バッチアプリケーションを構築する際に想定しているアーキテクチャの概要について説明します。

## 技術スタック {#technology-stack}

本アーキテクチャを構成する主なライブラリを以下に示します。

![アーキテクチャ概要図](../../images/app-architecture/batch-application/batch-library-light.png#only-light){ loading=lazy }
![アーキテクチャ概要図](../../images/app-architecture/batch-application/batch-library-dark.png#only-dark){ loading=lazy }

なお、本アーキテクチャでは、図の通り [クライアントサイドレンダリング方式の Web アプリケーション](../client-side-rendering/csr-architecture-overview.md) を参照・利用しています。
Web アプリケーションの利用ライブラリについては、[こちら](../client-side-rendering/csr-architecture-overview.md#technology-stack) をご覧ください。

### 利用ライブラリ {#oss-library}

バッチアプリケーションに必要なライブラリについて説明します。

- [Spring Batch :material-open-in-new:](https://spring.pleiades.io/projects/spring-batch){ target=_blank }

    トランザクション管理、ジョブ処理統計、開始 / 停止 /スキップ、リソース管理、大量のレコード処理といったバッチ処理機能を提供します。

- [Spring Batch Test :material-open-in-new:](https://mvnrepository.com/artifact/org.springframework.batch/spring-batch-test){ target=_blank }

    Spring Batch ベースのテストを実行するライブラリです。
    ジョブとステップを起動・実行するためのテストセットアップを使用できます。

## Spring Batch のアーキテクチャ {#spring-batch-architecture}

### Spring Batch のバッチ処理方式 {#spring-batch-process-type}

![アーキテクチャ概要図](../../images/app-architecture/batch-application/spring-batch-architecture-light.png#only-light){ loading=lazy }
![アーキテクチャ概要図](../../images/app-architecture/batch-application/spring-batch-architecture-dark.png#only-dark){ loading=lazy }

- [BatchAutoConfiguration :material-open-in-new:](https://spring.pleiades.io/spring-boot/api/java/org/springframework/boot/autoconfigure/batch/BatchAutoConfiguration.html){ target=_blank }

    起動時にコンテキストでジョブが見つかった場合に、自動的に実行されます。
    バッチジョブを起動するための `JobLauncherApplicationRunner` を呼び出します。
    このクラスは、 `@SpringBootApplication` アノテーションを使用している場合に自動的に有効となります。

- [JobLauncherApplicationRunner :material-open-in-new:](https://spring.pleiades.io/spring-boot/api/java/org/springframework/boot/autoconfigure/batch/JobLauncherApplicationRunner.html){ target=_blank }

- [JobLauncher :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/launch/JobLauncher.html){ target=_blank }
- [JobExecution :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/JobExecution.html){ target=_blank }
- [Job :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/Job.html){ target=_blank }
- [StepExecution :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/StepExecution.html){ target=_blank }
- [Step :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/Step.html){ target=_blank }
- [Tasklet :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/core/step/tasklet/Tasklet.html){ target=_blank }
- [Chunk :material-open-in-new:](https://spring.pleiades.io/spring-batch/docs/current/api/org/springframework/batch/item/Chunk.html){ target=_blank }

    Spring Batch におけるチャンク指向の処理の詳細は [こちら :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/step/chunk-oriented-processing.html) をご覧ください。

### Spring Batch のメタデータスキーマ {#spring-batch-meta-data-schema}
