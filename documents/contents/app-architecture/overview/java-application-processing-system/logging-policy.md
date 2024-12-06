---
title: Java アプリケーションの 処理方式
description: アプリケーションの形態によらず、 Java アプリケーションで 考慮すべき関心事について、実装方針を説明します。
---

# ログ出力方針 {#logging-policy}

概要編では、一般的なログの種類とログレベルを定めます。
アプリケーション形態ごとに個別に検討が必要なものについては別途定めます。
アプリケーション形態別のアプリケーションアーキテクチャ解説も、あわせて参照してください。

## ログの種類 {#log-pattern}

AlesInfiny Maia で定義するログの種類は以下の通りです。

- 操作ログ

    ユーザーの操作履歴や、アプリケーションに対して行った操作を記録するログを操作ログと呼びます。

- 通信ログ

    アプリケーションがネットワークを介して通信する際、送受信する業務データや、送信先の情報、ヘッダー情報等を記録するログを通信ログと呼びます。

- 監査ログ

    アプリケーションの持つデータに対して行われた CRUD 処理を、誰がいつ実行したか記録するログを監査ログと呼びます。

- アプリケーションログ

    ここまでのログの種類に該当しない、アプリケーションのロジック内から出力する汎用的なログをアプリケーションログと呼びます。

## ログレベル {#log-level}

出力するログには、ログを出力する業務処理内で指定したログレベルを付与します。
ログに出力する情報によって、適切なログレベルを選択します。
ログレベルの定義は以下の通りです。

- FATAL

    業務の即時停止につながる可能性のあるログを出力するときに使用するログレベルです。

- ERROR

    一部の業務が停止する可能性のあるログを出力するときに使用するログレベルです。
    マスターデータの不整合や、原因の不明なエラー発生など、システム運用担当者による確認や対処が必要となる状態を通知する目的に使用します。

- WARN

    業務は継続できるものの、一時的に発生したエラー状態を出力するときに使用するログレベルです。
    業務エラーの記録など、システム運用担当者による対応は不要なものの、システムとして不安定な状態を記録する際使用します。

- INFO

    システム運用にあたって必要となる情報を出力するときに使用するログレベルです。
    バッチ処理の開始／終了の記録など、システムの状態を記録する際使用します。

- DEBUG

    開発者がアプリケーションの開発のために使用するログレベルです。
    各メソッドの入出力データなど、開発目的の情報を記録する際使用します。

## ログレベルと環境ごとの出力設定 {#configuration-of-log-levels-and-output-per-environment}

システムの実行環境にあわせて、適切なレベルのログを出力するように構成します。

| ログレベル |      本番環境      |     テスト環境     |  ローカル開発環境  |
| ---------- | :----------------: | :----------------: | :----------------: |
| FATAL      | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| ERROR      | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| WARN       | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| INFO       | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| DEBUG      |                    |                    | :white_check_mark: |

## ログに含める標準データ {#standard-log-data}

以下の情報をログに含めます。

- ログ出力日時
- ログレベル
- メッセージ
- プロセス ID
- スレッド名
- 例外メッセージとスタックトレース ( 例外を記録するログのみ )

!!! info "プロセス ID とスレッド名の用途"
    プロセス ID とスレッド名は主にバッチのログをトレースするために出力します。
    複数のバッチが同時に動作、また複数のスレッドの処理が同時に動作しながらログ出力すると、各処理で出力したログが混ざりあった状態のログができあがります。
    プロセス ID はバッチの実行単位で出力したログだけをフィルタリングして、処理ごとにログを分解して解析するために使用できます。
    スレッド名は、 1 つのスレッド内で出力したログだけをフィルタリングして、処理ごとにログを分解して解析するために使用できます。
    マルチスレッドプログラミングを行うなど、スレッド名だけではログのトレースが実現できない場合は、別途ログをトレースするための手段を検討します。

## ロギングライブラリ {#logging-libraries}

AlesInfiny Maia では、 Java アプリケーションのロギングライブラリとして [Apache Log4j 2 :material-open-in-new:](https://logging.apache.org/log4j/2.x/){ target=_blank } を使用します。
またロギングファサードとして [SLF4J :material-open-in-new:](https://www.slf4j.org/){ target=_blank } を使用します。

### ログの設定ファイル {#logging-configuration-files}

log4j 2 では、設定ファイルを使用しログの動作を制御します。

ログの設定ファイルは、以下の形式をサポートしています。
なお、 AlesInfiny Maia では、ログの設定ファイルに XML 形式を採用しています。

- XML
- JSON
- YAML
- Properties

また、 AlesInfiny Maia はマルチプロジェクト構成を採用しています。
エントリーポイントとなるサブプロジェクトが複数ある場合、それぞれでログ形式を定めることが考えられます。

そのため、以下のようにログの設定ファイルをエントリーポイントとなる各プロジェクトに配置します。

```terminal linenums="0"
root/ ------------------------------------------ root フォルダー
 ├ web-admin/ ---------------------------------- 管理アプリのエントリーポイントとなるサブプロジェクト
 │ └ src/main/resource/ ------------------------ 設定ファイルを一元管理するフォルダー
 │    └ log4j.xml ------------------------------ ログの設定ファイル
 └ web-consumer/ ------------------------------- コンシューマーアプリのエントリーポイントとなるサブプロジェクト 2
   └ src/main/resource/ ------------------------ 設定ファイルを一元管理するフォルダー
      └ log4j.xml ------------------------------ ログの設定ファイル
```

log4j.xml の具体的な設定については、[こちら](../../../guidebooks/how-to-develop/java/sub-project-settings/web-project-settings.md#logging-configuration) を確認してください。

<!-- 以降、logging-configuration の項目に移動する -->

## 依存ライブラリの設定 {#config-dependencies}

web プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-starter-web`：Spring MVC を使用して Web アプリケーションを構築するためのスターター

- `h2`：テストやローカル実行で利用する組み込みの H2 データベース

- `springdoc-openapi-starter-webmvc-ui`：Spring Web MVC アプリケーション向けの、 OpenAPI 形式の API ドキュメントを生成するためのライブラリ

- `spring-boot-starter-actuator`: ヘルスチェックを含めたアプリケーション監視・管理機能を構築するためのスターター

- `org.springframework.boot:spring-boot-starter-log4j2`: Spring Boot アプリケーションで log4j 2 を使用するためのスターター

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

## ログの設定 {#logging-configuration}

`src/main/resource` に `log4j.xml` ファイルを配置しログの設定を記述します。
以下は、ログの設定例です。

```xml title="log4j.xml"
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

log4j.xml のタグの構成要素は以下の通りです。

- Appenders

    ログの出力先を指定します。
    ログイベントをどのリソース（コンソール、ファイル、データベースなど）に送信するかを決定します。

- Loggers

    ログのエントリーポイントを指定します。
    この設定では、どのレベルのメッセージをログに記録するかや、 Appenders のどの要素にメッセージを送信するかなどを指定します。

その他の詳細な設定については、[公式ページ :material-open-in-new:](https://logging.apache.org/log4j/2.x/manual/configuration.html){ target=_blank } を確認してください。

<!-- infrastructure 層以外の各サブプロジェクトに以下を記載する -->

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

<!-- textlint-disable ja-technical-writing/sentence-length -->

依存関係に記載している `org.springframework.boot:spring-boot-starter` ライブラリは、デフォルトで Logback 用のライブラリである `org.springframework.boot:spring-boot-starter-logging` が推移的依存で追加されます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

AlesInfiny Maia OSS Edition では、ロギングライブラリとして log4j 2 を使用します。
そのため、以下のようにデフォルトのロギングライブラリを依存関係から除外する設定を記述します。

``` groovy title="spring-boot-starter-logging の除外設定"
configurations {
 all {
  exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
 }
}
```

<!-- infrastructure 層の各サブプロジェクトに以下を記載する -->

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings-infra}

<!-- textlint-disable ja-technical-writing/sentence-length -->

依存関係に記載している `org.mybatis.spring.boot:mybatis-spring-boot-starter` ライブラリは、デフォルトで Logback 用のライブラリである `org.springframework.boot:spring-boot-starter-logging` が推移的依存で追加されます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

AlesInfiny Maia OSS Edition では、ロギングライブラリとして log4j 2 を使用します。
そのため、以下のようにデフォルトのロギングライブラリを依存関係から除外する設定を記述します。

``` groovy title="spring-boot-starter-logging の除外設定"
configurations {
 all {
  exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
 }
}
```
