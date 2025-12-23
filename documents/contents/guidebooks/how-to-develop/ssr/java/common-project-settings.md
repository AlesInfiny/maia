---
title: Java 編 （SSR 編）
description: SSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
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
include 'aaa', 'bbb', 'web', 'system-common' // サブプロジェクトの名前
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

ビルドスクリプトの共通化は CSR 編と同様です。

[こちら](../../csr/java/common-project-settings.md#common-build-script) を参照してください。
