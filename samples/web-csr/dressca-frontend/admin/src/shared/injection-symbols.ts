import type { InjectionKey } from 'vue';
import type { CustomErrorHandler } from './error-handler/custom-error-handler';

/**
 * カスタムエラーハンドラーを provide/inject するための一意なキー。
 */
export const customErrorHandlerKey = Symbol(
  'customErrorHandler',
) as InjectionKey<CustomErrorHandler>;
