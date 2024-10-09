import { inject } from 'vue';
import type { CustomErrorHandler } from './custom-error-handler';
import { customErrorHandlerKey } from '../injection-symbols';

export function useCustomErrorHandler(): CustomErrorHandler {
  return inject(customErrorHandlerKey)!;
}
