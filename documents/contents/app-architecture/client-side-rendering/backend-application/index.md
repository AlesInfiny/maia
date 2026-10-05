---
title: CSR 編 - Web API
description: バックエンドアプリケーションのアーキテクチャについて、 層ごとに詳細を説明します。
---

# バックエンドアプリケーションのアーキテクチャ {#top}

## 技術スタック {#technology-stack}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia ）を構成する OSS を以下に示します。

![OSS 構成要素](../../../images/app-architecture/client-side-rendering/backend-oss-components-light.png#only-light){ loading=lazy }
![OSS 構成要素](../../../images/app-architecture/client-side-rendering/backend-oss-components-dark.png#only-dark){ loading=lazy }

利用ライブラリの一覧については、 [技術スタック](../csr-architecture-overview.md#technology-stack) を参照してください。

## アーキテクチャ {#backend-architecture}

### モジュラーモノリスアーキテクチャ {#modular-monolith}

AlesInfiny Maia のアプリケーションアーキテクチャは、境界付けられたコンテキストの単位でアプリケーションを分割するモジュラーモノリスアーキテクチャを採用しています。
Spring Modulith では、このような分割単位を [アプリケーションモジュール :material-open-in-new:](https://spring.pleiades.io/spring-modulith/reference/fundamentals.html){ target=_blank } と呼びます。
各アプリケーションモジュールの内部には、コンテキストごとにアプリケーションコア層とインフラストラクチャ層を配置し、クリーンアーキテクチャの考え方に基づいて構成します。
プレゼンテーション層はアプリケーションモジュールの外側に、独立したサブプロジェクトとして配置します。
アーキテクチャの全体概要は以下の通りです。

![バックエンドのアーキテクチャ概要図](../../../images/app-architecture/client-side-rendering/csr-backend-architecture-light.png#only-light){ loading=lazy }
![バックエンドのアーキテクチャ概要図](../../../images/app-architecture/client-side-rendering/csr-backend-architecture-dark.png#only-dark){ loading=lazy }

### アプリケーションモジュールの概要 {#application-module-overview}

アプリケーションモジュールとは、境界付けられたコンテキストの単位でアプリケーションをパッケージ分割した際の、それぞれの区画を指します。

アプリケーションモジュールは原則として互いに直接依存しない構成とすることで、モジュール間を疎結合に保ち、変更の影響範囲を局所化します。
あるモジュールが他のモジュールの機能を必要とする場合であっても、依存を許可するモジュールは必要最小限にとどめてください。

また、アプリケーションモジュールには、モジュールの外部（他のモジュールやプレゼンテーション層）から参照できる公開範囲と、モジュール内部でのみ利用する非公開範囲が存在します。
実装する際は、この公開範囲と非公開範囲を意識し、適切に配置してください。

#### 公開範囲・非公開範囲の検証 {#visibility-verification}

公開範囲・非公開範囲の設定とモジュール間の依存関係が設計どおりであることを確認するため、 Spring Modulith を用いてモジュール構造を検証します。
検証の具体的な方法については、[モジュール間の依存関係を検証するテストの追加](../../../guidebooks/how-to-develop/csr/java/sub-project-settings/application-modules-project-settings.md#add-modularity-test) を参照してください。

### 各層の詳細 {#layer-details}

1. [プレゼンテーション層](presentation.md)

1. [アプリケーションコア層](application-core.md)

1. [インフラストラクチャ層](infrastructure.md)

## プロジェクト構成 {#project-module-mapping}

### サブプロジェクトの構成 {#subproject-structure}

AlesInfiny Maia では Java のプロジェクト構成として、複数のサブプロジェクトに分割し、それらをルートプロジェクトでまとめて管理するマルチプロジェクト構成を採用します。
サブプロジェクトの分割については、以下のように構成することを推奨します。

- 各アプリケーションモジュールをまとめて配置するサブプロジェクト

    境界付けられたコンテキストの単位に分割したアプリケーションモジュール群を配置します。
    各アプリケーションモジュールの内部に、アプリケーションコア層・インフラストラクチャ層に相当する構成要素を配置します。

- プレゼンテーション層を担うサブプロジェクト

    Web アプリケーション、バッチアプリケーションなど、実行形態が異なるものごとに、それぞれ独立したサブプロジェクトとして構成します。
    共通のアプリケーションモジュールを参照して業務ロジックを再利用しつつ、実行形態に応じた入出力の実装を個別に持つ構成となります。

- システム共通機能を配置するサブプロジェクト

    どのサブプロジェクトからも利用される、業務に依存しない共通機能を配置します。
    業務機能とシステム共通機能を分割することで、プロジェクトの役割や依存関係が明確になり、保守性が高まると考えられます。

プレゼンテーション層のサブプロジェクトから参照できるのは、各アプリケーションモジュールの公開範囲に配置されたコンポーネントだけです。
非公開範囲に配置されたコンポーネントには直接依存しないでください。

一方でアプリケーションモジュール同士は、原則として互いに直接依存しません。

サブプロジェクトの構成、およびアプリケーションモジュールの構成の一例は、以下の通りです。

![フォルダ構成図](../../../images/app-architecture/client-side-rendering/csr-project-structure-light.png#only-light){ loading=lazy }
![フォルダ構成図](../../../images/app-architecture/client-side-rendering/csr-project-structure-dark.png#only-dark){ loading=lazy }

プロジェクト構造全体は、 Spring Initializr で生成した Gradle Groovy DSL プロジェクトの構造と変わりはありません。

### パッケージ構成 {#package-structure}

<!-- textlint-disable @textlint-ja/no-synonyms -->
パッケージの構成としては、システムで 1 つのフォルダー ( aaa.bbb.ccc ) をベースにパッケージを作成します。
アプリケーションモジュールは、コンテキストごとに 1 つのパッケージとして作成します。
<!-- textlint-enable @textlint-ja/no-synonyms -->
各アプリケーションモジュールの内部については、アプリケーションコア層に相当する構成要素をドメインの単位でパッケージ化します。
このとき、公開範囲に配置する型はコンテキストのパッケージ直下または `internal` 以外のサブパッケージに配置し、非公開範囲に配置する型は `internal` パッケージの配下に配置します。
以降の階層については、管理や機能面を考慮し、必要に応じてサブパッケージを作成してください。
