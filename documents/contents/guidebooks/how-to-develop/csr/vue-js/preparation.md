---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# 事前準備 {#top}

## ローカル開発環境の構築 {#create-dev-environment}

ローカル開発環境の構築について [ローカル開発環境の構築](../local-environment/index.md) を参照し、最低限必要なソフトウェアをインストールしてください。

## Visual Studio Code の拡張機能インストール {#install-extensions}

Visual Studio Code を利用する場合、フロントエンドアプリケーションの開発のために以下の拡張機能をインストールします。

- [Vue (Official) :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=Vue.volar){ target=_blank }

    Vue.js アプリケーションの開発に推奨されている拡張機能です。
    詳細は [公式ドキュメント :material-open-in-new:](https://ja.vuejs.org/guide/scaling-up/tooling#ide-support){ target=_blank }を参照してください。

- [ESLint :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint){ target=_blank }

    TypeScript のコード品質を向上させるための拡張機能です。
    リアルタイムでのコードのエラーを検出する機能を提供します。

- [Stylelint :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=stylelint.vscode-stylelint){ target=_blank }

    CSS のコード品質を向上させるための拡張機能です。
    リアルタイムでのコードのエラーを検出する機能を提供します。

- [Prettier - Code formatter :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode){ target=_blank }

    EditorConfig と連携して、統一したスタイルにコードを整形する機能を提供します。

- [language-postcss :material-open-in-new:](https://marketplace.visualstudio.com/items?itemName=cpylua.language-postcss){ target=_blank }

    CSS で記述されたコードの可読性を向上させる機能を提供します。
    また、 Stylelint と連携することで、 CSS のコードのエラーを検出できます。
