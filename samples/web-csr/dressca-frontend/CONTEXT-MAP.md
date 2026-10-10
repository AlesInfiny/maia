---
title: dressca-frontend のコンテキストマップ
description: dressca-frontend を構成するコンテキストと、アプリケーション全体で使う用語を定義します。
---

# コンテキストマップ {#top}

dressca-frontend は、 `admin` と `consumer` の 2 つのフロントエンドアプリケーションを持つ npm workspaces のモノレポです。
各アプリケーションは、 `src/` の下で境界付けられたコンテキストごとにコードを分けています。
層の構成と参照方向は [ADR 0002](./docs/adr/0002-app-owned-route-table.md) で定めています。

## コンテキスト {#contexts}

- admin
    - カタログ管理（ `catalog-management` ）: 運営者がカタログアイテムを管理します。
    - セキュリティ（ `security` ）: 運営者の認証と認可を扱います。
- consumer
    - 買い物（ `shopping` ）: 購入者が商品を閲覧し、買い物かごに入れて注文します。
    - セキュリティ（ `security` ）: 購入者の認証を扱います。

`business-common` と `system-common` はコンテキストではなく共通の層です。
どちらもコンテキストに依存してはいけません（ `eslint.project-rules.ts` の `createLayerDependencyRules` を参照）。

## 保守するチーム {#owners}

フォルダーごとに、保守するチームを次のように想定します。

- 業務共通チーム
    - app 層（ `src/main.ts` 、 `src/App.vue` 、 `src/business-common/router/` ）
    - business-common （ `src/business-common/` ）
- 各業務チーム
    - pages 層（ `src/pages/` ）
    - コンテキスト（ `src/<context>/` ）
- システム共通チーム（または業務共通チームの兼務）
    - system-common （ `src/system-common/` ）

画面を追加するときは、業務チームがルート表とルート名の定数に直接追記し、業務共通チームがレビューします。

## 関係 {#relationships}

- admin のカタログ管理の画面は、セキュリティのロールの判定と組み合わせて構成します。
  組み合わせは画面で行い、コンテキスト同士は依存しません。
- consumer の買い物コンテキストでは、注文のドメインが買い物かごのドメインの状態を読み取ります。
  同じコンテキストの中でのドメインをまたぐ参照で、コンテキストをまたぐ依存ではありません。

## 用語 {#language}

次の用語は、両方のアプリケーションのすべてのコンテキストで共通に使います。

画面（ Page ）
:   `src/pages/` の下に置く、ルーティングの対象となる Vue のコンポーネントです。
    ファイル名は `<画面名>Page.vue` にし、フォルダー構成を URL のパスと対応させます。
    URL との対応は app 層のルート表（ `src/business-common/router/routes.ts` ）で定義します。

View
:   廃止した用語です。
    以前は画面と同じ意味で使い、コンテキストの `views/` フォルダーに置いていました。

公開 API （ Public API ）
:   コンテキストの直下に置く `public-api.ts` です。
    コンテキストの外のコードは、このファイルを経由してだけコンテキストを参照します。
    公開するのはユースケースコンポーザブル、画面に表示する業務の部品、それらの型だけです。

ユースケースコンポーザブル
:   1 つのユースケースの状態と操作をまとめて提供するコンポーザブルです。
    状態は読み取り専用で公開し、操作は結果（ Outcome ）を返します。
    排他制御や API の型などの詳細は内部に隠します。

結果（ Outcome ）
:   ユースケースコンポーザブルの操作が返す判別共用体です。
    `kind` プロパティで結果の種類を表します。
    画面は Outcome を受け取り、通知の文言と遷移先を決めます。

app 層
:   `src/main.ts` 、 `src/App.vue` 、 `src/business-common/router/` からなる最上位の層です。
    ルーターの生成、ガードの登録、画面遷移を伴う共通処理の結線を担います。
    業務共通チームが保守するため、ルーティングは business-common のフォルダーに置きます。
    `src/business-common/router/` は business-common 層ではなく app 層に属し、参照の制限も app 層のものに従います。
