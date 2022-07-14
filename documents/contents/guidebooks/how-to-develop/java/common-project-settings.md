# プロジェクトの共通設定
<!-- cSpell:ignore subprojects projectlombok -->

プロジェクト全体の設定として、ルートプロジェクト内で設定すべき内容について解説します。

## マルチプロジェクト構成のための設定 {: #config-multi-project }

Spring Initializr を利用して作成したプロジェクトの雛型は、単一のプロジェクト構成を想定したものであるため、マルチプロジェクトとして動作するようにします。

ルートプロジェクト内に配置したサブプロジェクトをプロジェクトとして取り込むように、ルートプロジェクト直下の `setting.gradle` を修正します。

```groovy title="setting.gradle"
rootProject.name = 'Dressca-backend'
include 'application-core', 'system-common', 'infrastructure', 'web', 'batch'
```

`rootProject.name` にルートプロジェクトの名前を設定し、 `include` にサブプロジェクトの名前を列挙します。

## ビルドスクリプトの共通化 {: #common-build-script }

ビルドをする上で、各サブプロジェクト共通の設定は、ルートプロジェクトの `build.gradle` 内の `subprojects` ブロックに定義します。
ここに定義された内容は、全てのサブプロジェクトで定義したのと同等の扱いになります。

設定内容はそれぞれのプロジェクトによりますが、一般的な設定項目について以降で解説します。

### プラグインの導入 {: #common-plugin }

各サブプロジェクト共通で利用する Gradle のプラグインを定義します。

Java プロジェクトをビルドする基本的なプラグインである Java プラグイン、 Checkstyle や SpotBugs などの静的テストツール用のプラグイン、 JaCoCo などのカバレッジ取得ツール用プラグイン等が定義する候補になります。

```groovy title="build.gradle"
subprojects {
  apply plugin: 'java'
  apply plugin: 'jacoco'
  apply plugin: 'checkstyle'
  apply plugin: 'com.github.spotbugs'
}
```

### 依存ライブラリの設定 {: #common-dependencies }
  
サブプロジェクト毎の役割に関わらず、システム全体で利用され得るライブラリについては、共通の依存ライブラリとして定義します。
例えば、ボイラープレートコードを削減するためのライブラリである Lombok や、各種ユーティリティ用のライブラリなどが共通の依存ライブラリとして定義する候補になります。

Spring Initializr でルートプロジェクトの雛型を作成した際に、共通の依存ライブラリを設定した場合には、既に `dependencies` ブロックが記述されています。
まずはこの `dependencies` ブロックを `subprojects` ブロック内に移動させます。
雛型作成時に未定義の場合は、新しく `dependencies` ブロックを追加してください。
その後、 `dependencies` ブロックに必要な依存ライブラリを追加します。

```groovy  title="build.gradle"
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

### タスクの設定 {: #common-tasks }

導入したプラグインによって定義されたタスクに対して、設定が必要であれば設定を追加します。
設定項目や設定の要否はプラグインによります。
例えば、各種ツール類のバージョン指定や、静的テストツールのルールのようなインプットファイルの指定、レポート等の出力設定などが一般的には考えられます。

Maia OSS 版として推奨する各プラグインの設定については、以下を参照してください。

- [Java プラグイン](https://docs.gradle.org/current/userguide/java_plugin.html)
- [Checkstyle プラグイン](https://docs.gradle.org/current/userguide/checkstyle_plugin.html)
- [SpotBugs プラグイン](https://spotbugs.readthedocs.io/ja/latest/gradle.html)
- [JaCoCo プラグイン](https://docs.gradle.org/current/userguide/jacoco_plugin.html)

また、必要であれば独自のタスクを定義することも可能です。
