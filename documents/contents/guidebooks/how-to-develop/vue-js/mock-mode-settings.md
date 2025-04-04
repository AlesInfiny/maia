---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# モックモードの設定 {#top}

フロントエンド・バックエンドアプリケーションを並行して開発する場合や、
外部の API へリクエストを行う等の理由で環境の用意が難しい場合には、モックの利用が有効です。
モックを用いることで、バックエンドアプリケーションの API サーバーが用意できなくても、フロントエンドアプリケーション単独で動作を確認できます。
加えて、任意のレスポンスデータやレスポンスコードを設定できるため、実物の API サーバーでは再現が難しいようなエラーケースの表示も容易に確認できます。

モックを実現するライブラリとして [Mock Service Worker :material-open-in-new:](https://mswjs.io/){ target=_blank } （以下、 MSW と記載します）を用います。
MSW は、 API リクエストをインターセプトすることで、ネットワークレベルでのモックを実現します。

以下では、 Vite と MSW の設定方法を説明します。

## フォルダー構成 {#folder-structure}

モックモードの設定に関係するフォルダーとファイルは以下の通りです。

```terminal linenums="0"
<workspace-name>
├ public/
│ └ mockServiceWorker.js -- ライブラリが生成するワーカースクリプト
│ mock/
│ ├ data/ ----------------- レスポンスデータの置き場
│ ├ handlers/ ------------- ハンドラーの置き場
│ │ └ index.ts ------------ ハンドラーを集約するためのファイル
│ └ browser.ts ------------ ブラウザー用のワーカープロセスを起動するためのスクリプト
├ src/
│ ├ generated/
│ │ └ api-client/
│ │   └ model/    --------- openapi-generator で自動生成した API モデル
│ └ main.ts --------------- アプリケーションのエントリーポイント
├ .env.mock --------------- モックモードの環境設定ファイル
├ package.json ------------ ワークスペースのメタデータ、依存関係、スクリプトなどを定義するファイル
└ vite.config.ts ---------- Vite の設定ファイル
```

## Vite の設定 {#vite-settings}

ワークスペース直下の`package.json` にモックモードの起動スクリプトを定義します。

```json title="package.json"
"mock": "vite --mode mock",
```

モックモード用の環境設定ファイルとして、`vite.config.ts`と同じ階層に`.env.mock` を作成します。必要に応じて環境変数を定義してください。
モックモードを動作させるためだけであれば、追加の定義は不要です。

```env title=".env.mock"
VITE_XXX_YYY=
```

ワークスペース直下で以下のコマンドを実行し、モックモードでサーバーが起動できることを確認します。

```terminal
npm run mock
```

## Mock Service Worker の設定 {#msw-settings}

ワークスペースの直下で以下のコマンドを実行し、 MSW をインストールします。

```terminal
npm install msw
```

続けて以下のコマンドを実行し、初期設定をします。

```terminal
npx msw init ./public --save
```

コマンドを実行すると、引数で指定したフォルダー（上述の例では `public` フォルダー）に、
ワーカースクリプト（ `mockServiceWorker.js`） が作成され、`package.json` にフォルダーのパスが登録されます。
`mockServiceWorker.js` は公開フォルダーに配置する必要があります。

`mockServiceWorker.js` はバージョン情報を持ち、 MSW のライブラリバージョンと同期する必要があるので、リポジトリにコミットし、バージョン管理対象にすることが推奨されています[^1]。

```json title="package.json"
"msw": {
  "workerDirectory": [
    "public"
  ]
},
```

ワークスペース直下に`mock`フォルダーを作成し、`mock`フォルダーの配下に`browser.ts`を作成します。

```ts title="browser.ts"
import { setupWorker } from 'msw/browser';
import { handlers } from './handlers';

export const worker = setupWorker(...handlers);
```

`mock`フォルダーの配下に、`handlers`フォルダーを作成し、さらにその配下に`index.ts`を作成します。
ハンドラーの実装は別途行うため、現時点では空で構いません。

```ts title="index.ts"
export const handlers = []; // 後で実装します。
```

アプリケーションのエントリーポイントで、
モックモードで起動した場合にワーカーを立ち上げるように設定します。
`main.ts`に以下のように設定してください。

```ts title="main.ts"
async function enableMocking(): Promise<ServiceWorkerRegistration | undefined> {
  const { worker } = await import('../mock/browser'); // モックモード以外ではインポート不要なので、動的にインポートします。
  return worker.start({
    onUnhandledRequest: 'bypass', // MSW のハンドラーを未設定のリクエストに対して警告を出さないように設定します。
  });
}

if (import.meta.env.MODE === 'mock') {
  try {
    await enableMocking(); // ワーカーの起動を待ちます。
  } catch (error) {
    console.error('モック用のワーカープロセスの起動に失敗しました。', error);
  }
}

// ワーカーが起動したら、アプリケーションを立ち上げます。
const app = createApp(App);
```

??? info "ワーカープロセスの起動を待つ理由"
    ワーカープロセスが起動する前にアプリケーションが立ち上がると、 API リクエストをインターセプトできずに予期せぬ挙動を引き起こす可能性があるためです。
    たとえばトップページをマウントした際に API リクエストを行うアプリケーションでは、このリクエストで予期せぬエラーが発生し、エラーページに遷移してしまうといったことが考えられます。

再度下記のコマンドで Vite のサーバーを立ち上げ、ワーカープロセスが起動していることを確認します。
開発者ツール上に 「[MSW] Mocking enabled.」 のメッセージが表示されていれば、設定は成功です。

```terminal
npm run mock
```

[^1]: [Committing the worker script :material-open-in-new:](https://mswjs.io/docs/best-practices/managing-the-worker/#committing-the-worker-script){ target=_blank }
