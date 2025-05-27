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
  handle(
    error: unknown,
    callback: () => void,
    handlingUnauthorizedError?: (() => void) | null,
    handlingNetworkError?: (() => void) | null,
    handlingServerError?: (() => void) | null,
  ): void;
}

/**
 * カスタムエラーハンドラーを取得します。
 * @returns カスタムエラーハンドラー。
 */
export function useCustomErrorHandler(): CustomErrorHandler {
  const customErrorHandler: CustomErrorHandler = {
    handle: (
      error: unknown,
      callback: () => void,
      handlingUnauthorizedError: (() => void) | null = null,
      handlingNetworkError: (() => void) | null = null,
      handlingServerError: (() => void) | null = null,
    ) => {
      const unhandledErrorEventBus = useEventBus(unhandledErrorEventKey);
      const unauthorizedErrorEventBus = useEventBus(unauthorizedErrorEventKey);
      // ハンドリングできるエラーの場合はコールバックを実行します。
      if (error instanceof CustomErrorBase) {
        callback();

        // エラーの種類によって共通処理を行います。
        // switch だと instanceof での判定ができないため if 文で判定します。
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
        // ハンドリングできないエラーの場合は上位にエラーを再スローします。
        throw error;
      }
    },
  };
  return customErrorHandler;
}
