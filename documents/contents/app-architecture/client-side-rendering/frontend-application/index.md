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

Vue Router : [公式ドキュメント :material-open-in-new:](https://router.vuejs.org/introduction.html){ target=_blank }

![Vue Router によるルーティング](../../../images/app-architecture/client-side-rendering/routing-by-vue-router-light.png#only-light){ loading=lazy }

![Vue Router によるルーティング](../../../images/app-architecture/client-side-rendering/routing-by-vue-router-dark.png#only-dark){ loading=lazy }

#### モデルコンポーネントとの連携 {#linkage-with-model-component}

Vue.js ではバックエンドアプリケーションとの連携をモデルが行います。そのため、ユーザーが行う画面コンポーネントからの処理や入力情報をモデルに連携する必要があります。この連携ではビューモデルのプレゼンテーションロジックから、後述するモデルコンポーネントの Service や Store の Action を呼び出すことで、データの取得・更新をします。

#### フロント入力チェック {#input-validation}

文字種や文字数などの入力チェックは、ビューモデルで行い、不要なバックエンドとの通信の発生を防止します。  AlesInfiny Maia では VeeValidate と yup という OSS ライブラリを利用します。 VeeValidate はフォームや入力コンポーネントを監視し、 yup は検証スキームを定義する OSS です。

![VeeValidation と yup による入力チェック](../../../images/app-architecture/client-side-rendering/input-validation-light.png#only-light){ loading=lazy }
![VeeValidation と yup による入力チェック](../../../images/app-architecture/client-side-rendering/input-validation-dark.png#only-dark){ loading=lazy }

### モデルコンポーネント {#model-component}

![MVVM パターン モデル](../../../images/app-architecture/client-side-rendering/model-component-light.png#only-light){ loading=lazy }
![MVVM パターン モデル](../../../images/app-architecture/client-side-rendering/model-component-dark.png#only-dark){ loading=lazy }

モデルはデータの状態管理や画面(ビュー)へのデータ連携、 Web API の呼び出しおよびハンドリングなどの役割を持つコンポーネントです。モデルは以下の要素で構成されます。またフロントエンドで扱うデータモデルと API モデルとの乖離を吸収し、扱いやすい状態に加工する役割も持ちます。

- Service : ビューモデルからのリクエストに対して、 Store の呼び出し、 Web API の呼び出しなどデータの連携に必要な処理をします。
- Store : フロントエンドで扱う状態を保持するコンテナです。 AlesInfiny Maia では Pinia という Vue.js の Store ライブラリを利用して管理します。

Pinia : [公式ドキュメント :material-open-in-new:](https://pinia.vuejs.org/introduction.html){ target=_blank }

ただし、このモデルの構成は複雑な状態管理をするアプリケーションを想定しており、小規模なアプリケーションや状態管理を必要としないページの場合は、 Service やモデルを省略することも考えられます。この場合は、ビューモデルから直接 Web API を呼び出します。

#### Storeの構成要素 {#store-structure}

Pinia における Store は、 State・Getter・Action という 3 つの要素から構成されています。

![Pinia のアーキテクチャ](../../../images/app-architecture/client-side-rendering/pinia-architecture-light.png#only-light){ loading=lazy }
![Pinia のアーキテクチャ](../../../images/app-architecture/client-side-rendering/pinia-architecture-dark.png#only-dark){ loading=lazy }

| 要素   | 説                                               |
| ------ | ------------------------------------------------ |
| State  | Store で管理するデータそのもの。                   |
| Getter | State の値や State から算出した結果を返すもの。     |
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

OpenAPI : [公式ドキュメント :material-open-in-new:](https://swagger.io/specification/){ target=_blank }

Axios : [github :material-open-in-new:](https://github.com/axios/axios){ target=_blank }

![OpenAPIを利用したバックエンドとの連携](../../../images/app-architecture/overview/client-side-rendering-maia-light.png#only-light){ loading-lazy }
![OpenAPIを利用したバックエンドとの連携](../../../images/app-architecture/overview/client-side-rendering-maia-dark.png#only-dark){ loading-lazy }

!!! note ""

    上の図で使用している OSS 製品名およびロゴのクレジット情報は [こちら](../../../about-maia/credits.md) を参照してください。

!!! note "OpenAPI Generator の自動生成コード"
      OpenAPI Generator はサーバー、クライアント双方の様々なコードの自動生成に対応しています。生成可能なコードについては公式ドキュメントを参照してください。

      - [OpenAPI Generator : Generators List :material-open-in-new:](https://openapi-generator.tech/docs/generators){ target=_blank }

<!-- バックエンド編のAPIドキュメントへリンク -->

## フォルダー構成 {#project-structure}

Vue.js プロジェクトのフォルダー構成は、ブランクプロジェクト作成時のデフォルトの構成を基に以下のように行います。なおこのフォルダー配下の構成については、コンポーネント設計方法に依存するため、各プロジェクトの方針に従います。

```text title="プロジェクトのフォルダー構成全体像" linenums="0"
<project-name>
├─ cypress/ ------------------ cypress による E2E テストに関するファイルを格納します。
├─ public/ ------------------- メディアファイルや favicon など静的な資産を格納します。
├─ src/
│  ├─ api-client/ ------------ API クライアントの設定ファイルを格納します。
│  ├─ assets/ ---------------- コードや動的ファイルが必要とするCSSや画像などのアセットを格納します。
│  ├─ components/ ------------ 単体で自己完結している再利用性の高い vue コンポーネントなどを格納します。
│  ├─ config/ ---------------- 設定ファイルを格納します。
│  ├─ generated/ ------------- 自動生成されたファイルを格納します。
│  ├─ locales/ --------------- メッセージ管理に関するファイルを格納します。
│  ├─ router/ ---------------- ルーティング定義を格納します。
│  ├─ services/ -------------- サービスに関するファイルを格納します。
│  ├─ shared/ ---------------- アプリケーション全体で再利用する共通機能のファイルを格納します。
│  ├─ stores/ ---------------- store に関するファイルを格納します。
│  ├─ validation/ ------------ 一元化するバリデーション定義ファイルを格納します。
│  ├─ views/ ----------------- ルーティングで指定される vue ファイルを格納します。またページ固有の挙動などもここに含めます。
│  ├─ App.vue
│  └─ main.ts
├─ index.html
└─ package.json
```

### views フォルダー {#views-directory}

views フォルダーはルーティングで指定される vue ファイルを格納します。そのためこの下層のフォルダー構造はサイト構造を意識して作成することを推奨します。以下の例で Login.vue なら ```https://xxxx.com/authentication/login``` と設定します。

```text title="views フォルダー" linenums="0"
src/
└─ views/
   ├─ authentication/
   │  ├─ LoginView.vue
   │  └─ LogoutView.vue
   ├─ catalog/
   └─ order/
```

!!! note "Vue Router の設定"
      Vue Router では URL のパスと対象のファイルを指定することで、ルーティングを設定します。以下は `https://xxxx.com/authentication/login` という URL に対して上記の `LoginView.vue` を設定している例です。

      ```typeScript title="index.ts"
      import { createRouter, createWebHistory } from "vue-router";

      const router = createRouter({
         history: createWebHistory(import.meta.env.BASE_URL),
         routes: [
            {
               path: "/authentication/login",
               name: "authentication/login",
               component: () => import('@/views/authentication/LoginView.vue'),
            },
         ],
      })
      ```

### components フォルダー {#components-directory}

components フォルダーは主に、再利用性の高い vue コンポーネントファイルを格納します。さらにこの下層フォルダーはドメインで分割し、それを操作するコンポーネントを格納します。こうすることで再利用性を活かすために、どのドメインを対象にしたコンポーネントなのかを明確にします。また vue ファイルに限らずプロジェクト内で再利用性の高いもの（icon など）もこちらに格納します。

```text title="components フォルダー" linenums="0"
src/
└─ components/
   ├─ authentication/
   │  ├─ LoginForm.vue
   │  └─ LogoutMessage.vue
   ├─ product/
   │  ├─ ProductDetail.vue
   │  └─ ProductList.vue
   └─ icon/
```

上記の拡張として Atomic Design でコンポーネント設計をする場合は、 atoms, molecules, organisms でフォルダーを構成します。この際 atoms と molecules は同一フォルダーにコンポーネント構成パーツとしてまとめ、 organisms との区別を「store へのアクセスの有無」として行うことでドメイン分割が容易になります。

!!! note "Atomic Design"
      Atomic Design とは UI の構成要素を 5 段階に分けてパーツ単位で UI デザインを設計する方法のことです。最も小さい単位である Atoms パーツを組み合わせた Molecules, さらにそれらを組み合わせた Organism, というように要素を細分化し、それらを組み合わせて画面を作成します。コンポーネントの再利用性やデザイン変更の反映のしやすさといったメリットがあります。

      - [Atomic Design by Brad Frost :material-open-in-new:](https://atomicdesign.bradfrost.com/){ target=_blank }

```text title="components フォルダー by Atomic Design" linenums="0"
src/
└─ components/
   ├─ atoms-and-molecules/
   │  ├─ Button.vue
   │  ├─ Input.vue
   │  └─ Form.vue
   │
   ├─ organisms/
   │  ├─ authentication/
   │  │  ├─ LoginForm.vue
   │  │  └─ LogoutMessage.vue
   │  └─ product/
   │     ├─ ProductDetail.vue
   │     └─ ProductList.vue
   │
   └─ icon/
```

!!! note "URL とドメイン"
    views フォルダーは URL 本位、 components フォルダーはドメイン本位で構成するため、下層フォルダー構造は一致しません。（部分的に一致することはあります。）

<!--
#### テストファイル

テストファイルは ``` __test__ ``` フォルダーを作らず、対象コンポーネントの隣に配置します。
-->