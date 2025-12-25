---
title: Java 編 （SSR 編）
description: SSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

## system-common プロジェクトの設定 {#top}

system-common プロジェクトの依存ライブラリについては、特に必須や推奨するライブラリはありません。
開発するシステム共通部品で必要なライブラリを適宜追加します。

また、 system-common プロジェクトは他のプロジェクトを参照する想定はないので、他のプロジェクトを依存関係に含める必要はありません。

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

ロギングライブラリの除外設定は CSR 編と同様です。

[こちら](../../../csr/java/sub-project-settings/system-common-project-settings.md#logging-library-exclusion-settings) を参照して、ロギングライブラリの除外設定を追記してください。

## プロジェクトのビルド方法の設定 {#config-build}

プロジェクトのビルド方法の設定は CSR 編と同様です。

[こちら](../../../csr/java/sub-project-settings/system-common-project-settings.md#config-build) を参照して、ビルド方法の設定を追記してください。

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```shell title="system-common プロジェクトのビルド"
./gradlew system-common:build
```

??? info "ここまでの手順を実行した際の `system-common/build.gradle` の例"

    ```groovy title="system-common/build.gradle"
    plugins {
      id 'java'
      id 'org.springframework.boot' version 'x.x.x'
      id 'io.spring.dependency-management' version 'x.x.x'
    }

    group = 'プロジェクトのグループ名'
    version = 'x.x.x-SNAPSHOT'
    description = 'プロジェクトの説明'

    java {
      toolchain {
        languageVersion = JavaLanguageVersion.of(x)
      }
    }

    repositories {
      mavenCentral()
    }

    dependencies {
      // その他、プロジェクトに必要な依存ライブラリは任意で追加してください。
    }

    configurations {
      all {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
      }
    }


    bootJar {
      enabled = false
    }

    jar {
      enabled = true
    }
    ```

## メッセージ管理の設定 {#message-management-settings}

system-common プロジェクトで管理する共通メッセージの設定方法については、[こちら](./message-management.md) を参照してください。
