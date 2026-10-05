---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# ブランクプロジェクトの作成 {#top}

下記の手順では、 Node.js のルートプロジェクトとワークスペースを作成し、作成したワークスペースに Vue.js のブランクプロジェクトを作成します。
本ページに記載しているターミナルの出力例は、 Node.js v24 系 、 npm v11 系 、 create-vue v3.24.0 を使用してプロジェクトを作成した際のものです。バージョンが異なる場合、出力内容は異なる可能性があります。

## プロジェクトの全体像 {#project-overview}

[mono-repo](../../../../app-architecture/overview/repository-structure.md) 構成では、
複数のフロントエンドアプリケーションのプロジェクトを 1 つのリポジトリで管理します。
[npm workspaces :material-open-in-new:](https://docs.npmjs.com/cli/v11/using-npm/workspaces){ target=_blank } を用いることで、
プロジェクトごとにワークスペースを作成し、管理できます。
プロジェクトをまたがるワークスペースや、ワークスペースをまたがるプロジェクトを作成できますが、
原則としてワークスペースとプロジェクトが 1:1 で対応するようにします。

![プロジェクトフォルダの構造](../../../../images/guidebooks/how-to-develop/csr/vue-js/project-folder-structure-light.png#only-light){ loading=lazy align=right }
![プロジェクトフォルダの構造](../../../../images/guidebooks/how-to-develop/csr/vue-js/project-folder-structure-dark.png#only-dark){ loading=lazy align=right }

## プロジェクトの初期化 {#init-npm-project}

以下のコマンドを実行して、ルートプロジェクトを初期化します。

```shell
npm init -y --init-type=module --init-private
```

実行に成功すると、 package.json ファイルが作成されます。

```text
Wrote to ...\package.json:

{
  "name": "root-project-name",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "keywords": [],
  "author": "",
  "license": "ISC",
  "type": "module",
  "private": true
}
```

!!! info "npm init コマンドのオプションについて"
    npm init コマンドの実行時に指定しているオプションの目的は下記の通りです。

    - `-y`
      対話的な質問を省略し、すべて既定値を使って package.json を自動生成するオプションです。
    - `--init-type=module`
      package.json に `"type": "module"` を追加し、ESM 形式をデフォルトとして扱うようにします。
    - `--init-private`
      package.json に `"private": true` を設定し、誤って npm レジストリへ公開されることを防ぎます。

## Vue.js およびオプションのインストール {#install-vue-js-and-options}

以下のコマンドを実行し、任意のワークスペース名（プロジェクト名）を指定して Vue.js をインストールします。

```shell
npm init -w <workspace-name> vue@{バージョン} .
```

create-vue パッケージをインストールする必要があり、続行するかどうかを確認するメッセージが表示されるので、「y」を選択します。

`-w` オプションで指定したワークスペース名と同じ名称を入力します。

```text
┌  Vue.js - The Progressive JavaScript Framework
│
◆  Package name:
│  <workspace-name>
└
```

TypeScript を使用するかどうかを確認されるので、 Yes を選択します。

```text
◆  Use TypeScript?
│  ● Yes / ○ No
└
```

インストールオプションを確認されるのでそれぞれインストールするかどうかを選択します。フロントエンドアプリケーションのアーキテクチャに基づき、使用するものを選択すると、以下のようになります。
「 Linter (error prevention) 」を選択すると、 ESLint がインストールされます。

```text
◆  Select features to include in your project: (↑/↓ to navigate, space to select, a to toggle all, enter to confirm)
│  ◼ JSX Support
│  ◼ Router (SPA development)
│  ◼ Pinia (state management)
│  ◼ Vitest (unit testing)
│  ◼ End-to-End Testing
│  ◼ Linter (error prevention)
│  ◼ Prettier (code formatting)
└
```

E2E テストのフレームワークには Playwright を選択します。

```text
◆  Select an End-to-End testing framework: (↑/↓ to navigate, enter to confirm)
│  ● Playwright (https://playwright.dev/)
│  ○ Cypress
└
```

以下の実験的機能は選択しません。

```text
◆  Select experimental features to include in your project: (↑/↓ to navigate, space to select, a to toggle all, enter to
│   confirm)
│  ◻ Replace Prettier with Oxfmt
│  ◻ Vue 3.6 (Release Candidate)
│  ◻ Replace TypeScript with typescript-native-bridge (tsgo)
└
```

```text
◆  Skip all example code and start with a blank Vue project?
│  ○ Yes / ● No
└
```

プロジェクトの作成が完了すると以下のように Git コマンドを実行して構成管理するよう勧められますが、ここでのコマンド実行は不要です。

```text
| Optional: Initialize Git in your project directory with:

   git init && git add -A && git commit -m "initial commit"
```

## Oxlint の除去 {#remove-oxlint}

create-vue で Linter を選択すると、 ESLint に加えて [Oxlint :material-open-in-new:](https://oxc.rs/docs/guide/usage/linter){ target=_blank } が必ずインストールされる構成になります。
本ガイドではリンターを ESLint に一本化するため、パッケージをインストールする前に、ワークスペースから Oxlint を除去します。

ワークスペースの直下にいることを確認し、以下のとおり変更します。

1. Oxlint の設定ファイル .oxlintrc.json を削除します。

1. package.json の `scripts` から `lint:oxlint` と `lint:eslint` を削除し、 `lint` を ESLint の直接実行に変更します。

    ```json title="package.json（変更前）"
    "lint": "run-s \"lint:*\"",
    "lint:oxlint": "oxlint . --fix",
    "lint:eslint": "eslint . --fix --cache",
    ```

    ```json title="package.json（変更後）"
    "lint": "eslint . --fix --cache",
    ```

1. package.json の `devDependencies` から `oxlint` と `eslint-plugin-oxlint` を削除します。

1. eslint.config.ts から、 eslint-plugin-oxlint の import と、 Oxlint の設定ファイルを読み込む行を削除します。

    ```typescript title="eslint.config.ts（削除する行）"
    import pluginOxlint from 'eslint-plugin-oxlint'

    ...pluginOxlint.buildFromOxlintConfigFile('.oxlintrc.json'),
    ```

1. .vscode/extensions.json の `recommendations` から、 Oxlint の拡張機能 `oxc.oxc-vscode` を削除します。

## ブランクプロジェクトのビルドと実行 {#build-and-serve-blank-project}

以下のようにコマンドを実行し、必要なパッケージをインストールしてアプリケーションを実行します。

```shell
npm install
npm run format -w <workspace-name>
npm run dev -w <workspace-name>
```

`npm run dev` が成功すると以下のように表示されるので、「 Local: 」に表示された URL をブラウザーで表示します。ブランクプロジェクトのランディングページが表示されます。

```text
> workspace-name@0.0.0 dev
> vite


  VITE v8.x.x  ready in xxxx ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  Vue DevTools: Open http://localhost:5173/__devtools__/ as a separate window
  ➜  Vue DevTools: Press Alt(⌥)+Shift(⇧)+D in App to toggle the Vue DevTools
  ➜  press h + enter to show help
```
