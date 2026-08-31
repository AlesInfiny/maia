---
title: Java 編 （SSR 編）
description: SSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

<!-- cSpell:ignore configfile taskdef -->

# MyBatis Generator の設定 {#top}

MyBatis Generator の設定は CSR 編と同様です。

[こちら](../../../csr/java/sub-project-settings/mybatis-generator-settings.md) を参照してください。

MyBatis Generator の設定ファイルの配置および Gradle タスクの定義は application-modules プロジェクトではなく、機能モジュールのプロジェクトに配置してください。
また、 SSR 編ではコンテキスト単位でパッケージを分割しないため、出力先のパッケージは機能モジュールのフォルダー構成に合わせて指定してください。
