import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import { globalErrorHandler } from './shared/error-handler/global-error-handler'
import { useLogger } from './composables/use-logger'
const logger = useLogger()

/**
 * MSW (Mock Service Worker) を有効化します。
 * モックモード時のみ動的にモジュールをインポートし、Service Worker を開始します。
 * @returns Service Worker の登録情報。モックモードでない場合は undefined。
 */
async function enableMocking(): Promise<ServiceWorkerRegistration | undefined> {
  const { worker } = await import('../mock/browser') // モックモード以外ではインポート不要なので、動的にインポートします。
  return worker.start({
    onUnhandledRequest: 'bypass', // MSW のハンドラーを未設定のリクエストに対して警告を出さないように設定します。
  })
}

if (import.meta.env.MODE === 'mock') {
  try {
    await enableMocking() // ワーカーの起動を待ちます。
  } catch (error) {
    logger.error('モック用のワーカープロセスの起動に失敗しました。', error)
  }
}

const app = createApp(App)

app.use(createPinia())

app.use(globalErrorHandler)

app.mount('#app')
