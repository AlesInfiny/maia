---
title: CSR 編 - Web API
description: バックエンドアプリケーションのアーキテクチャについて、 層ごとに詳細を説明します。
---

# インフラストラクチャ層 {#top}

インフラストラクチャ層の実装方針について説明します。

## データベースアクセスのフレームワーク {#database-access-framework}

Spring Boot のアプリケーションにおけるデータベースアクセスの代表的なフレームワークには、以下の 3 つがあります。

- [MyBatis-Spring](#mybatis-spring)
- [Spring Data JDBC](#spring-data-jdbc)
- [Spring Data JPA](#spring-data-jpa)

ここではそれぞれの特徴を説明します。

### MyBatis-Spring {#mybatis-spring}

MyBatis は、ストアドプロシージャの呼び出しや、高度なマッピング処理を提供する O/R マッパーです。
他の O/R マッパーのようにテーブルとオブジェクトをマッピングするのではなく、 SQL の実行結果に対してオブジェクトをマッピングします。
複数のテーブルを結合する場合、他の O/R マッパーは実装が煩雑になりがちですが、 MyBatis では結合した結果に対するマッピングを定義すれば良いため、実装をシンプルに保てます。
MyBatis は、多数のテーブルが関連しあうシステムと相性のよい仕組みを持っています。

MyBatis を利用するためには、全てのクエリとその実行結果のマッピングを XML に定義します。
Spring Data でサポートされているような基本的な CRUD 処理は、[自動生成ツール :material-open-in-new:](https://mybatis.org/generator/){ target=_blank } で生成できます。
他の O/R マッパーと比較してコード量は多くなりますが、自動生成ツールの恩恵により開発作業の時間は大きく変わりません。

MyBatis-Spring は MyBatis を Spring Framework 上で利用しやすいように、連携機能を提供するパッケージです。
MyBatis のコンポーネントを DI コンテナーで管理可能にし、 Spring の汎用的な例外への変換も実現できます。

### Spring Data JDBC {#spring-data-jdbc}

JDBC でのデータベースアクセス実装をサポートするフレームワークです。
基本的な CRUD 処理の実装や Bean へのマッピング機能などが提供されるため、通常の JDBC による実装と比較して少量のコードで実装できます。

Spring Data JDBC の詳細については [こちら :material-open-in-new:](https://spring.pleiades.io/projects/spring-data-jdbc){ target=_blank } を参照してください。

### Spring Data JPA {#spring-data-jpa}

JPA は、データベースレコードとテーブルエンティティ間のマッピング、およびテーブルエンティティへの変更をデータベースへ反映する仕組みを API 仕様として定めたものです。
Spring Data JPA は JPA の参照実装である Hibernate を基に、データベースアクセスの実装をサポートするフレームワークです。
Spring Data JDBC と同様、基本的な CRUD の実装はフレームワークが提供してくれます。
シンプルなデータベースアクセスの場合、少量のコードで実装できます。

Spring Data JPA を採用する場合、開発者が JPA について正しく理解していないと、クエリの発行タイミングや実際に発行される SQL を把握しにくくなります。
クエリの発行はフレームワークがある程度隠ぺいしてくれます。
そのため、無意識のうちに繰り返し大量の情報を取得するような実装を行いがちです。
特に複数テーブルが関連するデータを取得する場合、テーブル結合の有無をクエリ単位で定義することが難しいため、性能に影響を与える実装のリスクが高まります。
JPA を用いた開発経験がある理解度の高いメンバーを揃えることを推奨します。

Spring Data JPA の詳細については [こちら :material-open-in-new:](https://spring.pleiades.io/projects/spring-data-jpa){ target=_blank } を参照してください。

### 各フレームワークの比較 {#framework-comparison}

データベースのテーブル構造やクエリがシンプルで、より軽量な実装をしたい場合は Spring Data JDBC や Spring Data JPA の採用を推奨します。
テーブルの関連が複雑で、結合を含む複雑なクエリが多く発生する場合は MyBatis-Spring の採用を推奨します。

AlesInfiny Maia OSS Edition では、 MyBatis ( MyBatis-Spring ) を標準として採用します。一定以上の規模のテーブルの関連は複雑になりがちであり、 MyBatis は軽量なデータベースアクセス実装にも対応できるためです。

## MyBatis を用いたデータベースアクセス実装 {#mybatis-implementation}

MyBatis を利用した実装方針を説明します。

### MyBatis Generator による自動生成 {#mybatis-generator}

[MyBatis Generator :material-open-in-new:](https://mybatis.org/generator/){ target=_blank } はデータベースのテーブル構造を読み取り、テーブル単位の CRUD を実現するクエリとマッピングを自動生成するツールです。
基本的な CRUD 処理は、自動生成するコードのみで実現できます。
具体的な生成されるファイルは以下の通りです。

- 各テーブルに対応するテーブルエンティティクラス

- 基本的な CRUD に対応する SQL と、テーブルエンティティとのマッピングが定義された XML ファイル

- 各クエリに対応する Java のインターフェース

### 自動生成以外のクエリの個別実装 {#mybatis-original-implementation}

テーブル結合を含む場合など、自動生成するクエリで実現できないデータアクセス処理は個別に実装します。
MyBatis Generator が生成するファイルと同等のファイルを、個別のクエリに合わせて実装します。

### リポジトリの実装 {#repository-implementation}

[アプリケーションコア層のリポジトリ ( インターフェース )](../csr-architecture-overview.md#application-core) を実装する具象クラスを作成します。
アプリケーションサービスやドメインサービスからリポジトリ ( インターフェース ) を介して呼び出されます。
リポジトリの主な実装内容は以下の通りです。

- テーブルエンティティとエンティティや値オブジェクトの変換

    アプリケーションコア層で定義するリポジトリ ( インターフェース ) の入出力は、エンティティや値オブジェクトを中心とするデータ型で定義します。
    インフラストラクチャ層でのデータベースとの API は、テーブル構造を元にしたテーブルエンティティで行うため、リポジトリでエンティティや値オブジェクトとの変換処理が必要です。

    テーブルエンティティとエンティティや値オブジェクトの変換は、インフラストラクチャ層の関心ごとです。
    変換処理を共通化する場合は、インフラストラクチャ層に変換機能を配置します。

- リポジトリ ( インターフェース ) の仕様にあわせたデータアクセス処理の実行

    実行するクエリに対応した MyBatis の Java インターフェースを呼び出し、クエリを実行します。
