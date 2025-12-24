---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# プラグイン、依存ライブラリのバージョン定義一元化 {#top}
<!-- cSpell:ignore buildscript subprojects projectlombok Dspring -->

アプリケーションが使用する各種プラグインおよびライブラリのバージョンは、サブプロジェクト間のバージョン齟齬などを防ぐために `dependencies.gradle` で一元管理します。

## ルートプロジェクトの設定 {#config-root-project}

ルートプロジェクト直下に `dependencies.gradle` ファイルを追加してください。その後以下のように、利用するプラグインとライブラリのバージョン、ライブラリ定義文字列を変数として定義します。

```groovy title="{ルートプロジェクト}/dependencies.gradle"
ext {
  // プラグインのバージョン
  springBootVersion = 'x.x.x'
  springDependencyManagementVersion = 'x.x.x'
  springdocOpenapiGradlePluginVersion = 'x.x.x'

  // 依存ライブラリのバージョン
  springdocOpenapiVersion = 'x.x.x'
  h2Version = 'x.x.x'
  
  // ライブラリ定義文字列
  supportDependencies = [
    spring_boot_starter : 'org.springframework.boot:spring-boot-starter',
    spring_boot_starter_webmvc : 'org.springframework.boot:spring-boot-starter-webmvc',
    spring_boot_starter_actuator : 'org.springframework.boot:spring-boot-starter-actuator',
    spring_boot_starter_test : 'org.springframework.boot:spring-boot-starter-test',
    spring_boot_starter_webmvc_test : 'org.springframework.boot:spring-boot-starter-webmvc-test',
    spring_boot_starter_log4j2 : 'org.springframework.boot:spring-boot-starter-log4j2',
    spring_boot_h2console : 'org.springframework.boot:spring-boot-h2console',
    springdoc_openapi_starter_webmvc_ui : "org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiVersion",
    h2database : "com.h2database:h2:$h2Version",
  ]
}
```

!!! tip "dependencies.gradle に記載する情報の範囲について"
    `dependencies.gradle` には依存ライブラリのバージョン部分のみを定義してもかまいません。
    しかし、ライブラリ定義文字列全体を変数として定義することで、 GitHub が提供する依存関係監視ツール [Dependabot :material-open-in-new:](https://docs.github.com/ja/code-security/dependabot){ target=_blank } による通知が受けられます。

次に、上記ファイルをルートプロジェクトの `build.gradle` 内の `buildscript` ブロックで読み込みます。これにより、各サブプロジェクトからそれぞれの変数を参照できるようになります。
以下に示す `buildscript` ブロックを、ルートプロジェクトの `build.gradle` の先頭に追加してください。

```groovy title="{ルートプロジェクト}/build.gradle"
buildscript {
  apply from: 'dependencies.gradle'
}
```

??? info "ここまでの手順を実行した際の `{ルートプロジェクト}/build.gradle` の例"

    ```groovy title="{ルートプロジェクト}/build.gradle"
    buildscript {
      apply from: 'dependencies.gradle'
    }

    plugins {
      id 'com.github.spotbugs' version 'x.x.x' apply false
    }

    subprojects {

      apply plugin: 'java'
      apply plugin: 'jacoco'
      apply plugin: 'checkstyle'
      apply plugin: 'com.github.spotbugs'

      dependencies {
        // Lombok の設定
        annotationProcessor 'org.projectlombok:lombok'
        testAnnotationProcessor 'org.projectlombok:lombok'
        compileOnly 'org.projectlombok:lombok'
        testCompileOnly 'org.projectlombok:lombok'
      }

      test {
        // UTテスト時はtestプロファイルを利用
        jvmArgs=['-Dspring.profiles.active=test']
        useJUnitPlatform()
      }

      checkstyle {
        toolVersion = 'x.x.x'
      }

      spotbugs {
        toolVersion = 'x.x.x'
        excludeFilter.set(rootProject.file('フィルタファイルのパス'))
        ignoreFailures = true
      }

      jacocoTestReport {
        reports {
          html.required = true
        }
        afterEvaluate {
          classDirectories.setFrom(classDirectories.files.collect {
            fileTree(dir: it, excludes: ['**/xxx/*', '**/yyy.class'])
          })
        }
      }
    }
    ```

## サブプロジェクトの設定 {#config-sub-project}

各サブプロジェクトでは、下記のように `dependencies.gradle` で定義された変数を読み取る形にプラグインや依存ライブラリの記載を修正します。
以下に、バージョン定義を `dependencies.gradle` に移管した web プロジェクトの `build.gradle` の一部を示します。

!!! info "Groovy ファイルにおける一重引用符と二重引用符の使い分け"
    Groovy では文字列は一重引用符で囲み、変数を含む文字列は二重引用符で囲んで表現します。
    変数を含む文字列を一重引用符で囲むとエラーが出るため、注意して使い分けてください。

```groovy title="web/build.gradle" hl_lines="3 4 5 9 10 11 12 13 14 20 21"
plugins {
  id 'java'
  id 'org.springframework.boot' version "${springBootVersion}"
  id 'io.spring.dependency-management' version "${springDependencyManagementVersion}"
  id 'org.springdoc.openapi-gradle-plugin' version "${springdocOpenapiGradlePluginVersion}"
}

dependencies {
  implementation supportDependencies.spring_boot_starter_webmvc
  implementation supportDependencies.h2database
  implementation supportDependencies.spring_boot_h2console
  implementation supportDependencies.springdoc_openapi_starter_webmvc_ui
  implementation supportDependencies.spring_boot_starter_actuator
  implementation supportDependencies.spring_boot_starter_log4j2

  implementation project(':application-core')
  implementation project(':infrastructure')
  implementation project(':system-common')

  testImplementation supportDependencies.spring_boot_starter_test
  testImplementation supportDependencies.spring_boot_starter_webmvc_test
}
```

??? info "ここまでの手順を実行した際の `web/build.gradle` の例"

    ```groovy title="web/build.gradle"
    plugins {
      id 'java'
      id 'org.springframework.boot' version "${springBootVersion}"
      id 'io.spring.dependency-management' version "${springDependencyManagementVersion}"
      id 'org.springdoc.openapi-gradle-plugin' version "${springdocOpenapiGradlePluginVersion}"
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
      implementation supportDependencies.spring_boot_starter_webmvc
      implementation supportDependencies.h2database
      implementation supportDependencies.spring_boot_h2console
      implementation supportDependencies.springdoc_openapi_starter_webmvc_ui
      implementation supportDependencies.spring_boot_starter_actuator
      implementation supportDependencies.spring_boot_starter_log4j2

      implementation project(':application-core')
      implementation project(':infrastructure')
      implementation project(':system-common')

      testImplementation supportDependencies.spring_boot_starter_test
      testImplementation supportDependencies.spring_boot_starter_webmvc_test
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

バージョン定義一元化を実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。

```shell title="バックエンドアプリケーションのビルド"
./gradlew build
```
