---
title: Vue.js 開発手順
description: Vue.js を用いた クライアントサイドアプリケーションの 開発手順を説明します。
---

# エラーハンドラーの設定 {#top}

[クライアントサイドの例外処理方針](../../../app-architecture/client-side-rendering/global-function/exception-handling.md#frontend-error-handling)
に記載の通り、フロントエンドアプリケーションでは、業務フロー上は想定されないシステムのエラーを表すシステム例外と、
業務フロー上想定されるエラーを表す業務例外をそれぞれ捕捉し、適切にハンドリングする必要があります。

## グローバルエラーハンドラーの設定 {#global-error-handler}

業務フロー上発生が想定されないエラーを捕捉し、ハンドリングするためのグローバルエラーハンドラーを設定します。

### グローバルエラーハンドラーのインストール

グローバルエラーハンドラーはアプリケーション全体で使用したいので、
アプリケーションのエントリーポイントでグローバルな値として割り当てます。

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

??? note "グローバルエラーハンドラーの実装例"

    ```ts title="global-error-handler.ts"
    import type { App, ComponentPublicInstance } from 'vue';
    import { router } from '../../router';

    export const globalErrorHandler = {
        /* eslint no-param-reassign: 0 */
        install(app: App) {
            app.config.errorHandler = (
            err: unknown,
            instance: ComponentPublicInstance | null,
            info: string,
            ) => {
            // 本サンプルAPではログの出力とエラー画面への遷移を行っています。
            // APの要件によってはサーバーやログ収集ツールにログを送信し、エラーを握りつぶすこともあります。
            /* eslint no-console: 0 */
            console.log(err, instance, info);
            router.replace({ name: 'error' });
            };

            // Vue.js 以外のエラー
            // テストやデバッグ時にエラーの発生を検知するために利用する
            window.addEventListener('error', (event) => {
            /* eslint no-console: 0 */
            console.log(event);
            });

            // テストやデバッグ時に予期せぬ非同期エラーの発生を検知するために利用する
            window.addEventListener('unhandledrejection', (event) => {
            /* eslint no-console: 0 */
            console.log(event);
            });
        },
    };
    ```

## カスタムエラーハンドラーの設定 {#custom-error-handler}

業務フロー上発生が想定されるエラーを補足し、ハンドリングするためのカスタムエラーハンドラーを設定します。

### カスタムエラーハンドラーのインストール

カスタムエラーハンドラーは、エラーの発生が予測されるコンポーネントで使用したいので、
カスタムエラーハンドラーを必要とするコンポーネントから都度呼び出せるように、
アプリケーションのエントリーポイントでカスタムエラーハンドラーを登録します。

``` ts title="main.ts" hl_lines="4 14"
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import App from './App.vue';
import { router } from './router';

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.use(globalErrorHandler);
app.use(createCustomErrorHandler());

app.mount('#app');
```

### カスタムエラーハンドラーの使用

エラーの発生が予測されるコンポーネントでは、カスタムエラーハンドラーを呼び出します。

### カスタムエラーの実装

標準の`Error`クラスを拡張して、業務例外を表すクラスを作成します。

``` ts title="custom-error.ts"
export abstract class CustomErrorBase extends Error {
  cause?: Error | null;

  constructor(message: string, cause?: Error) {
    super(message);
    // ラップ前のエラーを cause として保持
    this.cause = cause;
  }
}

export class BusinessError extends CustomErrorBase {
  constructor(message: string, cause?: Error) {
    super(message, cause);
    this.name = 'BusinessError';
  }
```

### カスタムエラーの`throw`

```ts
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_AXIOS_BASE_ENDPOINT_ORIGIN,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (axios.isAxiosError(error)) {
      if (!error.response) {
        return Promise.reject(new NetworkError('Network Error', error));
      }
      if (error.response.status === 500) {
        return Promise.reject(new ServerError('Server Error', error));
      }
      if (error.response.status === 401) {
        return Promise.reject(
          new UnauthorizedError('Unauthorized Error', error),
        );
      }

      return Promise.reject(new HttpError(error.message, error));
    }
    return Promise.reject(error);
  },
);
```

### カスタムエラーの捕捉

各コンポーネントでは、アプリケーションから提供されるカスタムエラーハンドラーを注入します。
例外の発生が予測される箇所では、`try/catch`節を使用して例外を捕捉して、
捕捉したエラーをカスタムエラーハンドラーに渡します。

```vue
<script setup lang="ts">
import { useCustomErrorHandler } from '@/shared/error-handler/use-custom-error-handler';
// カスタムエラーハンドラーを注入
const customErrorHandler = useCustomErrorHandler();

const update = async () => {
  try {
    // 例外の発生が予測される処理
  } catch (error) {
    // カスタムエラーハンドラーを使用
    customErrorHandler.handle(error, () => {
    });
  }
};
</script>
```

### カスタムエラーのハンドリング

カスタムエラーハンドラーでは、カスタムエラーの種類に応じたハンドリングを行います。
ハンドリングできなかったエラーは再度`throw`し、グローバルエラーハンドラーで捕捉します。

```ts
export function createCustomErrorHandler(): CustomErrorHandler {
  const customErrorHandler: CustomErrorHandler = {
    install: (app: App) => {
      app.provide(customErrorHandlerKey, customErrorHandler);
    },
    handle: (
      error: unknown,
      callback: () => void,
      handlingUnauthorizedError: (() => void) | null = null,
      handlingNetworkError: (() => void) | null = null,
      handlingServerError: (() => void) | null = null,
    ) => {
      // ハンドリングできるエラーの場合はコールバックを実行
      if (error instanceof CustomErrorBase) {
        callback();

        // エラーの種類によって共通処理を行う
        // switch だと instanceof での判定ができないため if 文で判定
        if (error instanceof UnauthorizedError) {
          if (handlingUnauthorizedError) {
            handlingUnauthorizedError();
          } else {
            const routingStore = useRoutingStore();
            routingStore.setRedirectFrom(
              router.currentRoute.value.path.slice(1),
            );
            router.push({ name: 'authentication/login' });
            showToast('ログインしてください。');
          }
        } else if (error instanceof NetworkError) {
          if (handlingNetworkError) {
            handlingNetworkError();
          } else {
            showToast('ネットワークエラーが発生しました。');
          }
        } else if (error instanceof ServerError) {
          if (handlingServerError) {
            handlingServerError();
          } else {
            showToast('サーバーエラーが発生しました。');
          }
        }
      } else {
        // ハンドリングできないエラーの場合は上位にエラーを投げる
        throw error;
      }
    },
  };
  return customErrorHandler;
}
```