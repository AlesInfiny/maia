import { setupWorker } from 'msw/browser';
import { handlers } from './handler';

/**
 * ブラウザー用のワーカーを起動します。
 */
export const worker = setupWorker(...handlers);
