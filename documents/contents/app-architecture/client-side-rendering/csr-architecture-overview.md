# CSRアーキテクチャ概要

Maia OSS 版において、クライアントサイドレンダリング方式のWebアプリケーションを構築する際に想定しているアーキテクチャの概要について説明します。

## 技術スタック {: #technology-stack }

本アーキテクチャを構成する主なライブラリを以下に示します。

![構成ライブラリ一覧](../../images/app-architecture/client-side-rendering/csr-library-light.png#only-light){ loading=lazy }

### 利用ライブラリ {: #used-libraries }

クライアントサイドレンダリング方式のWebアプリケーションに必要なライブラリについて説明します。

- Spring Core
  
    DI コンテナや AOP の機能を提供する Spring Framework の コアライブラリです。
    ベースとなるSpring Framework のコアライブラリや、Spring Framework の自動設定サポートを含む Spring Boot の基本的な機能を提供します。
    [Spring Core](https://spring.pleiades.io/spring-framework/docs/current/reference/html/core.html#spring-core)

- Spring Boot
  
    Spring Framework をベースとするアプリケーション開発を効率的に行うための仕組みを提供するフレームワークです。
    Spring Framework の課題である煩雑な Bean 定義や設定ファイルを可能な限り自動設定したり、
    実装するコード量を軽減するアノテーションを提供する。
    [Spring Boot](https://spring.pleiades.io/projects/spring-boot)

- Spring MVC

    Spring MVC は Spring Framework をベースとする Front Controllerパターンの Web MVC フレームワークです。
    [Spring MVC](https://spring.pleiades.io/spring-framework/docs/current/reference/html/web.html#mvc)

- Spring Validation

    Bean に対するデータの値チェック機能を提供するライブラリです。
    アノテーションベースで汎用的に利用できる値チェックが提供され、入力値チェック等が簡潔に実現できます。

- Spring Test

    Spring Framework をベースとするアプリケーション実装をテストするためのライブラリです。
    Unit Jupiter、Hamcrest、Mockito などのライブラリと連携して、テスト実装をサポートする機能を提供します。
    [Spring Test](https://spring.pleiades.io/spring-framework/docs/current/reference/html/testing.html)

- Apache Log4j 2

    Apache Log4j 2 は Java のロギングフレームワークです。
    複数のロガーに対して、フィルターやローテーションやログレベルなどの細やかな管理ができます。
    [Apache Log4j 2](https://logging.apache.org/log4j/2.x/)

- MyBatis
  
    MyBatis はデータベースアクセスの実装に利用する OR-Mapper で、XML ファイルまたはアノテーションに SQL や レコードとオブジェクトのマッピングを定義することができます。
    [MyBatis](https://mybatis.org/mybatis-3/ja/index.html)

- H2 Database
  
    H2 Database は Java 上で動作するリレーショナルデータベースで、
    単体テストやローカル環境でのアプリケーション実行など、
    ローカル環境でデータベースアクセスを含む動的テストを簡易に実施するために利用できます。
    [H2 Database](https://www.h2database.com/html/main.html)

- springdoc-openapi

    OpenAPI 形式の API ドキュメントを生成するためのライブラリです。
    Controller の実装から API ドキュメントを自動的に生成することができます。
    API ドキュメントの生成にあたり、Controller の実装だけでは不十分な情報に関しては、アノテーションを利用して情報を付与することもできます。
    [springdoc-openapi](https://springdoc.org/)

## アプリケーションアーキテクチャ {: #application-architecture }

Maia OSS 版のアプリケーションアーキテクチャは、クリーンアーキテクチャを基本としています。
アーキテクチャの全体概要は以下の通りです。

![アーキテクチャ概要図](../../images/app-architecture/client-side-rendering/csr-architecture-light.png#only-light){ loading=lazy }

## レイヤー構造詳細 {: #layer-structure }

クライアントサイドレンダリング方式のWebアプリケーションにおける、各層とそれを構成するコンポーネントの役割について、それぞれ説明します。

### アプリケーションコア層 {: #application-core }

アプリケーションコア層は、ドメインモデルの定義と業務処理を実装する業務中心の層です。

- ドメインモデル

    ドメインモデルは、業務で扱うドメインをクラスとして表現するエンティティと値オブジェクトで構成されます。
    共にモデルのデータ構造と振る舞いを持つことは変わりませんが、
    エンティティはプロパティが可変である一方、値オブジェクトのプロパティは不変です。

- ドメインサービス

    ドメインサービスはドメインに対する CRUD 処理を中心に、自身のドメインの範囲内の業務処理を実装します。
    ドメイン単位でクラスにまとめ、ドメインに対する処理毎にメソッドとして実装します。
    必要に応じて リポジトリを利用して、データベースなどの外部リソースにアクセスしますが、あくまでもアプリケーションコア層の リポジトリインタフェースを利用し、インフラストラクチャ層のリポジトリ実装に依存しないことに注意してください。

- アプリケーションサービス

    アプリケーションサービスは、システムの要求機能を実装するクラスです。
    基本的に1つの API の業務処理がアプリケーションサービスの1メソッドに対応します。
    必要に応じてドメインサービスを組み合わせて、要求される機能を実現します。

- リポジトリ（インタフェース）
  
    アプリケーションコア層のリポジトリはインタフェースであり、インフラストラクチャ層のリポジトリで実装されます。
    依存関係逆転の法則に従い、アプリケーションコア層の実装がインフラストラクチャ層に依存しないようするためのインタフェースです。

### プレゼンテーション層 {: #presentation }

プレゼンテーション層は、クライアントのやり取りを担う層で、各 API の入出力と業務処理の呼び出しを担当します。

- コントローラ
  
    コントローラは Spring MVC のコントローラに対応し、各 API の定義と実装を担うクラスです。
    業務処理であるApplication Service を呼び出し、
    その結果からレスポンスデータの生成を行います

- Filter
  
    Filter はコントローラの前後に共通で実行される処理を実装するクラスです。

- Config

    Config は Spring Boot アプリケーションを実行する際の設定を実装するクラスです。
    Spring Boot では、Filter の設定や Spring Security を利用する場合の設定など、 各設定を Java のコードで実装できます。

### インフラストラクチャ層 {: #infrastructure }

インフラストラクチャ層は、データベースを中心とする外部リソースにアクセスする処理を実現する層です。

- リポジトリ
  
    インフラストラクチャ層のリポジトリは、アプリケーションコア層のリポジトリインターフェースの実装クラスで、具体的なデータベースアクセス処理を実装します。
    実際のデータベースとのやり取りは Entity で行いますが、アプリケーションコア層のデータのやり取りは Domain Model で行うため、変換処理も必要になります。

- テーブルエンティティ

    テーブルエンティティはデータベースのテーブルに対応するデータ構造を表現するクラスですです。
    1つのテーブルエンティティオブジェクトがテーブルの1レコードに対応します。

### プロジェクト構成と各レイヤーとのマッピング {: #project-layer-mapping }

Maia OSS 版では Java のプロジェクト構成として、複数のサブプロジェクトに分割し、それらをルートプロジェクトでまとめて管理するマルチプロジェクト構成を採用します。
サブプロジェクトの分割については、これまでに説明したアプリケーションコア層、プレゼンテーション層、インフラストラクチャー層の各層を1つのサブプロジェクトとして対応させることを推奨します。

どの層からも利用されるようなシステム共通機能については、依存関係からアプリケーションコア層に含める考えもありますが、こちらも独立したサブプロジェクトとすることを推奨します。
業務機能とシステム共通機能を分割することで、プロジェクトの役割や依存関係が明確になり、保守性が高まると考えられます。

各サブプロジェクトの内部構成については、以下のような構成を推奨します。

![フォルダ構成図](../../images/app-architecture/client-side-rendering/csr-project-structure-light.png#only-light){ loading=lazy }

プロジェクト構造全体としては、Spring Initializr で生成された基本的な gradle プロジェクトの構造と変わりはありません。
パッケージの構成としては、システムで一意のフォルダ（aa.bb.cc）をベースに、各層に対応するパッケージ（application-coreなど）を作成します。
アプリケーションコア層については、ドメインの単位でパッケージを作成し、それ以外の層については、構成するコンポーネント単位でパッケージを作成することを想定しています。
以降の階層については、管理や機能面を考慮し、必要に応じてサブパッケージを作成してください。
