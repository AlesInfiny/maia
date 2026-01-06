// このサンプルコードでは、ログ出力先としてコンソール、ユーザーへの通知先としてブラウザの標準ダイアログを使用するので、ファイル全体に対して ESLint の設定を無効化しておきます。
// 実際のアプリケーションでは、適切なログ出力先や、通知先のコンポーネントを使用してください。
import { UnauthorizedError, NetworkError, ServerError } from '@/shared/custom-errors'
import { formatError } from '@/shared/helpers/format-error'
import { useLogger } from '@/composables/use-logger'
import type { MaybeAsyncFunction, MaybePromise } from '@/types'

/**
 * エラーを受け取り、呼び出し側の後処理（callback）および
 * エラー種別ごとの追加処理を順に実行する非同期ハンドラー関数の型です。
 */
export type handleErrorAsyncFunction = (
  error: unknown,
  callback: MaybeAsyncFunction<void>,
  handlingUnauthorizedError?: MaybeAsyncFunction<void> | null,
  handlingNetworkError?: MaybeAsyncFunction<void> | null,
  handlingServerError?: MaybeAsyncFunction<void> | null,
) => MaybePromise<void>

/**
 * カスタムエラーハンドラーを取得します。
 * @returns handleErrorAsyncFunction 型の関数。
 */
export function useCustomErrorHandler(): handleErrorAsyncFunction {
  const logger = useLogger()
  const handleErrorAsync = async (
    error: unknown,
    callback: MaybeAsyncFunction<void>,
    handlingUnauthorizedError: MaybeAsyncFunction<void> | null = null,
    handlingNetworkError: MaybeAsyncFunction<void> | null = null,
    handlingServerError: MaybeAsyncFunction<void> | null = null,
  ) => {
    if (error instanceof Error) {
      // Error の発生をログに記録します。
      logger.error(formatError(error))

      // 呼び出し側で指定したコールバック関数を実行します。
      await callback()

      // エラーの種類によって共通処理を行います。
      // switch だと instanceof での判定ができないため if 文で判定します。
      if (error instanceof UnauthorizedError) {
        if (handlingUnauthorizedError) {
          await handlingUnauthorizedError()
        } else {
          logger.info('401 エラーに対する共通処理を実行します。')
        }
      } else if (error instanceof NetworkError) {
        if (handlingNetworkError) {
          await handlingNetworkError()
        } else {
          logger.info('ネットワークエラーに対する共通処理を実行します。')
        }
      } else if (error instanceof ServerError) {
        if (handlingServerError) {
          await handlingServerError()
        } else {
          logger.info('500 エラーに対する共通処理を実行します。')
        }
      }
    } else {
      logger.error('Error 型でない想定外のエラーを検出しました、対処できないため再スローします。')
      throw error
    }
  }
  return handleErrorAsync
}
