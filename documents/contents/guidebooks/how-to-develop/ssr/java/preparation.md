---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# 事前準備 {#top}

## ローカル開発環境の構築 {#create-dev-environment}

ローカル開発環境の構築について [ローカル開発環境の構築](../local-environment/index.md) を参照し、最低限必要なソフトウェアをインストールしてください。

## Visual Studio Code の拡張機能インストール {#install-extensions}

Visual Studio Code を利用する場合、 Java アプリケーションを開発するために以下の拡張機能をインストールします。

- [Spring Boot Extension Pack :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack){ target=_blank }

    Spring Boot アプリケーションの開発とデプロイを提供します。

- [Extension Pack for Java :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack){ target=_blank }

    Java アプリケーションの作成、テスト、デバッグ等の基本的な機能を提供します。

    <!-- textlint-disable ja-technical-writing/sentence-length -->

    アプリケーションの起動で利用する [Gradle for Java :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-gradle){ target=_blank } やコード補完やエラーレポートを提供する [Language Support for Java(TM) by Red Hat :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=redhat.java){ target=_blank } といったの拡張機能が追加でインストールされます。

    <!-- textlint-enable ja-technical-writing/sentence-length -->

- [Lombok Annotations Support for VS Code :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-lombok){ target=_blank }

    Java アプリケーションにおける Lombok アノテーションのサポートを提供します。
