---
title: Java 編
description: バックエンドで動作する Java アプリケーションの 開発手順を解説します。
---

# サブプロジェクトの個別設定 {#top}

サブプロジェクト単位で個別に設定が必要な内容について解説します。

ここで解説する設定はあくまで推奨設定であり、検討した上で変更することは問題ありません。
また、依存ライブラリについて、プロジェクトで必要なライブラリを適宜追加することも問題ありません。

以降解説するサブプロジェクトとそれぞれの依存関係は図の通りです。

![サブプロジェクトの依存関係](../../../../images/guidebooks/how-to-develop/java/subproject-dependencies-light.png#only-light){ loading=lazy }
![サブプロジェクトの依存関係](../../../../images/guidebooks/how-to-develop/java/subproject-dependencies-dark.png#only-dark){ loading=lazy }

1. [web プロジェクトの設定](./web-project-settings.md)

    web プロジェクトに対して個別に実施する設定について解説します。

1. [infrastructure プロジェクトの設定](./infrastructure-project-settings.md)

    infrastructure プロジェクトに対して個別に実施する設定について解説します。

1. [application-core プロジェクトの設定](./application-core-project-settings.md)

    application-core プロジェクトに対して個別に実施する設定について解説します。

1. [batch プロジェクトの設定](./batch-project-settings.md)

    batch プロジェクトに対して個別に実施する設定について解説します。

1. [system-common プロジェクトの設定](./system-common-project-settings.md)

    system-common プロジェクトに対して個別に実施する設定について解説します。

1. [プラグイン、依存ライブラリのバージョン定義一元化](./project-version-control.md)

    プラグイン、依存ライブラリのバージョン定義を一元的に管理する方法について解説します。