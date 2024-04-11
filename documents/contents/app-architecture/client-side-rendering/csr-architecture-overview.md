---
title: CSR 編
description: クライアントサイドレンダリングを行う Web アプリケーションのアーキテクチャについて解説します。
---

# CSR アーキテクチャ概要 {#top}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia）において、クライアントサイドレンダリング方式の Web アプリケーションを構築する際に想定しているアーキテクチャの概要について説明します。

## 技術スタック {#technology-stack}

本アーキテクチャを構成する主なライブラリを以下に示します。

![構成ライブラリ一覧](../../images/app-architecture/client-side-rendering/csr-library-light.png#only-light){ loading=lazy }
![構成ライブラリ一覧](../../images/app-architecture/client-side-rendering/csr-library-dark.png#only-dark){ loading=lazy }

??? note "利用ライブラリ（フロントエンド）"

    - [TypeScript :material-open-in-new:](https://www.typescriptlang.org/){ target=_blank }

          JavaScript を拡張して静的型付にしたプログラミング言語です。
      
    - [Vue.js :material-open-in-new:](https://v3.ja.vuejs.org/){ target=_blank }

          シンプルな設計で拡張性の高い JavaScript のフレームワークです。
      
    - [Vite :material-open-in-new:](https://ja.vitejs.dev/){ target=_blank }

          ES modules を利用してプロジェクトの高速な起動・更新を実現するフロントエンドビルドツールです。
      
    - [Pinia :material-open-in-new:](https://pinia.vuejs.org/){ target=_blank }

          Vue.js 用の状態管理ライブラリです。
      
    - [Vue Router :material-open-in-new:](https://router.vuejs.org/){ target=_blank }

          Vue.js を利用した SPA で、ルーティング制御をするための公式プラグインです。
          
    - [Axios :material-open-in-new:](https://github.com/axios/axios){ target=_blank }

          Vue.js で非同期通信を行うためのプロミスベースの HTTP クライアントです。
          
    - [VeeValidate :material-open-in-new:](https://vee-validate.logaretm.com/){ target=_blank }

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

    - [Spring Core :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/reference/html/core.html#spring-core){ target=_blank }
    
        DI コンテナや AOP の機能を提供する Spring Framework のコアライブラリです。
        ベースとなる Spring Framework のコアライブラリや、 Spring Framework の自動設定サポートを含む Spring Boot の機能を提供します。

    - [Spring Boot :material-open-in-new:](https://spring.pleiades.io/projects/spring-boot){ target=_blank }
    
        Spring Framework をベースとするアプリケーション開発を効率的に行うためのフレームワークです。
        Spring Framework の課題である煩雑な Bean 定義や設定ファイルを可能な限り自動設定したり、実装するコード量を軽減するアノテーションを提供します。

    - [Spring MVC :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/reference/html/web.html#mvc){ target=_blank }

        Spring MVC は Spring Framework をベースとする Front Controller パターンの Web MVC フレームワークです。

    - Spring Validation

        Bean に対するデータの値チェック機能を提供するライブラリです。
        アノテーションベースで汎用的に利用できる値チェックが提供され、入力値チェック等が簡潔に実現できます。

    - [Spring Test :material-open-in-new:](https://spring.pleiades.io/spring-framework/docs/current/reference/html/testing.html){ target=_blank }

        Spring Framework をベースとするアプリケーション実装をテストするためのライブラリです。
        Unit Jupiter 、 Hamcrest 、 Mockito などのライブラリと連携して、テスト実装をサポートする機能を提供します。

    - [Apache Log4j 2 :material-open-in-new:](https://logging.apache.org/log4j/2.x/){ target=_blank }

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
