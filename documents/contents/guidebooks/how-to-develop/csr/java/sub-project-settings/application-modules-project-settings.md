---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

<!-- cspell:ignore xxcontext yycontext -->

# application-modules プロジェクトの設定 {#top}

application-modules プロジェクトで必要な設定を解説します。
application-modules プロジェクトには、区切られた文脈単位に分割されたモジュールを配置します。

```text
application-modules/
 └ src/main/java/{ プロジェクトのグループ名 }/applicationmodules
   ├ xxcontext --------------------------------- Xxコンテキストのルートパッケージ 
   ├ yycontext --------------------------------- Yyコンテキストのルートパッケージ
   └ shared ------------------------------------ 複数のコンテキスト間で共有するパッケージ
```

なお、システム共通機能は system-common プロジェクトに配置し、 application-modules プロジェクトには含めません。

## 依存ライブラリの設定 {#config-dependencies}

application-modules プロジェクトで必要になるライブラリは、データアクセス処理やモジュラーモノリスアーキテクチャの実装に必要なライブラリです。
データアクセス処理の実装に AlesInfiny Maia OSS Edition で推奨する MyBatis を利用する場合には、 `mybatis-spring-boot-starter` を利用することを推奨します。
application-modules プロジェクトで利用を推奨するライブラリは以下の通りです。

- `spring-boot-transaction`: Spring Boot アプリケーションでトランザクションを管理するスターター
- `spring-modulith-bom`: Spring Modulith の依存関係を管理するための BOM
- `spring-modulith-starter-core`: モジュラーモノリスアーキテクチャを実装するためのスターター
- `spring-modulith-starter-test`: モジュラーモノリスアーキテクチャのテストを実装するためのスターター
- `mybatis-spring-boot-starter`： MyBatis と Spring Boot を統合するためのスターター
- `mybatis-spring-boot-starter-test`： MyBatis と Spring Boot を統合したアプリケーションをテストするためのスターター

```groovy title="application-modules/build.gradle"
dependencies {
  implementation platform("org.springframework.modulith:spring-modulith-bom:x.x.x")
  implementation 'org.springframework.boot:spring-boot-transaction'
  implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'

  compileOnly 'org.springframework.modulith:spring-modulith-starter-core'

  testImplementation 'org.springframework.modulith:spring-modulith-starter-test'
  testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:x.x.x'
}
```

データアクセス処理の実装に MyBatis 以外を利用する場合、適切なライブラリに切り替えてください。

??? info "各依存ライブラリのバージョンの参照先"

    - [Spring Modulith BOM :material-open-in-new:](https://mvnrepository.com/artifact/org.springframework.modulith/spring-modulith-bom){ target=_blank }
    - [MyBatis Spring Boot Starter :material-open-in-new:](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter){ target=_blank }
    - [MyBatis Spring Boot Starter Test :material-open-in-new:](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter-test){ target=_blank }

## 依存プロジェクトの設定 {#config-projects}

application-modules プロジェクトは system-common を参照しています。
そのため、 `build.gradle` で以下のように他のプロジェクトを依存関係に含めます。

```groovy title="application-modules/build.gradle"
dependencies {
  implementation project(':system-common')
}
```

## ロギングライブラリの除外設定 {#logging-library-exclusion-settings}

<!-- textlint-disable ja-technical-writing/sentence-length -->

依存関係に記載している `org.mybatis.spring.boot:mybatis-spring-boot-starter` ライブラリは、デフォルトで Logback 用のライブラリである `org.springframework.boot:spring-boot-starter-logging` が推移的依存で追加されます。

<!-- textlint-enable ja-technical-writing/sentence-length -->

AlesInfiny Maia OSS Edition では、ロギングライブラリとして Apache Log4j 2 （以降 log4j2 ）を使用します。
そのため、以下のようにデフォルトのロギングライブラリを依存関係から除外する設定を記述します。

```groovy title="spring-boot-starter-logging の除外設定"
configurations {
 all {
  exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
 }
}
```

## プロジェクトのビルド方法の設定 {#config-build}

application-modules プロジェクトは単体で動作せず、他プロジェクトからライブラリとして呼び出します。
呼び出し可能な Jar ファイルで出力する必要がないため以下を設定します。

```groovy title="application-modules/build.gradle"
bootJar {
  enabled = false
}

jar {
  enabled = true
}
```

## 不要な設定やファイルの削除 {#remove-unnecessary-settings-and-files}

[こちら](../common-project-settings.md#java-plugin) で、使用するテストフレームワークを集約管理しているため、 test タスクに関するブロックを削除します。

```groovy title="application-modules/build.gradle" hl_lines="1-3"
tasks.named('test') {
  useJUnitPlatform()
}
```

<!-- textlint-disable ja-technical-writing/sentence-length -->

また、併せて不要なファイルを削除します。
application-modules プロジェクトの `src` 以下にある、 `ApplicationModulesApplication.java` と `ApplicationModulesApplicationTest.java` を削除してください。

<!-- textlint-enable ja-technical-writing/sentence-length -->

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```shell title="application-modules プロジェクトのビルド"
./gradlew application-modules:build
```

??? info "ここまでの手順を実行した際の `application-modules/build.gradle` の例"

    ```groovy title="application-modules/build.gradle"
    plugins {
      id 'java'
      id 'org.springframework.boot' version 'x.x.x'
      id 'io.spring.dependency-management' version 'x.x.x'
    }

    group = 'プロジェクトのグループ名'
    version = 'x.x.x-SNAPSHOT'
    description = 'プロジェクトの説明'

    java {
      toolchain {
        languageVersion = JavaLanguageVersion.of(x)
      }
    }

    repositories {
      mavenCentral()
    }

    dependencies {
      implementation platform("org.springframework.modulith:spring-modulith-bom:x.x.x")
      implementation 'org.springframework.boot:spring-boot-transaction'
      implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:x.x.x'

      compileOnly 'org.springframework.modulith:spring-modulith-starter-core'

      testImplementation 'org.springframework.modulith:spring-modulith-starter-test'
      testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:x.x.x'
      // その他、プロジェクトに必要な依存ライブラリは任意で追加してください。
    }

    configurations {
      all {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
      }
    }

    bootJar {
      enabled = false
    }

    jar {
      enabled = true
    }
    ```

## モジュールの追加 {#add-application-module}

application-modules プロジェクトに、区切られた文脈単位のモジュールを追加する手順を解説します。
モジュールは、 Spring Modulith のアプリケーションモジュールとして定義します。
アプリケーションモジュールとして定義することで、モジュール間の依存関係を Spring Modulith で検証できます。
アプリケーションモジュールの詳細は、[Spring Modulith のリファレンスドキュメント :material-open-in-new:](https://spring.pleiades.io/spring-modulith/reference/fundamentals.html){ target=_blank } を参照してください。

### モジュールのパッケージ作成 {#create-module-package}

`{ プロジェクトのグループ名 }.applicationmodules` パッケージの直下に、モジュールごとのパッケージを作成します。
ここで作成したパッケージが、モジュールのルートパッケージです。
ルートパッケージの直下には、他のモジュールへ公開する型のみを配置します。
公開しない型は `internal` パッケージ以下に配置します。

```text
application-modules/
 └ src/main/java/{ プロジェクトのグループ名 }/applicationmodules
   └ xxcontext --------------------------------- Xxコンテキストのルートパッケージ
     ├ package-info.java ----------------------- モジュールを定義するファイル
     └ internal -------------------------------- 他のモジュールへ公開しない型を配置するパッケージ
```

### package-info.java の配置 {#create-package-info}

モジュールのルートパッケージ直下に `package-info.java` を配置し、 `#!java @ApplicationModule` アノテーションを付与します。
このアノテーションは、[依存ライブラリの設定](#config-dependencies) で追加した `spring-modulith-starter-core` に含まれます。

```java title="xxcontext/package-info.java"
@ApplicationModule(
    displayName = "Xx コンテキスト",
    allowedDependencies = { "shared" },
    type = Type.CLOSED)
package com.example.applicationmodules.xxcontext;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
```

主な属性は以下の通りです。

- `displayName`: モジュールの表示名です。省略した場合はルートパッケージ名を使用します。
- `allowedDependencies`: 依存を許可するモジュールを指定します。省略した場合はすべてのモジュールへの依存を許可します。すべてのモジュールへの依存を禁止する場合は、空の配列を指定します。
- `type`: モジュールの公開範囲を指定します。指定できる値は [オープンモジュールとクローズドモジュールの指定](#specify-module-type) を参照してください。

### オープンモジュールとクローズドモジュールの指定 {#specify-module-type}

`type` 属性には、モジュールをオープンモジュールとクローズドモジュールのどちらとするかを指定します。
それぞれの違いは以下の通りです。

- クローズドモジュール（ `#!java Type.CLOSED` ）: 既定値です。他のモジュールから参照できる型は、ルートパッケージ直下の型だけです。
- オープンモジュール（ `#!java Type.OPEN` ）: サブパッケージに配置した型を含め、すべての型を他のモジュールから参照できます。

モジュールは、原則としてクローズドモジュールとし、内部実装を他のモジュールから隠蔽します。
複数のモジュールから共有する `shared` パッケージのように、サブパッケージの型も公開する場合は、オープンモジュールとします。

```java title="shared/package-info.java" hl_lines="3"
@ApplicationModule(
    displayName = "共有モジュール",
    type = Type.OPEN)
package com.example.applicationmodules.shared;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
```

!!! warning "オープンモジュールの利用"

    オープンモジュールは内部実装を隠蔽できないため、モジュール間の結合度が高くなりやすい定義方法です。
    オープンモジュールとして定義するモジュールは、必要最小限にとどめてください。

## MyBatis Generator によるコードの自動生成 {#code-generation-with-mybatis-generator}

<!-- textlint-disable ja-technical-writing/sentence-length -->

MyBatis Generator は、 MyBatis を使用する際に、データベースのテーブルからテーブルエンティティやマッパーインターフェース、 SQL マッピングファイルを自動的に生成するツールです。

<!-- textlint-enable ja-technical-writing/sentence-length -->

具体的な設定方法については、[こちら](./mybatis-generator-settings.md) を参照してください。

## メッセージ管理の設定 {#message-management-settings}

application-modules プロジェクトで管理する業務メッセージの設定方法については、[こちら](./message-management.md) を参照してください。
