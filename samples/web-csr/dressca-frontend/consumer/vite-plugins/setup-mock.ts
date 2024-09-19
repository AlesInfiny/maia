import type { ViteDevServer } from 'vite';
import { createMockServer } from '../mock/mock-web-api';

export const setupMockPlugin = () => ({
  name: 'mock',
  configureServer(server: ViteDevServer) {
    createMockServer(server.middlewares);
  },
});
