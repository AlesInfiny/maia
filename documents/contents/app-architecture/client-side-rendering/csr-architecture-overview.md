# CSRアーキテクチャ概要

Maia OSS 版において、クライアントサイドレンダリング方式のWebアプリケーションを構築する際に想定しているアーキテクチャの概要について説明します。

## 技術スタック

<!--
- 概要編で挙げた図をライブラリレベルまで詳細化した図。
- このレベル <https://terasolunaorg.github.io/guideline/5.7.0.RELEASE/ja/Overview/FrameworkStack.html#software-framework>
-->

本アーキテクチャを構成する主なライブラリを以下に示します。

![構成ライブラリ一覧](../../../images/app-architecture/client-side-rendering/csr-architecture-overview/csr-library-light.png#only-light){ loading=lazy }

### 利用ライブラリ
<!--
- 技術スタックの図で記載したライブラリ一つ一つの説明。
- Spring系ライブラリ、という書き方ではなく、実際にサンプルAPに組み込むものを具体的に書く。
    - 基本的には簡易な機能説明でよいが、プロジェクト側で設定が必要なものなどはある程度具体的な使い方まで説明。（今回は書かないが、Spring Securityなど）
-->

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

<!--
### 利用ライブラリ（旧：削除予定）

`spring-boot-starter-*`で記述されるライブラリは、Spring Boot が提供するスターターで、ライブラリの依存関係が規定されています。
Spring Boot の関連ライブラリの多くはこのスターターによるライブラリ群として提供されるので、以降の説明でもスターター単位で説明します。

- `spring-boot-starter`
  
    Spring Boot アプリケーションを動作させるためのコアライブラリ群です。
    ベースとなるSpring Framework のコアライブラリや、Spring Framework の自動設定サポートを含む Spring Boot の基本的な機能を提供します。

- `spring-boot-starter-web`

    Spring MVC を使用して、RESTful を含む Web アプリケーションを構築するためのスターターです。
    Spring MVC は Spring Framework をベースとする Front Controllerパターンの Web MVC フレームワークです。
    デフォルトの埋め込みコンテナーとして Apache Tomcat も提供します。

- `spring-boot-starter-validation`

    Bean に対するデータの値チェック機能を提供するスターターです。
    アノテーションベースで汎用的に利用できる値チェックが提供され、入力値チェック等が簡潔に実現できます。

- `spring-boot-starter-test`

    JUnit Jupiter、Hamcrest、Mockito などのライブラリを使用して Spring Boot アプリケーションをテストするためのスターターです。

- `spring-boot-starter-log4j2`
    ロギングに Apache Log4j 2 を使用するためのスターターです。
    Apache Log4j 2 は Java のロギングフレームワークのデファクトスタンダードの1つで、
    複数のロガーに対して、フィルターやローテーションやログレベルなどの細やかな管理ができます。

- `mybatis-spring-boot-starter`
  
    MyBatis を Spring Boot 上で利用するためのスターターです。
    MyBatis はデータベースアクセスの実装に利用する OR-Mapper で、XML ファイルまたはアノテーションに SQL や レコードとオブジェクトのマッピングを定義することができます。

- `h2`
  
    H2 Database を利用するためのライブラリです。
    H2 Database は Java 上で動作するリレーショナルデータベースで、
    単体テストやローカル環境でのアプリケーション実行など、
    ローカル環境でデータベースアクセスを含む動的テストを簡易に実施するために利用できます。

- `springdoc-openapi-ui`
    OpenAPI 形式の API ドキュメントを生成するためのライブラリです。
    Controller の実装から API ドキュメントを自動的に生成することができます。
    API ドキュメントの生成にあたり、Controller の実装だけでは不十分な情報に関しては、アノテーションを利用して情報を付与することもできます。
-->

## アプリケーションアーキテクチャ
<!--
- 本アーキテクチャの全体概要図。以下のことが伝わるように
    - クリーンアーキテクチャを基本としていること
    - アプリケーションコア層/プレゼンテーション層/インフラストラクチャ層の各名称と依存関係
    - 各層に含まれる主要なコンポーネント（Controller、Domain Modelなど重要なものだけ。図が煩雑になりすぎる場合は、次節で解説しているので割愛も可）
    - システム共通機能は、ここには出てこなくてよい（役割としては単なる部品であり、アーキテクチャ図に含めると説明しにくい。）
- クリーンアーキテクチャ、レイヤードアーキテクチャ、ＤＤＤの知識は前提にしてよい（リンク先だけ提示）
-->
Maia OSS 版のアプリケーションアーキテクチャは、クリーンアーキテクチャを基本としています。
アーキテクチャの全体概要は以下の通りです。

![アーキテクチャ概要図](../../../images/app-architecture/client-side-rendering/csr-architecture-overview/csr-architecture-light.png#only-light){ loading=lazy }

## レイヤー構造詳細
<!--
- 各レイヤーとそれを構成するコンポーネントの役割を解説。
- 各種DTOなども含める。
- レイヤーをまたぐコンポーネントの依存関係も示す。とくにRepository関連は分かりやすいように図示。
- ここを参考に <http://terasolunaorg.github.io/guideline/current/ja/Overview/ApplicationLayering.html>
-->

クライアントサイドレンダリング方式のWebアプリケーションにおける、各層とそれを構成するコンポーネントの役割について、それぞれ説明します。

### アプリケーションコア層

アプリケーションコア層は、ドメインモデルの定義と業務処理を実装する業務中心の層です。

- Domain Model

    Domain Model はドメインのデータ構造を表す Value Object クラスです。
    Domain Model 自身に業務処理が実装されることはありません。

- Domain Service

    Domain Service はドメインに対する CRUD 処理を中心に、自身のドメインの範囲内の業務処理を実装します。
    ドメイン単位でクラスにまとめ、ドメインに対する処理毎にメソッドとして実装します。
    必要に応じて Repository を利用して、データベースなどの外部リソースにアクセスしますが、あくまでもアプリケーションコア層の Repository を利用し、インフラストラクチャ層の Repository 実装に依存しないことに注意してください。

- Application Service

    Application Service は、システムの要求機能を実装するクラスです。
    基本的に1つの API の業務処理が Application Service の1メソッドに対応します。
    必要に応じて Domain Service を組み合わせて、要求される機能を実現します。

- Repository
  
    アプリケーションコア層の Repository はインタフェースであり、インフラストラクチャ層の Repository で実装されます。
    依存関係逆転の法則に従い、アプリケーションコア層の実装がインフラストラクチャ層に依存しないようするためのインタフェースです。

### プレゼンテーション層

プレゼンテーション層は、クライアントのやり取りを担う層で、各 API の入出力と業務処理の呼び出しを担当します。

- Controller
  
    Controller は Spring MVC のコントローラに対応し、各 API の定義と実装を担うクラスです。
    業務処理であるApplication Service を呼び出し、
    その結果からレスポンスデータの生成を行います

- Filter
  
    Filter は Controller の前に共通で実行される処理を実装するクラスです。

- Config

    Config は Spring Boot アプリケーションを実行する際の設定を実装するクラスです。
    Spring Boot では、Filter の設定や Spring Security を利用する場合の設定など、 各設定を Java のコードで実装できます。

### インフラストラクチャ層

インフラストラクチャ層は、データベースを中心とする外部リソースにアクセスする処理を実現する層です。

- Repository
  
    インフラストラクチャ層の Repository は、アプリケーションコア層の Repository インターフェースを実装するクラスで、データベースアクセス処理を実装します。
    実際のデータベースとのやり取りは Entity で行いますが、アプリケーションコア層のデータのやり取りは Domain Model で行うため、変換処理も必要になります。

- Entity

    Entity はデータベースのテーブルに対応するデータ構造を表現する Value Object です。
    1つの Entity オブジェクトがテーブルの1レコードに対応します。

### プロジェクト構成と各レイヤーとのマッピング
<!--
- Maia OSS 版が推奨する Java プロジェクトの構成（マルチプロジェクト構成）と各レイヤーのマッピング。
- 各レイヤーに相当するプロジェクトの内部構成についても簡単に示す。（ディレクトリ構造を示す程度でよい）
- システム共通機能についても軽く触れる。
    - どの層からも参照される共通機能を提供する。
    - アプリケーションコア層に実装してもよいが、特に中規模以上の開発などにおいてシステム共通化チームからの提供物を別ライブラリとして分けておいた方が保守性が高まると考えられるため、別プロジェクトにすることを推奨。
- 参考 <http://terasolunaorg.github.io/guideline/current/ja/Overview/ApplicationLayering.html#application-layering-project-structure>
-->

Maia OSS 版では Java のプロジェクト構成として、複数のサブプロジェクトに分割し、それらをルートプロジェクトでまとめて管理するマルチプロジェクト構成を採用します。
サブプロジェクトの分割については、これまでに説明したアプリケーションコア層、プレゼンテーション層、インフラストラクチャー層の各層を1つのサブプロジェクトとして対応させることを推奨します。

どの層からも利用されるようなシステム共通機能については、依存関係からアプリケーションコア層に含める考えもありますが、こちらも独立したサブプロジェクトとすることを推奨します。
業務機能とシステム共通機能を分割することで、プロジェクトの役割や依存関係が明確になり、保守性が高まると考えられます。

各サブプロジェクトの内部構成については、以下のような構成を推奨します。

![フォルダ構成図](../../../images/app-architecture/client-side-rendering/csr-architecture-overview/csr-folder-structure-light.png#only-light){ loading=lazy }
