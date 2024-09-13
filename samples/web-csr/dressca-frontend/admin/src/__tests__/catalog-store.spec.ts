import { describe, expect, test } from 'vitest';
import { useCatalogStore } from '@/stores/catalog/catalog';

describe('カタログストア', () => {
  test('カタログカテゴリ', async () => {
    const store = useCatalogStore();
    await store.fetchCategories();
    expect(store.categories[0].name).toBe('すべて');
  });
});
