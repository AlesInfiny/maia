import type { App, ComponentPublicInstance } from 'vue'
import { useLogger } from '@/composables/use-logger'

const logger = useLogger()

export const globalErrorHandler = {
  /* eslint no-param-reassign: 0 */
  install(app: App) {
    app.config.errorHandler = (
      err: unknown,
      instance: ComponentPublicInstance | null,
      info: string,
    ) => {
      // 本サンプルAPではコンソールへログの出力を行います。
      // APの要件によってはサーバーやログ収集ツールにログを送信し、エラーを握りつぶすこともあります。
      logger.error(err, instance, info)
    }

    // Vue.js 以外のエラー
    // テストやデバッグ時にエラーの発生を検知するために利用します。
    window.addEventListener('error', (event) => {
      logger.error(event)
    })

    // テストやデバッグ時に予期せぬ非同期エラーの発生を検知するために利用します。
    window.addEventListener('unhandledrejection', (event) => {
      logger.error(event)
    })
  },
}
