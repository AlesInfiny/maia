---
title: Vue.js 開発手順
description: Vue.js を用いた クライアントサイドアプリケーションの 開発手順を説明します。
---

# モックモードの設定 {#top}

Mock Service Worker を用います。

本章で登場するフォルダーとファイルは以下の通りです。

```terminal linenums="0"
<workspace-name>
├ public/
│ └ mockServiceWorker.js -- ワーカースクリプト
│ mock/
│ ├ data/ ----------------- データの置き場
│ ├ handlers/ ------------- ハンドラーの置き場
│ ├ browser.ts ------------ ブラウザー用のサーバーを起動するためのスクリプト
│ └ handlers.ts ------------ ハンドラーを集約するためのファイル
├ src/
│ └ generated/
│   └ api-client/ --------- openapi-generator で自動生成したコード
│ └ main.ts --------------- アプリケーションのエントリーポイント
├ .env.mock --------------- モックモードの環境設定ファイル
├ package.json ------------ ワークスペースのメタデータ、依存関係、スクリプトなどを定義するファイル
└ vite.config.ts ---------- Vite の設定ファイル
```

## Vite の設定 {#vite-settings}

```json title="package.json"
    "mock": "vite --mode mock",
```

`.env.mock` を作成します。

```env title=".env.mock"
VITE_XXX_YYY=
```

サーバーが起動できることを確認します。
API コールの部分ではエラーが出力されます。

```terminal
npm run mock
```

## Mock Service Worker の設定 {#msw-settings}

ターミナルを開き、対象プロジェクトのワークスペースフォルダーで以下のコマンドを実行します。

```terminal
npm install msw
```

続けて以下のコマンドを実行し、初期設定をします。

```terminal
npx msw init ./public --save
```

`mockServiceWorker.js` が作成されます。
`package.json` に正しいフォルダーが指定されていることを確認してください。

`mockServiceWorker.js` はバージョン管理対象にしてください。

```json title="package.json"
  "msw": {
    "workerDirectory": [
      "public"
    ]
  },
```

`browser.ts` を作成します。

```ts title="browser.ts"
import { setupWorker } from 'msw/browser';
import { handlers } from './handler';

export const worker = setupWorker(...handlers);
```

`handlers.ts`を作成します。
ハンドラーの実装は別途行うため、現時点では空で構いません。

```ts title="handlers.ts"
export const handlers = []; // 後で実装します。
```

アプリケーションのエントリーポイントで、
モックモードで起動した場合に Mock Service Worker を立ち上げるように設定します。
エントリーポイントで下記のように設定してください。

```ts title="main.ts"
async function enableMocking(): Promise<ServiceWorkerRegistration | undefined> {
  const { worker } = await import('../mock/browser');
  return worker.start({
    onUnhandledRequest: 'bypass',
  });
}

if (import.meta.env.MODE === 'mock') {
  try {
    await enableMocking();
  } catch (error) {
    console.error('モック用のワーカープロセスの起動に失敗しました。', error);
  }
}

const app = createApp(App);
```

再度下記のコマンドで Vite のサーバーを立ち上げ、ワーカープロセスが起動していることを確認します。
開発者ツールに `[MSW] Mocking enabled.` のメッセージが表示されていれば、設定は成功です。

```terminal
npm run mock
```

## ハンドラーの実装 {#implement-handler}
