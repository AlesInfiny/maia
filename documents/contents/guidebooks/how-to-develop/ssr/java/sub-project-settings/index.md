---
title: Java 編 （SSR 編）
description: SSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# サブプロジェクトの個別設定 {#top}

サブプロジェクト単位で個別に設定が必要な内容について解説します。

ここで解説する設定はあくまで推奨設定であり、検討した上で変更することは問題ありません。
また、依存ライブラリについて、プロジェクトで必要なライブラリを適宜追加することも問題ありません。

以降解説するサブプロジェクトとそれぞれの依存関係は図の通りです。

![サブプロジェクトの依存関係](../../../../../images/guidebooks/how-to-develop/ssr/java/subproject-dependencies-light.png#only-light){ loading=lazy }
![サブプロジェクトの依存関係](../../../../../images/guidebooks/how-to-develop/ssr/java/subproject-dependencies-dark.png#only-dark){ loading=lazy }

1. [web プロジェクトの設定](./web-project-settings.md)

    web プロジェクトに対して個別に実施する設定について解説します。

1. [機能モジュールプロジェクトの設定](./infrastructure-project-settings.md)

    機能モジュールプロジェクトに対して個別に実施する設定について解説します。

1. [system-common プロジェクトの設定](./system-common-project-settings.md)

    system-common プロジェクトに対して個別に実施する設定について解説します。

1. [プラグイン、依存ライブラリのバージョン定義一元化](./project-version-control.md)

    プラグイン、依存ライブラリのバージョン定義を一元的に管理する方法について解説します。

1. [メッセージ管理機能の設定](./message-management.md)

    マルチプロジェクト構成におけるメッセージ管理の設定方法について解説します。

1. [集約例外ハンドラーの設定](./exception-handling.md)

    集約例外ハンドラーの設定について解説します。

1. [MyBatis Generator の設定](./mybatis-generator-settings.md)

    機能モジュールプロジェクトに対して MyBatis Generator の設定を追加する方法について解説します。
