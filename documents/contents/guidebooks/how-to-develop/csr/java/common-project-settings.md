---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# プロジェクトの共通設定 {#top}
<!-- cSpell:ignore projectlombok Dspring findbugs -->

プロジェクト全体の設定として、ルートプロジェクト内で設定すべき内容について解説します。
Spring Initializr で作成したルートディレクトリを Visual Studio Code 等で開いてください。

## マルチプロジェクト構成のための設定 {#config-multi-project}

Spring Initializr を利用して作成したプロジェクトの雛型は、単一のプロジェクト構成を想定したものであるため、マルチプロジェクトとして動作するようにします。

ルートプロジェクト内に配置したサブプロジェクトをプロジェクトとして取り込むように、ルートプロジェクト直下の `settings.gradle` を修正します。以下のように、`rootProject.name` にルートプロジェクトの名前を設定し、 `include` にサブプロジェクトの名前を列挙します。なお各プロジェクトの名前はフォルダー名（Spring Initializr で設定した Metadata: Artifact）に対応します。

```groovy title="{ルートプロジェクト}/settings.gradle"
rootProject.name = 'xx-system' // ルートプロジェクトの名前
include 'application-modules', 'web', 'batch', 'system-common' // サブプロジェクトの名前
```

次に、ルートプロジェクトにある不要な記述を取り除きます。`build.gradle`から以下の項目を削除してください。

```groovy title="{ルートプロジェクト}/build.gradle"  hl_lines="2-4 7-9 11-15 17-19 22-24 27-29"
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

- Java プラグイン: Java プロジェクトをビルドする基本的なプラグイン
- Checkstyle プラグイン: 静的テストツール用のプラグイン
- SpotBugs プラグイン: 静的テストツール用のプラグイン
- JaCoCo プラグイン: カバレッジ取得ツール用プラグイン

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

本ガイドではカスタマイズの具体例として、以下のシナリオの例を示します。

- test タスクでは `test` プロファイルを使用する
- テストフレームワークとして JUnit5 を使用する
- ソースファイルの文字コードを明示的に指定する

    実行環境（OS や JDK の設定）によっては、ソースファイルの文字コードを正しく認識できず、コンパイル時に文字化けやエラーを引き起こす可能性があります。
    このような事態を避けるため、 `compileJava` タスク、 `compileTestJava` タスクおよび `javadoc` タスクの `encoding` オプションで、文字コードを明示的に指定することを推奨します。

これらのシナリオを踏まえた `build.gradle` の設定例は以下の通りです。

```groovy title="{ルートプロジェクト}/build.gradle"  hl_lines="2-4 8 9"
  
subprojects {
  compileJava.options.encoding = 'UTF-8'
  compileTestJava.options.encoding = 'UTF-8'
  javadoc.options.encoding = 'UTF-8'

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

また、自動生成されたクラスなど、特定のクラスに対して Checkstyle の静的テスト対象から除外するように設定できます。
設定方法については、[こちら :material-open-in-new:](https://checkstyle.sourceforge.io/filters/suppressionfilter.html){ target=_blank } を参照してください。

Checkstyle が解析に利用するツール本体のバージョン（`toolVersion`）は明示的に指定してください。
バージョンを指定しない場合、 Gradle が提供するデフォルトバージョンが利用されます。
Gradle のデフォルトバージョンの Checkstyle では、 Google Style のインプットファイルを適用したタスクでバージョン間の機能の違いにより、エラーとなる可能性があります。
[こちら :material-open-in-new:](https://mvnrepository.com/artifact/com.puppycrawl.tools/checkstyle){ target=_blank } を参照して、利用する JDK のバージョンと互換性のある範囲で、以下の `toolVersion` に適切なバージョンを指定してください。

```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3"
subprojects {
  checkstyle {
    toolVersion = 'x.x.x'
  }
}
```

VS Code の拡張機能である [Checkstyle for Java](https://marketplace.visualstudio.com/items?itemName=shengchen.vscode-checkstyle) を利用している場合、 Checkstyle プラグインに適用したルールを Checkstyle for Java にも適用します。
ルートディレクトリ直下の .vscode フォルダーの `settings.json` に設定を追記します。
.vscode フォルダーおよび `settings.json` がない場合は新規作成してください。

```json
{
  "java.checkstyle.version": "x.x.x",
  "java.checkstyle.configuration": "ディレクトリパスを含むインプットファイル名",
  "java.checkstyle.properties": {
    "config_loc": "ディレクトリパス"
  }
}
```

Checkstyle プラグインのその他の設定項目については、[こちら :material-open-in-new:](https://docs.gradle.org/current/userguide/checkstyle_plugin.html){ target=_blank } を参照してください。

#### SpotBugs プラグイン {#spotbugs-plugin}

SpotBugs プラグインのカスタマイズを行う `build.gradle` の設定方法を解説します。

SpotBugs を利用する際、自動生成されたクラスやメソッドが SpotBugs の警告の対象になることがあります。
このような場合、 SpotBugs ではフィルタファイルを適用することでクラスやメソッド、バグのパターン単位で警告のフィルタリングを設定できます。
SpotBugs のフィルタリングの設定内容については、[こちら :material-open-in-new:](https://spotbugs.readthedocs.io/ja/latest/filter.html){ target=_blank } をご覧ください。

また、 SpotBugs が解析に利用するツール本体のバージョン（`toolVersion`）も明示的に指定してください。
バージョンを指定しない場合、 Gradle が提供するデフォルトバージョンが利用されるため、利用する JDK のバージョン等によっては動作しない場合があります。
バージョンは [こちら :material-open-in-new:](https://mvnrepository.com/artifact/com.github.spotbugs/spotbugs){ target=_blank } を参照してください。

フィルタファイルの適用と `toolVersion` の指定方法は以下の `build.gradle` を参照してください。

```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3 4"
subprojects {
  spotbugs {
    toolVersion = 'x.x.x'
    excludeFilter.set(rootProject.file('フィルタファイルのパス'))
    ignoreFailures = true
  }
}
```

バージョンは [こちら :material-open-in-new:](https://mvnrepository.com/artifact/com.github.spotbugs/spotbugs){ target=_blank } を参照してください。

SpotBugs はデフォルトでコンソール上に警告を出力しますが、詳細を確認しやすくするために、以下のように `spotbugsMain` タスクに対して HTML 形式のレポートを出力する設定を追加できます。

```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3-10"
subprojects {
  spotbugsMain {
    reports {
      // XML 形式のレポートが不要な場合は以下を追加
      xml.required = false
      html {
        required = true
        outputLocation = layout.buildDirectory.file('reports/spotbugs/main.html')
      }
    }
  }
}
```

??? info "Lombok の自動生成コードに対する SpotBugs の警告を抑制する方法"

    Lombok を利用している場合、Lombok が自動生成するコードに対して SpotBugs の警告が出力される場合があります。
    この警告を抑制するには、Lombok の設定ファイル（`lombok.config`）と [SpotBugs Annotations :material-open-in-new:](https://spotbugs.readthedocs.io/ja/latest/annotations.html){ target=_blank } を利用します。
    
    まず `lombok.config` ファイルをプロジェクトのルートディレクトリに配置し、Lombok の自動生成コードに対して SpotBugs の警告を抑制するアノテーションを付与する設定を追加します。
    ```properties title="{ルートプロジェクト}/lombok.config"
    lombok.extern.findbugs.addSuppressFBWarnings = true
    ```
    
    次に、SpotBugs Annotations の依存関係を追加します。
    `build.gradle` の全サブプロジェクトの依存関係に以下を追加してください。
    
    ```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3 4"
    subprojects {
      dependencies {
        compileOnly 'com.github.spotbugs:spotbugs-annotations:x.x.x'
        testCompileOnly 'com.github.spotbugs:spotbugs-annotations:x.x.x'
      }
    }
    ```

    SpotBugs Annotations のバージョンは、[こちら :material-open-in-new:](https://mvnrepository.com/artifact/com.github.spotbugs/spotbugs-annotations){ target=_blank } を参照してください。

??? info "SpotBugs の実行時に SLF4J に関する警告が出力される場合の対処法"

    SpotBugs Gradle Plugin では、 SLF4J の実装ライブラリを指定していない場合に警告が出力されることがあります（[spotbugs-gradle-plugin#136 :material-open-in-new:](https://github.com/spotbugs/spotbugs-gradle-plugin/issues/136){ target=_blank }）。
    この警告を解消するには、 `spotbugsSlf4j` の configuration に SLF4J の実装ライブラリを追加してください。

    ```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3-5"
    subprojects {
      dependencies {
        // SpotBugsの警告対策
        // https://github.com/spotbugs/spotbugs-gradle-plugin/issues/136
        spotbugsSlf4j 'org.slf4j:slf4j-simple'
      }
    }
    ```

SpotBugs プラグインのその他の設定項目については、[こちら :material-open-in-new:](https://spotbugs.readthedocs.io/ja/latest/gradle.html){ target=_blank } を参照してください。

#### JaCoCo プラグイン {#jacoco-plugin}

JaCoCo プラグインのカスタマイズを行う `build.gradle` の設定方法を解説します。

JaCoCo でカバレッジ・レポートから除外したいファイルやクラスがある場合、以下のように指定します。

```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="6-10"
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

JaCoCo がカバレッジ計測に利用するツール本体のバージョン（`toolVersion`）は明示的に指定してください。
バージョンを指定しない場合、 Gradle が提供するデフォルトバージョンが利用されるため、利用する JDK のバージョン等によっては動作しない場合があります。
バージョンは [こちら :material-open-in-new:](https://mvnrepository.com/artifact/org.jacoco/org.jacoco.build){ target=_blank } を参照してください。

```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="3"
subprojects {
  jacoco {
    toolVersion = 'x.x.x'
  }
}
```

また、 test タスクの実行後に JaCoCo のカバレッジ・レポートが自動的に生成されるようにする場合、 test タスクの設定に以下の記述を追加してください。

```groovy title="{ルートプロジェクト}/build.gradle" hl_lines="7"  
subprojects {
  test {
    // UTテスト時はtestプロファイルを利用
    jvmArgs=['-Dspring.profiles.active=test']

    useJUnitPlatform()
    finalizedBy jacocoTestReport
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

      compileJava.options.encoding = 'UTF-8'
      compileTestJava.options.encoding = 'UTF-8'
      javadoc.options.encoding = 'UTF-8'

      dependencies {
        // Lombok の設定
        annotationProcessor 'org.projectlombok:lombok'
        testAnnotationProcessor 'org.projectlombok:lombok'
        compileOnly 'org.projectlombok:lombok'
        testCompileOnly 'org.projectlombok:lombok'

        compileOnly 'com.github.spotbugs:spotbugs-annotations:x.x.x'
        testCompileOnly 'com.github.spotbugs:spotbugs-annotations:x.x.x'

        // SpotBugsの警告対策
        // https://github.com/spotbugs/spotbugs-gradle-plugin/issues/136
        spotbugsSlf4j 'org.slf4j:slf4j-simple'
      }

      test {
        // UTテスト時はtestプロファイルを利用
        jvmArgs=['-Dspring.profiles.active=test']

        useJUnitPlatform()
        finalizedBy jacocoTestReport
      }

      checkstyle {
        toolVersion = 'x.x.x'
      }

      spotbugs {
        toolVersion = 'x.x.x'
        excludeFilter.set(rootProject.file('フィルタファイルのパス'))
        ignoreFailures = true
      }

      spotbugsMain {
        reports {
          // XML 形式のレポートが不要な場合は以下を追加
          xml.required = false
          html {
            required = true
            outputLocation = layout.buildDirectory.file('reports/spotbugs/main.html')
          }
        }
      }

      jacoco {
        toolVersion = 'x.x.x'
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

## 動作環境ごとの設定の切り替え {#environment-settings-switching}

Spring Boot の [プロファイル機能 :material-open-in-new:](https://spring.pleiades.io/spring-boot/reference/features/profiles.html){ target=_blank } を利用すると、開発環境／本番環境／単体テスト実行時など、動作環境ごとに設定を切り替えられます。

### 環境ごとの properties ファイルの分割 {#profile-properties-files}

環境固有の設定は、 `application-{プロファイル名}.properties` という命名規則に従ってプロファイルごとに分割します。
このように分割しておくことで、環境に応じて使用するデータベースの切り替えや出力するログレベルの制御を簡単に行えるようになります。

以下が環境ごとに分割した `application.properties` の例です。

- `application-common.properties`: 全ての環境で共通して使用する設定
- `application-dev.properties`: 開発環境固有の設定
- `application-prd.properties`: 本番環境固有の設定
- `application-ut.properties`: 単体テスト実行時固有の設定

### プロファイルグループによる切り替え単位の定義 {#profile-groups}

各プロファイルは、 `application.properties` の `spring.profiles.group.<グループ名>` プロパティを用いて、起動時に指定する環境名（プロファイルグループ）ごとに読み込む組み合わせをまとめます。

```properties title="application.properties"
# 環境別のプロファイルグループ設定（common:全環境共通、dev:開発環境用、prd:本番環境用、ut:単体テスト用）
spring.profiles.group.local=common,dev
spring.profiles.group.production=common,prd
spring.profiles.group.test=common,ut

# 環境情報未指定の場合に使用するプロファイルグループ
spring.profiles.default=production
```

- `spring.profiles.group.<グループ名>=<プロファイル名1>,<プロファイル名2>,...`

    起動時にグループ名を指定すると、カンマ区切りで列挙したプロファイルに対応する `application-{プロファイル名}.properties` が組み合わせて読み込まれます。

- `spring.profiles.default`

    アプリケーション起動時にプロファイルの指定がない場合に使用するプロファイルグループを指定します。

    既定のプロファイルグループ以外を使用する場合は、起動コマンドに `-Dspring.profiles.active=<プロファイルグループ名>` を追加して、使用するプロファイルグループを明示的に指定します。 [Java プラグイン](#java-plugin) の設定で解説した test タスクの `-Dspring.profiles.active=test` も、この仕組みを利用して `test` グループ（ `common` と `ut` の組み合わせ）を指定しています。

### `application.properties` を配置するサブプロジェクト {#profile-properties-module}

<!-- textlint-disable ja-technical-writing/sentence-length -->
マルチプロジェクト構成を採る場合、 `application.properties` 系のファイルは実行可能なサブプロジェクト（ `#!java @SpringBootApplication` を持つクラスを含むサブプロジェクト）の `src/main/resources` に配置します。
ライブラリとして利用するサブプロジェクトには配置しません。
<!-- textlint-enable ja-technical-writing/sentence-length -->

Spring 公式のマルチモジュールプロジェクトの作成ガイドでも、次のように明記されています。

> 実行時にライブラリを使用するアプリケーションと衝突する可能性があるため、 `application.properties` をライブラリに配置することはお勧めしません（クラスパスから読み込まれる `application.properties` は 1 つだけです）。
>
> — [Creating a Multi Module Project :material-open-in-new:](https://spring.pleiades.io/guides/gs/multi-module/){ target=_blank }（「ライブラリプロジェクトを作成する」の節）
