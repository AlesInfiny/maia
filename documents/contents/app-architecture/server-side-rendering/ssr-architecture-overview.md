---
title: SSR 編
description: サーバーサイドレンダリングを行う Web アプリケーションの アーキテクチャについて解説します。
---

# SSR アーキテクチャ概要 {#top}

## 技術スタック {#tech-stack}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia ） の SSR アプリケーションの構成を以下に示します。

![技術スタック](../../images/app-architecture/server-side-rendering/tech-stack-light.png#only-light){ loading=lazy }
![技術スタック](../../images/app-architecture/server-side-rendering/tech-stack-dark.png#only-dark){ loading=lazy }

??? note "利用ライブラリ（フロントエンド）"

    - [Bootstrap :material-open-in-new:](https://getbootstrap.com/){ target=_blank }

        レスポンシブデザインに対応した CSS フレームワークです。  
        グリッドレイアウト、フォーム、ボタン、ナビゲーションなどの UI コンポーネントが豊富に用意されており、統一感のあるデザインと効率的な UI 開発を可能にします。

    - [Dart Sass :material-open-in-new:](https://sass-lang.com/dart-sass/){ target=_blank }

        Sass（Syntactically Awesome Style Sheets）の公式実装であり、変数、ネスト、ミックスインなどを使って CSS をより効率的・構造的に記述できます。  

??? note "利用ライブラリ（バックエンド）"

    - [Spring Core :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/core.html){ target=_blank }
    
        DI コンテナや AOP の機能を提供する Spring Framework のコアライブラリです。
        ベースとなる Spring Framework のコアライブラリや、 Spring Framework の自動設定サポートを含む Spring Boot の機能を提供します。

    - [Spring Boot :material-open-in-new:](https://spring.pleiades.io/projects/spring-boot){ target=_blank }
    
        Spring Framework をベースとするアプリケーション開発を効率的に行うためのフレームワークです。
        Spring Framework の課題である煩雑な Bean 定義や設定ファイルを可能な限り自動設定したり、実装するコード量を軽減するアノテーションを提供します。

    - [Spring MVC :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/web/webmvc.html){ target=_blank }

        Spring MVC は Spring Framework をベースとする Front Controller パターンの Web MVC フレームワークです。

    - [Spring Validation :material-open-in-new:](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html){ target=_blank }

        Bean に対するデータの値チェック機能を提供するライブラリです。
        アノテーションベースで汎用的に利用できる値チェックが提供され、入力値チェック等が簡潔に実現できます。

    - [Spring Test :material-open-in-new:](https://spring.pleiades.io/spring-framework/reference/testing.html){ target=_blank }

        Spring Framework をベースとするアプリケーション実装をテストするためのライブラリです。
        Unit Jupiter 、 Hamcrest 、 Mockito などのライブラリと連携して、テスト実装をサポートする機能を提供します。
    
    - [Thymeleaf :material-open-in-new:](https://www.thymeleaf.org/){ target=_blank }

        Thymeleaf は Java 向けのサーバーサイドテンプレートエンジンです。
        Spring MVC と連携して利用することで、 Controller が用意したモデルデータを画面に表示するビューの実装をシンプルに記述できます。
    
    - [Thymeleaf Layout Dialect :material-open-in-new:](https://ultraq.github.io/thymeleaf-layout-dialect/){ target=_blank }

        Thymeleaf Layout Dialect は、テンプレートの共通レイアウト（ヘッダー、フッター、メニューなど）を効率的に管理するための拡張機能です。
        ベースとなるレイアウト HTML を定義し、各ページがそのレイアウトを継承する形でコンテンツを差し替えられるため、コードの重複を避け、統一感のある画面構造を実現できます。   

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

AlesInfiny Maia における SSR アプリケーションアーキテクチャの全体概要を以下に示します。

![アプリケーションアーキテクチャ](../../images/app-architecture/server-side-rendering/ssr-architecture-light.png#only-light){ loading=lazy }
![アプリケーションアーキテクチャ](../../images/app-architecture/server-side-rendering/ssr-architecture-dark.png#only-dark){ loading=lazy }

## アーキテクチャの構造詳細 {#backend-structure}

サーバーサイドレンダリング方式の Web アプリケーションにおける、各層とそれを構成するコンポーネントの役割について、それぞれ説明します。

!!! warning "SSR アプリケーションのアーキテクチャについて"

    本ドキュメントおよび SSR サンプルアプリケーションでは、トランザクションスクリプトを採用しています。
    しかし AlesInfiny Maia は、アプリケーションのアーキテクチャをトランザクションスクリプトに限定するものではありません。
    アプリケーションのアーキテクチャ選定方針は [こちら](../overview/java-application-architecture-selection-guideline.md) を参照してください。

### プレゼンテーション層 {#presentation}

プレゼンテーション層は、ユーザーインターフェースを提供する層です。
ユーザーからの入力を受け、アプリケーションコア層に処理を依頼します。
アプリケーションコア層からの処理結果に応じて、ユーザーに情報を表示します。

#### コントローラー {#Controller}

コントローラーは HTTP リクエストを受け取り、アプリケーションコア層に必要な処理を指示し、描画に必要なデータを準備してビューへ引き渡す役割を担います。
Spring MVC のコントローラーに相当し、以下を行います。

- HTTP リクエストの受け取りとルーティング
- セッションからの情報取得
- ビューモデルだけで実行できない入力チェックの実行
- アプリケーションコア層のアプリケーションサービスの呼び出し
- アプリケーションサービスの処理結果に応じたビューモデルの構築
- 業務エラーをハンドリングしビューモデルに設定
- ビューモデルオブジェクトのビューへの受け渡し
- PRG(Post/Redirect/Get) パターンによるリダイレクトの制御

#### ビュー {#view}

ビューは、ユーザーに情報を表示する役割を担います。ビューには UI の構造やスタイルを定義します。
具体的には Thymeleaf を用いてビューモデルを HTML に変換する処理を担います。
ビューでは以下を行います。

- ビューモデルのオブジェクトを HTML に表示
- コントローラーのメソッドを呼び出せるような入力フォームの構築
- HTML の要素に対して CSS を適用
- クライアント側で完結する処理は JavaScript で処理

#### ビューモデル {#view-model}

ブラウザーからのイベントを受け、プレゼンテーションロジックを実行します。
ビューモデルでは以下を行います。

- ビューに渡すデータの保持
- ビューに表示する形式へのデータ変換・整形
- HTTP リクエストの保持
- 入力チェックのための Bean Validation のアノテーション付与

ビューとビューモデルの間のデータのバインドには、 Thymeleaf で提供される [`th:object` や `th:field` :material-open-in-new:](https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.html){ target=_blank } のタグ属性を利用します。

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

#### リポジトリ {#repository}

インフラストラクチャ層のリポジトリは、アプリケーションコア層のリポジトリインターフェースの実装クラスで、具体的なデータベースアクセス処理を実装します。
データベースとはテーブルエンティティの型を利用してデータをやり取りします。
それに対してリポジトリインターフェースはエンティティや値オブジェクトの型を用いて構成されています。
インフラストラクチャ層のリポジトリは、これらの型同士の変換処理を行う役割も持ちます。

#### テーブルエンティティ {#table-entity}

テーブルエンティティはデータベースのテーブルに対応するデータ構造を表現するクラスです。
1 つのテーブルエンティティオブジェクトがテーブルの 1 レコードに対応します。
