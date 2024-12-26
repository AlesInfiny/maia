import type { App } from 'vue';
import { showToast } from '@/services/notification/notificationService';
import { useRoutingStore } from '@/stores/routing/routing';
import { router } from '@/router';
import { customErrorHandlerKey } from '@/shared/injection-symbols';
import { i18n } from '@/locales/i18n';
import { errorMessageFormat } from '@/shared/error-handler/error-message-format';
import {
  CustomErrorBase,
  HttpError,
  UnauthorizedError,
  NetworkError,
  ServerError,
} from './custom-error';

export interface CustomErrorHandler {
  install(app: App): void;
  handle(
    error: unknown,
    callback: () => void,
    handlingHttpError?: ((httpError: HttpError) => void) | null,
    handlingUnauthorizedError?: (() => void) | null,
    handlingNetworkError?: (() => void) | null,
    handlingServerError?: (() => void) | null,
  ): void;
}

export function createCustomErrorHandler(): CustomErrorHandler {
  const { t } = i18n.global;
  const customErrorHandler: CustomErrorHandler = {
    install: (app: App) => {
      app.provide(customErrorHandlerKey, customErrorHandler);
    },
    handle: (
      error: unknown,
      callback: () => void,
      handlingHttpError: ((httpError: HttpError) => void) | null = null,
      handlingUnauthorizedError: (() => void) | null = null,
      handlingNetworkError: (() => void) | null = null,
      handlingServerError: (() => void) | null = null,
    ) => {
      // ハンドリングできるエラーの場合はコールバックを実行
      if (error instanceof CustomErrorBase) {
        callback();

        if (error instanceof HttpError) {
          // 業務処理で発生した HttpError を処理する
          if (handlingHttpError) {
            handlingHttpError(error);
          }
          // エラーの種類によって共通処理を行う
          // switch だと instanceof での判定ができないため if 文で判定
          if (error instanceof UnauthorizedError) {
            if (handlingUnauthorizedError) {
              handlingUnauthorizedError();
            } else {
              const routingStore = useRoutingStore();
              routingStore.setRedirectFrom(
                router.currentRoute.value.path.slice(1),
              );
              router.push({ name: 'authentication/login' });
              if (!error.response) {
                showToast(t('loginRequiredError'));
              } else {
                const message = errorMessageFormat(
                  error.response.exceptionId,
                  error.response.exceptionValues,
                );
                showToast(
                  message,
                  error.response.exceptionId,
                  error.response.title,
                  error.response.detail,
                  error.response.status,
                  100000,
                );
              }
            }
          } else if (error instanceof NetworkError) {
            if (handlingNetworkError) {
              handlingNetworkError();
            } else {
              // NetworkError ではエラーレスポンスが存在しないため ProblemDetails の処理は実施しない
              showToast(t('networkError'));
            }
          } else if (error instanceof ServerError) {
            if (handlingServerError) {
              handlingServerError();
            } else if (!error.response) {
              showToast(t('serverError'));
            } else {
              const message = errorMessageFormat(
                error.response.exceptionId,
                error.response.exceptionValues,
              );
              showToast(
                message,
                error.response.exceptionId,
                error.response.title,
                error.response.detail,
                error.response.status,
                100000,
              );
            }
          }
        }
      } else {
        // ハンドリングできないエラーの場合は上位にエラーを投げる
        throw error;
      }
    },
  };
  return customErrorHandler;
}
