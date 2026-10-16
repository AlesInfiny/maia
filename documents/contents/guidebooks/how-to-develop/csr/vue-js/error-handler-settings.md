---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

<!-- cSpell:ignore unhandledrejection -->

# エラーハンドラーの設定 {#top}

[フロントエンドの例外処理方針](../../../../app-architecture/client-side-rendering/global-function/exception-handling.md#frontend-error-handling-policy)
に記載の通り、業務フロー上は想定されないシステムのエラーを表すシステム例外と、業務フロー上想定されるエラーを表す業務例外をそれぞれ捕捉し、適切にハンドリングする必要があります。

## グローバルエラーハンドラーの設定 {#global-error-handler-setting}

業務フロー上発生が想定されないエラーを捕捉し、ハンドリングするためのグローバルエラーハンドラーを設定します。
エラーハンドラーは業務知識を持たないシステム共通の部品なので、新しく`system-common`フォルダーを作成し、下図の階層に、`global-error-handler.ts`を作成します。

```text title="フォルダー構造" linenums="0"
<workspace-name>
└─ src/
  └─ system-common/ --------------- システム共通の機能が配置されるフォルダー
     └─ error-handler/
        └─ global-error-handler.ts
```

グローバルエラーハンドラーは、 Vue.js の [プラグイン :material-open-in-new:](https://ja.vuejs.org/guide/reusability/plugins){ target=_blank } として実装します。
プラグインは、アプリケーション全体で利用したい機能やコンポーネントがある場合に有用です。

<!-- textlint-disable ja-technical-writing/sentence-length -->

??? example "グローバルエラーハンドラーの実装例"

    <!-- textlint-disable ja-technical-writing/sentence-length -->

    Vue.js アプリケーションで発生したエラーに対するハンドリングは、 Vue.js で用意されている [app.config.errorHandler :material-open-in-new:](https://ja.vuejs.org/api/application#app-config-errorhandler){ target=_blank } に実装します。 JavaScript の構文エラーや、 Vue アプリケーション外の例外に対しては、[addEventListener() :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/API/EventTarget/addEventListener){ target=_blank } メソッドを用いてイベントリスナーを追加することでハンドリングします。同期処理については [error :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/API/Window/error_event){ target=_blank } イベントを検知することでハンドリングし、 API 通信や I/O 処理のような非同期処理については [unhandledrejection :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/API/Window/unhandledrejection_event){ target=_blank } イベントを検知することで、ハンドリングします。

    <!-- textlint-enable ja-technical-writing/sentence-length -->

    システム共通のコードは、画面を遷移させられません（ [アーキテクチャ解説 - 参照方向](../../../../app-architecture/client-side-rendering/frontend-application/index.md#reference-direction) ）。
    そのため、グローバルエラーハンドラーは、エラー画面へ遷移する関数を引数で受け取るプラグインのファクトリー（ `createGlobalErrorHandler` ）として実装します。

    ```typescript title="global-error-handler.ts"
    --8<-- "samples/web-csr/dressca-frontend/consumer/src/system-common/error-handler/global-error-handler.ts"
    ```

<!-- textlint-enable ja-technical-writing/sentence-length -->

実装したグローバルエラーハンドラーを、 core 層のアプリケーションのエントリーポイントでインストールします。
エラー画面へ遷移する関数は、ここで渡します。

??? example "エントリーポイントの実装例"

    ```typescript title="main.ts" hl_lines="2-3 12-18"
    import { createApp } from 'vue'
    import { createGlobalErrorHandler } from '@/system-common/error-handler/global-error-handler'
    import { routeNames } from '@/core/router/route-names'
    import { createPinia } from 'pinia'
    import App from './App.vue'
    import { router } from '@/core/router'

    const app = createApp(App)

    app.use(createPinia())
    app.use(router)
    app.use(
      createGlobalErrorHandler({
        navigateToErrorPage: () => {
          void router.replace({ name: routeNames.error })
        },
      }),
    )

    app.mount('#app')
    ```

## カスタムエラーハンドラーの設定 {#custom-error-handler-setting}

業務フロー上想定されるエラーを把握するためには、バックエンドアプリケーションとの連携が必要です。
そのため、業務例外に対するハンドリングは、[OpenAPI 仕様書からのクライアントコード生成](./create-api-client-code.md) にてバックエンドアプリケーションとの連携を設定したあと、[カスタムエラーの設定](./custom-error-settings.md) で行います。
