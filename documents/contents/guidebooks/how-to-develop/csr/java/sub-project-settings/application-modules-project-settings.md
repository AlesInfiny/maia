---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# application-modules プロジェクトの設定 {#top}

application-modules プロジェクトで必要な設定を解説します。

application-modules プロジェクトには、コンテキストごとの業務モジュールを配置します。
業務モジュールは業務ロジックとデータアクセス処理の双方を含むため、両方の実装に必要なライブラリを設定します。
なお、システム共通機能は system-common プロジェクトに配置し、 application-modules プロジェクトには含めません。

## 依存ライブラリの設定 {#config-dependencies}

機能モジュールのプロジェクトで必要になるライブラリは、データアクセス処理やモジュラーモノリスアーキテクチャの実装に必要なライブラリです。
データアクセス処理の実装に AlesInfiny Maia OSS Edition で推奨する MyBatis を利用する場合には、 `mybatis-spring-boot-starter` を利用することを推奨します。
application-modules プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-transaction`: Spring Boot アプリケーションでトランザクションを管理するスターター
- `spring-modulith-bom`: Spring Modulith の依存関係を管理するための BOM
- `spring-modulith-starter-core`: モジュラーモノリスアーキテクチャを実装するためのスターター
- `spring-modulith-starter-test`: モジュラーモノリスアーキテクチャのテストを実装するためのスターター
- `mybatis-spring-boot-starter`： MyBatis と Spring Boot を統合するためのスターター
- `mybatis-spring-boot-starter-test`： MyBatis と Spring Boot を統合したアプリケーションをテストするためのスターター

```groovy title="application-modules/build.gradle"
dependencies {
  implementation platform("org.springframework.modulith:spring-modulith-bom:x.x.x")
  implementation 'org.springframework.boot:spring-boot-transaction'
  implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'

  compileOnly 'org.springframework.modulith:spring-modulith-starter-core'

  testImplementation 'org.springframework.modulith:spring-modulith-starter-test'
  testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:x.x.x'
}
```

データアクセス処理の実装に MyBatis 以外を利用する場合、適切なライブラリに切り替えてください。

??? info "各依存ライブラリのバージョンの参照先"

    - [Spring Modulith BOM :material-open-in-new:](https://mvnrepository.com/artifact/org.springframework.modulith/spring-modulith-bom){ target=_blank }
    - [MyBatis Spring Boot Starter :material-open-in-new:](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter){ target=_blank }
    - [MyBatis Spring Boot Starter Test :material-open-in-new:](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter-test){ target=_blank }

## 依存プロジェクトの設定 {#config-projects}

application-modules プロジェクトは system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。

```groovy title="application-modules/build.gradle"
dependencies {
  implementation project(':system-common')
}
```

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

<!-- textlint-disable ja-technical-writing/sentence-length -->

依存関係に記載している `org.mybatis.spring.boot:mybatis-spring-boot-starter` ライブラリは、デフォルトで Logback 用のライブラリである `org.springframework.boot:spring-boot-starter-logging` が推移的依存で追加されます。

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

application-modules プロジェクトは単体で動作せず、他プロジェクトからライブラリとして呼び出します。
呼び出し可能な Jar ファイルで出力する必要がないため以下を設定します。

```groovy title="application-modules/build.gradle"
bootJar {
  enabled = false
}

jar {
  enabled = true
}
```

## 不要な設定やファイルの削除 {#remove-unnecessary-settings-and-files}

[こちら](../common-project-settings.md#java-plugin) で、使用するテストフレームワークを集約管理しているため、 test タスクに関するブロックを削除します。

```groovy title="application-modules/build.gradle" hl_lines="1-3"
tasks.named('test') {
  useJUnitPlatform()
}
```

また、併せて不要なファイルを削除します。
application-modules プロジェクトの `src` 以下にある、以下のファイルを削除してください。

- `ApplicationModulesApplication.java`
- `ApplicationModulesApplicationTest.java`

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```shell title="application-modules プロジェクトのビルド"
./gradlew application-modules:build
```

??? info "ここまでの手順を実行した際の `application-modules/build.gradle` の例"

    ```groovy title="application-modules/build.gradle"
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
      implementation platform("org.springframework.modulith:spring-modulith-bom:x.x.x")
      implementation 'org.springframework.boot:spring-boot-transaction'
      implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'

      compileOnly 'org.springframework.modulith:spring-modulith-starter-core'

      testImplementation 'org.springframework.modulith:spring-modulith-starter-test'
      testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:x.x.x'
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

## MyBatis Generator によるコードの自動生成 {#code-generation-with-mybatis-generator}

<!-- textlint-disable ja-technical-writing/sentence-length -->

MyBatis Generator は、 MyBatis を使用する際に、データベースのテーブルからテーブルエンティティやマッパーインターフェース、 SQL マッピングファイルを自動的に生成するツールです。

<!-- textlint-enable ja-technical-writing/sentence-length -->

自動生成したクラスは、コンテキストの `internal/infrastructure/repository/mybatis/generated` パッケージへ出力します。
具体的な設定方法については、[こちら](./mybatis-generator-settings.md) を参照してください。

## メッセージ管理の設定 {#message-management-settings}

application-modules プロジェクトで管理する業務メッセージの設定方法については、[こちら](./message-management.md) を参照してください。
