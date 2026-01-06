import { useEventBus } from '@vueuse/core'
import { CustomErrorBase, UnauthorizedError, NetworkError, ServerError } from './custom-error'
import { unauthorizedErrorEventKey, unhandledErrorEventKey } from '../events'
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
  const handleErrorAsync = async (
    error: unknown,
    callback: MaybeAsyncFunction<void>,
    handlingUnauthorizedError: MaybeAsyncFunction<void> | null = null,
    handlingNetworkError: MaybeAsyncFunction<void> | null = null,
    handlingServerError: MaybeAsyncFunction<void> | null = null,
  ) => {
    const logger = useLogger()
    const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey)
    const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey)
    // ハンドリングできるエラーの場合はコールバックを実行します。
    if (error instanceof CustomErrorBase) {
      logger.error(JSON.stringify(error.toJSON()))
      await callback()

      // エラーの種類によって共通処理を行います。
      // switch だと instanceof での判定ができないため if 文で判定します。
      if (error instanceof UnauthorizedError) {
        if (handlingUnauthorizedError) {
          await handlingUnauthorizedError()
        } else {
          unauthorizedErrorEventBus.emit({
            details: 'ログインしてください。',
          })
        }
      } else if (error instanceof NetworkError) {
        if (handlingNetworkError) {
          await handlingNetworkError()
        } else {
          unhandledErrorEventBus.emit({
            message: 'ネットワークエラーが発生しました。',
          })
        }
      } else if (error instanceof ServerError) {
        if (handlingServerError) {
          await handlingServerError()
        } else {
          unhandledErrorEventBus.emit({
            message: 'サーバーエラーが発生しました。',
          })
        }
      }
    } else {
      // ハンドリングできないエラーの場合は上位にエラーを再スローします。
      throw error
    }
  }
  return handleErrorAsync
}
