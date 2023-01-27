---
title: Java 編
description: バックエンドで動作する Java アプリケーションの開発手順を解説します。
---

# サブプロジェクトの個別設定 {#top}
<!-- cSpell:ignore springdoc datasource -->

サブプロジェクト単位で個別に設定が必要な内容について解説します。

ここで解説する設定はあくまで推奨設定であり、検討した上で変更することは問題ありません。
また、依存ライブラリについて、プロジェクトで必要なライブラリを適宜追加することも問題ありません。

## web プロジェクトの設定 {#config-web}

web プロジェクトで必要な設定を解説します。

### web プロジェクトの依存ライブラリの設定 {#config-web-dependencies}

web プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-starter-web`：Spring MVC を使用して Web アプリケーションを構築するためのスターター

- `h2`：テストやローカル実行で利用する組み込みの H2 データベース

- `springdoc-openapi-ui`：OpenAPI 形式の API ドキュメントを生成するためのライブラリ

- `spring-boot-starter-test`：Spring Boot アプリケーションをテストするためのスターター

上記のライブラリを依存ライブラリとして、 `build.gradle` の `dependencies` ブロックに追加します。

### web プロジェクトの依存プロジェクトの設定 {#config-web-projects}

web プロジェクトは application-core 、 infrastructure 、 system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。
  
```groovy title="build.gradle"
dependencies {
  implementation project(':application-core')
  implementation project(':infrastructure')
  implementation project(':system-common')
}
```

### spring の設定 {#config-web-spring}

Spring Boot に関する主な設定は、 web プロジェクトの `src/main/resource` 以下に `application.properties` もしくは `application.yaml` ファイルを作成して行います。
設定できる項目は、[アプリケーションプロパティ設定一覧](https://spring.pleiades.io/spring-boot/docs/current/reference/html/application-properties.html)を参照してください。
設定項目は多岐に渡るため、一般的に設定する項目について例示します。

- データソース
    - spring.datasource.drive-class-name： JDBC ドライバーの完全修飾名
    - spring.datasource.url：データベースの JDBC URL
    - spring.datasource.username：データベースのログインユーザー名
    - spring.datasource.password：データベースのログインパスワード
- データベース初期化設定
    - spring.sql.init.mode：データベースの初期化有無
- ロギング
    - logging.xxx でロギングの各種設定が可能
- MyBatis の設定
    - mybatis.configuration.XXX で MyBatis の設定を記述可能

## infrastructure プロジェクトの設定 {#config-infrastructure}

infrastructure プロジェクトで必要な設定を解説します。

### infrastructure プロジェクトの依存ライブラリの設定 {#config-infrastructure-dependencies}

infrastructure プロジェクトで必要になるライブラリは、主にデータアクセス処理の実装に必要なライブラリです。
データアクセス処理の実装に AlesInfiny Maia で推奨する MyBatis を利用する場合には、 `mybatis-spring-boot-starter` を利用することを推奨します。

```groovy title="build.gradle"
dependencies {
  implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'
}
```

データアクセス処理の実装に MyBatis 以外を利用する場合、適切なライブラリに切り替えてください。

### infrastructure プロジェクトの依存プロジェクトの設定 {#config-infrastructure-projects}

infrastructure プロジェクトは application-core 、 system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。
  
```groovy title="build.gradle"
dependencies {
  implementation project(':application-core')
  implementation project(':system-common')
}
```

## application-core プロジェクトの設定 {#config-application-core}

application-core プロジェクトで必要な設定を解説します。

### application-core プロジェクトの依存ライブラリの設定 {#config-application-core-dependencies}

外部ライブラリの脆弱性などの影響を受けて、アプリケーションコア層が変更されるような事態を避けるため、 application-core プロジェクトはできる限り外部のライブラリに依存しないよう構成しておくことを推奨します。

### application-core プロジェクトの依存プロジェクトの設定 {#config-application-core-projects}

application-core プロジェクトは system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。
  
```groovy title="build.gradle"
dependencies {
  implementation project(':system-common')
}
```

## システム共通プロジェクトの設定 {#config-system-common}

システム共通プロジェクトの依存ライブラリについては、特に必須や推奨するライブラリはありません。
開発するシステム共通部品で必要なライブラリを適宜追加します。

また、システム共通プロジェクトは他のプロジェクトを参照する想定はないので、他のプロジェクトを依存関係に含める必要はありません。
