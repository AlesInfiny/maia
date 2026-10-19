---
title: CSR 編
description: クライアントサイドレンダリングを行う Web アプリケーションのアーキテクチャについて解説します。
---

# フロントエンドアプリケーションのアーキテクチャ {#top}

## 技術スタック {#technology-stack}

AlesInfiny Maia OSS Edition （以降、 AlesInfiny Maia ）を構成する OSS を以下に示します。

![OSS 構成要素](../../../images/app-architecture/client-side-rendering/oss-components-light.png#only-light){ loading=lazy }
![OSS 構成要素](../../../images/app-architecture/client-side-rendering/oss-components-dark.png#only-dark){ loading=lazy }

!!! note ""

    上の図で使用している OSS 製品名およびロゴのクレジット情報は [こちら](../../../about-maia/credits.md) を参照してください。

利用ライブラリの一覧については、 [技術スタック](../csr-architecture-overview.md#technology-stack) を参照してください。

## アーキテクチャ {#frontend-architecture}

### MVVMパターン {#mvvm-pattern}

AlesInfiny Maia で採用している Vue.js のソフトウェア・アーキテクチャは MVVM パターンに分類されます。
以下にアーキテクチャを示します。

![フロントエンド コンポーネント構成](../../../images/app-architecture/client-side-rendering/frontend-architecture-light.png#only-light){ loading=lazy }
![フロントエンド コンポーネント構成](../../../images/app-architecture/client-side-rendering/frontend-architecture-dark.png#only-dark){ loading=lazy }

<!-- markdownlint-disable-next-line no-emphasis-as-heading -->
**ビュー**

:  ブラウザーへのレンダリングおよびブラウザーからのイベントの待ち受けを役割として担います。ビューには UI の構造やスタイルを定義します。

<!-- markdownlint-disable-next-line no-emphasis-as-heading -->
**ビューモデル**

:  ブラウザーからのイベントを受け、プレゼンテーションロジックを実行します。ビューモデルのプレゼンテーションロジックには、レンダリングに必要な処理や入力チェック、モデルを通じたデータの取得や更新などの処理を実装します。

<!-- markdownlint-disable-next-line no-emphasis-as-heading -->
**モデル**

:  状態管理やブラウザー外部との入出力を担い、データ構造やデータの状態管理、 Web API 呼び出しや Web API 呼び出し結果のハンドリングなどの処理を実装します。

<!-- textlint-disable -->
Vue.js ではビューとビューモデルを [単一ファイルコンポーネント(SFC) :material-open-in-new:](https://ja.vuejs.org/guide/scaling-up/sfc){ target=_blank } と呼ばれる同一のファイル(拡張子.vue)に記述できるので、図ではビュー&ビューモデルと表現しています。
<!-- textlint-enable -->

### ビュー＆ビューモデル コンポーネント {#view-and-viewmodel-component}

![MVVM パターン ビュー＆ビューモデル](../../../images/app-architecture/client-side-rendering/view%26viewmodel-component-light.png#only-light){ loading=lazy }
![MVVM パターン ビュー＆ビューモデル](../../../images/app-architecture/client-side-rendering/view%26viewmodel-component-dark.png#only-dark){ loading=lazy }

ビューとビューモデルはそれぞれブラウザーへのレンダリングとそのブラウザーから受けたイベントに対するプレゼンテーションロジックなどを行うコンポーネントです。
ブラウザーに表示する画面は Component という複数の画面構成要素と View というそれらを組み合わせたページから構成されます。
これらの画面コンポーネントが、デザインやデータバインドなどの画面表示（ビュー）と、イベント処理や入力処理などの画面要素に対する処理（ビューモデル）を持っています。

#### 画面コンポーネント {#screen-components}

Vue.js はコンポーネント指向のフレームワークであることから画面要素を Component という再利用可能な単位で分割し、複数の画面コンポーネントを組み合わせることによってひとつの画面(View)を構成します。
View がルーティングによって遷移される画面として指定されます。画面コンポーネントは実際の画面では以下のようなイメージになります。

![画面コンポーネント イメージ](../../../images/app-architecture/client-side-rendering/screen-component-detail-light.png#only-light){ loading=lazy }
![画面コンポーネント イメージ](../../../images/app-architecture/client-side-rendering/screen-component-detail-dark.png#only-dark){ loading=lazy }

#### 画面遷移 {#screen-transition}

画面遷移には、 Vue Router という Vue.js の拡張ライブラリを利用します。 Vue Router はルーティング定義に基づいて遷移先の画面コンポーネントを特定し、表示する画面コンポーネントを切り替えることで画面遷移を実現します。 Vue Router による画面遷移はフロントエンドのみで完結するためバックエンドへ通信しません。また AlesInfiny Maia では、「View」を切り替えの単位としています。

Vue Router: [公式ドキュメント :material-open-in-new:](https://router.vuejs.org/introduction.html){ target=_blank }

![Vue Router によるルーティング](../../../images/app-architecture/client-side-rendering/routing-by-vue-router-light.png#only-light){ loading=lazy }

![Vue Router によるルーティング](../../../images/app-architecture/client-side-rendering/routing-by-vue-router-dark.png#only-dark){ loading=lazy }

#### モデルコンポーネントとの連携 {#linkage-with-model-component}

Vue.js ではバックエンドアプリケーションとの連携をモデルが行います。そのため、ユーザーが行う画面コンポーネントからの処理や入力情報をモデルに連携する必要があります。この連携ではビューモデルのプレゼンテーションロジックから、後述するモデルコンポーネントの Service や Store の Action を呼び出すことで、データの取得・更新をします。

#### フロント入力チェック {#input-validation}

文字種や文字数などの入力チェックは、ビューモデルで行い、不要なバックエンドとの通信の発生を防止します。  AlesInfiny Maia では VeeValidate と Zod という OSS ライブラリを利用します。 VeeValidate はフォームや入力コンポーネントを監視し、 Zod は検証スキーマを定義する OSS です。

![VeeValidate と Zod による入力チェック](../../../images/app-architecture/client-side-rendering/input-validation-light.png#only-light){ loading=lazy }
![VeeValidate と Zod による入力チェック](../../../images/app-architecture/client-side-rendering/input-validation-dark.png#only-dark){ loading=lazy }

### モデルコンポーネント {#model-component}

![MVVM パターン モデル](../../../images/app-architecture/client-side-rendering/model-component-light.png#only-light){ loading=lazy }
![MVVM パターン モデル](../../../images/app-architecture/client-side-rendering/model-component-dark.png#only-dark){ loading=lazy }

モデルはデータの状態管理や画面(ビュー)へのデータ連携、 Web API の呼び出しおよびハンドリングなどの役割を持つコンポーネントです。モデルは以下の要素で構成されます。またフロントエンドで扱うデータモデルと API モデルとの乖離を吸収し、扱いやすい状態に加工する役割も持ちます。

- Service: ビューモデルからのリクエストに対して、 Store の呼び出し、 Web API の呼び出しなどデータの連携に必要な処理をします。
- Store: フロントエンドで扱う状態を保持するコンテナです。 AlesInfiny Maia では Pinia という Vue.js の Store ライブラリを利用して管理します。

Pinia: [公式ドキュメント :material-open-in-new:](https://pinia.vuejs.org/introduction.html){ target=_blank }

ただし、このモデルの構成は複雑な状態管理をするアプリケーションを想定しており、小規模なアプリケーションや状態管理を必要としないページの場合は、 Service やモデルを省略することも考えられます。この場合は、ビューモデルから直接 Web API を呼び出します。

#### Storeの構成要素 {#store-structure}

Pinia における Store は、 State・Getter・Action という 3 つの要素から構成されています。

![Pinia のアーキテクチャ](../../../images/app-architecture/client-side-rendering/pinia-architecture-light.png#only-light){ loading=lazy }
![Pinia のアーキテクチャ](../../../images/app-architecture/client-side-rendering/pinia-architecture-dark.png#only-dark){ loading=lazy }

| 要素   | 説                                                                                                                                                                                   |
| ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| State  | Store で管理するデータそのもの。                                                                                                                                                     |
| Getter | State の値や State から算出した結果を返すもの。                                                                                                                                      |
| Action | Store で管理しているデータである State に対して変更を行うもの。また API の呼び出しや API のレスポンスのハンドリングを行うもの。原則として、 State の変更を伴わない処理を持たせない。 |

Store は State をグローバルなシングルトンとして管理します。そのため本来 State は直接取得・更新ができますが、 Getter と Action を通じてアクセスするルールを設けて State の参照・更新を制御し、データの一貫性を持つことが重要です。

#### State の更新 {#update-state}

State の更新は Action を利用します。この際、ビューモデルから Service を経由して Action を呼び出すことで、 State の更新を一元管理します。

#### State の参照 {#get-state}

State の参照には Getter を利用します。 Getter は State を参照できますが、 State の値は変更できません。そのため安全に State の値を参照できます。

#### バックエンドとのAPI連携 {#communicate-with-backend}

AlesInfiny Maia では API 仕様を OpenAPI を用いて作成します。ここには API の機能が説明されており、フロントエンドエンジニアとバックエンドエンジニアの間で API 設計に乖離が生じないようにします。
また [OpenAPI generator :material-open-in-new:](https://github.com/OpenAPITools/openapi-generator){ target=_blank } というツールを利用して、 API クライアントコードを自動生成できます。
AlesInfiny Maia ではクライアント API アクセス方式に、 Promise ベースでリクエストの設定が容易である Axios を採用しています。

OpenAPI: [公式ドキュメント :material-open-in-new:](https://swagger.io/specification/){ target=_blank }

Axios: [github :material-open-in-new:](https://github.com/axios/axios){ target=_blank }

![OpenAPIを利用したバックエンドとの連携](../../../images/app-architecture/overview/client-side-rendering-maia-light.png#only-light){ loading-lazy }
![OpenAPIを利用したバックエンドとの連携](../../../images/app-architecture/overview/client-side-rendering-maia-dark.png#only-dark){ loading-lazy }

!!! note ""

    上の図で使用している OSS 製品名およびロゴのクレジット情報は [こちら](../../../about-maia/credits.md) を参照してください。

!!! note "OpenAPI Generator の自動生成コード"
      OpenAPI Generator はサーバー、クライアント双方の様々なコードの自動生成に対応しています。生成可能なコードについては公式ドキュメントを参照してください。

      - [OpenAPI Generator: Generators List :material-open-in-new:](https://openapi-generator.tech/docs/generators){ target=_blank }

<!-- バックエンド編のAPIドキュメントへリンク -->

## フォルダー構成 {#project-structure}

`src` フォルダーの下は、 `views` や `services` といった種類別のフォルダーではなく、業務の関心事を単位に構成します。
種類別のフォルダーを最上位に置く構成と比べると、ひとつの機能に関わるコードがひとつのフォルダーにまとまります。
機能を追加するときに触るフォルダーが限られ、削除するときもコードの取り残しが起こりにくくなります。

フォルダーの階層を説明するために、以下の用語を使います。

- **core 層**: `main.ts` 、 `App.vue` 、 `core` フォルダーからなる最上位の層です。ルーターの生成と、共通処理の結線を担います。
- **画面**: `pages` フォルダーに置く、ルーティングの対象となるコンポーネントです。
- **コンテキスト**: `src` の直下に置く、業務上のまとまりです。境界付けられたコンテキスト（ Bounded Context ）に相当し、同じ用語が同じ意味で通じる範囲を表します。
- **ドメイン**: コンテキストの直下に置くフォルダーです。買い物かごや認証のように、ひとつの業務の関心事を表します。
- **公開 API**: コンテキストの直下に置く `public-api.ts` です。 pages と core 層からコンテキストを参照する入口です。
- **ユースケースコンポーザブル**: ひとつのユースケースの状態と操作をまとめて提供するコンポーザブルです。操作は結果（ Outcome ）を返します。
- **結果（ Outcome ）**: ユースケースの操作の帰結を、画面が分岐できる種類（ `kind` ）で表した値です。
- **システム共通**: 業務知識を持たないコードを置く層です。 `system-common` フォルダーに対応します。
- **業務共通**: 業務知識を持ち、フロントエンドに持ち主となるドメインがないコードを置く層です。 `business-common` フォルダーに対応します。

ここでいう**業務知識**とは、業務のルールや業務で使う用語に依存する知識のことです。
システム共通と業務共通をまとめて、**共通層**と呼びます。
core 層、 pages 、コンテキスト、業務共通、システム共通は、 [参照方向](#reference-direction) の段を表すので「層」と呼びます。
ドメインの下に置く `composables` や `stores` などは、層と区別して「ドメイン内のフォルダー」と呼びます。

```text title="プロジェクトのフォルダー構成全体像" linenums="0"
<project-name>
├─ e2e/ ---------------------- Playwright による E2E テストに関するファイルを格納します。
├─ public/ ------------------- ファイル名を変えずに配信するファイル（ favicon など）を格納します。
├─ src/
│  ├─ core/ ------------------ core 層のコードを格納します。グローバルスタイルもここに置きます。
│  ├─ pages/ ----------------- 画面を格納します。
│  ├─ system-common/ --------- システム共通のコードを格納します。
│  ├─ business-common/ ------- 業務共通のコードを格納します。
│  ├─ <context>/ ------------- コンテキストです。公開 API とドメインのフォルダーを格納します。
│  │  ├─ <domain>/ ----------- ドメインです。 composables や services などのフォルダーを格納します。
│  │  └─ public-api.ts ------- 公開 API です。
│  ├─ App.vue
│  └─ main.ts
├─ index.html
└─ package.json
```

`src` の直下のフォルダーは、すべていずれかの層に属します。
CSS や画像の置き場所は、 [グローバルスタイルとアセットの置き場所](#styles-and-assets) に示します。

### サンプルアプリケーションの構成例 {#sample-structure}

サンプルアプリケーションの consumer と admin は、それぞれ以下の構成になっています。

```text title="consumer のフォルダー構成" linenums="0"
src/
├─ core/ --------------------- core 層
│  ├─ router/ ---------------- ルート表、ルート名の定数、ナビゲーションガードの結線を格納します。
│  └─ styles/ ---------------- グローバルスタイルを格納します。
├─ pages/ -------------------- 画面
│  ├─ authentication/
│  ├─ basket/
│  ├─ ordering/
│  ├─ DisplayItemPage.vue
│  ├─ ErrorPage.vue
│  └─ NotFoundPage.vue
├─ system-common/ ------------ システム共通
│  ├─ api-client/ ------------ API クライアントの設定ファイルを格納します。
│  ├─ components/
│  ├─ error-handler/ --------- グローバルエラーハンドラーと、共通のエラー処理を格納します。
│  ├─ events/ ---------------- イベントバスの定義を格納します。
│  ├─ generated/ ------------- 自動生成されたファイルを格納します。
│  ├─ guards/ ---------------- 業務知識を持たないナビゲーションガードを格納します。
│  ├─ helpers/
│  ├─ locales/ --------------- メッセージ管理に関するファイルを格納します。
│  ├─ logger/
│  ├─ types/
│  └─ validation/
├─ business-common/ ---------- 業務共通
│  ├─ components/
│  ├─ composables/
│  ├─ helpers/
│  └─ stores/
├─ shopping/ ----------------- 買い物のコンテキスト
│  ├─ display-item/ ---------- 陳列品のドメイン
│  │  ├─ components/
│  │  ├─ composables/
│  │  ├─ models/
│  │  └─ services/
│  ├─ basket/ ---------------- 買い物かごのドメイン
│  ├─ ordering/ -------------- 注文のドメイン
│  └─ public-api.ts
├─ security/ ----------------- セキュリティのコンテキスト
│  ├─ authentication/ -------- 認証のドメイン
│  └─ public-api.ts
├─ App.vue
└─ main.ts
```

```text title="admin のフォルダー構成" linenums="0"
src/
├─ core/ --------------------- core 層
├─ pages/ -------------------- 画面（ホーム画面を含みます）
├─ system-common/ ------------ システム共通
├─ business-common/ ---------- 業務共通
├─ catalog-management/ ------- カタログ管理のコンテキスト
│  ├─ catalog/ --------------- カタログのドメイン
│  └─ public-api.ts
├─ security/ ----------------- セキュリティのコンテキスト
│  ├─ authentication/ -------- 認証のドメイン
│  ├─ authorization/ --------- 認可のドメイン
│  └─ public-api.ts
├─ App.vue
└─ main.ts
```

2 つのアプリケーションには、以降で説明する同じ規則を当てはめています。
それでも構成が異なるのは、扱う業務が異なるためです。

| 項目                          | consumer                                                   | admin                                                       |
| ----------------------------- | ---------------------------------------------------------- | ----------------------------------------------------------- |
| 商品を扱うドメイン            | `shopping/display-item` （陳列品を見て買い物かごに入れる） | `catalog-management/catalog` （商品を登録、更新、削除する） |
| 認可のドメイン                | なし                                                       | `security/authorization`                                    |
| メッセージ管理（ `locales` ） | あり                                                       | なし（メッセージ管理のライブラリを導入していないため）      |

機能を追加するときにどのフォルダーへ置くかは、以降の 5 つの節（参照方向、コンテキストとドメインの置き方、共通層に置くかどうかの判断基準、ドメイン内のフォルダー、命名）に示す規則で決まります。

### 参照方向 {#reference-direction}

参照は、 core 層からシステム共通へ向かう一方向とし、逆向きの参照は禁止します。

```text title="参照方向" linenums="0"
core 層（ core 、 App.vue 、 main.ts ）
      ↓
pages
      ↓
コンテキスト
      ↓
業務共通（ business-common ）
      ↓
システム共通（ system-common ）
```

参照される層ほど業務知識が少なくなるように並べています。
参照を一方向に限ると、変更の影響は参照する側へだけ伝わります。
業務の変更が、業務知識を持たない部品に波及しません。

この規則は ESLint の `@typescript-eslint/no-restricted-imports` で強制します。
動的インポートだけは `no-restricted-syntax` で禁止します。
設定方法は [静的コード分析とフォーマット - フォルダー間の参照方向の強制](../../../guidebooks/how-to-develop/csr/vue-js/static-verification-and-format.md#layer-dependency-rules) を参照してください。

層の間の参照には、以下の制限も加えます。

- core 層は最上位の層です。アプリケーションを組み立てるためにすべての層を参照するので、参照を制限しません。
- pages から core 層へは、ルート名の定数（ `@/core/router/route-names` ）だけを参照できます。
- pages は API クライアントと API の型を参照できません。 API の呼び出しは、コンテキストのユースケースコンポーザブルが扱います。
- 画面を遷移させられるのは、 pages と core 層だけです。それ以外の層は、 vue-router の型だけを参照できます。遷移先が必要な部品は、遷移先を引数で受け取ります。
- 動的インポート（ `import()` ）は、 core 層だけで使用します。画面の遅延読み込みは、 core 層のルート表で定義します。

たとえば、システム共通のグローバルエラーハンドラーは、エラー画面へ遷移する関数を core 層の `main.ts` から引数で受け取ります。
システム共通はエラー画面のルート名を知らずに済み、参照方向を守れます。

動的インポートは、 ESLint の import の検査の対象になりません。
core 層以外で使用できると、参照方向の規則を迂回できるため、使用できる層を限ります。

なお、 ESLint の規則は `@/` で始まるパスに対して設定しています。
相対パスでコンテキストの外を参照すると、規則に違反していても検出できません。
コンテキストの外は、 [コンテキストとドメインの置き方](#context-criteria) に示すとおり `@/` で参照してください。

### コンテキストとドメインの置き方 {#context-criteria}

ドメインのフォルダーは、必ずコンテキストの下に置きます。
コンテキストにドメインがひとつしかない場合も同様です。
コンテキストを省略できるとすると、 `src` の直下にあるフォルダーがコンテキストなのかドメインなのかを、中身を見なければ判断できなくなるためです。

コンテキストを参照できるのは、 pages と core 層だけです。
参照は公開 API （ `public-api.ts` ）を経由し、コンテキストの内部のフォルダーを直接参照しません。
コンテキストは、他のコンテキストを参照しません。
他のコンテキストの公開 API も参照できません。

コンテキストを組み合わせる処理は、画面か core 層に書きます。
たとえば admin のカタログアイテム編集画面は、カタログ管理のコンテキストの `useCatalogItemEditor` と、セキュリティのコンテキストの `useAuthorization` を組み合わせます。
そのうえで、管理者のロールを持つユーザーにだけ、更新と削除のボタンを有効にしています。

コンテキストを独立させると、ひとつのコンテキストの変更が他のコンテキストに波及しません。
組み合わせを画面と core 層に集めると、コンテキストの間の依存がどこにあるかを、画面と core 層を見るだけで把握できます。
また、参照の入口を公開 API に限ると、コンテキストの内部のフォルダー構成を変えても、参照元を修正する必要がありません。

公開 API で公開するのは、以下のものだけです。

- ユースケースコンポーザブル
- 画面に表示する業務の部品（コンポーネント）
- core 層が結線するガードのファクトリー（遷移先は引数で受け取ります）
- それらの型

ストアやサービスは公開しません。
ストアを公開すると、画面がユースケースの操作を経由せずに状態を書き換えられるようになるためです。

```typescript title="admin のカタログ管理のコンテキストの public-api.ts"
--8<-- "samples/web-csr/dressca-frontend/admin/src/catalog-management/public-api.ts"
```

同じコンテキストの中は、相対パスで参照します。
同じコンテキストの中であれば、ドメインをまたぐ参照も制限しません。
たとえば consumer の注文のドメイン（ `shopping/ordering` ）は、注文する商品を得るために、買い物かごのドメイン（ `shopping/basket` ）を参照しています。
一方、コンテキストの外は `@/` で参照します。
書き方を分けておくと、 `@/` で始まる参照だけを ESLint で検査すれば、コンテキストの外への参照をすべて検査できます。

画面を遷移させるときは、遷移先のルート名を文字列で書かず、 core 層のルート名の定数（ `@/core/router/route-names` ）を参照します。
ルート名の文字列を変更しても遷移元を修正する必要がなく、定数名の誤りは型検査で検出できるためです。

### 共通層に置くかどうかの判断基準 {#common-layer-criteria}

コードをどこに置くかは、以下の表を上から順に当てはめて判断します。

| 判断                                                         | 配置先                            |
| ------------------------------------------------------------ | --------------------------------- |
| 業務知識を持たない                                           | システム共通（ `system-common` ） |
| 業務知識を持ち、持ち主となるドメインがある                   | そのドメイン                      |
| 業務知識を持つが、フロントエンドに持ち主となるドメインがない | 業務共通（ `business-common` ）   |

業務知識を持たないコードは、参照元がひとつのドメインだけでもシステム共通に置きます。
たとえば consumer の入力チェックの定義（ `system-common/validation/validation-items.ts` ）は、現時点では認証のドメインからしか参照されません。
それでも、業務に依存しない汎用的なチェックなので、他のドメインからも再利用できるようシステム共通に置いています。

業務知識を持つコードは、参照元が多くても、持ち主となるドメインがあればそのドメインに置きます。
たとえば admin の認可のドメイン（ `security/authorization` ）は、同じコンテキストの認証のドメインに加えて、カタログ管理の画面からも参照されます。
それでもユーザーのロールという固有のモデルを持つので、業務共通へは移しません。

業務共通に置くのは、業務知識を持ちながら、フロントエンドに持ち主となるドメインがないコードです。
たとえば通知の表示は、どのドメインの操作の結果も通知するので、特定のドメインに属しません。
アセットの URL を生成する処理は、バックエンドではアセット管理のモジュールに属します。
しかしフロントエンドにはアセット管理の画面や状態がないため、ドメインを作らず業務共通に置いています。

参照元の数だけで判断すると、共通層は「よく使われるものの置き場」になり、業務知識が持ち主のドメインから離れて散らばります。
旧構成の `shared` フォルダーがその例で、「アプリケーション全体で再利用する共通機能」と定義され、参照の多さだけが基準になっていました。

### ドメイン内のフォルダー {#domain-folders}

ドメインの下には、以下のフォルダーを必要に応じて置きます。

| フォルダー    | 格納するもの                                                     |
| ------------- | ---------------------------------------------------------------- |
| `composables` | ユースケースコンポーザブルなど、このドメインのコンポーザブル。   |
| `components`  | 画面を構成する、このドメインの vue コンポーネント。              |
| `services`    | Web API の呼び出しなど、 Vue に依存しない処理。                  |
| `stores`      | Pinia の Store 。                                                |
| `validation`  | このドメインの入力チェックの定義。                               |

これらは代表例です。
ドメイン固有の関心事があれば、フォルダーを追加して構いません。
たとえば admin の認可のドメインには、ロールの定義を置く `constants` フォルダーがあります。
両アプリケーションの認証のドメインには、ナビゲーションガードを置く `guards` フォルダーがあります。
consumer の買い物のコンテキストの各ドメインには、フロントエンドで扱うデータモデルを置く `models` フォルダーがあります。

画面とルーティングは、ドメインに置きません。
画面は `pages` フォルダーに、ルーティングは core 層に置きます。
画面とルーティングを URL に沿って一か所に集めると、ドメインの構成と URL の構成を、独立に変えられるためです。

`services` フォルダーのコードは、 Vue に依存させません。
Pinia 、 vue-router 、ストア、コンポーザブルも同様です。
Vue に依存する処理は `composables` フォルダーに置きます。
`services` のコードを Vue に依存させないと、 Vue のアプリケーションを起動せずに単体でテストできます。

画面は、ユースケースコンポーザブルを通じてコンテキストの機能を使います。
ユースケースコンポーザブルの操作は、業務例外を捕捉して結果（ Outcome ）で返します。
画面は結果の種類に応じて、通知の文言と遷移先を決めます。
業務例外の扱い方は、 [カスタムエラーの設定](../../../guidebooks/how-to-develop/csr/vue-js/custom-error-settings.md) を参照してください。

これに対して、共通層の下に置くフォルダーは、 [サンプルアプリケーションの構成例](#sample-structure) に示した種類から、必要なものだけを作ります。
共通層はアプリケーション全体から参照されるので、フォルダーの種類を増やすと影響範囲も広がります。
種類を増やす場合は、プロジェクトで合意してください。

### 命名 {#naming-rules}

コンテキストのフォルダー名は、バックエンドのモジュール名をもとに決めて構いませんが、一致させる必要はありません。
たとえば admin の `catalog-management` は、バックエンドの `catalogmanagement` モジュールに合わせた名前です。
一方で `security` に対応するモジュールはバックエンドにありません。
バックエンドには認可のモジュールがありますが、フロントエンドでは認証と認可をまとめてひとつのコンテキストとして扱っているためです。

ドメインのフォルダー名は、フロントエンドのユースケースで決めます。
たとえば consumer の `display-item` と admin の `catalog` は、ともに商品を扱います。
しかし、陳列品を見て買い物かごに入れるユースケースと、商品を登録、更新、削除するユースケースは別のものなので、別の名前のドメインにしています。

コンテキストとドメインのフォルダー名は kebab-case で書きます。
ドメイン内のフォルダー名は、 [ドメイン内のフォルダー](#domain-folders) の表に示した名前を使います。

画面のファイル名は `<画面名>Page.vue` とし、 `pages` フォルダーの下のフォルダー構成を URL のパスと対応させます。
URL から画面のファイルを探せるようにするためです。

### pages フォルダー {#pages-directory}

pages フォルダーは、ルーティングの対象となる画面を格納します。
フォルダー構成は URL のパスと対応させます。
たとえば admin の `/catalog/items/edit/:itemId` という URL の画面は、 `pages/catalog/items/edit/ItemsEditPage.vue` です。
`:itemId` のようなパスのパラメーターは、フォルダーにしません。

```text title="admin の pages フォルダー" linenums="0"
src/
└─ pages/
   ├─ authentication/
   │  └─ login/
   │     └─ LoginPage.vue ---- /authentication/login
   ├─ catalog/
   │  └─ items/
   │     ├─ add/
   │     │  └─ ItemsAddPage.vue ---- /catalog/items/add
   │     ├─ edit/
   │     │  └─ ItemsEditPage.vue --- /catalog/items/edit/:itemId
   │     └─ ItemsPage.vue -------- /catalog/items
   ├─ ErrorPage.vue -------------- /error
   ├─ HomePage.vue --------------- /
   └─ NotFoundPage.vue ----------- 該当する画面がない URL
```

!!! note "Vue Router の設定"
      Vue Router では URL のパスと対象のファイルを指定することで、ルーティングを設定します。
      ルーティングの設定は、 core 層のルート表（ `core/router/routes.ts` ）にまとめて記述します。
      ルート名の定数は、同じフォルダーの `route-names.ts` に定義します。
      以下は admin のルート表です。

      ```typescript title="core/router/routes.ts"
      --8<-- "samples/web-csr/dressca-frontend/admin/src/core/router/routes.ts"
      ```

### components フォルダー {#components-directory}

components フォルダーは、画面を構成する vue コンポーネントファイルを格納します。
対象のドメインは、上位のフォルダーによって決まります。
複数のドメインから使うコンポーネントは、 [共通層に置くかどうかの判断基準](#common-layer-criteria) に従って置き場所を決めます。

```text title="components フォルダー" linenums="0"
src/
├─ system-common/
│  └─ components/ ------------ 業務知識を持たないコンポーネント
│     └─ LoadingSpinnerOverlay/
├─ business-common/
│  └─ components/ ------------ 業務知識を持ち、持ち主となるドメインがないコンポーネント
│     └─ NotificationToast.vue
└─ shopping/
   └─ basket/
      └─ components/ --------- 買い物かごのドメインのコンポーネント
         └─ BasketItem.vue
```

### グローバルスタイルとアセットの置き場所 {#styles-and-assets}

CSS や画像は、役割によって置き場所を分けます。
ここでは、以下の用語を使います。

- **グローバルスタイル**: アプリケーション全体に適用する CSS です。 Tailwind CSS を読み込む `base.css` が該当します。
- **アセット**: コードから import して使う画像などのファイルです。

グローバルスタイルは、 core 層の `core/styles` フォルダーに置きます。
グローバルスタイルを読み込むのは `main.ts` だけで、アプリケーションを組み立てる処理の一部だからです。
グローバルスタイルだけが参照するフォントや画像（ `@font-face` や `url()` で読み込むファイル）も、 `core/styles` フォルダーの下に置きます。

アセットは、参照するコードと同じ層か、それより下位の層に置きます。
core 層は他の層から参照できないため、コンポーネントが使う画像を core 層に置くことはできません。

- ひとつのコンポーネントだけが使うアセットは、そのコンポーネントと同じフォルダーに置きます。
- 複数の場所で使うアセットは、参照するすべての層から参照できる層の `assets` フォルダー（ `business-common/assets` 、 `system-common/assets` など）に置きます。
- `assets` フォルダーは、置くファイルができたときに作ります。

```text title="グローバルスタイルとアセットの置き場所" linenums="0"
<project-name>
├─ public/ ------------------- ファイル名を変えずに配信するファイル（ favicon など）
└─ src/
   ├─ core/
   │  └─ styles/
   │     └─ base.css --------- グローバルスタイル
   ├─ system-common/
   │  └─ assets/ ------------- 業務知識を持たない部品が共通で使うアセット
   └─ business-common/
      └─ assets/ ------------- 業務知識を持つ部品が共通で使うアセット
```

プロジェクトの直下の `public` フォルダーには、ファイル名を変えずに配信する必要があるファイルと、コードから参照しないファイル（ favicon など）だけを置きます。
Vite は `public` フォルダーのファイルを、ファイル名にハッシュを付けずにそのままビルド結果へコピーします。
それ以外の画像は、アセットとして import して参照します。
詳細は [Vite の公式ドキュメント :material-open-in-new:](https://ja.vite.dev/guide/assets){ target=_blank } を参照してください。

!!! note "create-vue の標準構成と異なる点"
      create-vue で作成したプロジェクトでは、 CSS と画像が `src/assets` フォルダーにまとめて出力されます。
      AlesInfiny Maia では、グローバルスタイルとアセットを分けて置き、 `src/assets` フォルダーは使いません。
      グローバルスタイルは core 層に置くのに対し、コンポーネントが使う画像は core 層に置けず、置ける層が異なるためです。
      また、 `src` の直下のフォルダーをすべていずれかの層に属させることで、参照方向の規則の対象外となるフォルダーをなくします。

### テストファイルの配置 {#test-file-placement}

単体テストのファイルは、テスト対象と同じフォルダーの下に `__tests__` フォルダーを作って配置します。
テストコードを対象の近くへ置きながら、実装のファイルと混在させないためです。
ドメイン内のフォルダーだけでなく、 pages や core 層のフォルダーも同様です。

```text title="テストファイルの配置" linenums="0"
src/
├─ core/
│  └─ router/
│     ├─ __tests__/
│     │  └─ routes.spec.ts
│     └─ routes.ts
├─ pages/
│  └─ basket/
│     ├─ __tests__/
│     │  └─ BasketPage.spec.ts
│     └─ BasketPage.vue
└─ security/
   └─ authentication/
      └─ composables/
         ├─ __tests__/
         │  └─ use-authentication.spec.ts
         └─ use-authentication.ts
```
