---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

## system-common プロジェクトの設定 {#top}

system-common プロジェクトの依存ライブラリについては、特に必須や推奨するライブラリはありません。
開発するシステム共通部品で必要なライブラリを適宜追加します。

また、 system-common プロジェクトは他のプロジェクトを参照する想定はないので、他のプロジェクトを依存関係に含める必要はありません。

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

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```winbatch title="system-common プロジェクトのビルド"
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

    bootJar {
      enabled = false
    }

    jar {
      enabled = true
    }
    ```
