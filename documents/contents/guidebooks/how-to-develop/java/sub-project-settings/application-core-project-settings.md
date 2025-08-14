---
title: Java 編
description: サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# application-core プロジェクトの設定 {#top}

application-core プロジェクトで必要な設定を解説します。

## 依存ライブラリの設定 {#config-dependencies}

外部ライブラリの脆弱性などの影響を受けて、アプリケーションコア層が変更されるような事態を避けるため、 application-core プロジェクトはできる限り外部のライブラリに依存しないよう構成しておくことを推奨します。

## 依存プロジェクトの設定 {#config-projects}

application-core プロジェクトは system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。

```groovy title="application-core/build.gradle"
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
application-core プロジェクトの `src` 以下にある、 `ApplicationCoreApplication.java` と `ApplicationCoreApplicationTest.java` を削除してください。

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```winbatch title="application-core プロジェクトのビルド"
./gradlew application-core:build
```

??? info "ここまでの手順を実行した際の `application-core/build.gradle` の例"

    ```groovy title="application-core/build.gradle"
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

application-core プロジェクトで管理する業務メッセージの設定方法については、[こちら](./message-management.md) を参照してください。
