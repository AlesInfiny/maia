---
title: Java 編 （CSR 編）
description: CSR アプリケーションの サーバーサイドで動作する Java アプリケーションの 開発手順を解説します。
---

# プロジェクトの雛型作成 {#top}

## Spring Initializr の利用 {#use-spring-initializr}
<!-- cSpell:ignore applicationcore -->

各 Gradle Groovy DSL プロジェクトの雛型は、 Spring Initializr を利用して作成します。
Spring Initializr は Spring Boot を利用するプロジェクトの雛型を簡潔に作成できるツールです。
[Web サービス :material-open-in-new:](https://start.spring.io/){ target=_blank } を利用する方法や、各種 IDE にプラグインや拡張機能を導入することで IDE 上でも利用できます。
Visual Studio Code の場合、 [Spring Boot Extension Pack :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack){ target=_blank } が導入済みであれば利用できます。

ここでは Web サービスでの作成を前提に説明します。

## ルートプロジェクトの作成 {#create-root-project}

ルートプロジェクトの作成手順は CSR 編と同様です。

[CSR編 > Java編 > プロジェクトの雛形作成 > ルートプロジェクトの作成](../../csr/java/create-project.md#create-root-project) を参照して、ルートプロジェクトを作成してください。

## サブプロジェクトの作成 {#create-sub-projects}

ルートプロジェクトと同様に、サブプロジェクトの雛型を Spring Initializr を利用して作成します。
AlesInfiny Maia OSS Edition では、アプリケーションの機能、プレゼンテーション層およびシステム共通機能を 1 つのサブプロジェクトとして対応させることを推奨します。

![推奨するサブプロジェクト](../../../../images/guidebooks/how-to-develop/ssr/java/recommended-subproject-light.png#only-light){ loading=lazy }
![推奨するサブプロジェクト](../../../../images/guidebooks/how-to-develop/ssr/java/recommended-subproject-dark.png#only-dark){ loading=lazy }

以下、サブプロジェクト毎に異なる設定項目について説明します。
その他の項目はルートプロジェクトと同様に設定してください。

- Project Metadata：Group

    対象プロジェクトが含まれるグループ名を設定します。
    対象プロジェクトのパッケージ名は、このグループ名から始まることが想定されます。
    ハイフンなど、パッケージ名に利用不可の文字を指定した場合、パッケージ名としては取り除かれますが、元々使わないことが望ましいです。

- Project Metadata：Artifact

    サブプロジェクトの基点のフォルダー名となるプロジェクト名を設定します。

- Project Metadata：Name

    アプリケーション名を設定します。
    この名前は Spring Boot 実行クラス ( Main クラス ) のクラス名やパッケージした際の war や jar 名に利用されます。

- Project Metadata：Package Name

    パッケージ名を設定します。
    通常変更の必要はありませんが、 Artifact 名にハイフンを利用している場合は、自動入力されているパッケージ名からハイフンを削除してください。例えば、 com.example.system-common というパッケージ名であれば、 com.example.systemcommon のように修正してください。

- Dependencies

    プロジェクトで利用するパッケージがあれば設定します。
    本ガイドでは後続手順の [サブプロジェクトの個別設定](./sub-project-settings/index.md) にて追加するため設定不要 ( 任意 ) です。

## ルートプロジェクトとサブプロジェクトの配置 {#put-projects}

ルートプロジェクトと各サブプロジェクトの雛型を作成したら、
これらを展開し適切に配置します。

各雛型は zip 形式で生成されるため、まずは全ての雛型を展開します。
次にルートプロジェクト内の src フォルダーを削除します。
その後、ルートプロジェクトのフォルダー直下に各サブプロジェクトのフォルダーを配置します。
各機能のサブプロジェクト内に、アプリケーションコア層およびインフラストラクチャ層のフォルダーを作成します。
これらの手順を行った後のフォルダーの構成は以下の通りです。

![SpringBootプロジェクトのフォルダー構成](../../../../images/guidebooks/how-to-develop/ssr/java/springboot-project-structure-light.png#only-light){ loading=lazy }
![SpringBootプロジェクトのフォルダー構成](../../../../images/guidebooks/how-to-develop/ssr/java/springboot-project-structure-dark.png#only-dark){ loading=lazy }
