---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# CSS の設定と CSS フレームワークの適用 {#top}

AlesInfiny Maia OSS Edition では、特定の CSS フレームワークを採用することを推奨しているわけではありません。
本章では、一例として Tailwind CSS を導入する手順を紹介しますが、実際の開発プロジェクトでは、プロジェクトの特性に応じた技術を選定してください。
Tailwind CSS を使用しないプロジェクトの場合、本章の手順は実行する必要がありません。

## Tailwind CSS {#tailwind-css}

Tailwind CSS は、 あらかじめ用意されたユーティリティクラスを組み合わせることで、
CSS ファイルを記述せずにデザインを実現する、ユーティリティファーストな CSS フレームワークです。
本ページに記載している出力例は、 Tailwind CSS v4.1.12 を使用したものです。バージョンが異なる場合、出力内容は異なる可能性があります。

### Tailwind CSS のインストール {#install-tailwind-css}

[公式ドキュメント :material-open-in-new:](https://tailwindcss.com/docs/installation/using-vite){ target=_blank } に従って、 Tailwind CSS と Vite のプラグインをインストールします。

```shell
npm install -D tailwindcss @tailwindcss/vite

### Tailwind CSS の設定 {#settings-tailwind-css}

[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) を実行すると、デフォルトでは　`./src/assets/base.css` と `./src/assets/main.css` の 2 つの CSS ファイルが作成されます。
サンプルアプリケーションでは base.css に統一します。
main.css を削除し、 base.css を次のように書き換えます。

```css title="base.css"
/* Tailwind CSS は 外部からのインポートではなく Vite のプラグインでバンドルするので、 Stylelintで URL 形式に自動修正されないように無効化します */
/* stylelint-disable-next-line import-notation */
@import 'tailwindcss';
```

あわせて、 `./src/main.ts` の `import` を以下のように書き換えます。

```typescript title="main.ts"
import '@/assets/base.css'
```

Tailwind CSS のプラグインを使用するために、 Vite の設定ファイルを更新します。
次のように vite.config.ts を変更します。

```typescript title="vite.config.ts" hl_lines="1 4"
import tailwindcss from '@tailwindcss/vite'

export default defineConfig(({ mode }) => {
  const plugins = [vue(), vueJsx(), vueDevTools(), tailwindcss()]
```

ワークスペースの直下で下記のコマンドを実行し、正常にビルドできることを確認してください。
`./dist/assets/` 配下に CSS ファイルが出力されます。

```shell
npm run build
```

### Tailwind CSS の適用確認 {#check-tailwind-css}

Tailwind CSS が正常に適用されていることを確認します。
確認のために、`./src/views/HomeView.vue` に次のようなコードを挿入します。

```vue title="HomeView.vue" hl_lines="3"
<template>
  <main>
    <h1 class="text-5xl font-bold text-blue-600 underline">AlesInfiny</h1>
    <TheWelcome />
  </main>
</template>
```

ワークスペースの直下で再度ビルドし、開発サーバーを立ち上げます。

```shell
npm run build
npm run dev
```

ターミナルに表示される URL へアクセスして、青色の AlesInfiny の文字列が下線つきで表示されていることを確認してください。

デフォルトで作成された CSS ファイルを削除したことで、デフォルトのアプリケーションの表示にはスタイル崩れが起きていますが、問題ありません。

### Tailwind CSS 用の Stylelint の設定 {#settings-stylelint-for-tailwind-css}

Tailwind CSS に固有の構文に対して不要な警告が出力されないように、 Stylelint の設定を変更します。
ワークスペース直下の .stylelintrc.js に次のような設定を追記します。

```javascript title="Tailwind CSS 用の .stylelintrc.js" hl_lines="3-33"
export default {
  extends: ['stylelint-config-standard', 'stylelint-config-recommended-vue'],
  rules: {
    'at-rule-no-unknown': [
      true,
      {
        ignoreAtRules: [
          /**
           * Tailwind CSS 固有のアットルール（ディレクティブ）をホワイトリストに登録します。
           * https://tailwindcss.com/docs/functions-and-directives#directives
           **/
          `import`,
          'theme',
          'source',
          'utility',
          'variant',
          'custom-variant',
          'apply',
          'reference',
        ],
      },
    ],
    'function-no-unknown': [
      true,
      {
        /**
         * Tailwind CSS が提供する関数をホワイトリストに登録します。
         * https://tailwindcss.com/docs/functions-and-directives#functions
         **/
        ignoreFunctions: ['alpha', 'spacing'],
      },
    ],
  },
  ignoreFiles: ['dist/**/*'],
  overrides: [
    {
      files: ['**/*.vue'],
      /** Vue ファイルの <style> ブロック内を Lint するための設定です。*/
      customSyntax: 'postcss-html',
    },
  ],
}
```

ワークスペースの直下で Stylelint を実行し、正常に終了することを確認してください。

```shell
npm run stylelint:ci
```

### Tailwind CSS 用の Prettier の設定 {#settings-stylelint-for-tailwind-css}

Tailwind CSS のユーティリティクラスの順序は開発者によってバラつきが出やすいので、 Prettier によってフォーマットします。
公式のプラグイン [prettier-plugin-tailwindcss :material-open-in-new:](https://github.com/tailwindlabs/prettier-plugin-tailwindcss){ target=_blank }をインストールします。
ワークスペース直下で、下記のコマンドを実行します。

```shell
npm install -D prettier-plugin-tailwindcss
```

ワークスペース直下の prettierrc.js に、 プラグイン用の設定を追加します。

```javascript title="Tailwind CSS 用の prettierrc.js"  hl_lines="8-9"
import prettierConfigBase from '../.prettierrc.js'
/**
 * @see https://prettier.io/docs/configuration
 * @type {import("prettier").Config}
 */
const config = {
  ...prettierConfigBase,
  plugins: ['prettier-plugin-tailwindcss'],
  tailwindStylesheet: './src/assets/base.css',
}

export default config

```

ワークスペースの直下で Prettier を実行し、正常に終了することを確認してください。

```shell
npm run format:ci
```
