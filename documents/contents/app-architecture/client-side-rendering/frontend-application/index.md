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

`src` フォルダーの下は、業務の関心事を単位に構成します。
最上位に置くのは、境界付けられたコンテキスト（ Bounded Context ）です。
これは、同じ用語が同じ意味で通じる業務上の範囲を指します。
コンテキストの下にドメインのフォルダーを置き、さらにその下に `views` や `services` といった層のフォルダーを並べます。

層を最上位に置く構成と比べると、ひとつの機能に関わるコードがひとつのフォルダーにまとまります。
機能を追加するときに触るフォルダーが限られ、削除するときもコードの取り残しが起こりにくくなります。

コンテキストの他に、複数のドメインから参照する機能を置く共通の層を 2 つ設けます。
業務知識を持つものを `business-common` に、持たないものを `system-common` に置きます。

```text title="プロジェクトのフォルダー構成全体像" linenums="0"
<project-name>
├─ cypress/ ------------------ cypress による E2E テストに関するファイルを格納します。
├─ public/ ------------------- メディアファイルや favicon など静的な資産を格納します。
├─ src/
│  ├─ assets/ ---------------- コードや動的ファイルが必要とするCSSや画像などのアセットを格納します。
│  ├─ system-common/ --------- 業務知識を持たない、システム共通の機能を格納します。
│  ├─ business-common/ ------- 業務知識を持ち、複数のドメインから参照される機能を格納します。
│  ├─ <context>/ ------------- 境界付けられたコンテキストです。ドメインのフォルダーを格納します。
│  │  └─ <domain>/ ----------- ドメインです。層のフォルダーを格納します。
│  ├─ App.vue
│  └─ main.ts
├─ index.html
└─ package.json
```

サンプルアプリケーション（ consumer ）では、以下のような構成になります。

```text title="サンプルアプリケーションのフォルダー構成" linenums="0"
src/
├─ assets/
├─ system-common/ ------------ システム共通
│  ├─ api-client/ ------------ API クライアントの設定ファイルを格納します。
│  ├─ components/
│  ├─ composables/
│  ├─ error-handler/ --------- グローバルエラーハンドラーを格納します。
│  ├─ events/ ---------------- イベントバスの定義を格納します。
│  ├─ generated/ ------------- 自動生成されたファイルを格納します。
│  ├─ helpers/
│  ├─ locales/ --------------- メッセージ管理に関するファイルを格納します。
│  ├─ router/ ---------------- 各ドメインのルーティング定義を集約します。
│  ├─ types/
│  ├─ validation/
│  └─ views/ ----------------- エラー画面など、業務知識を持たない画面を格納します。
├─ business-common/ ---------- 業務共通
│  ├─ components/
│  ├─ helpers/
│  ├─ services/
│  └─ stores/
├─ shopping/ ----------------- 買い物のコンテキスト
│  ├─ display-item/ ---------- カタログのドメイン
│  │  ├─ components/
│  │  ├─ router/
│  │  ├─ services/
│  │  ├─ stores/
│  │  └─ views/
│  ├─ basket/ ---------------- 買い物かごのドメイン
│  └─ ordering/ -------------- 注文のドメイン
├─ authentication/ ----------- 認証のコンテキスト
│  └─ login/ ----------------- ログインのドメイン
├─ App.vue
└─ main.ts
```

機能を追加するときにどのフォルダーへ置くかは、以下の 5 つの規則で決まります。

### 参照方向 {#reference-direction}

参照は、コンテキストから業務共通へ、業務共通からシステム共通へ向かう単方向とし、逆向きの参照は禁止します。

```text title="参照方向" linenums="0"
アプリケーションシェル（ App.vue / main.ts ）
      ↓
コンテキスト（ ドメイン ）
      ↓
business-common （ 業務共通 ）
      ↓
system-common （ システム共通 ）
```

この規則は ESLint の `no-restricted-imports` で強制します。
設定方法は [静的コード検証とフォーマット](../../../guidebooks/how-to-develop/csr/vue-js/static-verification-and-format.md#layer-dependency-rules) を参照してください。

アプリケーションシェル（ `App.vue` と `main.ts` ）は例外です。
全体を組み立てる役割を持つため、すべての層を参照できます。

ルーティング定義を集約するモジュールも例外です。
`system-common/router` に置きながら、全ドメインのルーティング定義を参照します。
この例外が層全体へ広がらないよう、集約モジュール自体は、システム共通の他のコードから参照できないようにします。

### コンテキストを作る基準 {#context-criteria}

コンテキストは、参照関係を持つドメインのまとまりに対して作ります。
バックエンドがアプリケーションモジュールでコンテキストを定義している場合は、その定義に合わせます。
境界の位置がずれると、同じ業務を指すフォルダーが両側で別の名前と粒度になり、分割の意図が読み取れなくなるためです。

同じ階層にあるコンテキストどうしの参照は、[参照方向](#reference-direction) の規則では禁止しません。
バックエンドがモジュール間の依存を `allowedDependencies` で許可している範囲にとどめてください。

ドメインがひとつしかない場合や、ドメイン間に参照関係がない場合は、コンテキストのフォルダーを作らず、ドメインのフォルダーを `src` の直下に置いても構いません。

### 共通層の分割基準 {#common-layer-criteria}

共通層に置くかどうか、置く場合はどの共通層かは、以下の順で判断します。

| 判断                                             | 配置先            |
| ------------------------------------------------ | ----------------- |
| ひとつのドメインからしか参照されない             | そのドメインの下  |
| 業務知識を持ち、かつ複数のドメインから参照される | `business-common` |
| 業務知識を持たない                               | `system-common`   |

ここでいう業務知識とは、業務のルールや業務で使う用語に依存する知識のことです。
たとえば通貨の表示フォーマットの変換は、どの業務でも同じように使えるのでシステム共通です。
買い物かごの状態を保持する Store は、買い物という業務を前提にしているので業務共通です。

それ自体がドメインとして成立するものは、複数のコンテキストから参照されていても共通層へ移しません。
共通層は、どのドメインにも属さないコードの置き場だからです。

たとえばサンプルアプリケーション（ admin ）の `authorization` は、認証とカタログ管理の 2 つのコンテキストから参照されます。
それでもユーザーとロールという固有のモデルを持つドメインなので、コンテキストとして置きます。
参照元が多いことは、持ち主がいないことを意味しません。

この区別がないと、共通層は「よく使われるものの置き場」になります。
旧構成の `shared` フォルダーは「アプリケーション全体で再利用する共通機能」と定義されており、参照の多さだけが基準になっていました。

`business-common` に置くものがないプロジェクトもあります。
その場合はフォルダーを作りません。
空のフォルダーがあると、中身を探すうちに、本来ドメインへ属するコードまで動かしてしまいます。

### ドメイン内のフォルダー {#domain-folders}

ドメインの下には、以下の 6 種類のフォルダーを必要に応じて置きます。

| フォルダー    | 格納するもの                                                       |
| ------------- | ------------------------------------------------------------------ |
| `views`       | ルーティングで指定される vue ファイル。                            |
| `components`  | View を構成する vue コンポーネント。                               |
| `composables` | Composition API を活用した再利用性の高い関数。                     |
| `services`    | ビューモデルからのリクエストを受け、 Store や Web API を呼ぶ処理。 |
| `stores`      | Pinia の Store 。                                                  |
| `router`      | このドメインのルーティング定義。                                   |

これらは代表例です。
ドメイン固有の関心事があれば、 `validation` や `constants` のようにフォルダーを追加して構いません。

これに対して、共通層の下の構成はプロジェクトで固定します。
共通層はアプリケーション全体から参照されるので、フォルダーが増えると影響範囲も広がります。

### 命名 {#naming-rules}

コンテキストのフォルダー名は、バックエンドのモジュール名に合わせます。

ドメインのフォルダー名は、フロントエンドの関心事で決めます。
バックエンドのモジュール名と一致させる必要はありません。
たとえば consumer の `display-item` は、「カタログを表示して選ぶ」というフロントエンド側の関心事を表しており、バックエンドのカタログ管理とは名前が一致しません。

層のフォルダー名は、[ドメイン内のフォルダー](#domain-folders) の表に示した名前を使います。

### views フォルダー {#views-directory}

views フォルダーは、ルーティングで指定される vue ファイルを格納します。
ドメインのフォルダーに従属するので、下層のフォルダー構成を URL に一致させる必要はありません。

```text title="views フォルダー" linenums="0"
src/
└─ authentication/
   └─ login/
      ├─ router/
      │  ├─ authentication-route-names.ts
      │  └─ authentication.ts
      └─ views/
         └─ LoginView.vue
```

!!! note "Vue Router の設定"
      Vue Router では URL のパスと対象のファイルを指定することで、ルーティングを設定します。
      ルーティング定義はドメインの `router` フォルダーに置き、 `system-common/router` で集約します。
      以下は `https://xxxx.com/authentication/login` という URL に対して上記の `LoginView.vue` を設定している例です。

      ```typescript title="authentication.ts"
      import type { RouteRecordRaw } from 'vue-router'
      import { authenticationRouteNames } from './authentication-route-names'

      export const authenticationRoutes: RouteRecordRaw[] = [
        {
          path: '/authentication/login',
          name: authenticationRouteNames.login,
          component: () => import('@/authentication/login/views/LoginView.vue'),
        },
      ]
      ```

      ```typescript title="system-common/router/index.ts"
      import { createRouter, createWebHistory } from 'vue-router'
      import { authenticationRoutes } from '@/authentication/login/router/authentication'

      export const router = createRouter({
        history: createWebHistory(import.meta.env.BASE_URL),
        routes: [...authenticationRoutes],
      })
      ```

### components フォルダー {#components-directory}

components フォルダーは主に、 View を構成する vue コンポーネントファイルを格納します。
対象のドメインは、上位のフォルダーによって決まります。
複数のドメインから使うコンポーネントは、[共通層の分割基準](#common-layer-criteria) に従って共通層に置きます。
また vue ファイルに限らずプロジェクト内で再利用性の高いもの（ icon など）もこちらに格納します。

```text title="components フォルダー" linenums="0"
src/
├─ system-common/
│  └─ components/ ------------ 業務知識を持たない共通コンポーネント
│     └─ LoadingSpinnerOverlay/
├─ business-common/
│  └─ components/ ------------ 複数のドメインから使う業務コンポーネント
│     └─ NotificationToast.vue
└─ shopping/
   └─ basket/
      └─ components/ --------- 買い物かごのドメイン固有のコンポーネント
         └─ BasketItem.vue
```

上記の拡張として Atomic Design でコンポーネント設計をする場合は、 atoms, molecules, organisms でフォルダーを構成します。この際 atoms と molecules は同一フォルダーにコンポーネント構成パーツとしてまとめ、 organisms との区別を「store へのアクセスの有無」として行うことでドメイン分割が容易になります。

!!! note "Atomic Design"
      Atomic Design とは UI の構成要素を 5 段階に分けてパーツ単位で UI デザインを設計する方法のことです。最も小さい単位である Atoms パーツを組み合わせた Molecules, さらにそれらを組み合わせた Organism, というように要素を細分化し、それらを組み合わせて画面を作成します。コンポーネントの再利用性やデザイン変更の反映のしやすさといったメリットがあります。

      - [Atomic Design by Brad Frost :material-open-in-new:](https://atomicdesign.bradfrost.com/){ target=_blank }

atoms と molecules は Store にアクセスしないため、共通層の `components` フォルダーに置きます。
organisms は Store にアクセスするため、対象のドメインの `components` フォルダーに置きます。

```text title="components フォルダー by Atomic Design" linenums="0"
src/
├─ system-common/
│  └─ components/
│     ├─ atoms-and-molecules/
│     │  ├─ Button.vue
│     │  ├─ Input.vue
│     │  └─ Form.vue
│     └─ icon/
└─ authentication/
   └─ login/
      └─ components/ --------- organisms に相当します。
         └─ LoginForm.vue
```

### テストファイルの配置 {#test-file-placement}

単体テストのファイルは、テスト対象と同じ層のフォルダーの下に `__tests__` フォルダーを作って配置します。
テストコードを対象の近くへ置きながら、実装のファイルと混在させないためです。

```text title="テストファイルの配置" linenums="0"
src/
└─ authentication/
   └─ login/
      └─ services/
         ├─ __tests__/
         │  └─ authentication-service.spec.ts
         └─ authentication-service.ts
```
