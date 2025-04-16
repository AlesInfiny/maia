import type { App } from 'vue';
import { showToast } from '@/services/notification/notificationService';
import { router } from '@/router';
import { customErrorHandlerKey } from '@/shared/injection-symbols';
import {
  CustomErrorBase,
  UnauthorizedError,
  NetworkError,
  ServerError,
} from './custom-error';

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
      // ハンドリングできるエラーの場合はコールバックを実行
      if (error instanceof CustomErrorBase) {
        callback();

        // エラーの種類によって共通処理を行う
        // switch だと instanceof での判定ができないため if 文で判定
        if (error instanceof UnauthorizedError) {
          if (handlingUnauthorizedError) {
            handlingUnauthorizedError();
          } else {
            // 現在の画面情報をクエリパラメーターに保持してログイン画面にリダイレクトします。
            router.push({
              name: 'authentication/login',
              query: {
                redirectName: router.currentRoute.value.name?.toString(),
                redirectParams: JSON.stringify(
                  router.currentRoute.value.params,
                ),
                redirectQuery: JSON.stringify(router.currentRoute.value.query),
              },
            });
            showToast('ログインしてください。');
          }
        } else if (error instanceof NetworkError) {
          if (handlingNetworkError) {
            handlingNetworkError();
          } else {
            showToast('ネットワークエラーが発生しました。');
          }
        } else if (error instanceof ServerError) {
          if (handlingServerError) {
            handlingServerError();
          } else {
            showToast('サーバーエラーが発生しました。');
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
