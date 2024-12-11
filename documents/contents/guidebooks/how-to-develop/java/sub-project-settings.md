---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# サブプロジェクトの個別設定 {#top}
<!-- cspell:ignore springdoc datasource -->

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

- `spring-boot-starter-actuator`: ヘルスチェックを含めたアプリケーション監視・管理機能を構築するためのスターター

- `spring-boot-starter-test`：Spring Boot アプリケーションをテストするためのスターター

上記のライブラリを依存ライブラリとして、 以下のように `build.gradle` の `dependencies` ブロックに追加します。

```groovy title="build.gradle"
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-web'
  implementation 'com.h2database:h2:x.x.x'
  implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:x.x.x'
  implementation 'org.springframework.boot:spring-boot-starter-actuator'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### OpenAPI 仕様書の出力設定 {#open-api-specification-output-configuration}

`springdoc-openapi-ui`を依存関係に追加した場合、 OpenAPI 仕様書のファイルがビルド時に出力されるようプロジェクトファイルを設定します。
以下に、`build.gradle` への設定内容を例示します。

```groovy title="build.gradle"
// OpenAPI 仕様書出力の作業ディレクトリを指定する。
afterEvaluate {
  tasks.named("forkedSpringBootRun") {
  workingDir("$rootDir/api-docs")
  }
}
// OpenAPI 仕様書の出力先を指定する。
openApi {
  apiDocsUrl.set("http://localhost:8080/api-docs")
  outputDir.set(file("$rootDir/api-docs"))
  outputFileName.set("api-specification.json")
}
// ビルド時に OpenAPI 仕様書の出力を行うよう設定する。
build.dependsOn("generateOpenApiDocs")
```

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
設定できる項目については、以下を参照してください。

- [アプリケーションプロパティ設定一覧 :material-open-in-new:](https://spring.pleiades.io/spring-boot/docs/current/reference/html/application-properties.html){ target=_blank }
- [本番対応機能 :material-open-in-new:](https://spring.pleiades.io/spring-boot/docs/current/reference/html/actuator.html){ target=_blank }

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
    - mybatis.configuration.xxx で MyBatis の設定を記述可能
- ヘルスチェック機能を含む Spring Boot Actuator に関する設定
    - management.endpoints.web.base-path: エンドポイントパスのカスタマイズ
    - management.endpoint.health.group.xxx.include: さまざまなサーバーの監視目的に合わせたヘルスチェックのプローブを作成可能
- バッチ処理
    - spring.batch.jdbc.initialize-schema: Spring Batch のメタデータテーブルの初期化設定
    - spring.batch.job.name: バッチアプリケーション起動時の実行するバッチジョブ名の設定

!!! note "spring.batch.jdbc.initialize-schema の設定とメタデータテーブルの関係"

    Spring Batch においては、バッチ処理の実行履歴やトランザクション管理など、ジョブ管理を行うメタデータテーブルを利用します。
    `spring.batch.jdbc.initialize-schema=never` でメタデータテーブルの初期化を実施しない設定とした場合、メタデータテーブルは作成されません。
    しかし、バッチ処理実行時においてメタデータテーブルが存在しない場合、メタデータテーブルが存在しないエラーが発生しバッチ処理が正常に動作しません。
    そのため、バッチアプリケーションの起動時に [メタデータテーブルを作成するスキーマ :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/schema-appendix.html){ target=_blank } を実行するよう指定する必要があります。
    バッチ処理のジョブ管理をクラウドサービスや特定のジョブ管理ツールに任せる場合など、Spring Batch で生成されるメタデータテーブルを利用したくない際の対処法は [こちら](../../../app-architecture/batch-application/batch-application-consideration/without-using-meta-data-table.md#batch-job-management-by-third-party-tool) をご覧ください。

### CORS （クロスオリジンリソース共有）環境の設定 {#cors-environment}

Web API を公開するオリジンと、呼び出し元となるクライアントスクリプトを公開するオリジンが異なる場合（クロスオリジン）の設定は、[こちら](../cors/index.md) を参照してください。

## infrastructure プロジェクトの設定 {#config-infrastructure}

infrastructure プロジェクトで必要な設定を解説します。

### infrastructure プロジェクトの依存ライブラリの設定 {#config-infrastructure-dependencies}

infrastructure プロジェクトで必要になるライブラリは、主にデータアクセス処理の実装に必要なライブラリです。
データアクセス処理の実装に AlesInfiny Maia OSS Edition で推奨する MyBatis を利用する場合には、 `mybatis-spring-boot-starter` を利用することを推奨します。

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

## batch プロジェクトの設定 {#config-batch}

batch プロジェクトで必要な設定を解説します。

### batch プロジェクトの依存ライブラリの設定 {#config-batch-dependencies}

batch プロジェクトで必要になるライブラリは、バッチ処理の実装やバッチ処理のためのデータアクセスを実現するライブラリです。
データアクセス処理やロギング処理用のライブラリは、後述する依存プロジェクトの設定によって参照しているため、 batch プロジェクトの依存ライブラリとしては記載していません。

```groovy title="build.gradle"
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-batch'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
  testImplementation 'org.springframework.batch:spring-batch-test:x.x.x'
}
```

### batch プロジェクトの依存プロジェクトの設定 {#config-batch-projects}

batch プロジェクトは application-core 、 infrastructure 、 system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。

```groovy title="build.gradle"
dependencies {
  implementation project(':application-core')
  implementation project(':infrastructure')
  implementation project(':system-common')
}
```

## システム共通プロジェクトの設定 {#config-system-common}

システム共通プロジェクトの依存ライブラリについては、特に必須や推奨するライブラリはありません。
開発するシステム共通部品で必要なライブラリを適宜追加します。

また、システム共通プロジェクトは他のプロジェクトを参照する想定はないので、他のプロジェクトを依存関係に含める必要はありません。
