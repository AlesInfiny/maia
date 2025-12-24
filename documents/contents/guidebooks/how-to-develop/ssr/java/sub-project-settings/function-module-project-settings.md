---
title: Java 編 （SSR 編）
description: SSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# 機能モジュールのプロジェクトの設定 {#top}

機能モジュールのプロジェクトで必要な設定を解説します。

## 依存ライブラリの設定 {#config-dependencies}

機能モジュールのプロジェクトの依存ライブラリは、 application-core プロジェクトと infrastructure プロジェクトを合わせたものとなります。

CSR 編の [application-core プロジェクトの設定](../../../csr/java/sub-project-settings/application-core-project-settings.md#config-dependencies) および [infrastructure プロジェクトの設定](../../../csr/java/sub-project-settings/infrastructure-project-settings.md#config-dependencies) を参照してください。

## 依存プロジェクトの設定 {#config-projects}

機能モジュールのプロジェクトは system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。

```groovy title="a-function/build.gradle"
dependencies {
  implementation project(':system-common')
}
```

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

application-core プロジェクトは単体で動作せず、他プロジェクトからライブラリとして呼び出します。
呼び出し可能な Jar ファイルで出力する必要がないため以下を設定します。

```groovy title="application-core/build.gradle"
bootJar {
  enabled = false
}

jar {
  enabled = true
}
```

また、併せて不要なファイルを削除します。
application-core プロジェクトの `src` 以下にある、 `AaaApplication.java` と `AaaApplicationTest.java` を削除してください。

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```winbatch title="a-function プロジェクトのビルド"
./gradlew a-function:build
```

??? info "ここまでの手順を実行した際の `a-function/build.gradle` の例"

    ```groovy title="a-function/build.gradle"
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
      implementation project(':system-common')
      // その他、プロジェクトに必要な依存ライブラリは任意で追加してください。
    }

    configurations {
      all {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
      }
    }


    tasks.named('test') {
      useJUnitPlatform()
    }

    bootJar {
      enabled = false
    }

    jar {
      enabled = true
    }
    ```

## メッセージ管理の設定 {#message-management-settings}

機能モジュールのプロジェクトで管理する業務メッセージの設定方法については、[こちら](./message-management.md) を参照してください。
