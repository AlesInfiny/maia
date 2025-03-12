---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

<!-- cSpell:ignore unhandledrejection -->

# エラーハンドラーの設定 {#top}

[フロントエンドの例外処理方針](../../../app-architecture/client-side-rendering/global-function/exception-handling.md#frontend-error-handling-policy)
に記載の通り、業務フロー上は想定されないシステムのエラーを表すシステム例外と、業務フロー上想定されるエラーを表す業務例外をそれぞれ捕捉し、適切にハンドリングする必要があります。

## グローバルエラーハンドラーの設定 {#global-error-handler-setting}

業務フロー上発生が想定されないエラーを捕捉し、ハンドリングするためのグローバルエラーハンドラーを設定します。
エラーハンドラーはアプリケーションの共通部品なので、新しく`shared`フォルダーを作成し、下図の階層に、`global-error-handler.ts`を作成します。

``` text title="フォルダー構造" linenums="0"
<workspace-name>
└─ src/
  └─ shared/ ---------------------- アプリケーションの共通部品が配置されるフォルダー
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

    ```ts title="global-error-handler.ts"
    import type { App, ComponentPublicInstance } from 'vue';
    import { router } from '../../router';

    export const globalErrorHandler = {
      install(app: App) {
        app.config.errorHandler = (
          err: unknown,
          instance: ComponentPublicInstance | null,
          info: string,
        ) => {
          // Vue.js アプリケーションでのエラー発生時に実行したい処理
          console.log(err, instance, info);
          router.replace({ name: 'error' });
        };

        window.addEventListener('error', (event) => {
          // 同期処理でのエラー発生時に実行したい処理
          console.log(event);
        });

        window.addEventListener('unhandledrejection', (event) => {
          // 非同期処理でのエラー発生時に実行したい処理
          console.log(event);
        });
      },
    };
    ```

<!-- textlint-enable ja-technical-writing/sentence-length -->

実装したグローバルエラーハンドラーを、アプリケーションのエントリーポイントでインストールします。

??? example "エントリーポイントの実装例"

    ``` ts title="main.ts" hl_lines="3 12"
    import { createApp } from 'vue';
    import { createPinia } from 'pinia';
    import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
    import App from './App.vue';
    import { router } from './router';

    const app = createApp(App);

    app.use(createPinia());
    app.use(router);

    app.use(globalErrorHandler);

    app.mount('#app');
    ```
