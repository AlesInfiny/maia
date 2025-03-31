import { setupServer } from 'msw/node';
import { handlers } from './handlers';

/**
 * Node.js 環境用のワーカーを起動します。
 */
export const server = setupServer(...handlers);
