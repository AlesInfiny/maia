---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# 開発に使用するパッケージ {#top}

## ブランクプロジェクト作成時にオプションとしてインストールされるパッケージ {#optional-packages}

以下のパッケージは Vue.js のブランクプロジェクト作成時にオプションとしてインストールされます。

- [Cypress :material-open-in-new:](https://www.cypress.io/){ target=_blank }

     E2E (End-to-End) テストツール

- [ESLint :material-open-in-new:](https://eslint.org/){ target=_blank }

    JavaScript の静的検証

- [JSX :material-open-in-new:](https://jsx.github.io/){ target=_blank }

    JavaScript ソースコード上で HTML タグを扱えるようにする

- [Pinia :material-open-in-new:](https://pinia.vuejs.org/){ target=_blank }

    Vue.js 用の状態管理ライブラリ

- [Prettier :material-open-in-new:](https://prettier.io/){ target=_blank }

    コードファイルのフォーマット

- [TypeScript :material-open-in-new:](https://www.typescriptlang.org/){ target=_blank }

    JavaScript を拡張して静的型付にしたプログラミング言語

- [Vitest :material-open-in-new:](https://vitest.dev/){ target=_blank }

    Vite 環境で動作する高速テスティングフレームワーク

- [Vue Router :material-open-in-new:](https://router.vuejs.org/){ target=_blank }

    Vue.js を利用した SPA で、ルーティング制御をするための公式プラグイン

なお、特定のパッケージをインストールすることで付随してインストールされるパッケージ（例： ESLint に対する eslint-config-prettier）は記載を省略しています。

## 追加でインストールするパッケージ {#additional-packages}

以下のパッケージは別途インストールが必要です。

- [Axios :material-open-in-new:](https://github.com/axios/axios){ target=_blank }

    Vue.js で非同期通信するためのプロミスベースの HTTP クライアント（[インストール方法](create-api-client-code.md#install-axios)）

- <!-- cspell: disable-line -->[openapi-generator :material-open-in-new:](https://github.com/OpenAPITools/openapi-generator){ target=_blank }

    Web API 仕様からクライアントコードの自動生成（[インストール方法](create-api-client-code.md#install-open-api-generator)）

- [Stylelint :material-open-in-new:](https://stylelint.io/){ target=_blank }

    CSS の静的検証ツール（[インストール方法](static-verification-and-format.md#install-stylelint)）

- [stylelint-config-standard :material-open-in-new:](https://github.com/stylelint/stylelint-config-standard){ target=_blank }

    Stylelint の標準設定（[インストール方法](static-verification-and-format.md#install-stylelint)）

- [stylelint-config-recommended-vue :material-open-in-new:](https://github.com/ota-meshi/stylelint-config-recommended-vue){ target=_blank }

    Stylelint の .vue ファイル向け推奨設定（[インストール方法](static-verification-and-format.md#install-stylelint)）

- [VeeValidate :material-open-in-new:](https://vee-validate.logaretm.com/){ target=_blank }

    Vue.js 用のリアルタイムバリデーションコンポーネントライブラリ（[インストール方法](input-validation.md#install-packages)）

- [yup :material-open-in-new:](https://github.com/jquense/yup){ target=_blank }

    JavaScript でフォームのバリデーションルールを宣言的に記述できるライブラリ（[インストール方法](input-validation.md#install-packages)）

- [Vue I18n :material-open-in-new:](https://vue-i18n.intlify.dev/){ target=_blank }

    Vue.js アプリケーションでメッセージを外部ファイルで一元管理したり、多言語対応するためのライブラリ（[インストール方法](input-validation.md#install-packages)）

- [VueUse :material-open-in-new:](https://vueuse.org/){ target=_blank }

    Vue.js アプリケーションで利用できる、 Composition API ベースのユーティリティ関数をまとめたライブラリ（[インストール方法](./event-handling-settings.md#install-vueuse)）

- [Tailwind CSS :material-open-in-new:](https://tailwindcss.com/){ target=_blank }

    CSS フレームワーク（[インストール方法](css.md#install-tailwind-css)）
