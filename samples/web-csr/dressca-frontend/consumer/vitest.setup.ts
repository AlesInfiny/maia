import { beforeAll, beforeEach, afterEach, afterAll, vi } from 'vitest';

/*
 * Vitestの自動テスト実行時に、共通で実行したい処理を定義する設定ファイルです。
 * たとえば、テストスイートの実行後にモック化した処理の実装を元に戻します。
 */

beforeAll(() => {});

beforeEach(() => {});

afterEach(() => {
  vi.restoreAllMocks();
});

afterAll(() => {});
