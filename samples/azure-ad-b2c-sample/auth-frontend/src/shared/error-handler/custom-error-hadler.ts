import type { App } from 'vue';
import { customErrorHandlerKey } from '@/shared/injection-symbols';
import {
  BrowserAuthError,
  InteractionRequiredAuthError,
} from '@azure/msal-browser';

export interface CustomErrorHandler {
  install(app: App): void;
  handle(error: unknown, callback: () => void): void;
}

export function createCustomErrorHandler(): CustomErrorHandler {
  const customErrorHandler: CustomErrorHandler = {
    install: (app: App) => {
      app.provide(customErrorHandlerKey, customErrorHandler);
    },
    handle: (error: unknown, callback: () => void) => {
      if (error instanceof Error) {
        callback();
        if (error instanceof BrowserAuthError) {
          console.log(error);
        } else if (error instanceof InteractionRequiredAuthError) {
          console.log(error);
        }
      } else {
        // ハンドリングできないエラーの場合は上位にエラーを投げる
        throw error;
      }
    },
  };
  return customErrorHandler;
}
