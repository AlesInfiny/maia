import './assets/base.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { authenticationGuard } from '@/shared/authentication/authentication-guard'
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler'
import { router } from './router'
import App from './App.vue'

/**
 * モック用のワーカープロセスが起動していることを確認します。
 * @returns {Promise<ServiceWorkerRegistration | undefined>}
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
    /* eslint no-console: 0 */
    console.error('モック用のワーカープロセスの起動に失敗しました。', error)
  }
}

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.use(globalErrorHandler)

authenticationGuard(router)

app.mount('#app')
