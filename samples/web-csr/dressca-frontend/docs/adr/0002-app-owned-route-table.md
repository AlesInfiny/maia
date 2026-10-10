---
title: ADR 0002 画面とルーティングの構成
description: 画面を src/pages に置き、 app 層のルート表でルーティングを定義する構成を定めます。
status: accepted
---

# 画面とルーティングを app 層と pages 層に集約する {#top}

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
画面数の増加とコンテキストをまたぐ画面の追加を前提に、ルーティングをまとめて管理できる構成を採用します。

また、フォルダーごとに保守するチームを次のように想定します。
保守するチームがすぐにわかるように、どのチームの持ち物でもないフォルダーは作りません。

- pages とコンテキストは、各業務チームが保守します。
- business-common は、業務共通チームが保守します。
- system-common は、システム共通チーム（または業務共通チームの兼務）が保守します。

## 決定 {#decision}

### 層の構成 {#layers}

上の層から下の層への参照だけを許可します。

- app 層（ `src/main.ts` 、 `src/App.vue` 、 `src/business-common/router/` ）
    - ルーターの生成、ガードの登録、画面遷移を伴う共通処理の結線を担います。
    - 業務共通チームが保守します。
      そのため、ルーティング（ `src/business-common/router/` ）は business-common のフォルダーに置きます。
    - `src/business-common/router/` は business-common のフォルダーにありますが、 business-common 層ではなく app 層に属します。
      参照方向は、フォルダーではなく層に従います。
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

- ルート定義は、 app 層のルート表（ `src/business-common/router/routes.ts` ）に手書きで集約します。
  画面の配置と URL の対応、ルートの属性を、このファイルだけで確認できます。
- ルート表は画面を動的インポートで参照し、画面ごとにコードを分割して遅延読み込みします。
- ルート名は、 app 層の定数（ `src/business-common/router/route-names.ts` の `routeNames` ）で定義します。
  ルート名は、意味を表す名前（例: `catalog-items-edit` ）にします。
- 同じファイルで vue-router の型（ `TypesConfig` の `RouteNamedMap` ）を拡張します。
  ルート名ごとのパスとパラメーターを型で宣言し、遷移先を型で検査します。
- ルート表にルートを追加したときは、ルート名の定数と型にも追加します。
- 画面を追加するときは、画面を作る業務チームがルート表とルート名の定数に直接追記し、業務共通チームがレビューします。
  ルートの属性（認証の要否など）の設定漏れは、このレビューで確認します。

### 画面ファイルの命名 {#naming}

- 画面ファイルの名前は `<画面名>Page.vue` にします。
- `src/pages/` のフォルダー構成は、 URL のパスのセグメントと対応させます。
- `:itemId` のような動的セグメントはフォルダーにしません。

admin の画面の配置は次のとおりです。

```text linenums="0"
src/pages/
├ HomePage.vue ---------------------- /
├ ErrorPage.vue --------------------- /error
├ NotFoundPage.vue ------------------ どのルートにも該当しない場合
├ authentication/login/
│ └ LoginPage.vue ------------------- /authentication/login
└ catalog/items/
  ├ ItemsPage.vue ------------------- /catalog/items
  ├ add/ItemsAddPage.vue ------------ /catalog/items/add
  └ edit/ItemsEditPage.vue ---------- /catalog/items/edit/:itemId
```

consumer の画面の配置は次のとおりです。

```text linenums="0"
src/pages/
├ DisplayItemPage.vue --------------- /
├ ErrorPage.vue --------------------- /error
├ NotFoundPage.vue ------------------ どのルートにも該当しない場合
├ authentication/login/
│ └ LoginPage.vue ------------------- /authentication/login
├ basket/
│ └ BasketPage.vue ------------------ /basket
└ ordering/
  ├ checkout/CheckoutPage.vue ------- /ordering/checkout
  └ done/DonePage.vue --------------- /ordering/done/:orderId
```

### ルートの属性 {#route-meta}

- ルートの属性は、 app 層のルート表の `meta` で宣言します。
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
- 遷移先は、 app 層のルート名の定数で指定します。
  ルート名とパラメーターの組み合わせは、拡張した型で検査されます。
- pages 層が参照できる app 層のモジュールは、ルート名の定数（ `@/business-common/router/route-names` ）だけです。
- 画面は、パラメーターをルート名を指定した `useRoute()` で型付きで取得します。
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
  | { kind: 'canceled' }
```

- 状態は読み取り専用の `Ref` で公開し、操作は結果（ Outcome ）を返す非同期関数で公開します。
- 業務上想定される結果は、例外ではなく Outcome で返します。
  見つからない場合、競合した場合、受け付けられない場合が該当します。
- 想定外のエラーは共通のエラー処理を通したうえで、 `kind: 'failed'` を返します。
- ログアウトなどの利用者の操作で通信が中断された場合は、 `kind: 'canceled'` を返します。
  利用者が意図した中断のため、画面は通知しません。
- 通知の文言と遷移先は、 Outcome を得た画面が決めます。

画面はユースケースコンポーザブルを呼び出し、 Outcome を通知と遷移に変換します。
ルートのパラメーターは、ゲッターではなく値で渡します。
ゲッターで渡すと、他の画面へ遷移したとき、画面が破棄される前に空のパラメーターで読み込み直してしまうためです。

```typescript
// pages/catalog/items/edit/ItemsEditPage.vue
const route = useRoute(routeNames.catalogItemsEdit)
const editor = useCatalogItemEditor(route.params.itemId)

const onUpdate = async () => {
  const outcome = await editor.update()
  if (outcome.kind === 'notFound') {
    showToast('更新対象のカタログアイテムが見つかりませんでした。')
    await router.push({ name: routeNames.catalogItems })
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

- app 層のルーティング（ `src/business-common/router/` ）には、 business-common の制限を適用しません。
  app 層は最上位の層のため、参照の制限を設けません。
- pages 層は、次を参照できません。
    - app 層のルーティング（ `@/business-common/router` と `@/business-common/router/**` 。ルート名の定数 `@/business-common/router/route-names` を除く）
    - コンテキストの `public-api.ts` 以外のファイル
    - `@/system-common/api-client` と `@/system-common/generated/**`
- pages 層のテストは、例外としてルーター（ `@/business-common/router` ）と API の型（ `@/system-common/generated/**` ）を参照できます。
  画面を遷移させるためと、 API のレスポンスを組み立てるためです。
- コンテキストは、次を参照できません。
    - app 層のルーティングと `@/pages/**`
    - 他のコンテキストの `public-api.ts` 以外のファイル
    - vue-router の値（型は参照できます）
- business-common （ `src/business-common/router/` を除く）は、 app 層のルーティングと `@/pages/**` を参照できません。
  コンテキストと vue-router の値も参照できません。
- system-common は、上記に加えて business-common も参照できません。
- app 層以外は、動的インポート（ `import()` ）を使えません。

- vue-router の型の参照は、 `@typescript-eslint/no-restricted-imports` の `allowTypeImports` で許可します。
- 動的インポートは `no-restricted-syntax` で `ImportExpression` を禁止します。
  画面を動的インポートするのは app 層のルート表だけです。
- system-common のルーティング定義に設けていた例外と、ルート名の集約モジュール（ `route-names.ts` ）は廃止します。

## 検討した選択肢 {#considered-options}

- ルート定義をコンテキストに残す（ ADR 0001 ）
    - コンテキストと画面の相互依存が残るため、採用しません。
- 画面を所有するコンテキストの中に置く
    - 現在のコードには、複数のコンテキストを組み合わせる画面がほとんどありません。
    - しかし本アプリケーションはエンタープライズアプリケーションのサンプルです。
      画面数の増加を見込み、 URL を軸とした標準の構成を優先します。
- 画面遷移をポート（ Navigator ）として注入する
    - 遷移先は、拡張した型で十分に検査できます。
    - 本番用以外のアダプターに実需がないため、採用しません。
- vue-router 5 に組み込まれた file-based routing を使う
    - 依存の追加なしで導入でき、ルート定義とルート名の型を画面の配置から自動で生成できます。
    - 一方で、ルートの属性が各画面に分散し、認証の要否をまとめて確認できなくなります。
    - また、画面ファイルの名前が URL の規約（ `index.vue` 、 `[itemId].vue` など）に縛られます。
    - ルートの属性をまとめて監査できることを優先し、採用しません。
- ルーティングを app 層専用のフォルダー（ `src/app/` ）に置く
    - フォルダーと層が一致し、参照方向の規則をフォルダー単位で書けます。
    - 一方で、 `src/app/` をどのチームが保守するのかがわかりにくくなります。
    - 保守するチームをフォルダーで表すことを優先し、採用しません。
- ルーティングを business-common 層の規則のまま `src/business-common/router/` に置く
    - ルート表の動的インポート、 vue-router の値、 security の参照が、 business-common の規則に違反します。
    - 違反を個別の例外で許可すると、廃止した system-common のルーティング定義の例外と同じ形になるため、採用しません。
- business-common 層全体の参照の制限を緩める
    - 業務の部品やストアからも、画面やコンテキストを参照できるようになるため、採用しません。
- ルート名の定数と `meta` の型だけを business-common に置く
    - コンテキストからルート名を参照しても、参照方向の規則で検出できません。
    - ルート表とルーターの生成は app 層のフォルダーに残るため、採用しません。
- ルート名の定数を `src/pages/route-names.ts` に置く
    - 参照方向の規則を変えずに、 pages から app 層への参照をなくせます。
    - 一方で、ルート名を保守するチームが業務チームになり、ルート表は app 層のフォルダーに残るため、採用しません。

検討の経緯と、各案で参照方向の規則に違反した件数は [Issue #5673 のコメント](https://github.com/AlesInfiny/maia/issues/5673#issuecomment-6095614298) に記録しています。

## 影響 {#consequences}

- 次の順で移行します。
    1. ルート表を app 層へ移し、ルートの属性、画面遷移、ガードを切り替えます。
       画面の振る舞いは変えません。
    1. ユースケースコンポーザブルを導入し、 `public-api.ts` を絞り込み、テストを移します。
    1. 参照方向のルールを更新します。
- すべてのルート名が変わります。
  ログイン後の戻り先を表すクエリの形式も変わります。
- 認証の要否は、指定のない画面を認証が必要な画面として扱う方式に変わります。
- business-common のフォルダーの中に、参照の制限が異なる区画（ `src/business-common/router/` ）ができます。
  ESLint の規則は `@/` のエイリアスで書いたパスだけを検査するため、 business-common のほかのフォルダーから相対パスでルーティングを参照しても検出できません。
- `documents` 配下のガイドのうち、フロントエンドのフォルダー構成とアーキテクチャの説明は別途更新します。
