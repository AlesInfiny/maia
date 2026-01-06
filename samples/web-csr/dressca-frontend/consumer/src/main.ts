import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { authenticationGuard } from '@/shared/authentication/authentication-guard'
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler'
import App from './App.vue'
import { router } from './router'
import { i18n } from './locales/i18n'
import { useLogger } from './composables/use-logger'
import '@/assets/base.css'

const logger = useLogger()

/**
 * MSW (Mock Service Worker) を有効化します。
 * モックモード時のみ動的にモジュールをインポートし、Service Worker を開始します。
 * @returns Service Worker の登録情報。モックモードでない場合は undefined。
 */
async function enableMocking(): Promise<ServiceWorkerRegistration | undefined> {
  const { worker } = await import('../mock/browser')
  return worker.start({
    onUnhandledRequest: 'bypass',
  })
}

/*
 * ワーカープロセスの起動前にアプリケーションがマウントされると、
 * ホーム画面に API をコールする処理があった場合に想定外のエラーが発生するので、
 * モック用のワーカープロセスの起動を待つ必要があります。
 */
if (import.meta.env.MODE === 'mock') {
  try {
    await enableMocking()
  } catch (error) {
    logger.error('モック用のワーカープロセスの起動に失敗しました。', error)
  }
}

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)
app.use(globalErrorHandler)

authenticationGuard(router)

app.mount('#app')
