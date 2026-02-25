---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

## system-common プロジェクトの設定 {#top}

system-common プロジェクトの依存ライブラリについては、 Spring Boot アプリケーションを実装する上で必要なライブラリを除き、必須または推奨するライブラリはありません。
開発するシステム共通部品で必要なライブラリを適宜追加します。
system-common プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-starter`： Spring Boot アプリケーションを構築するための依存関係を提供するスターター
- `spring-boot-starter-test`：Spring Boot アプリケーションをテストするためのスターター

```groovy title="system-common/build.gradle"

dependencies {
  implementation 'org.springframework.boot:spring-boot-starter'

  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

また、 system-common プロジェクトは他のプロジェクトを参照する想定はないので、他のプロジェクトを依存関係に含める必要はありません。

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

<!-- textlint-disable ja-technical-writing/sentence-length -->

依存関係に記載している `org.springframework.boot:spring-boot-starter` ライブラリは、デフォルトで Logback 用のライブラリである `org.springframework.boot:spring-boot-starter-logging` が推移的依存で追加されます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

AlesInfiny Maia OSS Edition では、ロギングライブラリとして Apache Log4j 2 （以降 log4j2 ）を使用します。
そのため、以下のようにデフォルトのロギングライブラリを依存関係から除外する設定を記述します。

```groovy title="spring-boot-starter-logging の除外設定"
configurations {
 all {
  exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
 }
}
```

## プロジェクトのビルド方法の設定 {#config-build}

system-common プロジェクトは単体で動作せず、他プロジェクトからライブラリとして呼び出します。
呼び出し可能な Jar ファイルで出力する必要がないため以下を設定します。

```groovy title="system-common/build.gradle"
bootJar {
  enabled = false
}

jar {
  enabled = true
}
```

また、併せて不要なファイルを削除します。
system-common プロジェクトの `src` 以下にある、 `SystemCommonApplication.java` と `SystemCommonApplicationTest.java` を削除してください。

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
      implementation 'org.springframework.boot:spring-boot-starter'
      
      testImplementation 'org.springframework.boot:spring-boot-starter-test'
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
