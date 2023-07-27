# sampledemo

## 開発環境

VSCode

- 拡張機能（推奨）
  - Vetur (octref.vetur)
  - eslint (dbaeumer.vscode-eslint)
  - prettier (esbenp.prettier-vscode)
  - sass (syler.sass-indented)

Node.js （v16）

yarn

- `npm i -g yarn`で最新をインストール

## 環境変数
.envファイルを作成して以下の値を設定する

```
USER_FLOW_SIGNIN=B2C_1_{『サインアップとサインイン』のユーザフロー名 }
USER_FLOW_EDIT_PROFILE=B2C_1_{ 『プロファイル編集』のユーザフロー名 }
ADB2C_SIGNIN_URI={ https://{テナント名}.b2clogin.com/{テナント名}.onmicrosoft.com/B2C_1_{『サインアップとサインイン』のユーザフロー名} }
ADB2C_EDIT_PROFILE_URI={ https://{テナント名}.b2clogin.com/{テナント名}.onmicrosoft.com/B2C_1_{プロファイル編集』のユーザフロー名} }
ADB2C_AUTHORITY_DOMAIN={ {テナント名}.b2clogin.com }
ADB2C_TASKS_SCOPE={APIの公開で設定したApplication ID URI}/tasks.read
API_GET_MESSAGE_URI={ メッセージ取得API URL 今回のデモでは http://localhost:8080/hello }
API_DELETE_USER_URI={ ユーザ削除API URL 今回のデモでは http://localhost:8080/users }

ADB2C_APP_CLIENT_ID={ アプリ(SPA用)のクライアントID }
APP_URI=http://localhost:3000
```

### 起動設定

## Build Setup

```bash
# パッケージ関連のインストール
$ yarn install

# developmentビルドとサーバーの起動
$ yarn dev
```

## 特殊なディレクトリ

以下のような特別なディレクトリを作成することができ、そのうちのいくつかは特別な動作をします。pages`だけが必要です。その機能を使いたくなければ、削除しても構いません。

### `assets`

assets ディレクトリには、Stylus や Sass ファイル、画像、フォントなど、コンパイルされていないアセットが格納されます。

このディレクトリの使用方法については、[ドキュメント](https://nuxtjs.org/docs/2.x/directory-structure/assets) を参照してください。

### `components`

componentsディレクトリには、Vue.jsのコンポーネントが含まれています。コンポーネントは、ページのさまざまな部分を構成し、ページ、レイアウト、さらには他のコンポーネントに再利用したり、インポートしたりすることができます。

このディレクトリの使い方については、[the documentation](https://nuxtjs.org/docs/2.x/directory-structure/components)で詳しく説明しています。

### `layouts`

レイアウトは、Nuxtアプリのルック＆フィールを変更したいときに、サイドバーを含めたり、モバイル用とデスクトップ用の個別のレイアウトを用意したりするのに非常に便利です。

このディレクトリの使用方法については、[the documentation](https://nuxtjs.org/docs/2.x/directory-structure/layouts)で詳しく説明しています。

### `pages`

このディレクトリには、アプリケーションのビューとルートが含まれます。Nuxtはこのディレクトリ内のすべての `*.vue` ファイルを読み込み、Vue Routerを自動的にセットアップします。

このディレクトリの使い方については、[the documentation](https://nuxtjs.org/docs/2.x/get-started/routing)を参照してください。

### `plugins`

pluginsディレクトリには、ルートVue.jsアプリケーションをインスタンス化する前に実行したいJavaScriptプラグインが含まれています。ここは、Vueプラグインを追加したり、関数や定数を注入したりする場所です。Vue.use()`が必要になるたびに、`plugins/`にファイルを作成し、そのパスを`nuxt.config.js` の plugins に追加する必要があります。

このディレクトリの使い方については、[the documentation](https://nuxtjs.org/docs/2.x/directory-structure/plugins)に詳細が記載されています。

### `static`

このディレクトリには、静的ファイルが格納されます。このディレクトリ内の各ファイルは `/` にマップされます。

例: `/static/robots.txt` は `/robots.txt` にマップされます。

このディレクトリの使い方は、[ドキュメント](https://nuxtjs.org/docs/2.x/directory-structure/static)に詳しく書かれています。

### `store`

このディレクトリには、Vuexのストアファイルが格納されます。このディレクトリにファイルを作成すると、自動的にVuexが有効になります。

このディレクトリの使用方法については、[ドキュメント](https://nuxtjs.org/docs/2.x/directory-structure/store)で詳しく説明しています。

## その他のディレクトリ（Nuxt.jsでは特に扱わないディレクトリ）

### `entities`

このディレクトリには、axios等で取得したAPIの返り値の型（あるいはそれを加工してアプリ内で扱いやすい様にしたデータモデル）を定義したファイルを格納します。

## 環境変数について

.envファイルや環境変数を定義しておき、nuxt.config.jsのenvオプションで明示的に取り込む。

例（nuxt.config.js）

```js
const { APP_URI } = process.env;
{ 
  ...
  env: { APP_URI }
  // 下の書き方は全ての環境変数が取り込まれてしまうので危険
  // env: process.env
}

```

例（.env）
```bash
APP_URI=http://localhost:3000
```
