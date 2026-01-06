---
title: CSR 編
description: クライアントサイドレンダリングを行う Web アプリケーションの アーキテクチャについて解説します。
---

# CSR アーキテクチャ概要 {#top}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）において、クライアントサイドレンダリング方式の Web アプリケーションを構築する際に想定しているアーキテクチャの概要について説明します。

## 技術スタック {#technology-stack}

本アーキテクチャを構成する主なライブラリを以下に示します。

![構成ライブラリ一覧](../../images/app-architecture/client-side-rendering/csr-library-light.png#only-light){ loading=lazy }
![構成ライブラリ一覧](../../images/app-architecture/client-side-rendering/csr-library-dark.png#only-dark){ loading=lazy }

!!! note ""

    上の図で使用している OSS 製品名およびロゴのクレジット情報は [こちら](../../about-maia/credits.md) を参照してください。

??? note "利用ライブラリ（フロントエンド）"

    - [TypeScript :material-open-in-new:](https://www.typescriptlang.org/){ target=_blank }

          JavaScript を拡張して静的型付にしたプログラミング言語です。
      
    - [Vue.js :material-open-in-new:](https://ja.vuejs.org/){ target=_blank }

          シンプルな設計で拡張性の高い JavaScript のフレームワークです。
      
    - [Vite :material-open-in-new:](https://ja.vite.dev/){ target=_blank }

          ES modules を利用してプロジェクトの高速な起動・更新を実現するフロントエンドビルドツールです。
      
    - [Pinia :material-open-in-new:](https://pinia.vuejs.org/){ target=_blank }

          Vue.js 用の状態管理ライブラリです。
      
    - [Vue Router :material-open-in-new:](https://router.vuejs.org/){ target=_blank }

          Vue.js を利用した SPA で、ルーティング制御をするための公式プラグインです。
          
    - [Axios :material-open-in-new:](https://github.com/axios/axios){ target=_blank }

          Vue.js で非同期通信を行うためのプロミスベースの HTTP クライアントです。
          
    - [VeeValidate :material-open-in-new:](https://vee-validate.logaretm.com/v4/){ target=_blank }

          Vue.js 用のリアルタイムバリデーションコンポーネントライブラリです。
          
    - [yup :material-open-in-new:](https://github.com/jquense/yup){ target=_blank }

          JavaScript でフォームのバリデーションルールを宣言的に記述できるライブラリです。

    - [Tailwind CSS :material-open-in-new:](https://tailwindcss.com/){ target=_blank }

          utility class を使って独自のボタンなどを作成する CSS フレームワークです。

    - [Prettier :material-open-in-new:](https://prettier.io/){ target=_blank }

          JavaScript, Vue, CSS, JSON などのコードフォーマッターです。

    - [ESLint :material-open-in-new:](https://eslint.org/){ target=_blank }

          JavaScript の静的検証ツールです。

    - [Stylelint :material-open-in-new:](https://stylelint.io/){ target=_blank }

          CSS の静的検証ツールです。

    - [Vitest :material-open-in-new:](https://vitest.dev/){ target=_blank }

          Vite 環境で動作する高速なテスティングフレームワークです。

    - [Cypress :material-open-in-new:](https://www.cypress.io/){ target=_blank }

          E2E テストツールです。

??? note "利用ライブラリ（バックエンド）"

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

    - [Apache Log4j 2 :material-open-in-new:](https://logging.apache.org/log4j/2.x/index.html){ target=_blank }

        Apache Log4j 2 は Java のロギングフレームワークです。
        複数のロガーに対して、フィルター、ローテーション、ログレベルなどの細やかな管理ができます。

    - [MyBatis :material-open-in-new:](https://mybatis.org/mybatis-3/ja/index.html){ target=_blank }
    
        MyBatis はデータベースアクセスの実装に利用する O/R Mapper です。
        XML ファイルまたはアノテーションに、 SQL やレコードとオブジェクトのマッピングを定義できます。

    - [H2 Database :material-open-in-new:](https://www.h2database.com/html/main.html){ target=_blank }
    
        H2 Database は Java 上で動作するリレーショナルデータベースです。
        単体テストやローカル環境でのアプリケーション実行など、ローカル環境でデータベースアクセスを含む動的テストを行うのに利用します。

    - [springdoc-openapi :material-open-in-new:](https://springdoc.org/){ target=_blank }

        OpenAPI 形式の Web API ドキュメントを生成するためのライブラリです。
        Controller の実装から Web API ドキュメントを自動的に生成できます。
        Web API ドキュメントの生成にあたり、 Controller の実装だけでは不十分な情報に関しては、アノテーションを利用して情報を付与できます。

## アプリケーションアーキテクチャ {#application-architecture}

AlesInfiny Maia のアプリケーションアーキテクチャは、クリーンアーキテクチャに基づいています。
アーキテクチャの全体概要は以下の通りです。

![アーキテクチャ概要図](../../images/app-architecture/client-side-rendering/csr-architecture-light.png#only-light){ loading=lazy }
![アーキテクチャ概要図](../../images/app-architecture/client-side-rendering/csr-architecture-dark.png#only-dark){ loading=lazy }

## 層の構造詳細 {#layer-structure}

クライアントサイドレンダリング方式の Web アプリケーションにおける、各層とそれを構成するコンポーネントの役割について、それぞれ説明します。

### アプリケーションコア層 {#application-core}

アプリケーションコア層は、システムの中核となる業務処理を実装する業務中心の層です。
具体的には、以下のようなコンポーネントで構成されます。

- エンティティ

    エンティティは、業務で扱うデータをクラスとして表現するコンポーネントです。
    `id` など、インスタンスを一意に特定する識別子を持ちます。  
    プロパティは可変であり、識別子以外の値が変化しても、同一性が保たれるオブジェクトです。  

- 値オブジェクト

    値オブジェクトも、エンティティと同様に業務で扱うデータをクラスとして表現するコンポーネントです。
    値オブジェクトは、インスタンスを作成した時点で各プロパティの値が固定されます。  
    プロパティの値がすべて等しければ同一とみなされ、プロパティの等価性によって区別されます。

- ドメインサービス

    ドメインサービスは、エンティティや値オブジェクトに含めることが適当ではないドメイン固有の処理を実装するコンポーネントです。
    ドメイン単位でクラスにまとめ、ドメインに対する処理毎にメソッドとして実装します。
    必要に応じてリポジトリを利用して、データベースなどの外部リソースにアクセスします。
    アプリケーションコア層のリポジトリインターフェースを利用し、インフラストラクチャ層のリポジトリ実装に依存しないよう注意してください。

- アプリケーションサービス

    アプリケーションサービスは、システムに必要な機能を実装するクラスです。
    1 つの Web API の業務処理がアプリケーションサービスの 1 メソッドに対応します。
    エンティティや値オブジェクト、リポジトリ ( インターフェース ) を組み合わせて、必要な機能を実現します。
    必要に応じてドメインサービスも利用します。

- リポジトリ ( インターフェース )

    アプリケーションコア層のリポジトリはインターフェースであり、インフラストラクチャ層のリポジトリで実装されます。
    依存関係逆転の法則に従い、アプリケーションコア層の実装がインフラストラクチャ層に依存しないようするためのインターフェースです。

### プレゼンテーション層 {#presentation}

プレゼンテーション層は、主にシステムの利用者とのやり取りを担う層です。
画面を構成するフロントエンドアプリケーションと、バックエンドアプリケーションのインターフェースとなる Web API を配置します。

- コントローラー

    コントローラーは Spring MVC のコントローラーに対応し、各 Web API の定義と実装を担います。
    業務処理であるアプリケーションサービスを呼び出し、その結果からレスポンスデータを生成します。

- API モデル

    Web API のリクエスト／レスポンスの形式を定義するクラスです。
    コントローラーが受け取る引数やレスポンスの型を Java のクラスで表現します。

- ビュー

    ビューは Vue.js の JavaScript アプリケーションとして実装します。

### インフラストラクチャ層 {#infrastructure}

インフラストラクチャ層は、データベースを中心とする外部リソースにアクセスする処理を実現する層です。

- リポジトリ

    インフラストラクチャ層のリポジトリは、アプリケーションコア層のリポジトリインターフェースの実装クラスで、具体的なデータベースアクセス処理を実装します。
    データベースとはテーブルエンティティの型を利用してデータをやり取りします。
    それに対してリポジトリインターフェースはエンティティや値オブジェクトの型を用いて構成されています。
    インフラストラクチャ層のリポジトリは、これらの型同士の変換処理を行う役割も持ちます。

- テーブルエンティティ

    テーブルエンティティはデータベースのテーブルに対応するデータ構造を表現するクラスです。
    1 つのテーブルエンティティオブジェクトがテーブルの 1 レコードに対応します。

### プロジェクト構成と各層とのマッピング {#project-layer-mapping}

AlesInfiny Maia では Java のプロジェクト構成として、複数のサブプロジェクトに分割し、それらをルートプロジェクトでまとめて管理するマルチプロジェクト構成を採用します。
サブプロジェクトの分割については、これまでに説明したアプリケーションコア層、プレゼンテーション層、インフラストラクチャ層の各層を 1 つのサブプロジェクトとして対応させることを推奨します。

どの層からも利用されるようなシステム共通機能については、依存関係からアプリケーションコア層に含める考えもありますが、こちらも独立したサブプロジェクトとすることを推奨します。
業務機能とシステム共通機能を分割することで、プロジェクトの役割や依存関係が明確になり、保守性が高まると考えられます。

各サブプロジェクトの内部構成については、以下のような構成を推奨します。

![フォルダ構成図](../../images/app-architecture/client-side-rendering/csr-project-structure-light.png#only-light){ loading=lazy }
![フォルダ構成図](../../images/app-architecture/client-side-rendering/csr-project-structure-dark.png#only-dark){ loading=lazy }

プロジェクト構造全体は、 Spring Initializr で生成した Gradle Groovy DSL プロジェクトの構造と変わりはありません。

<!-- textlint-disable @textlint-ja/no-synonyms -->
パッケージの構成としては、システムで 1 つのフォルダー ( aa.bb.cc ) をベースに、各層に対応するパッケージ ( application-core など ) を作成します。
<!-- textlint-enable @textlint-ja/no-synonyms -->
アプリケーションコア層については、ドメインの単位でパッケージを作成し、それ以外の層については、構成するコンポーネント単位でパッケージを作成することを想定しています。
以降の階層については、管理や機能面を考慮し、必要に応じてサブパッケージを作成してください。
