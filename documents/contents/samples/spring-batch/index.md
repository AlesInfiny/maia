---
title: Spring Batch を利用したバッチアプリケーション
description: Spring Batch を利用したバッチアプリケーションのサンプルと、 その起動方法を解説します。
---

# Spring Batch を利用したバッチアプリケーション {#top}

## 概要 {#about-this-sample}

Spring Batch を利用したバッチアプリケーションの簡易な実装サンプルを提供します。

バッチアプリケーションのアーキテクチャについては、
[バッチアプリケーションのアーキテクチャ](../../app-architecture/batch-application/batch-application-architecture.md) を参照してください。
サンプルではこちらのアーキテクチャに則って、バッチアプリケーションを実装しています。

## サンプルで定義されているジョブ {#sample-jobs}

サンプルでは以下の 2 つのジョブを定義しています。

- catalogItem_job
  
    Chunk モデルで作られたジョブです。商品情報を取得し、商品名を先頭 10 文字に切り詰めたうえで CSV に出力します。

- catalogItem_tasklet_job
  
    Tasklet モデルで作られたジョブです。処理内容は catalogItem_job と同様です。

## サンプルの起動方法 {#how-to-launch}

バッチアプリケーションは Web アプリケーションと同様に、 Spring Boot をベースに作られています。
そのためアプリケーションの起動方法についても、 Web アプリケーションと同様に以下の通りです。

- VS Code で Gradle のタスクを実行する場合

    VS Code のアクティビティーバーにある「 Gradle 」をクリックし、サイドバーの「 GRADLE PROJECTS 」タブから以下のタスクを実行します。

    batch > Tasks > application > bootRun

- コマンドラインから Gradle のタスクを実行する場合

    以下の通り、コマンドを実行します。

    ```shell title="コマンドラインでの Gradle の起動"
    gradlew :batch:bootRun
    ```

- VS Code の実行とデバッグビュー（Run and Debug）で起動する場合

    既定の 2 つのジョブそれぞれについて、 launch.json（VS Code 上のアプリケーションの実行構成ファイル）に定義済みです。

    VS Code のアクティビティーバーにある「 Run and Debug 」をクリックし、ビュー上部のドロップダウンリストにて、実行したいアプリケーションを指定して実行してください。

- 実行可能 jar としてパッケージングした jar を実行する場合

    以下の通り、コマンドを実行します。
  
    ```shell title="実行可能 jar の起動"
    # gradlew :batch:bootJar で実行可能jarを生成した想定
    java -jar batch/build/libs/batch-0.0.1-SNAPSHOT.jar
    ```

## 動作させるジョブの指定方法 {#specifying-the-job}

Spring Batch では、複数のジョブが定義されている場合、実行すべきジョブを指定しないとエラーが発生するため、ジョブの指定は必須です。

ジョブの指定方法は `spring.batch.job.name` という環境変数で指定します。
サンプルでは開発環境用のデフォルト設定として、 `application-dev.properties` にて
`spring.batch.job.name=catalogItem_tasklet_job` と指定しています。
これにより起動時にジョブを指定しなくても catalogItem_tasklet_job が実行されるようになっています。

```properties title="application-dev.properties でのジョブ指定"
spring.batch.job.name=catalogItem_tasklet_job
```

catalogItem_job を実行するように、起動時に指定する場合には、以下の方法で指定します。

- Gradle で実行する場合

    以下のように `--args` オプションで指定します。

    ```shell title="コマンドラインでの Gradle の起動（ジョブ指定）"
    gradlew :batch:bootRun --args="--spring.batch.job.name=catalogItem_job"
    ```

    VS Code から Gradle タスクを実行する場合は、タスク選択時に右クリックを押下することで、オプション引数を付与できます。

- 実行可能 jar としてパッケージングした jar を実行する場合

    以下のように、コマンドを実行します。

    ```shell title="実行可能 jar の起動（ジョブ指定）"
    # gradlew :batch:bootJar で実行可能 jar を生成した想定
    java -jar batch/build/libs/batch-0.0.1-SNAPSHOT.jar --spring.batch.job.name=catalogItem_job
    ```

## ジョブ独自の引数の指定方法 {#specifying-arguments}

Spring Batch で定義されたジョブは、それぞれ独自の引数を定義できます。
本サンプルで定義されている 2 つのジョブは、それぞれ `output` という名前のオプション引数を定義しており、
出力する CSV ファイル名を設定できます。

実行時に `output` という引数に `sample-output.csv` を指定する場合には、以下の方法で指定します。

- Gradle で実行する場合

    以下のように `--args` オプションで指定します。

    ```shell title="コマンドラインでの Gradle の起動（ジョブ引数指定）"
    gradlew :batch:bootRun --args="output=sample-output.csv"
    ```

    VS Code から Gradle タスクを実行する場合は、タスク選択時に右クリックを押下することで、オプション引数を付与できます。

- 実行可能 jar としてパッケージングした jar を実行する場合

    以下のように、コマンドを実行します。

    ```shell title="実行可能 jar の起動（ジョブ引数指定）"
    # gradlew :batch:bootJar で実行可能 jar を生成した想定
    java -jar batch/build/libs/batch-0.0.1-SNAPSHOT.jar output=sample-output.csv
    ```
