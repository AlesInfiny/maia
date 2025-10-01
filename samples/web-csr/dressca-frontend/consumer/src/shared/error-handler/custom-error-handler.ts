import { i18n } from '@/locales/i18n'
import { errorMessageFormat } from '@/shared/error-handler/error-message-format'
import { useEventBus } from '@vueuse/core'
import {
  CustomErrorBase,
  HttpError,
  UnauthorizedError,
  NetworkError,
  ServerError,
} from './custom-error'
import { unauthorizedErrorEventKey, unhandledErrorEventKey } from '../events'
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

export function useCustomErrorHandler(): handleErrorAsyncFunction {
  const { t } = i18n.global
  const handleErrorAsync = async (
    error: unknown,
    callback: MaybeAsyncFunction<void>,
    handlingHttpError: MaybeAsyncUnaryFunction<HttpError, void> | null = null,
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
      // eslint-disable-next-line no-console
      console.error(JSON.stringify(error.toJSON()))
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
          } else {
            unauthorizedErrorEventBus.emit({
              details: t('loginRequiredError'),
            })
            // ProblemDetail の構造に依存します。
            if (!error.response?.exceptionId) {
              unhandledErrorEventBus.emit({
                message: t('loginRequiredError'),
              })
            } else {
              const message = errorMessageFormat(
                error.response.exceptionId,
                error.response.exceptionValues,
              )
              unhandledErrorEventBus.emit({
                message,
                id: error.response.exceptionId,
                title: error.response.title,
                detail: error.response.detail,
                status: error.response.status,
                timeout: 100000,
              })
            }
          }
        } else if (error instanceof NetworkError) {
          if (handlingNetworkError) {
            await handlingNetworkError()
          } else {
            // NetworkError ではエラーレスポンスが存在しないため ProblemDetails の処理は実施しません。
            unhandledErrorEventBus.emit({
              message: t('networkError'),
            })
          }
        } else if (error instanceof ServerError) {
          if (handlingServerError) {
            await handlingServerError()
            // ProblemDetail の構造に依存します。
          } else if (!error.response?.exceptionId) {
            unhandledErrorEventBus.emit({
              message: t('serverError'),
            })
          } else {
            const message = errorMessageFormat(
              error.response.exceptionId,
              error.response.exceptionValues,
            )
            unhandledErrorEventBus.emit({
              message,
              id: error.response.exceptionId,
              title: error.response.title,
              detail: error.response.detail,
              status: error.response.status,
              timeout: 100000,
            })
          }
        }
      }
    } else {
      // ハンドリングできないエラーの場合は上位にエラーを再スローします。
      throw error
    }
  }
  return handleErrorAsync
}
