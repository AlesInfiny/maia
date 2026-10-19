---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# ブランクプロジェクトのフォルダー構造 {#top}

[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) 時点でのフォルダー構造は以下のようになっています。

```text linenums="0"
<root-project-name>
├ package.json -------------- ルートプロジェクトのメタデータ、依存関係、スクリプトなどを定義するファイル
├ package-lock.json --------- npm によって自動生成される、パッケージの依存関係を記録するファイル
└ <workspace-name>
  ├ .vscode/ ---------------- Visual Studio Code の環境設定ファイルを格納するフォルダー
  ├ e2e/ -------------------- Playwright による End-to-End テスト用のフォルダー
  ├ public/ ----------------- メディアファイルや favicon など静的な資産が配置されるフォルダー
  ├ src/ -------------------- アプリケーションのソースコードが配置されるフォルダー
  │ ├ assets/ --------------- コードや動的ファイルが必要とするCSSや画像などのアセットが配置されるフォルダー
  │ ├ components/ ----------- ページを構成する部品のコードが配置されるフォルダー
  │ ├ router/ --------------- ルーティング制御を行うコードが配置されるフォルダー
  │ ├ stores/ --------------- 状態管理を行うコードが配置されるフォルダー
  │ ├ views/ ---------------- ルーティングの対象となるページのコードが配置されるフォルダー
  │ ├ App.vue --------------- 画面のフレームを構成するコード
  │ └ main.ts --------------- 各ライブラリ等を読み込むためのコード
  ├ .editorconfig ----------- コーディングスタイルを定義する EditorConfig の設定ファイル
  ├ .gitattributes ---------- 特定のファイルやフォルダーに対して Git の操作をカスタマイズするための設定ファイル
  ├ .gitignore -------------- Git の管理対象外となるファイルやフォルダーをカスタマイズするための設定ファイル
  ├ .prettierrc.json -------- コードフォーマットのルールを定義する Prettier の設定ファイル
  ├ env.d.ts ---------------- TypeScript でコード補完機能（Intellisense）を適用するための設定ファイル
  ├ eslint.config.ts -------- ESLint の設定ファイル
  ├ index.html -------------- Web サイトのトップページとなるファイル
  ├ package.json ------------ ワークスペースのメタデータ、依存関係、スクリプトなどを定義するファイル
  ├ playwright.config.ts ---- Playwright の設定ファイル
  ├ README.md --------------- ブランクプロジェクト作成時点ではテンプレートの説明が記述されたファイル
  ├ tsconfig.app.json ------- アプリケーションの TypeScript として読み込む対象を定義する設定ファイル
  ├ tsconfig.json ----------- TypeScript の設定ファイル
  ├ tsconfig.node.json ------ Node.js での実行用に TypeScript として読み込む対象を定義する設定ファイル
  ├ tsconfig.vitest.json ---- 単体テストの TypeScript として読み込む対象を定義する設定ファイル
  ├ vite.config.ts ---------- Vite の設定ファイル
  └ vitest.config.ts -------- 単体テストの設定ファイル
```

## アプリケーションのフォルダー構造 {#application-folder-structure}

上記のうち `src` フォルダーの下は、ブランクプロジェクトを作成するツールが出力したままの状態です。
`components` 、 `router` 、 `stores` 、 `views` という種類別のフォルダーが `src` の直下に並んでいます。

AlesInfiny Maia では、 `src` フォルダーの下を業務の関心事で分割します。
ツールの出力とは構成が異なるので、ブランクプロジェクトの作成後に、ツールが出力したコードを以下の構造に従って移動します。
たとえばルーティングの設定は `core/router` へ、画面は `pages` へ移動します。
`business-common` 、コンテキストとドメインの各フォルダーは、該当するコードを置くときに作ります。

ツールが出力した `assets` フォルダーは使いません。
グローバルスタイル（アプリケーション全体に適用する CSS ）の `base.css` と `main.css` は、 `core/styles` へ移動します。
`main.css` は、 [CSS の設定と CSS フレームワークの適用](./css.md) で削除し、 `base.css` に統一します。

```text linenums="0"
<workspace-name>
└ src/
  ├ core/ ------------------- ルーターの生成やグローバルスタイルなど、アプリケーションの組み立てに関するコードを配置するフォルダー
  │ └ styles/ --------------- グローバルスタイルを配置するフォルダー
  ├ pages/ ------------------ 画面を配置するフォルダー
  ├ system-common/ ---------- 業務知識を持たない、システム共通のコードを配置するフォルダー
  ├ business-common/ -------- 業務知識を持ち、持ち主となるドメインがないコードを配置するフォルダー（該当するコードを置くときに作成）
  ├ <context>/ -------------- コンテキストのフォルダー（該当するコードを置くときに作成）
  │ ├ <domain>/ ------------- ドメインのフォルダー。この下に composables や services などのフォルダーを配置します。
  │ └ public-api.ts --------- コンテキストの外から参照する入口
  ├ App.vue
  └ main.ts
```

コンテキストとドメインの決め方、どのフォルダーに何を配置するかの判断基準は、[アーキテクチャ解説](../../../../app-architecture/client-side-rendering/frontend-application/index.md#project-structure) を参照してください。

なお本ガイドの以降の手順では、上記の構造を前提としたパスでファイルの配置場所を示します。

### サンプルコードの移動後の構造 {#reclassified-example-code}

ブランクプロジェクトの作成時に生成されたサンプルコードを、上記の構造に従って移動した結果を示します。
本ガイドの以降の手順は、この構造から始めます。

| 移動前                | 移動後                            | 移動先の理由                                                                             |
| --------------------- | --------------------------------- | ---------------------------------------------------------------------------------------- |
| `router/index.ts`     | `core/router/index.ts`            | ルーターの生成は、 core 層の役割です。                                                   |
| `views/HomeView.vue`  | `pages/HomePage.vue`              | 画面です。 URL が `/` なので、 `pages` の直下に置きます。                                |
| `views/AboutView.vue` | `pages/about/AboutPage.vue`       | 画面です。 URL のパス `/about` に合わせたフォルダーに置きます。                          |
| `assets/base.css`     | `core/styles/base.css`            | アプリケーション全体に適用するグローバルスタイルです。                                   |
| `assets/main.css`     | `core/styles/main.css`            | アプリケーション全体に適用するグローバルスタイルです。 CSS の設定で削除します。          |
| `assets/logo.svg`     | `core/images/logo.svg`            | core 層の `App.vue` だけが使う画像です。                                                 |
| `components/`         | `system-common/components/`       | 業務知識を持たない UI コンポーネントです。 `icons/` と `__tests__/` も含めて移動します。 |
| `stores/counter.ts`   | `system-common/stores/counter.ts` | 業務知識を持たないストアです。                                                           |

サンプルコードは業務知識を持たないので、 `business-common` フォルダーとコンテキストのフォルダーは作りません。
移動後の構造は以下のとおりです。

```text linenums="0"
<workspace-name>
└ src/
  ├ core/
  │ ├ images/
  │ │ └ logo.svg
  │ ├ router/
  │ │ └ index.ts
  │ └ styles/
  │   ├ base.css
  │   └ main.css
  ├ pages/
  │ ├ about/
  │ │ └ AboutPage.vue ------- /about
  │ └ HomePage.vue ---------- /
  ├ system-common/
  │ ├ components/
  │ │ ├ __tests__/
  │ │ │ └ HelloWorld.spec.ts
  │ │ ├ icons/
  │ │ │ ├ IconCommunity.vue
  │ │ │ ├ IconDocumentation.vue
  │ │ │ ├ IconEcosystem.vue
  │ │ │ ├ IconSupport.vue
  │ │ │ └ IconTooling.vue
  │ │ ├ HelloWorld.vue
  │ │ ├ TheWelcome.vue
  │ │ └ WelcomeItem.vue
  │ └ stores/
  │   └ counter.ts
  ├ App.vue
  └ main.ts
```

ファイルを移動したら、移動したファイルを参照する `import` を書き換えます。
フォルダーをまたぐ参照は、相対パスではなく `@/` エイリアスで記述します。
たとえば、 `main.ts` と `core/router/index.ts` の `import` は、以下のようになります。

```typescript title="main.ts"
import '@/core/styles/main.css'
// (中略)
import router from '@/core/router'
```

```typescript title="core/router/index.ts"
import HomePage from '@/pages/HomePage.vue'
// (中略)
      component: () => import('@/pages/about/AboutPage.vue'),
```
