---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# プロジェクトの共通設定 {#top}

## TypeScript の設定 {#typescript-settings}

TypeScript で作成されたファイルは、 `tsconfig.json` の設定値をもとにトランスパイル[^1]されます。
`tsconfig.json` の存在するフォルダーとその配下のフォルダーの該当ファイルに設定が適用されます。

[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) の手順に沿って `create-vue` でプロジェクトを作成すると、以下の `tsconfig.json` および `tsconfig.*.json` が生成されます。
各 `tsconfig.*.json` には `include` に指定したファイル群のトランスパイルに関する設定値が定義されています。

```terminal linenums="0"
<workspace-name>
├ cypress
|  └ tsconfig.json--------- E2E テストの TypeScript として読み込む対象を定義する設定ファイル(Cypress 用)
├ tsconfig.app.json ------- アプリケーションの TypeScript として読み込む対象を定義する設定ファイル
├ tsconfig.node.json ------ TypeScript の設定ファイルとして読み込む対象を定義する設定ファイル
├ tsconfig.json ----------- TypeScript の設定ファイル
└ tsconfig.vitest.json ---- 単体テストの TypeScript として読み込む対象を定義する設定ファイル(vitest 用)
```

自動生成されたルートの `tsconfig.json` では、 Project Reference 機能により `references` に指定された `tsconfig.*.json` を参照します。
つまり、 TypeScript プロジェクトを `references` で指定したファイルに基づいて論理分割しています。
論理分割することにより、以下のような利点があります。

- アプリケーションコードからテストコードを参照するような歪な依存関係を防ぐ

    単一の `tsconfig.json` のみを定義している場合、テストコードも `include` する必要があるので、アプリケーションのコードからテストコードのクラスやメソッドを参照してもエラーにならない事があります。
    Project Reference 機能を利用した場合、テストコードとアプリケーションコードとで `tsconfig.json` を分割し、アプリケーションコードからテストコードを参照できないように設定できます。

- ビルド[^2]時のパフォーマンスを改善する

    ビルドの度にコード全体をビルドするのではなく、更新があったプロジェクトのみをビルドします。

Project Reference 機能については [Project References :material-open-in-new:](https://www.typescriptlang.org/docs/handbook/project-references.html){ target=_blank } を参照してください。

なお、 `tsconfig.app.json` `tsconfig.node.json` には npm パッケージで提供されている `tsconfig` を継承するように設定されているため、継承元の設定値が存在します。
`extends` に定義されている継承元ファイルを参照して実際の設定値を確認できます。

![tsconfigの継承関係](../../../images/guidebooks/how-to-develop/vue-js/vue-tsconfig-light.png#only-light){ loading=lazy }
![tsconfigの継承関係](../../../images/guidebooks/how-to-develop/vue-js/vue-tsconfig-dark.png#only-dark){ loading=lazy }

### tsconfig の設定値の解説 {#tsconfig}

??? note "tsconfig.json の設定例"

    ``` json title="tsconfig.json"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/tsconfig.json
    ```

??? note "tsconfig.app.json の設定例"

    ``` json title="tsconfig.app.json"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/tsconfig.app.json
    ```

??? note "tsconfig.node.json の設定例"

    ``` json title="tsconfig.node.json"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/tsconfig.node.json
    ```

??? note "tsconfig.vitest.json の設定例"

    ``` json title="tsconfig.vitest.json"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/tsconfig.vitest.json
    ```

- `compilerOptions.noEmit`

    `tsc` によるトランスパイルの結果を出力しないよう設定するプロパティです。 `create-vue` した際のデフォルト値の `true` を設定します。
    これは、 `vue-tsc` で型チェックのみを行い、トランスパイルの結果は出力しないようにするためです。
    ビルドで利用する package.json に定義されたスクリプトでは Vite でトランスパイルを行うため、 `vue-tsc` でのトランスパイルは不要です。
    （※ `vue-tsc` は Vue の単一ファイルコンポーネントをサポートする `tsc` のラッパーです。）

    ??? note "create-vue で生成されるビルドに関するスクリプト"

        ```json title="package.json"
        {
          "scripts": {
            "build": "run-p type-check \"build-only {@}\" --",
            "build-only": "vite build",
            "type-check": "vue-tsc --build"
          }
        }
        ```

- `compilerOptions.tsBuildInfoFile`
  
    ビルド結果の差分を示す `.tsbuildinfo` ファイルの出力先を指定するプロパティです。
    Project Reference 機能を利用する際、出力先を明示しない場合はルートの tsconfig.json と同じフォルダーに `.tsbuildinfo` ファイルが出力されます。
    プロジェクトルートに不要なファイルが出力されると管理が煩雑になるため、 `create-vue` した際のデフォルト値である node_modules 配下の一時フォルダーを指定しています。

- `compilerOptions.module`
  
    トランスパイルしたファイルのモジュールシステムを設定するプロパティです。
    tsconfig.node.json で `ESNext` 、 tsconfig.json で `NodeNext` が `create-vue` した際にデフォルトで設定されます。
    Cypress が内部で利用している `ts-node` の挙動の都合上、 tsconfig.json に `compilerOptions.module` を設定する必要があります。
    `compilerOptions.module` の設定値については [The module output format :material-open-in-new:](https://www.typescriptlang.org/docs/handbook/modules/theory.html#the-module-output-format){ target=_blank } を参照してください。

- `compilerOptions.moduleResolution`
  
    モジュール解決の方針を設定するプロパティです。
    tsconfig.node.json では `create-vue` した際のデフォルト値として Vite での利用が推奨されている `Bundler` が設定されています。 `Bundler` についての詳細は [--moduleResolution bundler :material-open-in-new:](https://www.typescriptlang.org/docs/handbook/release-notes/typescript-5-0.html#--moduleresolution-bundler){ target=_blank } を参照してください。

## Vite の設定 {#vite-settings}

[ブランクプロジェクトの作成](./create-vuejs-blank-project.md) の手順に沿って `create-vue` でプロジェクトを作成すると、ワークスペースの直下に `vite.config.ts` が生成されます。
`vite.config.ts` に設定を追加することでビルド時の設定が定義できます。
`vite` コマンドを実行する際、ワークスペースの直下の `vite.config.ts` の設定値が自動的に読み込まれます。

### vite.config の設定値の解説 {#vite-config}

??? example "vite.config.ts の設定例"

    ```typescript title="サンプルアプリケーションの vite.config.ts"
    https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/vite.config.ts
    ```

- [条件付き設定 :material-open-in-new:](https://ja.vitejs.dev/config/#%E6%9D%A1%E4%BB%B6%E4%BB%98%E3%81%8D%E8%A8%AD%E5%AE%9A){ target=_blank }

    コマンドやモードに応じて異なる設定を適用する場合、関数を export して設定します。

    ```typescript title="vite.config.ts" hl_lines="6"
    export default defineConfig(({ command, mode, isSsrBuild, isPreview }) => {
      if (command === 'serve') {
        return {
        // 固有の設定
        }
      } else {
        // ...
      }
    })
    ```

    ??? example "条件付き設定の実装例"
        設定例では prod モードでビルド[^3]した際に、 Mock Service Worker のワーカースクリプトを削除するプラグインを読み込んでいます。

        ```typescript title="サンプルアプリケーションの vite.config.ts (抜粋)" hl_lines="6"
        https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/vite.config.ts#L30-L35
        ```

    なお、条件付き設定のために関数を export する際は `vitest.config.ts` の実装も変更が必要です。
    `vitest.config.ts` でも defineConfig を関数に変更しないと型推論が上手くできないためです。
    `vitest.config.ts` の設定については [Managing Vitest config file :material-open-in-new:](https://vitest.dev/config/){ target=_blank } を参照してください。

    ??? example "vitest.config.ts の実装例"

        ```typescript title="サンプルアプリケーションの vitest.config.ts"
        https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/vitest.config.ts
        ```

- `loadEnv()`

    モードに応じた `.env.*` ファイルを読み込み、環境変数を取得します。
    `vite` コマンドを実行する際、 `--mode` オプションに指定したパラメーターに応じた `.env.*` を読み込みます。
    例えば、 `vite build --mode dev` を実行すると `.env.dev` が読み込まれます。
    `.env.*` ファイルは環境変数を定義するために作成するもので、 `VITE_` で始まる環境変数の値を設定できます。
    モードに応じて異なる環境変数の値を設定したい場合に利用します。
    詳しくは [環境変数を設定に使用する :material-open-in-new:](https://ja.vitejs.dev/config/#%E7%92%B0%E5%A2%83%E5%A4%89%E6%95%B0%E3%82%92%E8%A8%AD%E5%AE%9A%E3%81%AB%E4%BD%BF%E7%94%A8%E3%81%99%E3%82%8B){ target=_blank } を参照してください。

    また、 `VITE_` で始まる環境変数を TypeScript で型定義するには `env.d.ts` ファイルを作成します。詳しくは [TypeScript 用の自動補完 :material-open-in-new:](https://ja.vitejs.dev/guide/env-and-mode#typescript-%E7%94%A8%E3%81%AE%E8%87%AA%E5%8B%95%E8%A3%9C%E5%AE%8C){ target=_blank } を参照してください。

- [resolve.alias :material-open-in-new:](https://ja.vitejs.dev/config/shared-options.html#resolve-alias){ target=_blank }

    パスエイリアスを設定して、 import するパスの指定を簡潔にできます。

- [server.proxy :material-open-in-new:](https://ja.vitejs.dev/config/server-options.html#server-proxy){ target=_blank }

    Vite 開発サーバーの起動時に、特定のパスで始まるリクエストの振り分け先を指定できます。
    設定例では `/api`, `/swagger` で始まるパスのリクエストをバックエンドアプリで処理するよう指定しています。

    以下のオプションを設定しています。

    - target

        URL の書換え先を設定しています。

    - changeOrigin

        Origin ヘッダーを `target` に指定した URL へ変更するよう設定しています。

    - autoRewrite

        Location ヘッダーを書き換えるよう設定しています。

    - secure

        SSL 証明書を検証しないよう設定しています。

    ??? info "API エンドポイントを設定する際の注意点"

        AlesInfiny Maia サンプルアプリでは、 バックエンドアプリとの API 通信のための OpenAPI や Axios の共通設定は `src/api-client/index.ts` で実装しています。以下の部分で `baseURL` を設定すると、 `dev` モードでビルドした際に `vite.config.ts` の `server.proxy` で設定した通りにパスの書換えができなくなります。そのため、 `dev` モードでは環境変数に空文字を設定して `basePath` `baseURL` に値を設定しないようにする、といった工夫が必要です。

        ```typescript title="サンプルアプリケーションの src/api-client/index.ts (抜粋)" hl_lines="2"
        https://github.com/AlesInfiny/maia/blob/main/samples/web-csr/dressca-frontend/consumer/src/api-client/index.ts#L18-L19
        ```

[^1]: 本ページでは、 TypeScript から JavaScript への変換を指します。
[^2]: 本ページでは、 `vite build` コマンドによりバンドル（トランスパイル後の JavaScript をブラウザーで扱いやすいよう単一のファイルにまとめる）まで行うことを指します。
<!-- textlint-disable ja-technical-writing/sentence-length -->
[^3]: Vue.js の開発者ツールを追加するプラグイン（`vueDevTools()`） はライブラリの内部で `apply: 'serve'` を指定して、 [条件付きの適用 :material-open-in-new:](https://ja.vite.dev/guide/using-plugins.html#%E6%9D%A1%E4%BB%B6%E4%BB%98%E3%81%8D%E3%81%AE%E9%81%A9%E7%94%A8){ target=_blank } を設定しています。そのため、追加の設定をしなくても prod モードでの本番環境向けのビルド結果に Vue.js の開発者ツールは含まれません。
<!-- textlint-enable ja-technical-writing/sentence-length -->
