import { createMockServer } from '../mock/mock-web-api';

export const setupMockPlugin = () => ({
  name: 'mock',
  configureServer({ middlewares }) {
    createMockServer(middlewares);
  },
});
