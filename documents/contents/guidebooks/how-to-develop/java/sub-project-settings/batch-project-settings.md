---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# batch プロジェクトの設定 {#top}

batch プロジェクトで必要な設定を解説します。

## batch プロジェクトの依存ライブラリの設定 {#config-dependencies}

batch プロジェクトで必要になるライブラリは、バッチ処理の実装やバッチ処理のためのデータアクセスを実現するライブラリです。
データアクセス処理やロギング処理用のライブラリは、後述する依存プロジェクトの設定によって参照しているため、 batch プロジェクトの依存ライブラリとしては記載していません。
batch プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-starter-batch`： Spring Batch アプリケーションを構築するための依存関係を提供するスターター

- `spring-batch-test`： Spring Batch アプリケーションのテストのライブラリ

- `spring-boot-starter-test`：Spring Boot アプリケーションをテストするためのスターター

```groovy title="batch/build.gradle"
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-batch'
  testImplementation 'org.springframework.batch:spring-batch-test:x.x.x'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## batch プロジェクトの依存プロジェクトの設定 {#config-projects}

batch プロジェクトは application-core 、 infrastructure 、 system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。

```groovy title="batch/build.gradle"
dependencies {
  implementation project(':application-core')
  implementation project(':infrastructure')
  implementation project(':system-common')
}
```

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルで以下を実行してください。

```winbatch title="batch プロジェクトのビルド"
./gradlew batch:build
```

??? info "ここまでの手順を実行した際の `batch/build.gradle` の例"

    ```groovy title="batch/build.gradle"
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
      implementation 'org.springframework.boot:spring-boot-starter-batch'
      testImplementation 'org.springframework.batch:spring-batch-test:x.x.x'
      testImplementation 'org.springframework.boot:spring-boot-starter-test'
      implementation project(':application-core')
      implementation project(':infrastructure')
      implementation project(':system-common')
      // その他、プロジェクトに必要な依存ライブラリは任意で追加してください。
    }

    tasks.named('test') {
      useJUnitPlatform()
    }

    ```
