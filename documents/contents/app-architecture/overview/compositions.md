---
title: 概要編
description: AlesInfiny Maia のアプリケーションアーキテクチャ概要を解説します。
---

# AlesInfiny Maia を構成するもの {#top}

## アプリケーション構成 {#application-composition}

AlesInfiny Maia として、アプリケーション形態ごとに標準的なアプリケーション構成を定義しています。
ここでは主要な構成要素を示します。
詳細はアプリケーション種別ごとの詳細ページ、および、サンプルプリケーションを参照してください。

### Web アプリケーション ( クライアントサイドレンダリング ) {#client-side-rendering}

Vue.js を用いた SPA の構成をとります。
バックエンドは Spring Framework / Spring Boot をベースとした Web API アプリケーションです。
データアクセスは、 JDBC / JPA / MyBatis から選択可能です。
サンプルアプリケーションでは MyBatis を採用しています。

![クライアントサイドレンダリング アプリケーションスタック](../../images/app-architecture/overview/client-side-rendering-maia-light.png#only-light){ loading=lazy }
![クライアントサイドレンダリング アプリケーションスタック](../../images/app-architecture/overview/client-side-rendering-maia-dark.png#only-dark){ loading=lazy }

### Web アプリケーション ( サーバーサイドレンダリング ) {#server-side-rendering}

( 今後追加予定 )

### バッチアプリケーション {#batch}

Spring Framework / Spring Batch をベースとするバッチアプリケーションです。

## 技術要素 {#technical-elements}

- Spring Framework

    Java を使った Web ベースのアプリケーションを構築するための OSS フレームワークです。
    DI コンテナー、 AOP 機能、 Spring Security による認証・認可、各種 O/R Mapper に対応するデータアクセス等、多くの機能を提供します。

- Spring Boot

    Spring Framework ベースのアプリケーションを手軽に作成できるフレームワークです。
    Spring Framework で煩雑となりがちな設定ファイルを極力排除し、必要最低限の設定で実行可能なアプリケーションの構築が可能になります。

- Spring Batch

    Spring Framework ベースの軽量で包括的なバッチアプリケーションフレームワークです。
    大量のレコードを高速に処理するための並列化機能や、データの一貫性を保証するトランザクション管理機能など、バッチアプリケーションに必要な機能を提供します。

- JDBC / MyBatis / JPA

    いずれも Java アプリケーションでリレーショナルデータベースへのアクセスを実装する際に候補となる技術です。
    Spring Framework はそれぞれの技術をサポートする機能を提供しているため、それらの特性を理解したうえで使い分けることが重要となります。

    - JDBC：小規模なプロジェクトでの利用に適したデータベースアクセス技術です。 Spring アプリケーションで使用する場合、 Spring Data JDBC を利用することで単純な検索処理などは SQL を記述することなく簡潔に実装することが出来ます。一方、複雑な SQL 等を発行する場合はソースコード中に SQL 文を直接記述する必要があり、ソースコードが冗長になりやすい、 SQL 単独での文法エラーに気付きにくいなどの問題につながる可能性があります。
    - MyBatis：規模の大きいプロジェクトでの利用に適した OR マッパーです。 SQL をソースコードとは別に XML ファイルに分けて記述するため、複雑な SQL も定義しやすく、それらをソースコードと分離した形で管理できるといった利点があります。また、 MyBatis Generator を利用することで DB のスキーマ情報からデータアクセス部品を自動生成できます。
    - JPA：JakartaEE が定めるデータアクセスの仕様であり、実際のプロダクトとしては本仕様に準拠した Hibernate などがよく利用されます。 JakartaEE 標準であるため、 JakartaEE に準拠した AP サーバーを利用している場合はベンダーサポートが受けられるという利点があります。一方、独特な仕様や動作を理解するための学習コストが高いため、有識者がいないプロジェクトでの利用は推奨しません。

- Vue.js

    JavaScript を使った Web ベースの SPA を構築するための OSS フレームワークです。
    コンポーネント機能により、画面を再利用可能な形に細分化できます。

- Axios

    JavaScript のプリケーションから Web API を呼び出すための HTTP クライアントです。
    バックエンド Web API との HTTP 通信によるデータ連携を実現します。

- Pinia

    Vue.js 向けの状態管理ライブラリです。
    各コンポーネントから操作可能なデータ格納領域を提供し、アプリケーション内での利用するデータの整合性を保つ役割を持ちます。
