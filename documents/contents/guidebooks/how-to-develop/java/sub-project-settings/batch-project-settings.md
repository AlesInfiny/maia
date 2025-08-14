---
title: Java 編
description: サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# batch プロジェクトの設定 {#top}
<!-- cSpell:ignore datasource hikari tasklet -->

batch プロジェクトで必要な設定を解説します。

## batch プロジェクトの依存ライブラリの設定 {#config-dependencies}

batch プロジェクトで必要になるライブラリは、バッチ処理の実装やバッチ処理のためのデータアクセスを実現するライブラリです。
データアクセス処理やロギング処理用のライブラリは、後述する依存プロジェクトの設定によって参照しているため、 batch プロジェクトの依存ライブラリとしては記載していません。
batch プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-starter-batch`： Spring Batch アプリケーションを構築するための依存関係を提供するスターター

- `spring-boot-starter-log4j2`: Spring Boot アプリケーションで Apache Log4j 2 （以降 log4j2 ）を使用するためのスターター

- `spring-batch-test`： Spring Batch アプリケーションのテストのライブラリ

- `spring-boot-starter-test`：Spring Boot アプリケーションをテストするためのスターター

```groovy title="batch/build.gradle"
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-batch'
  implementation 'org.springframework.boot:spring-boot-starter-log4j2'
  testImplementation 'org.springframework.batch:spring-batch-test:x.x.x'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

??? info "各依存ライブラリのバージョンの参照先"

    - [Spring Batch Test :material-open-in-new:](https://mvnrepository.com/artifact/org.springframework.batch/spring-batch-test){ target=_blank }

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

## Spring Boot の設定 {#config-spring}

batch プロジェクトに関する Spring Boot のプロパティ等を設定します。
batch プロジェクトの `src/main/resource` 以下に `application.properties` もしくは `application.yaml` ファイルを作成して行います。
設定できる項目については、以下を参照してください。

- [Spring Boot のアプリケーションプロパティ設定一覧 :material-open-in-new:](https://spring.pleiades.io/spring-boot/docs/current/reference/html/application-properties.html){ target=_blank }
- [本番対応機能 :material-open-in-new:](https://spring.pleiades.io/spring-boot/docs/current/reference/html/actuator.html){ target=_blank }
- [myBatis-spring-boot-starter のアプリケーションプロパティ設定一覧 :material-open-in-new:](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/#configuration){ target=_blank }

設定項目は多岐に渡るため、一般的に設定する項目について例示します。

- データソース
    - spring.datasource.hikari.driver-class-name： JDBC ドライバーの完全修飾名
    - spring.datasource.hikari.url：データベースの JDBC URL
    - spring.datasource.hikari.username：データベースのログインユーザー名
    - spring.datasource.hikari.password：データベースのログインパスワード
- データベース初期化設定
    - spring.sql.init.mode：データベースの初期化有無
- MyBatis の設定
    - mybatis.configuration.xxx で MyBatis の設定を記述可能
- バッチ処理
    - spring.batch.jdbc.initialize-schema: Spring Batch のメタデータテーブルの初期化設定
    - spring.batch.job.name: バッチアプリケーション起動時の実行するバッチジョブ名の設定

??? info "`application.properties` の設定例"

    ```properties title="開発環境での設定例（ H2 Database を使用する場合）"
    spring.datasource.hikari.driver-class-name=org.h2.Driver
    spring.datasource.hikari.url=jdbc:h2:mem:データベースの名前
    spring.datasource.hikari.username=データベースのログインユーザー名
    spring.datasource.hikari.password=データベースのログインパスワード
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    spring.h2.console.settings.web-allow-others=true
    spring.sql.init.mode=embedded
    mybatis.configuration.map-underscore-to-camel-case=true
    ```

    ```properties title="本番環境での設定例（ PostgreSQL を使用する場合）"
    spring.datasource.hikari.driver-class-name=org.postgresql.Driver
    spring.datasource.hikari.url=jdbc:postgresql://localhost:5432/データベースの名前
    spring.datasource.hikari.username=データベースのログインユーザー名
    spring.datasource.hikari.password=データベースのログインパスワード
    spring.sql.init.mode=never
    mybatis.configuration.map-underscore-to-camel-case=true
    ```

!!! note "spring.batch.jdbc.initialize-schema の設定とメタデータテーブルの関係"

    Spring Batch においては、バッチ処理の実行履歴やトランザクション管理など、ジョブ管理を行うメタデータテーブルを利用します。
    `spring.batch.jdbc.initialize-schema=never` でメタデータテーブルの初期化を実施しない設定とした場合、メタデータテーブルは作成されません。
    しかし、バッチ処理実行時においてメタデータテーブルが存在しない場合、メタデータテーブルが存在しないエラーが発生しバッチ処理が正常に動作しません。
    そのため、バッチアプリケーションの起動時に [メタデータテーブルを作成するスキーマ :material-open-in-new:](https://spring.pleiades.io/spring-batch/reference/schema-appendix.html){ target=_blank } を実行するよう指定する必要があります。
    バッチ処理のジョブ管理をクラウドサービスや特定のジョブ管理ツールに任せる場合など、Spring Batch で生成されるメタデータテーブルを利用したくない際の対処法は [こちら](../../../../app-architecture/batch-application/batch-application-consideration/without-using-meta-data-table.md) をご覧ください。

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

<!-- textlint-disable ja-technical-writing/sentence-length -->

依存関係に記載している `org.springframework.boot:spring-boot-starter-batch` ライブラリは、デフォルトで Logback 用のライブラリである `org.springframework.boot:spring-boot-starter-logging` が推移的依存で追加されます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

AlesInfiny Maia OSS Edition では、ロギングライブラリとして log4j2 を使用します。
そのため、以下のようにデフォルトのロギングライブラリを依存関係から除外する設定を記述します。

```groovy title="spring-boot-starter-logging の除外設定"
configurations {
 all {
  exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
 }
}
```

## バッチアプリケーションとして動作させる設定 {#config-batch-application}

batch プロジェクトをウェブアプリケーションではなく、バッチアプリケーションとして動作させるためクラスファイルを書き換えます。
なお、バッチアプリケーションの具体的な実装については解説しません。

batch プロジェクトの `src/main/java` 以下の `BatchApplication.java` の main メソッドを書き換えます。

```java title="BatchApplication.java"
public static void main(String[] args) {
  SpringApplication app = new SpringApplication(BatchApplication.class);
  // batch プロジェクトをウェブアプリケーションでは立ち上げない設定
  app.setWebApplicationType(WebApplicationType.NONE);
  System.exit(SpringApplication.exit(app.run(args)));
}
```

また併せて、 batch プロジェクトの `src/main/test` 以下の `BatchApplicationTest.java` を書き換えます。

```java title="BatchApplicationTest.java"
・・・
import org.springframework.batch.test.context.SpringBatchTest; // 追加

@SpringBootTest
@SpringBatchTest // 追加
class BatchApplicationTests {
・・・
}
```

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```shell title="batch プロジェクトのビルド"
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
      implementation 'org.springframework.boot:spring-boot-starter-log4j2'
      testImplementation 'org.springframework.batch:spring-batch-test:x.x.x'
      testImplementation 'org.springframework.boot:spring-boot-starter-test'
      implementation project(':application-core')
      implementation project(':infrastructure')
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

    ```
