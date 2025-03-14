---
title: Java 編
description: サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

<!-- cSpell:ignore configfile taskdef -->

# MyBatis Generator の設定 {#top}

infrastructure プロジェクトにおいて、 MyBatis Generator を利用してエンティティや O/R マッパーを自動的に生成するための設定について解説します。

## 事前準備 {#preparation}

本手順を実行する前に [infrastructure プロジェクトの設定](./infrastructure-project-settings.md) を完了してください。

MyBatis Generator を実行する際は、データベースにテーブルが作成されている必要があります。
本設定では [H2 Console :material-open-in-new:](https://www.h2database.com/html/download.html){ target=_blank } を利用して H2 Database に以下の DDL と DML を実行し、テーブルを作成しています。

??? example "サンプルアプリケーションの schema.sql"

    ```sql title="schema.sql"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/infrastructure/src/main/resources/data.sql
    ```

??? example "サンプルアプリケーションの data.sql"

    ```sql title="data.sql"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/infrastructure/src/main/resources/schema.sql
    ```

!!! warning "MyBatis Generator 実行用のテーブル作成について"

    サンプルアプリケーションの実行中は、 schema.sql と data.sql が自動的に読み込まれた H2 Database がインメモリで起動します。
    そのため、サンプルアプリケーションの実行用のデータベース作成の必要はありませんが、アプリケーションの停止と共にテーブルは削除されます。

    よって、 MyBatis Generator でコードを自動生成するためのテーブルを別途用意しておく必要があることに注意してください。

## MyBatis Generator の設定ファイルの作成 {#generator-files-settings}

infrastructure プロジェクトの src/main/resource フォルダーに設定ファイルである mybatisGeneratorConfig.xml を追加します。
mybatisGeneratorConfig.xml に設定する各要素については、[こちら :material-open-in-new:](https://mybatis.org/generator/configreference/xmlconfig.html){ target=_blank } を参照してください。

サンプルアプリケーションにおける設定例は以下の通りです。

??? example "サンプルアプリケーションの mybatisGeneratorConfig.xml"

    ```xml title="mybatisGeneratorConfig.xml"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-backend/infrastructure/src/main/resources/mybatisGeneratorConfig.xml
    ```

    各タブの設定内容は以下の通りです。

    - `<jdbcConnection>`: 接続するデータベースに関する設定です。
    - `<javaModelGenerator>`: データベースのテーブルに対応する Java のエンティティクラスを生成するための設定です。
    - `<sqlMapGenerator>`: MyBatis の SQL マッピングファイルを生成するための設定です。
    - `<javaClientGenerator>`: MyBatis のマッパーインターフェースを生成するための設定です。
    - `<table>`: 生成対象となるテーブルに関する設定です。

    なお、`<jdbcConnection>` の `driverClass` や `connectionURL` は使用するデータベースに合わせて変更してください。
    また、`<javaModelGenerator>` や `<sqlMapGenerator>` 等の `targetPackage` や `targetProject` の設定はフォルダー構成に合わせて修正してください。

次に、 `<javaClientGenerator>` や `<sqlMapGenerator>` で生成される XML ファイルをリソースに指定する設定を infrastructure プロジェクトの build.gradle に記述します。

```groovy title="build.gradle"
sourceSets {
  main {
    // mybatis SQL map XML ファイルを java 以下でも検知する
    resources.srcDirs = ["src/main/java", "src/main/resources"]
  }
}
```

## 依存ライブラリの設定 {#config-dependencies}

infrastructure プロジェクトの build.gradle の configurations に MyBatis Generator 実行用の依存関係のカスタム構成を定義します。

```groovy title="build.gradle"
configurations {
  // MyBatis Generator の自動生成で利用するライブラリのための依存関係の構成を定義
  mybatisTasks
}
```

次に、 build.gradle の dependencies に以下のような MyBatis Generator を利用するための依存関係を追加します。

- `mybatis-generator-core`：MyBatis Generator のタスクを実行するためのライブラリ

- `h2`：コードの自動生成で利用する組み込みの H2 データベース

この際、依存関係は前述の configurations で定義したカスタム構成である mybatisTasks を利用します。

```groovy title="build.gradle"
dependencies {
  mybatisTasks "org.mybatis.generator:mybatis-generator-core:x.x.x"
  mybatisTasks "com.h2database:h2:x.x.x"
}
```

## 自動生成タスクの追加 {#adding-generation-tasks}

MyBatis Generator を実行するタスク runMyBatisGenerator を定義します。
configfile には、[前述](#generator-files-settings) の mybatisGeneratorConfig.xml のパスを設定します。

```groovy title="build.gradle"
tasks.register('runMyBatisGenerator') {
  doLast {
    // MyBatis Generator のタスクを定義します。
    ant.taskdef(
      name: 'mybatisGenerator',
      classname: 'org.mybatis.generator.ant.GeneratorAntTask',
      classpath: configurations.mybatisTasks.asPath
    )
    // MyBatis Generator による自動生成を実行します。
    ant.mybatisGenerator(
      overwrite: true,
      configfile: file('src/main/resources/mybatisGeneratorConfig.xml'),
      verbose: true
    )
  }
}
```

## 自動生成タスクの実行 {#execution-of-generation-tasks}

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```winbatch title="自動生成タスクの実行コマンド"
./gradlew infrastructure:runMyBatisGenerator
```

実行後、 mybatisGeneratorConfig.xml の `<javaModelGenerator>` や `<sqlMapGenerator>` 等で設定した配置場所にファイルが自動生成されていることを確認してください。

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

    sourceSets {
      main {
        // mybatis SQL map XML ファイルを java 以下でも検知する
        resources.srcDirs = ["src/main/java", "src/main/resources"]
      }
    }

    repositories {
      mavenCentral()
    }

    configurations {
      mybatisTasks
      all {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
      }
    }

    dependencies {
      implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'
      implementation 'com.h2database:h2:x.x.x'
      implementation project(':application-core')
      implementation project(':system-common')

      mybatisTasks "org.mybatis.generator:mybatis-generator-core:x.x.x"
      mybatisTasks "com.h2database:h2:x.x.x"
      // その他、プロジェクトに必要な依存ライブラリは任意で追加してください。
    }

    bootJar {
      enabled = false
    }

    jar {
      enabled = true
    }

    tasks.register('runMyBatisGenerator') {
      doLast {
        // MyBatis Generator のタスクを定義します。
        ant.taskdef(
          name: 'mybatisGenerator',
          classname: 'org.mybatis.generator.ant.GeneratorAntTask',
          classpath: configurations.mybatisTasks.asPath
        )
        // MyBatis Generator による自動生成を実行します。
        ant.mybatisGenerator(
          overwrite: true,
          configfile: file('src/main/resources/mybatisGeneratorConfig.xml'),
          verbose: true
        )
      }
    }
    ```
