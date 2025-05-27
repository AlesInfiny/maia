import type { App } from 'vue';
import { customErrorHandlerKey } from '@/shared/injection-symbols';
import { useEventBus } from '@vueuse/core';
import {
  CustomErrorBase,
  UnauthorizedError,
  NetworkError,
  ServerError,
} from './custom-error';
import { unauthorizedErrorEventKey, unhandledErrorEventKey } from '../events';

/**
 * カスタムエラーハンドラーを型付けするためのインターフェースです。
 */
export interface CustomErrorHandler {
  install(app: App): void;
  handle(
    error: unknown,
    callback: () => void,
    handlingUnauthorizedError?: (() => void) | null,
    handlingNetworkError?: (() => void) | null,
    handlingServerError?: (() => void) | null,
  ): void;
}

/**
 * カスタムエラーハンドラーを provide する Vue プラグインです。
 * @returns カスタムエラーハンドラー。
 */
export function createCustomErrorHandler(): CustomErrorHandler {
  const customErrorHandler: CustomErrorHandler = {
    install: (app: App) => {
      app.provide(customErrorHandlerKey, customErrorHandler);
    },
    handle: (
      error: unknown,
      callback: () => void,
      handlingUnauthorizedError: (() => void) | null = null,
      handlingNetworkError: (() => void) | null = null,
      handlingServerError: (() => void) | null = null,
    ) => {
      const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey);
      const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey);
      // ハンドリングできるエラーの場合はコールバックを実行
      if (error instanceof CustomErrorBase) {
        callback();

        // エラーの種類によって共通処理を行う
        // switch だと instanceof での判定ができないため if 文で判定
        if (error instanceof UnauthorizedError) {
          if (handlingUnauthorizedError) {
            handlingUnauthorizedError();
          } else {
            unauthorizedErrorEventBus.emit({
              details: 'ログインしてください。',
            });
          }
        } else if (error instanceof NetworkError) {
          if (handlingNetworkError) {
            handlingNetworkError();
          } else {
            unhandledErrorEventBus.emit({
              message: 'ネットワークエラーが発生しました。',
            });
          }
        } else if (error instanceof ServerError) {
          if (handlingServerError) {
            handlingServerError();
          } else {
            unhandledErrorEventBus.emit({
              message: 'サーバーエラーが発生しました。',
            });
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
