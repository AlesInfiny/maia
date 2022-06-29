# Maia OSS 版を構成するもの

## アプリケーション構成 {: #application-composition }

Maia OSS 版として、アプリケーション形態ごとに標準的なアプリケーション構成を定義しています。
ここでは主要な構成要素を示します。
詳細はアプリケーション種別ごとの詳細ページ、および、サンプルプリケーションを参照してください。

### Web アプリケーション ( クライアントサイドレンダリング ) {: #client-side-rendering }

Vue.js を用いた SPA の構成をとります。
サーバーサイドは Spring Framework / Spring Boot をベースとした Web API アプリケーションです。
データアクセスは、 JDBC / JPA / MyBatis から選択可能です。
サンプルアプリケーションでは MyBatis を採用しています。

![クライアントサイドレンダリング アプリケーションスタック](../../images/app-architecture/overview/client-side-rendering-maia-light.png#only-light){ loading=lazy }
![クライアントサイドレンダリング アプリケーションスタック](../../images/app-architecture/overview/client-side-rendering-maia-dark.png#only-dark){ loading=lazy }

### Web アプリケーション ( サーバーサイドレンダリング ) {: #server-side-rendering }

( 今後追加予定 )

### バッチアプリケーション {: #batch }

Spring Framework / Spring Batch をベースとするバッチアプリケーションです。

## 技術要素 {: #technical-elements }

- Spring Framework

    Java を使った Web ベースのアプリケーションを構築するための OSS フレームワークです。
    DI コンテナー、 AOP 機能、 Spring Security による認証・認可、各種 O/R Mapper に対応するデータアクセス等、多くの機能を提供します。

- Spring Boot

    Spring Framework ベースのアプリケーションを手軽に作成することができるフレームワークです。
    Spring Framework で煩雑となりがちな設定ファイルを極力排除し、必要最低限の設定で実行可能なアプリケーションの構築が可能になります。

- Spring Batch

    Spring Framework ベースの軽量で包括的なバッチアプリケーションフレームワークです。
    大量のレコードを高速に処理するための並列化機能や、データの一貫性を保証するトランザクション管理機能など、バッチアプリケーションに必要な機能を提供します。

- Vue.js

    JavaScript を使った Web ベースの SPA を構築するための OSS フレームワークです。
    コンポーネント機能により、画面を再利用可能な形で細分化することができます。

- Axios

    JavaScript のプリケーションから Web API の呼び出しを行うための HTTP クライアントです。
    バックエンド Web API との HTTP 通信によるデータ連携を実現します。

- Pinia

    Vue.js 向けの状態管理ライブラリです。
    各コンポーネントから操作可能なデータ格納領域を提供し、アプリケーション内での利用するデータの整合性を保つ役割を持ちます。
