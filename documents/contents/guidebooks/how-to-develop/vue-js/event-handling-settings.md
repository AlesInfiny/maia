---
title: Vue.js 開発手順
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# イベントハンドリングの設定 {#top}

## イベント駆動の必要性 {#why-event-driven}

イベントバスの構造について述べます。

## VueUseの導入 {#install-vue-use}

イベントバス構造を表現するコンポーザブルとして、
[useEventBus](https://vueuse.org/core/useEventBus/#useeventbus){ target=_blank }を利用します。
下記のコマンドを実行して、依存関係に VueUse のコアパッケージを追加します。

```shell
npm install @vueuse/core
```

package.json の Dependencies に @vueuse/core が追加されたことを確認してください。

## イベントの定義 {#define-events}

イベントバスはアプリケーション横断的に使用されるので、関係するソースコードは shared フォルダー配下に配置します。

```text title="フォルダー構造" linenums="0" hl_lines="4"
<workspace-name>
└─ src/
  └─ shared/ ------------------- アプリケーションの共通部品が配置されるフォルダー
     └─ events/
            index.ts ----------- イベントの定義に関係するソースコード一式のエントリーポイント
```

```typescript
export declare function useEventBus<T = unknown, P = any>(
  key: EventBusIdentifier<T>,
): UseEventBusReturn<T, P>
```

```typescript
type UnhandledErrorEventPayload = {
  /** ユーザーへ通知するメッセージ。 */
  message: string
  /** エラーの ID （オプション） */
  id?: string
  /** エラーのタイトル（オプション） */
  title?: string
  /** エラーの詳細（オプション） */
  detail?: string
  /** HTTPステータスコード（オプション） */
  status?: number
  /** 通知のタイムアウト（ミリ秒、オプション） */
  timeout?: number
}
```

```typescript
export const unhandledErrorEventKey: EventBusKey<UnhandledErrorEventPayload> =
  Symbol('unhandledErrorEventKey')
```

## イベントの購読 {#subscribe-events}

`EventBusKey` を用いてイベントバスを取得し、`on` 関数を用いてイベントを購読します。
イベントの発火時に実行したい処理は`on` 関数の引数で渡します。

```vue title="イベントを購読する例"
<script setup lang="ts">
import { useEventBus } from '@vueuse/core'
import { unhandledErrorEventKey } from '@/shared/events'

const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey)
unhandledErrorEventBus.on((payload) =>
  showToast(
    payload.message,
    payload.id,
    payload.title,
    payload.detail,
    payload.status,
    payload.timeout,
  ),
)
</script>
```

## イベントの発火 {#emit-events}

`EventBusKey` を用いてイベントバスを取得し、`eDmit` 関数を用いてイベントを発火します。
 `emit` 関数に `Payload` 型の情報を引き渡すことで、イベントを購読しているコンポーネントに情報を連携できます。

```typescript title="イベントを発火する例"
import { useEventBus } from '@vueuse/core'
import { unhandledErrorEventKey } from '@/shared/events'
// (中略)
    } else if (error instanceof ServerError) {
    unhandledErrorEventBus.emit({
        message,
        id: error.response.exceptionId,
        title: error.response.title,
        detail: error.response.detail,
        status: error.response.status,
        timeout: 100000,
    })
    }
}
```

## 実装例{#implementation-example}
