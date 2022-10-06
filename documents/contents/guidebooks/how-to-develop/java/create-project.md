---
title: 雛形の作成
description: バックエンドアプリケーションの雛形の作成方法について解説します。
---

# プロジェクトの雛型作成 {#top}

## Spring Initializr の利用 {#use-spring-initializr}

各 Gradle プロジェクトの雛型は、 Spring Initializr を利用して作成します。
Spring Initializr は Spring Boot を利用するプロジェクトの雛型を簡潔に作成できるツールです。
[Web サービス](https://start.spring.io/)を利用する方法や、各種 IDE にプラグインや拡張機能を導入することで IDE 上でも利用できます。
VS Code の場合、 [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack) が導入済みであれば利用できます。

ここでは Web サービスでの作成を前提に説明します。

## ルートプロジェクトの作成 {#create-root-project}

ルートプロジェクトの雛型を Spring Initializr を利用して作成します。
Web 画面上の各種設定項目は以下の通りです。

- Project

    プロジェクトのビルドツールを Gradle と Apache Maven から選択します。
    Maia OSS 版では Gradle をビルドツールとして選択することをデフォルトとしており、以降の説明でも Gradle でビルドすることを前提として解説していますが、 Apache Maven も選択できます。

- Language

    開発言語として Java を選択します。

- Spring Boot
  
    利用する Spring Boot のバージョンを選択します。
    ベースとなる JDK や Spring Framework のバージョンを考慮して選択します。
    特に考慮点がなければ最新の GA 版を選択することを推奨します。
    選択できる Spring Boot のバージョンは、 [OSS サポート期限内のバージョン](https://spring.io/projects/spring-boot#support)に限られ、古いバージョンは指定できません。

- Project Meta Data：Artifact

    プロジェクトの基点のフォルダ名となるプロジェクト名を設定します。

- Project Meta Data：その他

    ルートプロジェクトの雛型作成では設定不要 ( 任意 ) です。

- Dependencies

    各サブプロジェクト全体で利用するパッケージがあれば登録します。
    後続手順の[プロジェクトの共通設定](./common-project-settings.md)にて、後から追加できます。
    登録すべき依存ライブラリについては、[プロジェクトの共通設定](./common-project-settings.md)を参照してください。

## サブプロジェクトの作成 {#create-sub-projects}

ルートプロジェクトと同様に、サブプロジェクトの雛型を Spring Initializr を利用して作成します。
以下、サブプロジェクト毎に異なる設定項目について説明します。
その他の項目はルートプロジェクトと同様に設定してください。

- Project Meta Data：Group

    対象プロジェクトが含まれるグループ名を設定します。
    対象プロジェクトのパッケージ名は、このグループ名から始まることが想定されます。
    ハイフンなど、パッケージ名に利用不可の文字を指定した場合、パッケージ名としては取り除かれますが、元々使わないことが望ましいです。

- Project Meta Data：Artifact

    サブプロジェクトの基点のフォルダ名となるプロジェクト名を設定します。
    また、グループ名に続くパッケージ名としても利用されますので、グループ名と同様、パッケージ名の命名規則と合致する名前にすることが望ましいです。

- Project Meta Data：Name

    アプリケーション名を設定します。
    この名前は Spring Boot 実行クラス ( Main クラス ) のクラス名やパッケージした際の war や jar 名に利用されます。

- Dependencies

    プロジェクトで利用するパッケージがあれば設定します。
    後続手順の[サブプロジェクトの個別設定](./sub-project-settings.md)にて、後から追加できます。
    各プロジェクトの依存ライブラリについては、[サブプロジェクトの個別設定](./sub-project-settings.md)を参照してください。

## ルートプロジェクトとサブプロジェクトの配置 {#put-projects}

ルートプロジェクトと各サブプロジェクトの雛型を作成したら、
これらを展開し適切に配置します。

各雛型は zip 形式で生成されるため、まずは全ての雛型を展開します。
その後、ルートプロジェクトのフォルダ直下に各サブプロジェクトのフォルダを配置します。
