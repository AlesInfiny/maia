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
  ├ cypress/ ---------------- cypress による End-to-End テスト用のフォルダー
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
  ├ cypress.config.ts ------- cypress の設定ファイル
  ├ env.d.ts ---------------- TypeScript でコード補完機能（Intellisense）を適用するための設定ファイル
  ├ eslint.config.ts -------- ESLint の設定ファイル
  ├ index.html -------------- Web サイトのトップページとなるファイル
  ├ package.json ------------ ワークスペースのメタデータ、依存関係、スクリプトなどを定義するファイル
  ├ README.md --------------- ブランクプロジェクト作成時点ではテンプレートの説明が記述されたファイル
  ├ tsconfig.app.json ------- アプリケーションの TypeScript として読み込む対象を定義する設定ファイル
  ├ tsconfig.json ----------- TypeScript の設定ファイル
  ├ tsconfig.node.json ------ Node.js での実行用に TypeScript として読み込む対象を定義する設定ファイル
  ├ tsconfig.vitest.json ---- 単体テストの TypeScript として読み込む対象を定義する設定ファイル
  ├ vite.config.ts ---------- Vite の設定ファイル
  └ vitest.config.ts -------- 単体テストの設定ファイル
```

各フォルダーの内部にどのようなサブフォルダーを作成するかは、[アーキテクチャ解説](../../../../app-architecture/client-side-rendering/frontend-application/index.md#project-structure) を参照してください。
