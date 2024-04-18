---
title: Vue.js 開発手順
description: Vue.js を用いたクライアントサイドアプリケーションの開発手順を説明します。
---

# ブランクプロジェクトのフォルダー構造 {#top}

ブランクプロジェクトを作成した時点でのフォルダー構造は以下のようになっています。

```terminal linenums="0"
<project-name>
├ cypress/ ---------------- cypress による End-to-End テスト用のフォルダー
├ dist/ ------------------- ビルド後の成果物が配置されるフォルダー
├ public/ ----------------- メディアファイルや favicon など静的な資産が配置されるフォルダー
├ src/ -------------------- アプリケーションのソースコードが配置されるフォルダー
│ ├ assets/ --------------- コードや動的ファイルが必要とするCSSや画像などのアセットが配置されるフォルダー
│ ├ components/ ----------- ページを構成する部品のコードが配置されるフォルダー
│ ├ router/ --------------- ルーティング制御を行うコードが配置されるフォルダー
│ ├ stores/ --------------- 状態管理を行うコードが配置されるフォルダー
│ ├ views/ ---------------- ルーティングの対象となるページのコードが配置されるフォルダー
│ ├ App.vue --------------- 画面のフレームを構成するコード
│ └ main.ts --------------- 各ライブラリ等を読み込むためのコード
├ .eslintrc.cjs ----------- ESLint の設定ファイル
├ cypress.config.ts ------- cypress の設定ファイル
├ env.d.ts ---------------- TypeScript でコード補完機能（Intellisense）を適用するための設定ファイル
├ index.html -------------- Web サイトのトップページとなるファイル
├ package.json ------------ パッケージのメタデータ（依存するパッケージなど）を定義するファイル
├ README.md --------------- ブランクプロジェクト作成時点ではテンプレートの説明が記述されたファイル
├ tsconfig.app.json ------- アプリケーションの TypeScript として読み込む対象を定義する設定ファイル
├ tsconfig.node.json ------ TypeScript の設定ファイルとして読み込む対象を定義する設定ファイル
├ tsconfig.json ----------- TypeScript の設定ファイル
├ tsconfig.vitest.json ---- 単体テストの TypeScript として読み込む対象を定義する設定ファイル
├ vite.config.ts ---------- Vite の設定ファイル
└ vitest.config.ts -------- 単体テストの設定ファイル
```

各フォルダーの内部にどのようなサブフォルダーを作成するかは、[アーキテクチャ解説](./../../../app-architecture/client-side-rendering/frontend-application/index.md#project-structure) を参照してください。
