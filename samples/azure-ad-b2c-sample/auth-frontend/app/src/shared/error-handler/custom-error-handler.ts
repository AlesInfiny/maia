/* eslint-disable no-console */
/* eslint-disable no-alert */
// このサンプルコードでは、ログ出力先としてコンソール、ユーザーへの通知先としてブラウザの標準ダイアログを使用するので、ファイル全体に対して ESLint の設定を無効化しておきます。
// 実際のアプリケーションでは、適切なログ出力先や、通知先のコンポーネントを使用してください。
import {
  UnauthorizedError,
  NetworkError,
  ServerError,
} from '@/shared/custom-errors';
import { formatError } from '@/shared/helpers/format-error';

/**
 * カスタムエラーハンドラーのインターフェースです。
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
      if (error instanceof Error) {
        // Error の発生をログに記録します。
        console.error(formatError(error));

        // 呼び出し側で指定したコールバック関数をを実行します。
        callback();

        // エラーの種類によって共通処理を行います。
        // switch だと instanceof での判定ができないため if 文で判定します。
        if (error instanceof UnauthorizedError) {
          if (handlingUnauthorizedError) {
            handlingUnauthorizedError();
          } else {
            console.info('401 エラーに対する共通処理を実行します。');
          }
        } else if (error instanceof NetworkError) {
          if (handlingNetworkError) {
            handlingNetworkError();
          } else {
            console.info('ネットワークエラーに対する共通処理を実行します。');
          }
        } else if (error instanceof ServerError) {
          if (handlingServerError) {
            handlingServerError();
          } else {
            console.info('500 エラーに対する共通処理を実行します。');
          }
        }
      } else {
        console.error(
          'Error 型でない想定外のエラーを検出しました、対処できないため再スローします。',
        );
        throw error;
      }
    },
  };
  return customErrorHandler;
}
