---
title: SSR 編
description: サーバーサイドレンダリングを行う Web アプリケーションの アーキテクチャについて解説します。
---

# SSR アーキテクチャ概要 {#top}

## 技術スタック {#tech-stack}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia ） の SSR アプリケーションの構成を以下に示します。

![技術スタック](../../images/app-architecture/server-side-rendering/tech-stack-light.png#only-light){ loading=lazy }
![技術スタック](../../images/app-architecture/server-side-rendering/tech-stack-dark.png#only-dark){ loading=lazy }

??? note ""

??? note "利用ライブラリ（バックエンド側）"

    - [Spring Core :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/core.html){ target=_blank }
    
        DI コンテナや AOP の機能を提供する Spring Framework のコアライブラリです。
        ベースとなる Spring Framework のコアライブラリや、 Spring Framework の自動設定サポートを含む Spring Boot の機能を提供します。

    - [Spring Boot :material-open-in-new:](https://spring.pleiades.io/projects/spring-boot){ target=_blank }
    
        Spring Framework をベースとするアプリケーション開発を効率的に行うためのフレームワークです。
        Spring Framework の課題である煩雑な Bean 定義や設定ファイルを可能な限り自動設定したり、実装するコード量を軽減するアノテーションを提供します。

    - [Spring MVC :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/web/webmvc.html){ target=_blank }

        Spring MVC は Spring Framework をベースとする Front Controller パターンの Web MVC フレームワークです。

    - Spring Validation

        Bean に対するデータの値チェック機能を提供するライブラリです。
        アノテーションベースで汎用的に利用できる値チェックが提供され、入力値チェック等が簡潔に実現できます。

    - [Spring Test :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/testing.html){ target=_blank }

        Spring Framework をベースとするアプリケーション実装をテストするためのライブラリです。
        Unit Jupiter 、 Hamcrest 、 Mockito などのライブラリと連携して、テスト実装をサポートする機能を提供します。
    
    - [Thymeleaf :material-open-in-new:](https://www.thymeleaf.org/){ target=_blank }

        Thymeleaf は Java 向けのサーバーサイドテンプレートエンジンです。
        Spring MVC と連携して利用することで、 Controller が用意したモデルデータを画面に表示するビューの実装をシンプルに記述できます。

    - [Apache Log4j 2 :material-open-in-new:](https://logging.apache.org/log4j/2.x/index.html){ target=_blank }

        Apache Log4j 2 は Java のロギングフレームワークです。
        複数のロガーに対して、フィルター、ローテーション、ログレベルなどの細やかな管理ができます。

    - [MyBatis :material-open-in-new:](https://mybatis.org/mybatis-3/ja/index.html){ target=_blank }
    
        MyBatis はデータベースアクセスの実装に利用する O/R Mapper です。
        XML ファイルまたはアノテーションに、 SQL やレコードとオブジェクトのマッピングを定義できます。

    - [H2 Database :material-open-in-new:](https://www.h2database.com/html/main.html){ target=_blank }
    
        H2 Database は Java 上で動作するリレーショナルデータベースです。
        単体テストやローカル環境でのアプリケーション実行など、ローカル環境でデータベースアクセスを含む動的テストを行うのに利用します。

## アプリケーションアーキテクチャ {#application-architecture}

AlesInfiny Maris における SSR アプリケーションアーキテクチャの全体概要を以下に示します。

![アプリケーションアーキテクチャ](../../images/app-architecture/server-side-rendering/ssr-architecture-light.png#only-light){ loading=lazy }
![アプリケーションアーキテクチャ](../../images/app-architecture/server-side-rendering/ssr-architecture-dark.png#only-dark){ loading=lazy }

## アーキテクチャの構造詳細 {#backend-structure}

サーバーサイドレンダリング方式の Web アプリケーションにおける、各層とそれを構成するコンポーネントの役割について、それぞれ説明します。

!!! warning "SSR アプリケーションのアーキテクチャについて"

    本ドキュメントおよび SSR サンプルアプリケーションでは、トランザクションスクリプトを採用しています。
    しかし AlesInfiny Maris は、アプリケーションのアーキテクチャをトランザクションスクリプトに限定するものではありません。
    アプリケーションのアーキテクチャ選定方針は [こちら](../overview/dotnet-application-architecture-selection-guideline.md) を参照してください。

### プレゼンテーション層 {#presentation}

プレゼンテーション層は、ユーザーインターフェースを提供する層です。
ユーザーからの入力を受け、アプリケーションコア層に処理を依頼します。
アプリケーションコア層からの処理結果に応じて、ユーザーに情報を表示します。

#### ビュー {#view}

ビューは、ユーザーに情報を表示する役割を担います。ビューには UI の構造やスタイルを定義します。
Razor コンポーネントの HTML 部分がビューに相当します。
ビューでは以下を行います。

- プレゼンテーションロジックの結果を HTML に表示する
- 入力チェックやアプリケーションサービスの呼び出し等、プレゼンテーションロジックが実行できるように入力フォームを構築する
- HTML の要素に対して、 CSS を適用する

#### ビューモデル {#view-model}

ブラウザーからのイベントを受け、プレゼンテーションロジックを実行します。
ビューモデルでは以下を行います。

- レンダリングに必要な処理
- 入力チェック（項目間チェック）
- アプリケーションサービスの呼び出しを通じたデータの取得・更新
- 業務エラーをハンドリングして、エラーメッセージをビューに設定する

ビューとビューモデルの間のデータのやり取りには、バインド変数またはバインドプロパティを使用します。

### アプリケーションコア層 {#application-core}

アプリケーションコア層は、ビジネスロジックを実装する層です。
トランザクション管理を含む、すべてのビジネスロジックをここに実装します。

#### アプリケーションサービス {#application-service}

アプリケーションサービスは、アプリケーションコア層の中で、ユースケースレベルのビジネスロジックを実装する役割を担います。
アプリケーションサービスでは以下を行います。

- データベーストランザクションの管理
- ビジネスロジック(計算処理、繰り返し、条件分岐など)の実行
- リポジトリインターフェースを介したデータアクセス

アプリケーションサービスの引数や戻り値では、複雑度に応じて以下を使用します（下に行くほど複雑になります）。

- 引数・戻り値
- 複数の引数・複数戻り値
- `record`
- 引数クラス・戻り値クラス

ビジネスロジックは手続き型で実装します。
他のアプリケーションサービスとビジネスロジックを共有しません。

#### リポジトリインターフェース {#repository-interface}

リポジトリインターフェースは、アプリケーションコア層の中で、データアクセスを抽象化する役割を担います。
リポジトリインターフェースは、アプリケーションサービスから利用されます。
これにより、アプリケーションサービスのテスト容易性を確保します。

実装クラスであるリポジトリは、インフラストラクチャ層に実装します。
アプリケーションサービスには、 DI コンテナーからリポジトリを注入します。

### インフラストラクチャ層 {#infrastructure}

インフラストラクチャ層は、データベースや外部システムとの接続を管理する層です。
データベースへの接続、外部システムと通信を担当します。
原則としてインフラストラクチャ層にビジネスロジックを実装しないようにします。

インフラストラクチャ層では、 Entity Framework Core （以降、 EF Core と記載）を使用します。
EF Core のツールを使用して、作成したテーブルエンティティのコードからデータベースを生成します (Code First) 。

#### リポジトリ {#repository}

リポジトリは、インフラストラクチャ層の中で、データアクセスを実装する役割を担います。
リポジトリは、リポジトリインターフェースを実装します。
リポジトリでは以下を行います。

- データベースへの接続
- データベースからのデータの取得
- データベースへのデータの登録・更新・削除

#### テーブルエンティティ {#table-entity}

テーブルエンティティはデータベースのテーブルに対応するデータ構造を表現するクラスです。
1 つのテーブルエンティティオブジェクトがテーブルの 1 レコードに対応します。

原則として、 1 つの C# コードファイル（ `*.cs` ）に 1 つのエンティティクラスを定義します。
