---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# エラーハンドラーの設定 {#top}

[フロントエンドの例外処理方針](../../../app-architecture/client-side-rendering/global-function/exception-handling.md#frontend-error-handling)
に記載の通り、業務フロー上は想定されないシステムのエラーを表すシステム例外と、業務フロー上想定されるエラーを表す業務例外をそれぞれ捕捉し、適切にハンドリングする必要があります。

## グローバルエラーハンドラーの設定 {#global-error-handler-setting}

業務フロー上発生が想定されないエラーを捕捉し、ハンドリングするためのグローバルエラーハンドラーを設定します。

### グローバルエラーハンドラーの使用 {#use-global-error-handler}

グローバルエラーハンドラーはアプリケーション全体で使用したいので、
アプリケーションのエントリーポイントで、 Vue.js の [プラグイン](https://ja.vuejs.org/guide/reusability/plugins) として、グローバルにインストールします。

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

### グローバルエラーハンドラーの実装 {#implement-global-error-handler}

Vue.js アプリケーションで発生したエラーに対するハンドリングは、 Vue.js で用意されている [app.config.errorHandler](https://ja.vuejs.org/api/application#app-config-errorhandler) に実装します。

```ts
app.config.errorHandler = (
  err: unknown,
  instance: ComponentPublicInstance | null,
  info: string,
) => {
  // Vue.js アプリケーションでのエラー発生時に実行したい処理
};
```

JavaScript の構文エラーや、 Vue アプリケーション外の例外に対しては、[addEventListener()](https://developer.mozilla.org/ja/docs/Web/API/EventTarget/addEventListener) メソッドを用いてイベントリスナーを追加することでハンドリングします。
同期処理については [error](https://developer.mozilla.org/ja/docs/Web/API/Window/error_event) イベントを検知することでハンドリングし、 API 通信や I/O 処理のような非同期処理については [unhandledrejection](https://developer.mozilla.org/ja/docs/Web/API/Window/unhandledrejection_event)
イベントを検知することで、ハンドリングします。

```ts
window.addEventListener('error', (event) => {
  // 同期処理でのエラー発生時に実行したい処理
});

window.addEventListener('unhandledrejection', (event) => {
  // 非同期処理でのエラー発生時に実行したい処理
});
```

これらを組み合わせたグローバルエラーハンドラーの実装例は以下の通りです。

<!-- cSpell:disable -->

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
    console.log(err, instance, info);
    router.replace({ name: 'error' });
    };

    window.addEventListener('error', (event) => {
    console.log(event);
    });

    window.addEventListener('unhandledrejection', (event) => {
    console.log(event);
    });
  },
};
```
<!-- cSpell:enable -->

## カスタムエラーハンドラーの設定 {#custom-error-handler-setting}

業務フロー上発生が想定されるエラーを捕捉し、ハンドリングするためのカスタムエラーハンドラーを設定します。

（今後追加予定）
