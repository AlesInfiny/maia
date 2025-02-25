---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# MyBatis Generator の設定 {#top}

infrastructure プロジェクトにおいて、 MyBatis Generator による Java クラスやマッパーを自動的に生成するための設定について解説します。

## 依存ライブラリの設定 {#config-dependencies}

infrastructure プロジェクトの build.gradle の configurations に MyBatis Generator 実行用の依存関係を定義します。

```groovy title="build.gradle"
configurations {
  // MyBatis Generator の自動生成で利用するライブラリのための依存関係の構成を定義
  mybatisTasks
}
```

次に、 build.gradle の dependencies に MyBatis Generator Core と利用するデータベースのドライバーの依存関係を追加します。
この際、依存関係は前述の configurations で定義したものを利用します。

```groovy title="build.gradle"
dependencies {
  mybatisTasks supportDependencies.mybatis_generator_core
  // h2 データベースを利用する際のドライバー
  mybatisTasks supportDependencies.h2database
}
```

他のプロジェクトと同様に、ライブラリのバージョン管理は root プロジェクトの settings.gradle で管理します。

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

## MyBatis Generator の設定ファイルの作成 {#generator-files-settings}

## 自動生成タスクの追加 {#adding-generate-tasks}
