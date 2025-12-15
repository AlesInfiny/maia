---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# web プロジェクトの設定 {#top}
<!-- cSpell:ignore datasource testdb hikari -->

web プロジェクトで必要な設定を解説します。

## 依存ライブラリの設定 {#config-dependencies}

web プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-starter-web`：Spring MVC を使用して Web アプリケーションを構築するためのスターター

- `h2`：テストやローカル実行で利用する組み込みの H2 データベース

- `springdoc-openapi-starter-webmvc-ui`：Spring Web MVC アプリケーション向けの、 OpenAPI 形式の API ドキュメントを生成するためのライブラリ

- `spring-boot-starter-actuator`: ヘルスチェックを含めたアプリケーション監視・管理機能を構築するためのスターター

- `spring-boot-starter-log4j2`: Spring Boot アプリケーションで Apache Log4j 2 （以降 log4j2 ）を使用するためのスターター

- `spring-boot-starter-test`：Spring Boot アプリケーションをテストするためのスターター

上記のライブラリを依存ライブラリとして、 以下のように `build.gradle` の `dependencies` ブロックに追加します。

```groovy title="web/build.gradle"
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-web'
  implementation 'com.h2database:h2:x.x.x'
  implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:x.x.x'
  implementation 'org.springframework.boot:spring-boot-starter-actuator'
  implementation 'org.springframework.boot:spring-boot-starter-log4j2'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

??? info "各依存ライブラリのバージョンの参照先"

    - [H2 Database Engine :material-open-in-new:](https://mvnrepository.com/artifact/com.h2database/h2){ target=_blank }
    - [SpringDoc OpenAPI Starter WebMVC UI :material-open-in-new:](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui){ target=_blank }

## 依存プロジェクトの設定 {#config-projects}

web プロジェクトは application-core 、 infrastructure 、 system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。
  
```groovy title="web/build.gradle"
dependencies {
  implementation project(':application-core')
  implementation project(':infrastructure')
  implementation project(':system-common')
}
```

## Spring Boot の設定 {#config-spring}

web プロジェクトに関する Spring Boot のプロパティ等を設定します。
web プロジェクトの `src/main/resource` 以下に `application.properties` もしくは `application.yaml` ファイルを作成して行います。
設定できる項目については、以下を参照してください。

- [Spring Boot のアプリケーションプロパティ設定一覧 :material-open-in-new:](https://spring.pleiades.io/spring-boot/appendix/application-properties/){ target=_blank }
- [本番対応機能 :material-open-in-new:](https://spring.pleiades.io/spring-boot/reference/actuator/){ target=_blank }
- [myBatis-spring-boot-starter のアプリケーションプロパティ設定一覧 :material-open-in-new:](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/#configuration){ target=_blank }

設定項目は多岐に渡るため、一般的に設定する項目について例示します。

- データソース
    - spring.datasource.hikari.driver-class-name： JDBC ドライバーの完全修飾名
    - spring.datasource.hikari.url：データベースの JDBC URL
    - spring.datasource.hikari.username：データベースのログインユーザー名
    - spring.datasource.hikari.password：データベースのログインパスワード
- データベース初期化設定
    - spring.sql.init.mode：データベースの初期化有無
- ロギング
    - logging.xxx でロギングの各種設定が可能
- MyBatis の設定
    - mybatis.configuration.xxx で MyBatis の設定を記述可能
- ヘルスチェック機能を含む Spring Boot Actuator に関する設定
    - management.endpoints.web.base-path: エンドポイントパスのカスタマイズ
    - management.endpoint.health.group.xxx.include: さまざまなサーバーの監視目的に合わせたヘルスチェックのプローブを作成可能

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
    logging.level.web=DEBUG
    ```
    
    ```properties title="本番環境での設定例（ PostgreSQL を使用する場合）"
    spring.datasource.hikari.driver-class-name=org.postgresql.Driver
    spring.datasource.hikari.url=jdbc:postgresql://localhost:5432/データベースの名前
    spring.datasource.hikari.username=データベースのログインユーザー名
    spring.datasource.hikari.password=データベースのログインパスワード
    spring.sql.init.mode=never
    ```

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

<!-- textlint-disable ja-technical-writing/sentence-length -->

依存関係に記載している `org.springframework.boot:spring-boot-starter-web` ライブラリは、デフォルトで Logback 用のライブラリである `org.springframework.boot:spring-boot-starter-logging` が推移的依存で追加されます。

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

## ログの設定 {#logging-configuration}

`src/main/resource` に `log4j2.xml` ファイルを配置しログの設定を記述します。
以下は、ログの設定例です。

```xml title="log4j2.xml"
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="error">

  <Appenders>
    <Console name="console" Target="SYSTEM_OUT">
      <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} %c %-5p %pid %t %m%n" />
    </Console>
    
    <Console name="application.log.appender" Target="SYSTEM_OUT">
        <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %pid %t %m%n"/>
    </Console>
  </Appenders>
  
  <Loggers>
    <Logger name="application.log" level="debug" additivity="false">
        <AppenderRef ref="application.log.appender" />
    </Logger>

    <Root level="info">
        <AppenderRef ref="console" />
    </Root>
  </Loggers>

</Configuration>
```

log4j2.xml のタグの構成要素は以下の通りです。

- Appenders

    ログの出力先を指定します。
    ログイベントをどのリソース（コンソール、ファイル、データベースなど）に送信するかを決定します。

- Loggers

    ログのエントリーポイントを指定します。
    この設定では、どのレベルのメッセージをログに記録するかや、 Appenders のどの要素にメッセージを送信するかなどを指定します。

その他の詳細な設定については、[公式ページ :material-open-in-new:](https://logging.apache.org/log4j/2.x/manual/configuration.html){ target=_blank } を確認してください。

## OpenAPI 仕様書の出力設定 {#open-api-specification-output-configuration}

OpenAPI 仕様書のファイルがビルド時に出力されるようプロジェクトファイルを設定します。
以下に、 `application.properties` と `build.gradle` への設定内容を例示します。
SpringDoc OpenAPI Gradle Plugin のバージョンは [こちら :material-open-in-new:](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-gradle-plugin){ target=_blank } を参照してください。

```properties title="web/src/main/resource/application.properties"
# springdoc-openapi用のURLを指定
springdoc.api-docs.path=/api-docs
```

```groovy title="web/build.gradle"
plugins {
  // OpenAPI のプラグインを追加する。
  id 'org.springdoc.openapi-gradle-plugin' version 'x.x.x'
}

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

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```shell title="web プロジェクトのビルド"
./gradlew web:build
```

??? info "ここまでの手順を実行した際の `web/build.gradle` の例"

    ```groovy title="web/build.gradle"
    plugins {
      id 'java'
      id 'org.springframework.boot' version 'x.x.x'
      id 'io.spring.dependency-management' version 'x.x.x'
      // OpenAPI のプラグインを追加する。
      id 'org.springdoc.openapi-gradle-plugin' version 'x.x.x'
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
      implementation 'org.springframework.boot:spring-boot-starter-web'
      implementation 'com.h2database:h2:x.x.x'
      implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:x.x.x'
      implementation 'org.springframework.boot:spring-boot-starter-actuator'
      implementation 'org.springframework.boot:spring-boot-starter-log4j2'
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

    tasks.named('test') {
      useJUnitPlatform()
    }
    ```

## CORS （クロスオリジンリソース共有）環境の設定 {#cors-environment}

Web API を公開するオリジンと、呼び出し元となるクライアントスクリプトを公開するオリジンが異なる場合（クロスオリジン）の設定は、[こちら](../../cors/index.md) を参照してください。

## メッセージ読込に関する設定 {#message-reading-settings}

他サブプロジェクトで管理されているメッセージを読み込む場合の設定は、[こちら](./message-management.md) を参照してください。

## H2 Database をサーバーモードで起動する設定 {#h2-database-server-settings}

開発環境において、 H2 Database をサーバーモードで起動する設定方法を解説します。

本設定は、開発環境で使用するデータベースが H2 Database であり、同一のデータベースへ同時にアクセスするアプリケーションが存在する場合に行います。
同一のデータベースへ同時にアクセスするアプリケーションの例としては、サンプルアプリの Dressca のように、コンシューマー向けの EC サイトとその EC サイトで売られる商品を管理するアプリが挙げられます。

対象は H2 Database をサーバーモードで起動するクラスと、開発環境におけるアプリケーションの設定などを行う `application-dev.properties` です。以下がそれぞれの設定例です。

??? example "H2 Database をサーバーモードで起動するクラスの例"

    定義するクラスには、 `@Component` と `@Profile` アノテーションを付与し、開発環境でのみ DI コンテナに Bean 登録されるように設定します。
    クラスには H2 Database を起動する処理と停止する処理をそれぞれ実装します。
    起動する処理は、アプリケーションが起動したタイミングで H2 Database を起動させるためにコンストラクタ内で行います。
    停止する処理は、アプリケーションが停止したタイミングで、 H2 Database を停止させるために `@PreDestroy` アノテーションを付与したメソッド内で行います。

    ```java
    /**
     * 開発環境で H2 Database をサーバーモードで立ち上げるためのクラスです。
     */
    @Component
    @Profile("local")
    public class H2ServerLauncher {

      private Server tcpServer;
      private final Logger apLog = LoggerFactory.getLogger(SystemPropertyConstants.APPLICATION_LOG_LOGGER);

      /**
       * {@link H2ServerLauncher} クラスのインスタンスを初期化し、 H2 Database をサーバーモードで起動します。
       */
      public H2ServerLauncher() {
        try {
          this.tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
        } catch (SQLException e) {
          apLog.info("H2 Database は既にサーバーモードで起動しています。");
        }
      }

      /**
       * インスタンスを破棄する際に H2 Database を停止します。
       */
      @PreDestroy
      public void stop() {
        if (this.tcpServer != null) {
          this.tcpServer.stop();
        }
      }
    }
    ```

??? example "`application-dev.properties` の例"

    ```properties
    spring.datasource.hikari.driver-class-name=org.h2.Driver

    # DBをサーバーモードで起動する場合の接続先情報
    spring.datasource.hikari.jdbc-url=jdbc:h2:tcp://localhost:9092/mem:データベースの名前
    # 起動した H2 Database にデータを流し込む設定
    spring.sql.init.mode=always

    spring.datasource.hikari.username=
    spring.datasource.hikari.password=
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    spring.h2.console.settings.web-allow-others=true
    ```
