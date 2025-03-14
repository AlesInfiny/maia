import { setupWorker } from 'msw/browser';
import { handlers } from './handlers';

/**
 * ブラウザー用のワーカーを起動します。
 */
export const worker = setupWorker(...handlers);
