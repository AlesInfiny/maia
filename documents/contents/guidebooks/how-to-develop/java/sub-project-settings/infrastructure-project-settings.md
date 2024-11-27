---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# infrastructure プロジェクトの設定 {#top}

infrastructure プロジェクトで必要な設定を解説します。

## 依存ライブラリの設定 {#config-dependencies}

infrastructure プロジェクトで必要になるライブラリは、主にデータアクセス処理の実装に必要なライブラリです。
データアクセス処理の実装に AlesInfiny Maia OSS Edition で推奨する MyBatis を利用する場合には、 `mybatis-spring-boot-starter` を利用することを推奨します。
infrastructure プロジェクトで利用を推奨するライブラリは以下の通りです。

- `mybatis-spring-boot-starter`： MyBatis と Spring Boot を統合するためのスターター

- `h2`：テストやローカル実行で利用する組み込みの H2 データベース

```groovy title="infrastructure/build.gradle"
dependencies {
  implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'
  implementation 'com.h2database:h2:x.x.x'
}
```

データアクセス処理の実装に MyBatis 以外を利用する場合、適切なライブラリに切り替えてください。

??? info "各依存ライブラリのバージョンの参照先"

    - [MyBatis Spring Boot Starter :material-open-in-new:](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter){ target=_blank }
    - [H2 Database Engine :material-open-in-new:](https://mvnrepository.com/artifact/com.h2database/h2){ target=_blank }

## 依存プロジェクトの設定 {#config-projects}

infrastructure プロジェクトは application-core 、 system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。
  
```groovy title="infrastructure/build.gradle"
dependencies {
  implementation project(':application-core')
  implementation project(':system-common')
}
```

## プロジェクトのビルド方法の設定 {#config-build}

infrastructure プロジェクトは単体で動作せず、他プロジェクトからライブラリとして呼び出します。
呼び出し可能な Jar ファイルで出力する必要がないため以下を設定します。

```groovy title="infrastructure/build.gradle"
bootJar {
  enabled = false
}

jar {
  enabled = true
}
```

また、併せて不要なファイルを削除します。
infrastructure プロジェクトの `src` 以下にある、 `InfrastructureApplication.java` と `InfrastructureApplicationTest.java` を削除してください。

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```winbatch title="infrastructure プロジェクトのビルド"
./gradlew infrastructure:build
```

??? info "ここまでの手順を実行した際の `infrastructure/build.gradle` の例"

    ```groovy title="infrastructure/build.gradle"
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
      implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'
      implementation 'com.h2database:h2:x.x.x'
      implementation project(':application-core')
      implementation project(':system-common')
      // その他、プロジェクトに必要な依存ライブラリは任意で追加してください。
    }

    bootJar {
      enabled = false
    }

    jar {
      enabled = true
    }
    ```
