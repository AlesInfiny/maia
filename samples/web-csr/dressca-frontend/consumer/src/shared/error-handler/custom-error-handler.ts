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

export interface CustomErrorHandler {
  handle(
    error: unknown,
    callback: () => void,
    handlingHttpError?: ((httpError: HttpError) => void) | null,
    handlingUnauthorizedError?: (() => void) | null,
    handlingNetworkError?: (() => void) | null,
    handlingServerError?: (() => void) | null,
  ): void
}

export function useCustomErrorHandler(): CustomErrorHandler {
  const { t } = i18n.global
  const customErrorHandler: CustomErrorHandler = {
    handle: (
      error: unknown,
      callback: () => void,
      handlingHttpError: ((httpError: HttpError) => void) | null = null,
      handlingUnauthorizedError: (() => void) | null = null,
      handlingNetworkError: (() => void) | null = null,
      handlingServerError: (() => void) | null = null,
    ) => {
      const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey)
      const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey)
      // ハンドリングできるエラーの場合はコールバックを実行します。
      if (error instanceof CustomErrorBase) {
        callback()

        if (error instanceof HttpError) {
          // 業務処理で発生した HttpError を処理します。
          if (handlingHttpError) {
            handlingHttpError(error)
          }
          // エラーの種類によって共通処理を行う
          // switch だと instanceof での判定ができないため if 文で判定します。
          if (error instanceof UnauthorizedError) {
            if (handlingUnauthorizedError) {
              handlingUnauthorizedError()
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
              handlingNetworkError()
            } else {
              // NetworkError ではエラーレスポンスが存在しないため ProblemDetails の処理は実施しません。
              unhandledErrorEventBus.emit({
                message: t('networkError'),
              })
            }
          } else if (error instanceof ServerError) {
            if (handlingServerError) {
              handlingServerError()
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
    },
  }
  return customErrorHandler
}
