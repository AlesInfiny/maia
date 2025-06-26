---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# ワークスペースの設定 {#top}

[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) で作成したルートプロジェクトとワークスペースについて、
追加で必要な設定をします。

## ワークスペースの定義 {#definition-workspaces}

ワークスペースの名称は、ルートプロジェクトの package.json の `workspaces` プロパティで定義し、この値はワークスペースの package.json の `name` プロパティと一致している必要があります。
[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) に従って初期設定した場合は、自動的に構成されます。

``` json title="package.json（ルート）"
  "workspaces": [
    "workspace-name"
  ]
```

``` json title="package.json（ワークスペース）"
{
  "name": "workspace-name",
}
```

## スクリプトの登録 {#register-npm-scripts}

ルートプロジェクトの package.json にスクリプトを登録します。
`-w` オプションでワークスペース名を指定することで、指定したワークスペースの package.json に存在するスクリプトを実行できます。
設定例を下記に示します。

``` json title="package.json（ルート）"
{
  "scripts": {
    "lint:ci:workspace-name": "npm run lint:ci -w workspace-name",
    "type-check:workspace-name": "npm run type-check -w workspace-name",
    "build-only:dev:workspace-name": "npm run build-only:dev -w workspace-name",
    "build:prod:workspace-name": "npm run build:prod -w workspace-name",
    "test:unit:workspace-name": "npm run test:unit -w workspace-name",
    "dev:workspace-name": "npm run dev -w workspace-name",
    "mock:workspace-name": "npm run mock -w workspace-name"
  },
}
```

## パッケージの依存関係の管理 {#manage-package-dependency}

パッケージのバージョンは、ルートプロジェクトの package-lock.json で管理されます。各ワークスペースには package-lock.json は作成されません。
パッケージのインストール・バージョン更新は、 npm workspaces を使用しない場合と同様に、各ワークスペースで `npm install` を行います。
しかし、インストールしたパッケージの依存関係は、ルートプロジェクトの package-lock.json に記録されます。
また、各ワークスペースの package.json でバージョンを指定することで、ワークスペース間で異なるバージョンのパッケージを使用できます。
