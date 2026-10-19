import '@/core/styles/base.css'
import { routeNames } from '@/core/router/route-names'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createGlobalErrorHandler } from '@/system-common/error-handler/global-error-handler'
import { getLogger } from '@/system-common/logger'
import { router } from '@/core/router'
import { registerNavigationGuards } from '@/core/router/guards'
import App from './App.vue'
import { z } from 'zod'
import { customErrorMap } from '@/system-common/validation/zod-settings'

const logger = getLogger()

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

z.setErrorMap(customErrorMap)

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

registerNavigationGuards(router)

app.mount('#app')
