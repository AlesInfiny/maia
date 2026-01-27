import {
  CustomErrorBase,
  HttpError,
  UnauthorizedError,
  NetworkError,
  ServerError,
} from '@/shared/error-handler/custom-error'
import { useLogger } from '@/composables/use-logger'
import type { MaybeAsyncFunction, MaybePromise, MaybeAsyncUnaryFunction } from '@/types'

export type handleErrorAsyncFunction = (
  error: unknown,
  callback: MaybeAsyncFunction<void>,
  handlingHttpError?: MaybeAsyncUnaryFunction<HttpError, void> | null,
  handlingUnauthorizedError?: MaybeAsyncFunction<void> | null,
  handlingNetworkError?: MaybeAsyncFunction<void> | null,
  handlingServerError?: MaybeAsyncFunction<void> | null,
) => MaybePromise<void>

/**
 * カスタムエラーハンドラーを提供します。
 * HttpError / UnauthorizedError / NetworkError / ServerError などの種類ごとに
 * 共通処理やイベント通知を行います。
 * @returns エラーを処理する非同期関数
 */
export function useCustomErrorHandler(): handleErrorAsyncFunction {
  const handleErrorAsync = async (
    error: unknown,
    callback: MaybeAsyncFunction<void>,
    handlingHttpError: MaybeAsyncUnaryFunction<HttpError, void> | null = null,
    handlingUnauthorizedError: MaybeAsyncFunction<void> | null = null,
    handlingNetworkError: MaybeAsyncFunction<void> | null = null,
    handlingServerError: MaybeAsyncFunction<void> | null = null,
  ) => {
    const logger = useLogger()
    // ハンドリングできるエラーの場合はコールバックを実行します。
    if (error instanceof CustomErrorBase) {
      await callback()
      if (error instanceof HttpError) {
        // 業務処理で発生した HttpError を処理します。
        if (handlingHttpError) {
          await handlingHttpError(error)
        }
        // エラーの種類によって共通処理を行う
        // switch だと instanceof での判定ができないため if 文で判定します。
        if (error instanceof UnauthorizedError) {
          if (handlingUnauthorizedError) {
            await handlingUnauthorizedError()
          }
        } else if (error instanceof NetworkError) {
          if (handlingNetworkError) {
            await handlingNetworkError()
          }
        } else if (error instanceof ServerError) {
          if (handlingServerError) {
            await handlingServerError()
          }
        } else {
          logger.error('エラーが発生しました。', error)
        }
      }
    } else {
      // ハンドリングできないエラーの場合は上位にエラーを再スローします。
      throw error
    }
  }
  return handleErrorAsync
}
