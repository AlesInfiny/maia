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

- **コンテキスト**: `src` の直下に置く、業務上のまとまりです。境界付けられたコンテキスト（ Bounded Context ）に相当し、同じ用語が同じ意味で通じる範囲を表します。
- **ドメイン**: コンテキストの直下に置くフォルダーです。買い物かごや認証のように、ひとつの業務の関心事を表します。
- **システム共通**: 業務知識を持たないコードを置く層です。 `system-common` フォルダーに対応します。
- **業務共通**: 業務知識を持ち、フロントエンドに持ち主となるドメインがないコードを置く層です。 `business-common` フォルダーに対応します。

システム共通と業務共通をまとめて、共通層と呼びます。

```text title="プロジェクトのフォルダー構成全体像" linenums="0"
<project-name>
├─ e2e/ ---------------------- Playwright による E2E テストに関するファイルを格納します。
├─ public/ ------------------- メディアファイルや favicon など静的な資産を格納します。
├─ src/
│  ├─ assets/ ---------------- コードや動的ファイルが必要とするCSSや画像などのアセットを格納します。
│  ├─ system-common/ --------- システム共通のコードを格納します。
│  ├─ business-common/ ------- 業務共通のコードを格納します。
│  ├─ <context>/ ------------- コンテキストです。ドメインのフォルダーを格納します。
│  │  └─ <domain>/ ----------- ドメインです。 views や stores などのフォルダーを格納します。
│  ├─ App.vue
│  └─ main.ts
├─ index.html
└─ package.json
```

### サンプルアプリケーションの構成例 {#sample-structure}

サンプルアプリケーションの consumer と admin は、それぞれ以下の構成になっています。

```text title="consumer のフォルダー構成" linenums="0"
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
│  ├─ display-item/ ---------- 陳列品のドメイン
│  │  ├─ components/
│  │  ├─ router/
│  │  ├─ services/
│  │  ├─ stores/
│  │  └─ views/
│  ├─ basket/ ---------------- 買い物かごのドメイン
│  └─ ordering/ -------------- 注文のドメイン
├─ security/ ----------------- セキュリティのコンテキスト
│  └─ authentication/ -------- 認証のドメイン
├─ App.vue
└─ main.ts
```

```text title="admin のフォルダー構成" linenums="0"
src/
├─ assets/
├─ system-common/ ------------ システム共通（ホーム画面を含みます）
├─ business-common/ ---------- 業務共通
├─ catalog-management/ ------- カタログ管理のコンテキスト
│  └─ catalog/ --------------- カタログのドメイン
├─ security/ ----------------- セキュリティのコンテキスト
│  ├─ authentication/ -------- 認証のドメイン
│  └─ authorization/ --------- 認可のドメイン
├─ App.vue
└─ main.ts
```

2 つのアプリケーションには、以降で説明する同じ規則を当てはめています。
それでも構成が異なるのは、扱う業務が異なるためです。

| 項目                          | consumer                                                   | admin                                                       |
| ----------------------------- | ---------------------------------------------------------- | ----------------------------------------------------------- |
| 商品を扱うドメイン            | `shopping/display-item` （陳列品を見て買い物かごに入れる） | `catalog-management/catalog` （商品を登録、更新、削除する） |
| 認可のドメイン                | なし                                                       | `security/authorization`                                    |
| ホーム画面                    | なし                                                       | `system-common/views`                                       |
| メッセージ管理（ `locales` ） | あり                                                       | なし（メッセージ管理のライブラリを導入していないため）      |

機能を追加するときにどのフォルダーへ置くかは、以下の 5 つの規則で決まります。

### 参照方向 {#reference-direction}

参照は、コンテキストから業務共通へ、業務共通からシステム共通へ向かう単方向とし、逆向きの参照は禁止します。

```text title="参照方向" linenums="0"
App.vue / main.ts
      ↓
コンテキスト
      ↓
業務共通（ business-common ）
      ↓
システム共通（ system-common ）
```

この規則は ESLint の `no-restricted-imports` で強制します。
設定方法は [静的コード検証とフォーマット](../../../guidebooks/how-to-develop/csr/vue-js/static-verification-and-format.md#layer-dependency-rules) を参照してください。

`App.vue` と `main.ts` は ESLint の規則の対象外です。
アプリケーション全体を組み立てる役割を持つため、すべての層を参照します。

`system-common/router` の `index.ts` と `route-names.ts` は、規則の例外です。
この 2 つは、各ドメインが定義したルーティング定義とルート名を集約するため、システム共通に置きながらコンテキストを参照します。
例外が層全体へ広がらないよう、システム共通の他のコードからは `route-names.ts` を参照できないようにしています。

なお、 ESLint の規則は `@/` で始まるパスに対して設定しています。
相対パスで書いた import は検出できないため、フォルダーをまたぐ import には `@/` を使ってください。

### コンテキスト {#context-criteria}

ドメインのフォルダーは、必ずコンテキストの下に置きます。
コンテキストにドメインがひとつしかない場合も同様です。
コンテキストを省略できるとすると、 `src` の直下にあるフォルダーがコンテキストなのかドメインなのかを、中身を見なければ判断できなくなるためです。

コンテキストやドメインをまたぐ参照は禁止しません。
たとえば admin では、カタログのドメイン（ `catalog-management/catalog` ）が認可のドメイン（ `security/authorization` ）を参照しています。
ログインしているユーザーのロールを確認するためです。

ただし、ドメインをまたぐ画面遷移では、遷移先のルート名を文字列で書かず、各ドメインが定義したルート名の定数を `@/system-common/router/route-names` から参照します。
ルート名の文字列を変更しても遷移元を修正する必要がなく、定数名の誤りは型検査で検出できるためです。

### 共通層の分割基準 {#common-layer-criteria}

コードをどこに置くかは、以下の表を上から順に当てはめて判断します。

| 判断                                                         | 配置先                            |
| ------------------------------------------------------------ | --------------------------------- |
| 業務知識を持たない                                           | システム共通（ `system-common` ） |
| 業務知識を持ち、持ち主となるドメインがある                   | そのドメイン                      |
| 業務知識を持つが、フロントエンドに持ち主となるドメインがない | 業務共通（ `business-common` ）   |

ここでいう業務知識とは、業務のルールや業務で使う用語に依存する知識のことです。

業務知識を持たないコードは、参照元がひとつのドメインだけでもシステム共通に置きます。
たとえば consumer の入力チェックの定義（ `system-common/validation` ）は、現時点では認証のドメインからしか参照されません。
それでも、業務に依存しない汎用的なチェックなので、他のドメインからも再利用できるようシステム共通に置いています。

業務知識を持つコードは、参照元が多くても、持ち主となるドメインがあればそのドメインに置きます。
たとえば admin の認可のドメイン（ `security/authorization` ）は、同じコンテキストの認証のドメインに加えて、カタログ管理のコンテキストからも参照されます。
それでもユーザーのロールという固有のモデルを持つので、業務共通へは移しません。
参照元が多いことは、持ち主がいないことを意味しません。

業務共通に置くのは、業務知識を持ちながら、フロントエンドに持ち主となるドメインがないコードです。
たとえば通知の表示は、どのドメインの操作の結果も通知するので、特定のドメインに属しません。
アセットの URL を生成する処理は、バックエンドではアセット管理のモジュールに属します。
しかしフロントエンドにはアセット管理の画面や状態がないため、ドメインを作らず業務共通に置いています。

この区別がないと、共通層は「よく使われるものの置き場」になります。
旧構成の `shared` フォルダーは「アプリケーション全体で再利用する共通機能」と定義されており、参照の多さだけが基準になっていました。

### ドメイン内のフォルダー {#domain-folders}

ドメインの下には、以下のフォルダーを必要に応じて置きます。

| フォルダー   | 格納するもの                                                       |
| ------------ | ------------------------------------------------------------------ |
| `views`      | ルーティングで指定される vue ファイル。                            |
| `components` | View を構成する vue コンポーネント。                               |
| `services`   | ビューモデルからのリクエストを受け、 Store や Web API を呼ぶ処理。 |
| `stores`     | Pinia の Store 。                                                  |
| `router`     | このドメインのルーティング定義とルート名の定数。                   |
| `validation` | このドメインの入力チェックの定義。                                 |

これらは代表例です。
ドメイン固有の関心事があれば、フォルダーを追加して構いません。
たとえば admin の認可のドメインには、ロールの定義を置く `constants` フォルダーがあります。

これに対して、共通層の下に置くフォルダーは、 [サンプルアプリケーションの構成例](#sample-structure) に示した種類から、必要なものだけを作ります。
共通層はアプリケーション全体から参照されるので、フォルダーの種類を増やすと影響範囲も広がります。
種類を増やす場合は、プロジェクトで合意してください。

### 命名 {#naming-rules}

コンテキストのフォルダー名は、バックエンドのモジュール名をもとに決めて構いません。
ただし、一致させる必要はありません。
たとえば admin の `catalog-management` は、バックエンドの `catalogmanagement` モジュールに合わせた名前です。
一方で `security` に対応するモジュールはバックエンドにありません。
フロントエンドでは、認証と認可をまとめてひとつのコンテキストとして扱っているためです。

ドメインのフォルダー名は、フロントエンドのユースケースで決めます。
たとえば consumer の `display-item` と admin の `catalog` は、ともに商品を扱います。
しかし、陳列品を見て買い物かごに入れるユースケースと、商品を登録、更新、削除するユースケースは別のものなので、別の名前のドメインにしています。

コンテキストとドメインのフォルダー名は kebab-case で書きます。
ドメイン内のフォルダー名は、 [ドメイン内のフォルダー](#domain-folders) の表に示した名前を使います。

### views フォルダー {#views-directory}

views フォルダーは、ルーティングで指定される vue ファイルを格納します。
ドメインのフォルダーに従属するので、下層のフォルダー構成を URL に一致させる必要はありません。

```text title="views フォルダー" linenums="0"
src/
└─ security/
   └─ authentication/
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
          component: () => import('@/security/authentication/views/LoginView.vue'),
        },
      ]
      ```

      ```typescript title="system-common/router/index.ts"
      import { createRouter, createWebHistory } from 'vue-router'
      import { authenticationRoutes } from '@/security/authentication/router/authentication'

      export const router = createRouter({
        history: createWebHistory(import.meta.env.BASE_URL),
        routes: [...authenticationRoutes],
      })
      ```

### components フォルダー {#components-directory}

components フォルダーは、 View を構成する vue コンポーネントファイルを格納します。
対象のドメインは、上位のフォルダーによって決まります。
複数のドメインから使うコンポーネントは、 [共通層の分割基準](#common-layer-criteria) に従って置き場所を決めます。

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

### テストファイルの配置 {#test-file-placement}

単体テストのファイルは、テスト対象と同じフォルダーの下に `__tests__` フォルダーを作って配置します。
テストコードを対象の近くへ置きながら、実装のファイルと混在させないためです。

```text title="テストファイルの配置" linenums="0"
src/
└─ security/
   └─ authentication/
      └─ services/
         ├─ __tests__/
         │  └─ authentication-service.spec.ts
         └─ authentication-service.ts
```
