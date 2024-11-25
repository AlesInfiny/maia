import { beforeAll, beforeEach, afterEach, afterAll } from 'vitest';
import { server } from './mock/node';

beforeAll(() => {
  server.listen();
});

beforeEach(() => {
});

afterEach(() => {
  server.resetHandlers();
});

afterAll(() => {
  server.close();
});
