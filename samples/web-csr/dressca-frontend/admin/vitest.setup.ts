import { beforeAll, beforeEach, afterEach, afterAll } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { createApp } from 'vue';
import { server } from './mock/node';

const app = createApp({});

beforeAll(() => {
  server.listen();
  server.events.on('request:start', ({ request }) => {
    /* eslint no-console: 0 */
    console.log('MSW intercepted:', request.method, request.url);
  });
});

beforeEach(() => {
  // エラー "getActivePinia()" was called but there was no active Pinia. が発生するので、事前にPiniaを初期化しておく。
  const pinia = createPinia();
  app.use(pinia);
  setActivePinia(pinia);
});

afterEach(() => {
  server.resetHandlers();
});

afterAll(() => {
  server.close();
});
