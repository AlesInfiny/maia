---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# ブランクプロジェクトの作成 {#top}

下記の手順では、 Node.js のルートプロジェクトとワークスペースを作成し、作成したワークスペースに Vue.js のブランクプロジェクトを作成します。

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
  "description": "",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "keywords": [],
  "author": "",
  "license": "ISC"
}
```

## Vue.js およびオプションのインストール {#install-vue-js-and-options}

以下のコマンドを実行し、任意のワークスペース名（プロジェクト名）を指定して Vue.js をインストールします。

```terminal
npm init -w <workspace-name> vue@latest .
```

create-vue パッケージをインストールする必要があり、続行するかどうかを確認するメッセージが表示されるので、「y」を選択します。

`-w` オプションで指定したワークスペース名と同じ名称を入力します。

```terminal
√ Package name: ... <workspace-name>
```

インストールオプションを確認されるので、左右カーソルキーで Yes / No を選択します。フロントエンドアプリケーションのアーキテクチャに基づき、使用するものに対して Yes を選択すると、以下のようになります。

```terminal
√ Add TypeScript? ... Yes
√ Add JSX Support? ... Yes
√ Add Vue Router for Single Page Application development? ... Yes
√ Add Pinia for state management? ... Yes
√ Add Vitest for Unit Testing? ... Yes
√ Add an End-to-End Testing Solution? » Cypress
√ Add ESLint for code quality? ... Yes
√ Add Prettier for code formatting? ... Yes
√ Add Vue DevTools 7 extension for debugging? (experimental) ... No
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


VITE v5.x.x  ready in xxxx ms

  -  Local:   http://localhost:5173/
  -  Network: use --host to expose
  -  press h + enter to show help
```
