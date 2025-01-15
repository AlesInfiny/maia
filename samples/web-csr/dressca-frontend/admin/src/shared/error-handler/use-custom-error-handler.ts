import { inject } from 'vue';
import type { CustomErrorHandler } from './custom-error-handler';
import { customErrorHandlerKey } from '../injection-symbols';

/**
 * カスタムエラーハンドラーを取得します。
 * @returns アプリケーションレベルで提供されたカスタムエラーハンドラー。
 */
export function useCustomErrorHandler(): CustomErrorHandler {
  return inject(customErrorHandlerKey)!;
}
