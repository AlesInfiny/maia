/* eslint-disable no-console */
import type { App, ComponentPublicInstance } from 'vue'

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
      console.error(err, instance, info)
    }

    // Vue.js 以外のエラー
    // テストやデバッグ時にエラーの発生を検知するために利用します。
    window.addEventListener('error', (event) => {
      console.error(event)
    })

    // テストやデバッグ時に予期せぬ非同期エラーの発生を検知するために利用します。
    window.addEventListener('unhandledrejection', (event) => {
      console.error(event)
    })
  },
}
