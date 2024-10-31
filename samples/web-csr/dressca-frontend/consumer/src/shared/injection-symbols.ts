import type { InjectionKey } from 'vue';
import type { CustomErrorHandler } from './error-handler/custom-error-handler';

export const customErrorHandlerKey = Symbol(
  'customErrorHandler',
) as InjectionKey<CustomErrorHandler>;
