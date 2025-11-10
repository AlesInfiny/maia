---
title: Vue.js 開発手順 （CSR 編）
description: Vue.js を用いた フロントエンドアプリケーションの 開発手順を説明します。
---

# イベントハンドリングの設定 {#top}

本章では、 VueUse の `useEventBus` を用いて、 Vue.js アプリケーションにおける横断的な関心事を疎結合に扱うためのイベントハンドリングの実装手順を解説します。

## イベントバスで横断的な関心事を扱う {#why-event-bus}

アプリケーションには、特定のコンポーネントに閉じない横断的な関心事が存在します。
例として、認証状態の管理、ルーティング制御、エラーハンドリング、トースト通知、ログ出力などが挙げられます。これらはアプリケーション全体で共有される必要があり、個々のコンポーネントだけで完結しません。

Vue.js では通常、コンポーネント間の情報共有には `props` や `emits` を用います。しかし、これらは親子関係に限定されるため、横断的な関心事の管理には不向きです。

代替として `provide`/`inject` を利用する方法があります。アプリケーションのエントリーポイントで `provide` した値を、任意の子コンポーネントで `inject` して利用できます。 Vue Router がその代表例です。ただし、`<script setup>` 外から利用できない制約があり、すべてのユースケースをカバーできるわけではありません。

一方で、 Pinia は `<script setup>` 外からも利用できるため、状態の横断的共有や永続化に適しています。たとえば、認証状態の管理やユーザー情報の保持などは Pinia を用いるのが妥当です。ただし、永続化を必要としない機能に Pinia を使うのは過剰設計となる場合があります。

そこで有効なのが、 Pub/Sub（Publisher/Subscriber）モデル[^1]に基づくイベントバスです。イベントバスを介して、イベントを発火するコンポーネント（Publisher）と、それを購読するコンポーネント（Subscriber）が疎結合に情報をやり取りできます。これにより、コンポーネント間の依存を最小化しつつ、柔軟なイベント駆動の仕組みを構築できます。

本章では例として、エラー発生イベントをイベントバス経由で通知し、その内容をトースト表示する実装方法を紹介します。

## VueUse の導入 {#install-vueuse}

イベントバスのライブラリとして、 VueUse の [useEventBus :material-open-in-new:](https://vueuse.org/core/useEventBus/#useeventbus){ target=_blank } を利用します。
ワークスペースの直下で下記のコマンドを実行して、依存関係に VueUse のコアパッケージを追加します。

```shell
npm install @vueuse/core
```

package.json の dependencies に @vueuse/core が追加されたことを確認してください。

```json
"dependencies": {
  "@vueuse/core": "XX.X.X",
},
```

## イベントの定義 {#define-events}

イベントバスはアプリケーション横断的に使用されるので、ソースコードは shared フォルダー配下に配置します。
使用するイベントバスの数が増加した場合は、適切にソースコードを分割してください。

```text title="フォルダー構造" linenums="0"
<workspace-name>
└─ src/
  └─ shared/ ------------------- アプリケーションの共通部品が配置されるフォルダー
     └─ events/
            index.ts ----------- イベントの定義に関係するソースコード一式のエントリーポイント
```

アプリケーション内でイベントバスの同一性を識別するためのキー値として、 JavaScript 標準の [`Symbol()` :material-open-in-new:](https://developer.mozilla.org/ja/docs/Web/JavaScript/Reference/Global_Objects/Symbol){ target=_blank } を用いて一意な値を生成します。

下記の例は、 ハンドリングされていないエラーが発生したことを示すイベントのイベントバスのキー値を生成する例です。
他のコンポーネントやモジュールからは、このキー値を用いてイベントバスを取得します。

```typescript
import type { EventBusKey } from '@vueuse/core'
export const unhandledErrorEventKey: EventBusKey<UnhandledErrorEventPayload> =
  Symbol('unhandledErrorEventKey')
```

その際、ペイロードの型を定義し、型引数として引き渡します。
ペイロードには、該当のイベントを購読するコンポーネントへ伝達したい情報を含めます。
このことで、イベントを購読するコンポーネントではペイロードから型安全に情報を受け取ることができます。

下記の例では、ユーザーへ通知するエラーの情報をペイロードに含めています。

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

これまでの手順を実行すると、 `UnhandledErrorEvent` を扱うイベントバスの定義は下記のようになります。

??? example "UnhandledErrorEvent を扱うイベントバスの定義例"

    ```typescript
    import type { EventBusKey } from '@vueuse/core'

    type UnhandledErrorEventPayload = {
      /** ユーザーへ通知するメッセージ。 */
      message: string
      /** エラーの ID （オプション） */
      id?: string
      /** エラーのタイトル（オプション） */
      title?: string
      /** エラーの詳細（オプション） */
      detail?: string
      /** HTTP ステータスコード（オプション） */
      status?: number
      /** 通知のタイムアウト（ミリ秒、オプション） */
      timeout?: number
    }

    export const unhandledErrorEventKey: EventBusKey<UnhandledErrorEventPayload> =
      Symbol('unhandledErrorEventKey')
    ```

## イベントの発火 {#emit-events}

イベントを発火するコンポーネントでは、`useEventBus()` を用いてイベントバスを取得し、`emit` 関数を用いてイベントを発火します。
 `emit` 関数にペイロードを引き渡します。

下記の例では、例外の型に応じたイベントを発火することで、イベントの発生をアプリケーション全体に通知しています。

```typescript title="イベントを発火する例"
import { useEventBus } from '@vueuse/core'
import { unhandledErrorEventKey } from '@/shared/events'

  const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey)
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

## イベントの購読 {#subscribe-events}

イベントを購読するコンポーネントでは、`useEventBus()` を用いてイベントバスを取得し、`on` 関数を用いてイベントを購読します。
イベントの発火時に実行したい処理は`on` 関数の引数で渡します。

下記の例では、 `unhandledErrorEvent` が発生したらトースト通知をしています。
トーストに表示する内容はペイロードから取得できます。

```vue title="イベントを購読する例"
<script setup lang="ts">
import { useEventBus } from '@vueuse/core'
import { unhandledErrorEventKey } from '@/shared/events'
import { showToast } from '@/services/notification/notificationService'

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

[^1]: 一般的な Pub/Sub モデルについての説明は、[パブリッシャーとサブスクライバーのパターン :material-open-in-new:](https://learn.microsoft.com/ja-jp/azure/architecture/patterns/publisher-subscriber){ target=_blank } を参照ください。また、 Pub/Sub（Publisher/Subscriber）モデルを使用するアーキテクチャを、イベント駆動アーキテクチャと呼びます。イベント駆動アーキテクチャについての説明は、[イベント ドリブン アーキテクチャ スタイル :material-open-in-new:](https://learn.microsoft.com/ja-jp/azure/architecture/guide/architecture-styles/event-driven){ target=_blank } を参照ください。
