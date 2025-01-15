import { beforeAll, beforeEach, afterEach, afterAll } from 'vitest';
import { server } from './mock/node';

/*
 * Vitestの自動テスト実行時に、共通で実行したい処理を定義する設定ファイルです。
 * たとえば、モックのワーカープロセスの起動、初期化、終了を設定しています。
 */

beforeAll(() => {
  server.listen();
});

beforeEach(() => {});

afterEach(() => {
  server.resetHandlers();
});

afterAll(() => {
  server.close();
});
