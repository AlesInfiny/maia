import type { App, ComponentPublicInstance, Plugin } from 'vue'
import { useLogger } from '@/system-common/composables/use-logger'

const logger = useLogger()

/**
 * グローバルエラーハンドラーを生成するための設定です。
 */
export interface GlobalErrorHandlerOptions {
  /**
   * 業務上想定しないシステムエラーが発生したとき、エラー画面へ遷移します。
   * 遷移先の画面は利用する側（ app 層）が決めます。
   */
  navigateToErrorPage: () => void
}

/**
 * 業務上想定しないシステムエラーをハンドリングするためのグローバルエラーハンドラーを生成します。
 * @param options グローバルエラーハンドラーの設定。
 * @returns `app.use` に登録するプラグイン。
 */
export function createGlobalErrorHandler(options: GlobalErrorHandlerOptions): Plugin {
  return {
    install(app: App) {
      app.config.errorHandler = (
        err: unknown,
        instance: ComponentPublicInstance | null,
        info: string,
      ) => {
        // 本サンプルAPではログの出力とエラー画面への遷移を行っています。
        // APの要件によってはサーバーやログ収集ツールにログを送信し、エラーを握りつぶすこともあります。
        logger.error(err, instance, info)
        options.navigateToErrorPage()
      }

      // Vue.js 以外のエラー
      // テストやデバッグ時にエラーの発生を検知するために利用する
      window.addEventListener('error', (event) => {
        logger.error(event)
      })

      // テストやデバッグ時に予期せぬ非同期エラーの発生を検知するために利用する
      window.addEventListener('unhandledrejection', (event) => {
        logger.error(event)
      })
    },
  }
}
