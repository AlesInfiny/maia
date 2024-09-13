import { describe, expect, test } from 'vitest';
import { catalogItemsApi } from '@/api-client';

describe('カタログアイテムAPI', () => {
  test('getById', async () => {
    const response = await catalogItemsApi.getById(1);
    expect(response.data.id).toBe(1);
  });
});
