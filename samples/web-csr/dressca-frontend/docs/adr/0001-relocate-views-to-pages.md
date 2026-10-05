---
title: ADR 0001 画面の src/pages への移動
description: コンテキストの views フォルダーにある画面を src/pages へ移動する決定を記録します。
status: superseded
---

# ルーティング対象の画面をコンテキストの views フォルダーから src/pages へ移動する {#top}

## ステータス {#status}

[ADR 0002](./0002-app-owned-file-based-routing.md) に置き換えられました。
以降は決定当時の記録として残します。

## 決定 {#decision}

画面の URL は、どのコンテキストやドメインがコードを所有するかではなく、リソースを軸に設計します。
また、複数のコンテキストを組み合わせる画面が増えています（ [コンテキストマップ](../../CONTEXT-MAP.md) を参照）。
フォルダー構成をこの実態に合わせるため、次のとおり決定しました。

- 各ワークスペースの画面を `{context}/{domain}/views/*View.vue` から最上位の `src/pages/` へ移動します。
- `src/pages/` の構成は、 URL のパスのセグメントと 1 対 1 に対応させます。
  `:itemId` のような動的セグメントはフォルダーにしません。
- 画面のファイル名は `*Page.vue` にします。
  用語は View を廃止して Page に統一します。
- ルート定義（パス、 `meta` 、ガード）は各コンテキストの `router/*.ts` に残します。
  変更するのは `component:` のインポート先だけです。
- 各コンテキストの直下に `public-api.ts` を設けます。
  `src/pages` と他のコンテキストは、このファイルを経由してだけコンテキストを参照します。
- `eslint.project-rules.ts` の `createLayerDependencyRules` で、 pages を既存の層の最上位に置きます。
  pages はコンテキストの `public-api.ts` 、 business-common 、 system-common を参照できます。
  どの層も `@/pages/**` を参照できません。

## 検討した選択肢 {#considered-options}

- vue-router の file-based routing （当時は `unplugin-vue-router` ）
    - 技術的には導入できました。
    - ただし、画面遷移のすべての呼び出しが手書きのルート名の定数に依存していました。
    - 物理的な移動と同時に呼び出し側をすべて書き換えることになるため、別の取り組みとして見送りました。
- リソースごとの平坦なフォルダー構成と、 URL の構造を反映したフォルダーの階層
    - file-based routing は見送ったものの、フォルダー構成が URL の形を表すように階層を選びました。
- pages からコンテキストを自由に参照する構成と、 `public-api.ts` を境界とする構成
    - コンテキストをまたぐ組み合わせをレビューで見える状態に保つため、 `public-api.ts` を境界とする構成を選びました。

## 影響 {#consequences}

- コンテキストごとに順に移行しました。
  最初に system-common のホーム、エラー、該当なしの画面を移し、次に admin のカタログ管理を移しました。
- pages 層のルールは、コンテキストを移行する前に `eslint.project-rules.ts` へ追加しました。
- file-based routing を見送ったため、ルートのパス、名前、 `meta` は変更しませんでした。
