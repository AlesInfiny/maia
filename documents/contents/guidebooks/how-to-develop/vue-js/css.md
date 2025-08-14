---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# CSS の設定と CSS フレームワークの適用 {#top}

AlesInfiny Maia OSS Edition では、特定の CSS フレームワークを採用することを推奨しているわけではありません。
本章では、一例として Tailwind CSS を導入する手順を紹介しますが、
実際の開発プロジェクトでは、プロジェクトの特性に応じた技術を選定してください。

## CSS の設定 {#settings-css}

Vue.js のブランクプロジェクトを作成すると、デフォルトで以下の 2 つの CSS ファイルが追加されます。

- `./src/assets/base.css`
- `./src/assets/main.css`

また、`./src/main.ts` に、以下のように main.css を読み込むコードが自動的に追加されます。

```typescript title="main.ts"
import './assets/main.css'
```

サンプルアプリケーションでは base.css に統一します。

1. `./src/assets/main.css` を削除します。
1. `./src/main.ts` の import を以下のように書き換えます。

```typescript title="main.ts"
import './assets/base.css'
```

## Tailwind CSS {#tailwind-css}

Tailwind CSS は、 あらかじめ用意されたユーティリティクラスを組み合わせることで、
CSS ファイルを記述せずにデザインを実現する、ユーティリティファーストな CSS フレームワークです。
本ページに記載している出力例は、 Tailwind CSS v3.4.17 を使用したものです。バージョンが異なる場合、出力内容は異なる可能性があります。

### Tailwind CSS のインストール {#install-tailwind-css}

[公式ドキュメント :material-open-in-new:](https://tailwindcss.com/docs/installation/using-postcss){ target=_blank } が推奨するとおり、 PostCSS のプラグインとして以下をインストールします。

- Tailwind CSS
- PostCSS
- autoprefixer

> Installing Tailwind CSS as a PostCSS plugin is the most seamless way to integrate it with build tools like webpack, Rollup, Vite, and Parcel.

```shell
npm install -D tailwindcss postcss autoprefixer postcss-nesting
```

- [PostCSS :material-open-in-new:](https://github.com/postcss/postcss){ target=_blank }

    [PostCSS](#postcss) で説明します。

- [autoprefixer :material-open-in-new:](https://autoprefixer.github.io/){ target=_blank }

    CSS に対してベンダープレフィクスを自動的に付与する PostCSS のプラグインです。このプラグインを使用することにより、ベンダープレフィクスを意識する必要が無くなります。

- [postcss-nesting :material-open-in-new:](https://github.com/csstools/postcss-plugins/tree/main/plugins/postcss-nesting){ target=_blank }

    CSS Nesting の仕様に従って、スタイルルールを互いに入れ子にできる postcss のプラグインです。

!!! info "ベンダープレフィクス"
    ベンダープレフィクスとは、ブラウザーのベンダーが独自の拡張機能を実装する際に、
    それがブラウザー独自であることを明示するために付ける識別子のことです。
    たとえば、 Chrome 独自の拡張機能の場合、 -webkit- というプレフィックスを使用します。

### Tailwind CSS の設定 {#settings-tailwind-css}

以下のコマンドを入力すると、`./tailwind.config.js` ファイルが作成されます。

```shell
npx tailwindcss init
```

作成された直後の tailwind.config.js は以下のとおりです。

```javascript title="tailwind.config.js"
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

CJS 形式（ `module.exports` ）で記述されているコードを、 ESM 形式（ `export default` ）へ変更します。
また、 content に、 Tailwind CSS を適用する対象ファイルのパス（ワイルドカード使用可）を設定します。

```javascript title="tailwind.config.js" hl_lines="2-3"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/tailwind.config.js
```

`./src/assets/base.css` の最初の行に、以下のように Tailwind CSS の各コンポーネントの @tailwind ディレクティブを追加します。

```css title="base.css"
@tailwind base;
@tailwind components;
@tailwind utilities;
```

## PostCSS {#postcss}

CSS を操作するための JavaScript ベースのプラグインです。このプラグインの API を利用する多くのプラグインが公開されており、前述の Tailwind CSS もその１つです。

PostCSS は Tailwind CSS と一緒にすでにインストール済みなので、インストールの必要はありません。

### PostCSS の設定 {#settings-postcss}

設定ファイル `./postcss.config.ts` を作成します。

```typescript title="postcss.config.ts"
https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/postcss.config.ts
```

プラグインとして postcss-nesting 、 Tailwind CSS 、 autoprefixer を使用することを宣言しています。
