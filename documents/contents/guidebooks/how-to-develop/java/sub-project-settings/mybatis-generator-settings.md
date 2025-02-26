---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

<!-- cSpell:ignore configfile taskdef -->

# MyBatis Generator の設定 {#top}

infrastructure プロジェクトにおいて、 MyBatis Generator による Java クラスやマッパーを自動的に生成するための設定について解説します。

## 事前準備 {#preparation}

データベースにテーブルが作成されている必要があります。
サンプルアプリケーションでは [H2 Console :material-open-in-new:](https://www.h2database.com/html/download.html){ target=_blank } を利用して H2 database に以下の schema.sql (DDL) と data.sql (DML) を実行し、テーブルを作成しています。

??? info "サンプルアプリケーションの schema.sql"

    ```sql title="schema.sql"
    --8<-- "https://raw.githubusercontent.com/AlesInfiny/maia/refs/heads/main/samples/web-csr/dressca-backend/infrastructure/src/main/resources/schema.sql"
    ```

??? info "サンプルアプリケーションの data.sql"

    ```sql title="data.slq"
    --8<-- "https://raw.githubusercontent.com/AlesInfiny/maia/refs/heads/main/samples/web-csr/dressca-backend/infrastructure/src/main/resources/data.sql"
    ```

## MyBatis Generator の設定ファイルの作成 {#generator-files-settings}

infrastructure プロジェクトの src/main/resource フォルダーに設定ファイルである mybatisGeneratorConfig.xml を追加します。
mybatisGeneratorConfig.xml に設定する各要素については、[こちら :material-open-in-new:](https://mybatis.org/generator/configreference/xmlconfig.html){ target=_blank } を参照してください。

サンプルアプリケーションにおける設定例は以下の通りです。
なお、`<jdbcConnection>` の driverClass や connectionURL は使用するデータベースに合わせて変更してください。
また、`<javaModelGenerator>` や `<sqlMapGenerator>` 等の targetPackage や targetProject はフォルダー構成に合わせて修正してください。

??? info "サンプルアプリケーションの mybatisGeneratorConfig.xml"

    ```xml title="{ルートプロジェクト}/build.gradle"
    --8<-- "https://raw.githubusercontent.com/AlesInfiny/maia/refs/heads/main/samples/web-csr/dressca-backend/infrastructure/src/main/resources/mybatisGeneratorConfig.xml"
    ```

## 依存ライブラリの設定 {#config-dependencies}

infrastructure プロジェクトの build.gradle の configurations に MyBatis Generator 実行用の依存関係を定義します。

```groovy title="build.gradle"
configurations {
  // MyBatis Generator の自動生成で利用するライブラリのための依存関係の構成を定義
  mybatisTasks
}
```

次に、 build.gradle の dependencies に MyBatis Generator Core と利用するデータベースのドライバーの依存関係を追加します。
この際、依存関係の名称は前述の configurations で定義した mybatisTasks を利用します。

```groovy title="build.gradle"
dependencies {
  mybatisTasks supportDependencies.mybatis_generator_core
  // h2 データベースを利用する際のドライバー
  mybatisTasks supportDependencies.h2database
}
```

他のプロジェクトと同様に、ライブラリのバージョン管理は root プロジェクトの settings.gradle で行います。

```groovy title="settings.gradle"
ext {
  h2Version = "x.x.x"
  mybatisGeneratorVersion = "x.x.x"

  supportDependencies = [
    h2database : "com.h2database:h2:$h2Version",
    mybatis_generator_core: "org.mybatis.generator:mybatis-generator-core:$mybatisGeneratorVersion",
    ...
  ]
}
```

## 自動生成タスクの追加 {#adding-generate-tasks}

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

## 自動生成タスクの実行

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```winbatch title="自動生成タスクの実行コマンド"
./gradlew infrastructure:runMyBatisGenerator
```

実行後、mybatisGeneratorConfig.xml の `<javaModelGenerator>` や `<sqlMapGenerator>` 等で設定した配置場所にファイルが自動生成されていることを確認してください。
