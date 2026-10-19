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

[フロントエンドアプリケーションのアーキテクチャ - フォルダー構成](../../../../app-architecture/client-side-rendering/frontend-application/index.md#project-structure) に従って、フォルダー構造を変更します。
ツールが出力したコードを以下の構造に従って移動します。
なお本ガイドの以降の手順では、上記の構造を前提としたパスでファイルの配置場所を示します。

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
  │ │ └ AboutPage.vue ------- AboutView.vue をリネーム
  │ └ HomePage.vue ---------- HomeView.vue をリネーム
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
