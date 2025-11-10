---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# プロジェクトの共通設定 {#top}
<!-- cSpell:ignore subprojects projectlombok Dspring -->

プロジェクト全体の設定として、ルートプロジェクト内で設定すべき内容について解説します。
Spring Initializr で作成したルートディレクトリを Visual Studio Code 等で開いてください。

## マルチプロジェクト構成のための設定 {#config-multi-project}

Spring Initializr を利用して作成したプロジェクトの雛型は、単一のプロジェクト構成を想定したものであるため、マルチプロジェクトとして動作するようにします。

ルートプロジェクト内に配置したサブプロジェクトをプロジェクトとして取り込むように、ルートプロジェクト直下の `settings.gradle` を修正します。以下のように、`rootProject.name` にルートプロジェクトの名前を設定し、 `include` にサブプロジェクトの名前を列挙します。なお各プロジェクトの名前はフォルダー名（Spring Initializr で設定した Metadata : Artifact）に対応します。

```groovy title="{ルートプロジェクト}/settings.gradle"
rootProject.name = 'xx-system' // ルートプロジェクトの名前
include 'application-core', 'infrastructure', 'web', 'batch', 'system-common' // サブプロジェクトの名前
```

次に、ルートプロジェクトにある不要な記述を取り除きます。`build.gradle`から以下の項目を削除してください。

```groovy title="{ルートプロジェクト}/build.gradle"  hl_lines="2 3 4 7 8 10 11 12 13 14 16 17 18 21 22 23 26 27 28"
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
  implementation 'org.springframework.boot:spring-boot-starter'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
  testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
  useJUnitPlatform()
}

```

## ビルドスクリプトの共通化 {#common-build-script}

ビルドをする上で、各サブプロジェクト共通の設定は、ルートプロジェクトの `build.gradle` 内の `subprojects` ブロックに定義します。
ここに定義された内容は、全てのサブプロジェクトで定義したものと同等の扱いになります。
Spring Initializr を利用して作成したプロジェクトには `subprojects` ブロックがないため、 `dependencies` ブロックの下部に以下の記述を追加します。

```groovy title="{ルートプロジェクト}/build.gradle"
subprojects {
}
```

設定内容はそれぞれのプロジェクトによりますが、一般的な設定項目について以降で解説します。
プラグインおよびライブラリのバージョンについてはプロジェクトに合わせて適切に選定してください。
特別な要件がない場合には [Maven Repository :material-open-in-new:](https://mvnrepository.com/){ target=_blank }を参照して最新版を利用し、バージョンによるエラーについては適宜対応してください。

また、以降の手順で追加するそれぞれのブロックは、並んでいる順番によっては正常に動作しない場合があります。
本ページの下部に、本手順を全て実行した際の例を示しているので、適宜そちらを参照してください。

### プラグインの導入 {#common-plugin}

各サブプロジェクト共通で利用する Gradle のプラグインを定義します。
以下が適用候補です。

- Java プラグイン：Java プロジェクトをビルドする基本的なプラグイン
- Checkstyle プラグイン：静的テストツール用のプラグイン
- SpotBugs プラグイン：静的テストツール用のプラグイン
- JaCoCo プラグイン：カバレッジ取得ツール用プラグイン

```groovy title="{ルートプロジェクト}/build.gradle"
subprojects {
  apply plugin: 'java'
  apply plugin: 'jacoco'
  apply plugin: 'checkstyle'
  apply plugin: 'com.github.spotbugs'
}
```

SpotBugs プラグインは Gradle の標準的なプラグインセットに含まれていないため、別途設定が必要になります。
`plugins` ブロックに以下の記述を追加してください。
またバージョンは [こちら :material-open-in-new:](https://mvnrepository.com/artifact/com.github.spotbugs.snom/spotbugs-gradle-plugin){ target=_blank } を参照してください。

```groovy title="{ルートプロジェクト}/build.gradle"
plugins {
  id 'com.github.spotbugs' version 'x.x.x' apply false
}
```

### 依存ライブラリの設定 {#common-dependencies}

サブプロジェクト毎の役割に関わらず、システム全体で利用され得るライブラリについては、共通の依存ライブラリとして定義します。
AlesInfiny Maia OSS Edition ではボイラープレートコードを削減するためのライブラリである Lombok の使用を推奨しています。

設定の手順として、まずは Spring Initializr でルートプロジェクトの雛型作成の際に追加された `dependencies` ブロックを、 `subprojects` ブロック内に移動させます。
その後、 `dependencies` ブロックに必要な依存ライブラリを以下のように追加します。

```groovy title="{ルートプロジェクト}/build.gradle"
subprojects {
  dependencies {
    // Lombok の設定
    annotationProcessor 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'
    compileOnly 'org.projectlombok:lombok'
    testCompileOnly 'org.projectlombok:lombok'
  }
}
```

### タスクの設定 {#common-tasks}

導入したプラグインによって定義されたタスクに対して、必要であれば設定を追加します。
設定項目や設定の要否はプラグインによります。
例えば、各種ツール類のバージョン指定や、静的テストツールのルールのようなインプットファイルの指定、レポート等の出力設定などが一般的には考えられます。

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia ）として推奨する各プラグインの設定は、以下の通りです。

#### Java プラグイン {#java-plugin}

Java プラグインのカスタマイズを行う `build.gradle` の設定方法を解説します。

本ガイドではカスタマイズの具体例として、 test タスクにおいて使用するプロファイルを変更する設定を示します。

```groovy title="{ルートプロジェクト}/build.gradle"  hl_lines="4"

subprojects {
  test {
    // UTテスト時はtestプロファイルを利用
    jvmArgs=['-Dspring.profiles.active=test']
    useJUnitPlatform()
  }
}

```

Java プラグインのその他の設定項目については、[こちら :material-open-in-new:](https://docs.gradle.org/current/userguide/java_plugin.html){ target=_blank } を参照してください。

#### Checkstyle プラグイン {#checkstyle-plugin}

Checkstyle プラグインのカスタマイズを行う `build.gradle` の設定方法を解説します。

<!-- textlint-disable ja-technical-writing/sentence-length -->

Checkstyle を利用する場合、静的テストを実行する際のルールをインプットファイルで定義します。
[Google Style :material-open-in-new:](https://google.github.io/styleguide/javaguide.html){ target=_blank } に準拠したルールを適用する場合、 Checkstyle が提供する [インプットファイル :material-open-in-new:](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml){ target=_blank } を利用します。
独自のルールを定義したい場合には、このインプットファイルを編集してください。

<!-- textlint-enable ja-technical-writing/sentence-length -->

デフォルトの設定では、以下の階層にある checkstyle.xml ファイルをインプットファイルとして読みこみます。ダウンロードしたインプットファイルの名前を checkstyle.xml に変更した後、フォルダーを追加して適切な位置に配置してください。

![Checkstyle のデフォルトの読み込み構成](../../../../images/guidebooks/how-to-develop/csr/java/checkstyle-default-structure-light.png#only-light){ loading=lazy }
![Checkstyle のデフォルトの読み込み構成](../../../../images/guidebooks/how-to-develop/csr/java/checkstyle-default-structure-dark.png#only-dark){ loading=lazy }

また、自動生成されたクラスなど、特定のクラスに対して Checkstyle の静的テスト対象から除外するように設定できます。
設定方法については、[こちら :material-open-in-new:](https://checkstyle.sourceforge.io/filters/suppressionfilter.html){ target=_blank } を参照してください。

Checkstyle プラグインのその他の設定項目については、[こちら :material-open-in-new:](https://docs.gradle.org/current/userguide/checkstyle_plugin.html){ target=_blank } を参照してください。

??? info "インプットファイルの命名や設置する階層をカスタマイズする場合"

    インプットファイルに任意の命名を適用する場合や、上記の階層以外にある checkstyle.xml をインプットファイルとして読み込む場合には、 `build.gradle` に以下の記述を追加してください。

    ```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="4 6"
    subprojects {
      checkstyle {
        // インプットファイルに任意の命名を適用する場合
        configFile = file('ディレクトリパスを含むインプットファイル名')
        // デフォルトの階層以外にある checkstyle.xml をインプットファイルとして読み込む場合
        configDirectory = rootProject.file('インプットファイルが格納されたディレクトリパス')
      }
    }
    ```

??? info "Google Style を適用した Checkstyle のタスクでエラーが起きた場合の対処法"

    Gradle がデフォルトで提供する Checkstyle のバージョンでは、Google Style のインプットファイルを適用したタスクでバージョン間の機能の違いを原因とするエラーが起きる可能性があります。[こちら :material-open-in-new:](https://mvnrepository.com/artifact/com.puppycrawl.tools/checkstyle){ target=_blank } を参照して、 Checkstyle の toolVersion に最新のバージョンを指定してください。
    ```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3"
    subprojects {
      checkstyle {
        toolVersion = 'x.x.x'
      }
    }
    ```

#### SpotBugs プラグイン {#spotbugs-plugin}

SpotBugs プラグインのカスタマイズを行う `build.gradle` の設定方法を解説します。

SpotBugs を利用する際、自動生成されたクラスやメソッドが SpotBugs の警告の対象になることがあります。
このような場合、 SpotBugs ではフィルタファイルを適用することでクラスやメソッド、バグのパターン単位で警告のフィルタリングを設定できます。
SpotBugs のフィルタリングの設定内容については、[こちら :material-open-in-new:](https://spotbugs.readthedocs.io/ja/latest/filter.html){ target=_blank } をご覧ください。
フィルタファイルを適用する際には、 `build.gradle` に以下の記述を追加してください。

```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3"
subprojects {
  spotbugs {
    excludeFilter.set(rootProject.file('フィルタファイルのパス'))
    ignoreFailures = true
  }
}
```

SpotBugs プラグインのその他の設定項目については、[こちら :material-open-in-new:](https://spotbugs.readthedocs.io/ja/latest/gradle.html){ target=_blank } を参照してください。

#### JaCoCo プラグイン {#jacoco-plugin}

JaCoCo プラグインのカスタマイズを行う `build.gradle` の設定方法を解説します。

JaCoCo でカバレッジ・レポートから除外したいファイルやクラスがある場合、以下のように指定します。

```groovy title="{ルートプロジェクト}/build.gradle"　hl_lines="6 7 8 9 10"
subprojects {
  jacocoTestReport {
    reports {
      html.required = true
    }
    afterEvaluate {
      classDirectories.setFrom(classDirectories.files.collect {
        fileTree(dir: it, excludes: ['**/xxx/*', '**/Yyy.class'])
      })
    }
  }
}
```

JaCoCo プラグインのその他の設定項目は、[こちら :material-open-in-new:](https://docs.gradle.org/current/userguide/jacoco_plugin.html){ target=_blank } を参照してください。

### フォーマッターの設定 {#formatter-settings}

ソースコードのフォーマットの一貫性を保つために、統合開発環境で提供されている自動フォーマット機能を利用します。
ルートディレクトリ直下の .vscode フォルダーの `settings.json` に設定を追記します。
.vscode フォルダーおよび `settings.json` がない場合は新規作成してください。

AlesInfiny Maia OSS Edition ではコーディング規約として [Google Java Style :material-open-in-new:](https://google.github.io/styleguide/javaguide.html){ target=_blank } を採用しています。
Visual Studio Code を利用する場合、 [こちら :material-open-in-new:](https://code.visualstudio.com/docs/java/java-linting#_applying-formatter-settings){ target=_blank } を参照して、以下のように `settings.json` にフォーマッターを設定してください。

```json title=".vscode/settings.json"
{
  "java.format.settings.url": "フォーマッター xml ファイルの URL またはファイルパス"
}
```

上記の設定の他にソースコードの入力や保存、ペースト時に自動的にフォーマットされるよう以下を追加してください。

```json title=".vscode/settings.json"
{
  "[java]": {
    "editor.formatOnSave": true,
    "editor.formatOnPaste": true,
    "editor.formatOnType": true
  }
}
```

!!! waring "フォーマッターと静的テストの競合"

    フォーマットツールが自動的に整形したソースコードが静的テストのルールに違反することで、静的テスト側で警告が発生するかもしれません。
    また、フォーマッターと静的テストの設定が一致していても、各ツールの仕様によってフォーマットの基準が異なり、意図しない警告が発生する可能性もあります。
    このような警告の常態化は、対処を必要とする重要な警告が埋もれてしまうことになり、プロジェクトに悪影響を与えます。
    フォーマッターや静的テストのルールの緩和なども含め、警告が出ないように設定を調整してください。

ここまでを実行した後に、適切にビルドが実行できるかを確認します。
ターミナルを用いてルートプロジェクト直下で以下を実行してください。
なお、以下のコマンドでビルドを実行すると、デフォルトで作成されたソースコードに対して Checkstyle の警告が出力されるので、出力内容に従って対処してください。

```shell title="バックエンドアプリケーションのビルド"
./gradlew build
```

??? info "ここまでの手順を実行した際の `{ルートプロジェクト}/build.gradle` の例"

    ```groovy title="{ルートプロジェクト}/build.gradle"
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
