---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# web プロジェクトの設定 {#top}
<!-- cSpell:ignore datasource testdb hikari -->

web プロジェクトで必要な設定を解説します。

## 依存ライブラリの設定 {#config-dependencies}

web プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-starter-webmvc`：Spring MVC を使用して Web アプリケーションを構築するためのスターター

- `h2`：テストやローカル実行で利用する組み込みの H2 データベース

- `spring-boot-h2console`：H2 Database の Web コンソールを有効化するためのライブラリ

- `spring-boot-starter-actuator`: ヘルスチェックを含めたアプリケーション監視・管理機能を構築するためのスターター

- `spring-boot-starter-log4j2`: Spring Boot アプリケーションで Apache Log4j 2 （以降 log4j2 ）を使用するためのスターター

- `spring-boot-starter-thymeleaf`：Thymeleaf テンプレートエンジンを使用して Web アプリケーションを構築するためのスターター

- `spring-boot-starter-test`：Spring Boot アプリケーションをテストするためのスターター

- `spring-boot-starter-webmvc-test`：Spring MVC アプリケーションをテストするためのライブラリ

上記のライブラリを依存ライブラリとして、 以下のように `build.gradle` の `dependencies` ブロックに追加します。

```groovy title="web/build.gradle"
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-webmvc'
  implementation 'com.h2database:h2:x.x.x'
  implementation 'org.springframework.boot:spring-boot-h2console'
  implementation 'org.springframework.boot:spring-boot-starter-actuator'
  implementation 'org.springframework.boot:spring-boot-starter-log4j2'
  implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
  testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
}
```

??? info "各依存ライブラリのバージョンの参照先"

    - [H2 Database Engine :material-open-in-new:](https://mvnrepository.com/artifact/com.h2database/h2){ target=_blank }

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

Spring Boot の設定は CSR 編と同様です。

[こちら](../../../csr/java/sub-project-settings/web-project-settings.md#config-spring) を参照してください。

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

ロギングライブラリの除外設定は CSR 編と同様です。

[こちら](../../../csr/java/sub-project-settings/web-project-settings.md#logging-library-exclusion-settings) を参照してください。

## ログの設定 {#logging-configuration}

ログの設定は CSR 編と同様です。

[こちら](../../../csr/java/sub-project-settings/web-project-settings.md#logging-configuration) を参照してください。

## メッセージ読込に関する設定 {#message-reading-settings}

他サブプロジェクトで管理されているメッセージを読み込む場合の設定は、[こちら](./message-management.md) を参照してください。

## H2 Database をサーバーモードで起動する設定 {#h2-database-server-settings}

H2 Database をサーバーモードで起動する設定は CSR 編と同様です。

[こちら](../../../csr/java/sub-project-settings/web-project-settings.md#h2-database-server-settings) を参照してください。
