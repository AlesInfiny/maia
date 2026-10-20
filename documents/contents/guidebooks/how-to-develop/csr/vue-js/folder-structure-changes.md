---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# フォルダー構造の変更 {#top}

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

[アーキテクチャ解説 - フォルダー構成](../../../../app-architecture/client-side-rendering/frontend-application/index.md#project-structure) に従って、 `src` 配下のフォルダー構造を変更します。
create-vue が出力したコードを以下の構造に従って移動します。

```text linenums="0"
<workspace-name>
└ src/
  ├ core/ ------------------- ルーターの生成やグローバルスタイルなど、アプリケーションの組み立てに関するコードを配置するフォルダー
  │ ├ router/ --------------- ルーティングに関連するコードを配置するフォルダー
  │ └ styles/ --------------- グローバルスタイルを配置するフォルダー
  ├ pages/ ------------------ 画面を配置するフォルダー
  ├ system-common/ ---------- 業務知識を持たない、システム共通のコードを配置するフォルダー
  ├ business-common/ -------- 業務知識を持ち、持ち主となるドメインがないコードを配置するフォルダー
  ├ <context>/ -------------- コンテキストのフォルダー（該当するコードを置くときに作成）
  │ ├ <domain>/ ------------- ドメインのフォルダー。この下に composables や services などのフォルダーを配置します。
  │ └ public-api.ts --------- コンテキストの外から参照する入口
  ├ App.vue
  └ main.ts
```

ツールが出力したコンポーネントとストアを、仮に `sample` コンテキストの `vue` ドメインとして扱います。
`system-common` と `business-common` は空のフォルダーとして作ります。
Git は空のフォルダーを管理しないので、 `.gitkeep` を置きます。
移動後の構造は以下のとおりです。

```text linenums="0"
<workspace-name>
└ src/
  ├ business-common/
  │ └ .gitkeep
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
  │ │ └ AboutPage.vue ------- AboutView.vue をリネーム
  │ └ HomePage.vue ---------- HomeView.vue をリネーム
  ├ sample/ ----------------- 新規作成：コンテキスト
  │ ├ vue/ ------------------ 新規作成：ドメイン
  │ │ ├ components/
  │ │ │ ├ __tests__/
  │ │ │ │ └ HelloWorld.spec.ts
  │ │ │ ├ icons/
  │ │ │ │ ├ IconCommunity.vue
  │ │ │ │ ├ IconDocumentation.vue
  │ │ │ │ ├ IconEcosystem.vue
  │ │ │ │ ├ IconSupport.vue
  │ │ │ │ └ IconTooling.vue
  │ │ │ ├ HelloWorld.vue
  │ │ │ ├ TheWelcome.vue
  │ │ │ └ WelcomeItem.vue
  │ │ └ stores/
  │ │   └ counter.ts
  │ └ public-api.ts --------- 新規作成：公開 API
  ├ system-common/
  │ └ .gitkeep
  ├ App.vue
  └ main.ts
```

以降の手順では、移動後の構造を前提としたパスでファイルの配置場所を示します。

ファイルを移動したら、移動したファイルを参照する `import` を書き換えます。
フォルダーをまたぐ参照は、相対パスではなく `@/` エイリアスで記述してください。
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

## 公開 API の作成 {#create-public-api}

コンテキストの外からは、コンテキストの内部のファイルを直接参照しません。
`pages` と他のコンテキストからの直接の参照は、 [静的コード分析とフォーマット - フォルダー間の参照方向の強制](./static-verification-and-format.md#layer-dependency-rules) で設定する ESLint のルールで検出します。
コンテキストの外で使うものは、コンテキストの直下に作成する `public-api.ts` から export します。

`sample` コンテキストのうち、コンテキストの外で使うのは、以下の 2 つのコンポーネントです。

- `HelloWorld.vue`: core 層の `App.vue` で使います。
- `TheWelcome.vue`: `pages` の `HomePage.vue` で使います。

`WelcomeItem.vue` と `icons` フォルダーのコンポーネントは、 `TheWelcome.vue` だけが使う部品です。
`counter.ts` のストアは、どこからも使われていません。
これらはコンテキストの外で使わないので、 export しません。
公開していないファイルはコンテキストの外から参照されないので、変更の影響がコンテキストの中にとどまります。

`.vue` ファイルのコンポーネントは default export なので、 `export { default as <名前> } from ...` の形式で、名前を付けて export します。

```typescript title="sample/public-api.ts"
export { default as HelloWorld } from './vue/components/HelloWorld.vue'
export { default as TheWelcome } from './vue/components/TheWelcome.vue'
```

## 公開 API からの import {#import-from-public-api}

`pages` と core 層からコンテキストのコードを使うときは、 `@/<コンテキスト>/public-api` から名前付きで import します。
`HomePage.vue` の `import` は、以下のようになります。

```vue title="pages/HomePage.vue" hl_lines="2"
<script setup lang="ts">
import { TheWelcome } from '@/sample/public-api'
</script>
```

core 層の `App.vue` も、同様に公開 API から import します。

```vue title="App.vue（抜粋）" hl_lines="3"
<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { HelloWorld } from '@/sample/public-api'
</script>
```
