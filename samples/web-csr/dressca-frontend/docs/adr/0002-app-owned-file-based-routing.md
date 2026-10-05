---
title: ADR 0002 画面とルーティングの構成
description: 画面を src/pages に置き、 file-based routing でルーティングを生成する構成を定めます。
status: accepted
---

# 画面とルーティングを app 層と pages 層に集約し file-based routing を採用する {#top}

## ステータス {#status}

承認済みです。
[ADR 0001](./0001-relocate-views-to-pages.md) を置き換えます。

## 背景 {#context}

ADR 0001 に従って画面を `src/pages` へ移動した結果、次の 3 つの問題が明らかになりました。

- 画面がユースケースの処理を抱え込んでいます。
  たとえば `ItemsEditPage.vue` は 588 行あり、排他制御の行バージョン、競合時の再取得、エラーの分類を画面が実装しています。
  コンテキストのサービスは API を呼び出すだけの薄い関数です。
- コンテキストの `public-api.ts` は、内部の関数や部品を再エクスポートするだけです。
  削除しても複雑さが別の場所に現れないため、コンテキストの境界として機能していません。
- ルート定義がコンテキストに残ったため、コンテキストは画面を知り、画面はコンテキストを知る相互依存になっています。
  ESLint のルールに違反しないのは、動的インポートが検査の対象外だからにすぎません。

本アプリケーションはエンタープライズアプリケーションのサンプルです。
画面数の増加とコンテキストをまたぐ画面の追加を前提に、標準的なルーティングの規約に沿った構成を採用します。

## 決定 {#decision}

### 層の構成 {#layers}

上の層から下の層への参照だけを許可します。

- app 層（ `src/main.ts` 、 `src/App.vue` 、 `src/app/` ）
    - ルーターの生成、ガードの登録、画面遷移を伴う共通処理の結線を担います。
- pages 層（ `src/pages/` ）
    - URL ごとの画面です。
    - コンテキストの機能を組み合わせ、結果を画面遷移や通知に変換します。
- コンテキスト（ `src/<context>/` ）
    - 業務の機能です。ユースケース、業務の部品、状態を持ちます。
- business-common （ `src/business-common/` ）
    - 複数のコンテキストで共有する業務の知識です。
- system-common （ `src/system-common/` ）
    - 業務の知識を持たない共通処理と汎用の UI 部品です。

コンテキストを参照できるのは、そのコンテキストの `public-api.ts` だけです。
これは pages 層と他のコンテキストの両方に適用します。

### ルーティング {#routing}

- vue-router 5 に組み込まれた file-based routing を使います。
  Vite プラグイン `vue-router/vite` の `routesFolder` に `src/pages` を指定します。
- ルート定義は `vue-router/auto-routes` から読み込み、 `src/app/router` で `createRouter` に渡します。
  手書きのルート定義とルート名の定数は作りません。
- ルート名は、プラグインが生成する既定の名前を使います。
  既定の名前はファイルの配置を表し、たとえば `/catalog/items/edit/[itemId]` になります。
- プラグインが生成する型定義ファイル `typed-router.d.ts` はリポジトリに含めます。
  型チェックをビルドより前に実行するためです。

### 画面ファイルの命名 {#naming}

file-based routing の標準の規約に従います。

- 子を持つ URL セグメントはフォルダーにし、そのセグメント自体の画面は `index.vue` にします。
- 子を持たない URL セグメントは `<セグメント名>.vue` にします。
- 動的セグメントは `[パラメーター名].vue` にします。
- どのルートにも該当しない場合の画面は `[...path].vue` にします。
- 画面ファイルは、コンポーネント名を複数の単語にする ESLint のルール（ `vue/multi-word-component-names` ）の対象外にします。
- ADR 0001 の `*Page.vue` の命名と「動的セグメントをフォルダーにしない」規約は廃止します。

admin の画面の配置は次のとおりです。

```text linenums="0"
src/pages/
├ index.vue ------------------- /
├ error.vue ------------------- /error
├ [...path].vue --------------- どのルートにも該当しない場合
├ authentication/
│ └ login.vue ----------------- /authentication/login
└ catalog/items/
  ├ index.vue ----------------- /catalog/items
  ├ add.vue ------------------- /catalog/items/add
  └ edit/[itemId].vue --------- /catalog/items/edit/:itemId
```

consumer の画面の配置は次のとおりです。

```text linenums="0"
src/pages/
├ index.vue ------------------- /
├ basket.vue ------------------ /basket
├ error.vue ------------------- /error
├ [...path].vue --------------- どのルートにも該当しない場合
├ authentication/
│ └ login.vue ----------------- /authentication/login
└ ordering/
  ├ checkout.vue -------------- /ordering/checkout
  └ done/[orderId].vue -------- /ordering/done/:orderId
```

### ルートの属性 {#route-meta}

- 各画面は `definePage()` でルートの属性を宣言します。
  属性の型（ `RouteMeta` ）は app 層で定義します。
- 認証の要否は `meta.requiresAuth` で表します。
  指定のない画面は認証が必要な画面として扱います。
  認証が不要な画面だけが `requiresAuth: false` を明示します。
- 画面内の操作からだけ到達させる画面は `meta.requiresInAppNavigation: true` を宣言します。
  consumer の注文確認画面と注文完了画面が該当します。
  判定は system-common の汎用のガードが行います。

### 画面遷移 {#navigation}

- 画面を遷移させられるのは pages 層と app 層だけです。
  コンテキスト、 business-common 、 system-common は vue-router を型としてだけ参照できます。
- 遷移先はルート名で指定します。
  ルート名とパラメーターの組み合わせは、生成された型で検査されます。
- 画面はパラメーターを、ルート名を指定した `useRoute()` で型付きで取得します。
- 遷移を伴うコンテキストの部品は、イベントを発行するだけにします。
  たとえばログアウトのメニューが該当し、遷移は利用する側が行います。
- 遷移先を必要とする共通処理は、遷移先を引数に取るファクトリー関数として提供します。
  認証のガードとグローバルエラーハンドラーが該当し、 app 層が結線します。
- ログイン後の戻り先は、クエリの `redirect` に遷移先のパス（ `to.fullPath` ）を格納します。
  ログイン画面は `/` で始まり、かつ `//` で始まらない値だけを戻り先として受け入れます。
  外部のサイトへ誘導されること（オープンリダイレクト）を防ぐためです。

### コンテキストの公開 API {#public-api}

`public-api.ts` は、ユースケース単位の少数の入口だけを公開します。

- 公開するものは、ユースケースコンポーザブル、画面に表示する業務の部品、それらの型です。
- 公開しないものは、ルート定義、ルート名、サービスの関数、ストア、入力検証のスキーマです。
  これらはユースケースコンポーザブルの内部に隠します。
- 排他制御の行バージョンと API のリクエストとレスポンスの型は、ユースケースコンポーザブルの外に出しません。
- 業務の知識を持たない汎用の UI 部品は `system-common/components` に置きます。
  確認のモーダルと通知のモーダルが該当します。

ユースケースコンポーザブルは次の形にします。

```typescript
// catalog-management/public-api.ts
export function useCatalogItemEditor(itemId: MaybeRefOrGetter<string>): {
  status: Readonly<Ref<'loading' | 'ready' | 'notFound' | 'failed'>>
  current: Readonly<Ref<CatalogItemSnapshot | undefined>>
  form: CatalogItemForm
  categories: Readonly<Ref<Option[]>>
  brands: Readonly<Ref<Option[]>>
  update(): Promise<UpdateOutcome>
  remove(): Promise<RemoveOutcome>
}

export type UpdateOutcome =
  | { kind: 'updated' }
  | { kind: 'conflict' }
  | { kind: 'notFound' }
  | { kind: 'failed' }
```

- 状態は読み取り専用の `Ref` で公開し、操作は結果（ Outcome ）を返す非同期関数で公開します。
- 業務上想定される結果は、例外ではなく Outcome で返します。
  見つからない場合、競合した場合、受け付けられない場合が該当します。
- 想定外のエラーは共通のエラー処理を通したうえで、 `kind: 'failed'` を返します。
- 通知の文言と遷移先は、 Outcome を得た画面が決めます。

画面はユースケースコンポーザブルを呼び出し、 Outcome を通知と遷移に変換します。

```typescript
// pages/catalog/items/edit/[itemId].vue
const route = useRoute('/catalog/items/edit/[itemId]')
const editor = useCatalogItemEditor(() => route.params.itemId)

const onUpdate = async () => {
  const outcome = await editor.update()
  if (outcome.kind === 'notFound') {
    showToast('更新対象のカタログアイテムが見つかりませんでした。')
    await router.push({ name: '/catalog/items/' })
  } else if (outcome.kind === 'conflict') {
    showToast('カタログアイテムの更新が競合しました。もう一度更新してください。')
  }
}
```

### テスト {#testing}

- ユースケースの振る舞いは、ユースケースコンポーザブルを直接テストします。
  API は MSW で代替し、 Pinia は実物を使います。
  競合した場合と見つからない場合もここで検証します。
- 画面のテストは、 Outcome から通知と遷移への変換と、権限による表示の制御に絞ります。
- 既存の画面のテストのうちユースケースを検証しているものは、ユースケースコンポーザブルのテストへ移します。
  移したテストは画面のテストから削除し、同じ検証を重ねません。
- API の境界にはポートを設けません。
  MSW がローカルの代替として機能し、本番用以外のアダプターに実需がないためです。

### 参照方向の強制 {#lint-rules}

`eslint.project-rules.ts` で次のルールを強制します。

- pages 層は、次を参照できません。
    - `@/app/**`
    - コンテキストの `public-api.ts` 以外のファイル
    - `@/system-common/api-client` と `@/system-common/generated/**`
- コンテキストは、次を参照できません。
    - `@/app/**` と `@/pages/**`
    - 他のコンテキストの `public-api.ts` 以外のファイル
    - vue-router の値（型は参照できます）
- business-common は、 `@/app/**` と `@/pages/**` を参照できません。
  コンテキストと vue-router の値も参照できません。
- system-common は、上記に加えて business-common も参照できません。
- app 層以外は、動的インポート（ `import()` ）を使えません。

- vue-router の型の参照は、 `@typescript-eslint/no-restricted-imports` の `allowTypeImports` で許可します。
- 動的インポートは `no-restricted-syntax` で `ImportExpression` を禁止します。
  file-based routing ではルート定義が自動で生成されるため、手書きの動的インポートは不要です。
- system-common のルーティング定義に設けていた例外と、ルート名の集約モジュール（ `route-names.ts` ）は廃止します。

## 検討した選択肢 {#considered-options}

- ルート定義をコンテキストに残す（ ADR 0001 ）
    - コンテキストと画面の相互依存が残るため、採用しません。
- 画面を所有するコンテキストの中に置く
    - 現在のコードには、複数のコンテキストを組み合わせる画面がほとんどありません。
    - しかし本アプリケーションはエンタープライズアプリケーションのサンプルです。
      画面数の増加を見込み、 URL を軸とした標準の構成を優先します。
- 画面遷移をポート（ Navigator ）として注入する
    - 遷移先は生成された型で十分に検査できます。
    - 本番用以外のアダプターに実需がないため、採用しません。
- 規約だけを合わせてルート表を手書きする
    - file-based routing は vue-router 5 に組み込まれており、依存の追加なしで導入できます。
    - 手書きのルート表は、画面の配置との二重管理になるため採用しません。
- `*Page.vue` の命名を維持する
    - プラグインの設定で吸収できますが、標準の規約から外れるため採用しません。

## 影響 {#consequences}

- 次の順で移行します。
    1. file-based routing を導入し、画面の配置、ルートの属性、画面遷移、ガードを切り替えます。
       画面の振る舞いは変えません。
    1. ユースケースコンポーザブルを導入し、 `public-api.ts` を絞り込み、テストを移します。
    1. 参照方向のルールを更新します。
- すべてのルート名が変わります。
  ログイン後の戻り先を表すクエリの形式も変わります。
- 認証の要否は、指定のない画面を認証が必要な画面として扱う方式に変わります。
- `documents` 配下のガイドのうち、フロントエンドのフォルダー構成とアーキテクチャの説明は別途更新します。
