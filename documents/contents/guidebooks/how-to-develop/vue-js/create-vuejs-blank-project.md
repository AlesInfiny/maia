---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# ブランクプロジェクトの作成 {#top}

下記の手順では、 Node.js のルートプロジェクトとワークスペースを作成し、作成したワークスペースに Vue.js のブランクプロジェクトを作成します。
本ページに記載しているターミナルの出力例は、 Node.js v22.17.1 、 npm v10.9.2 、 create-vue v3.17.0 を使用してプロジェクトを作成した際のものです。バージョンが異なる場合、出力内容は異なる可能性があります。

## プロジェクトの全体像 {#project-overview}

[mono-repo](../../../app-architecture/overview/repository-structure.md) 構成では、
複数のフロントエンドアプリケーションのプロジェクトを 1 つのリポジトリで管理します。
[npm workspaces :material-open-in-new:](https://docs.npmjs.com/cli/v10/using-npm/workspaces){ target=_blank } を用いることで、
プロジェクトごとにワークスペースを作成し、管理できます。
プロジェクトをまたがるワークスペースや、ワークスペースをまたがるプロジェクトを作成できますが、
原則としてワークスペースとプロジェクトが 1:1 で対応するようにします。

![プロジェクトフォルダの構造](../../../images/guidebooks/how-to-develop/vue-js/project-folder-structure-light.png#only-light){ loading=lazy align=right }
![プロジェクトフォルダの構造](../../../images/guidebooks/how-to-develop/vue-js/project-folder-structure-dark.png#only-dark){ loading=lazy align=right }

## プロジェクトの初期化 {#init-npm-project}

以下のコマンドを実行して、ルートプロジェクトを初期化します。

```terminal
npm init -y
```

実行に成功すると、 package.json ファイルが作成されます。

```terminal
Wrote to ...\package.json:

{
  "name": "root-project-name",
  "version": "1.0.0",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "keywords": [],
  "author": "",
  "license": "ISC",
  "description": ""
}
```

## Vue.js およびオプションのインストール {#install-vue-js-and-options}

以下のコマンドを実行し、任意のワークスペース名（プロジェクト名）を指定して Vue.js をインストールします。

```terminal
npm init -w <workspace-name> vue@{バージョン} .
```

create-vue パッケージをインストールする必要があり、続行するかどうかを確認するメッセージが表示されるので、「y」を選択します。

`-w` オプションで指定したワークスペース名と同じ名称を入力します。

```terminal
T  Vue.js - The Progressive JavaScript Framework
|
*  Package name:
|  <workspace-name>
—
```

インストールオプションを確認されるのでそれぞれインストールするかどうかを選択します。フロントエンドアプリケーションのアーキテクチャに基づき、使用するものを選択すると、以下のようになります。

```terminal
*  Select features to include in your project: (↑/↓ to navigate, space to select, a to toggle all, enter to confirm)
|  [+] TypeScript
|  [+] JSX Support
|  [+] Router (SPA development)
|  [+] Pinia (state management)
|  [+] Vitest (unit testing)
|  [+] End-to-End Testing
|  [+] ESLint (error prevention)
|  [+] Prettier (code formatting)
—

*  Select an End-to-End testing framework: (↑/↓ to navigate, enter to confirm)
|    Playwright
|  > Cypress (https://www.cypress.io/)
|    Nightwatch
—
```

<!-- textlint-disable ja-technical-writing/no-doubled-joshi -->
以下の実験的機能のインストールは必須ではありません。
<!-- textlint-enable ja-technical-writing/no-doubled-joshi -->

```terminal
*  Select experimental features to include in your project: (↑/↓ to navigate, space to select, a to toggle all, enter to
confirm)
|  [ ] Oxlint (experimental)
|  [•] rolldown-vite (experimental)
—
```

サンプルコードの生成をスキップするか選択します。
どちらを選択しても構いませんが、本ページ以降のガイドでは、 No を選択しサンプルコードを生成したことを前提として説明します。

```terminal
◆  Skip all example code and start with a blank Vue project?
│  ○ Yes / ● No
```

プロジェクトの作成が完了すると以下のように Git コマンドを実行して構成管理するよう勧められますが、ここでのコマンド実行は不要です。

```terminal
| Optional: Initialize Git in your project directory with:

   git init && git add -A && git commit -m "initial commit"
```

## ブランクプロジェクトのビルドと実行 {#build-and-serve-blank-project}

以下のようにコマンドを実行し、必要なパッケージをインストールしてアプリケーションを実行します。

```terminal
npm install
npm run format -w <workspace-name>
npm run dev -w <workspace-name>
```

`npm run dev` が成功すると以下のように表示されるので、「 Local: 」に表示された URL をブラウザーで表示します。ブランクプロジェクトのランディングページが表示されます。

```terminal
> workspace-name@0.0.0 dev
> vite


  VITE v7.x.x  ready in xxxx ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  Vue DevTools: Open http://localhost:5173/__devtools__/ as a separate window
  ➜  Vue DevTools: Press Alt(⌥)+Shift(⇧)+D in App to toggle the Vue DevTools
  ➜  press h + enter to show help
```
