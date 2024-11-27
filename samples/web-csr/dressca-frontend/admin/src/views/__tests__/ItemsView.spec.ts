import { describe, it, expect, vi } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import { router } from '@/router';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsView from '@/views/catalog/ItemsView.vue';
import {
  catalogBrandsApi,
  catalogCategoriesApi,
  catalogItemsApi,
} from '@/api-client';
import type {
  CatalogItemResponse,
  PagedListOfCatalogItemResponse,
} from '@/generated/api-client';
import type { AxiosResponse } from 'axios';

vi.mock('@/api-client', () => ({
  catalogBrandsApi: {
    getCatalogBrands: vi.fn().mockImplementation(async () => {
      return Promise.resolve({
        data: createBrandResponse(),
      });
    }),
  },
  catalogCategoriesApi: {
    getCatalogCategories: vi.fn().mockImplementation(async () => {
      return Promise.resolve({
        data: createCategoryResponse(),
      });
    }),
  },
  catalogItemsApi: {
    getByQuery: vi
      .fn()
      .mockImplementation(async (brandId: number, categoryId: number) => {
        return Promise.resolve({
          data: createPagedListCatalogItemResponse(),
        } as PagedListOfCatalogItemResponse);
      }),
  },
}));

describe('アイテム一覧画面のテスト', () => {
  it('アイテム一覧が表示できる', async () => {
    // Arrange
    const pinia = createPinia();
    setActivePinia(pinia);
    const customErrorHandler = createCustomErrorHandler();
    const wrapper = mount(ItemsView, {
      global: { plugins: [pinia, router, customErrorHandler] },
    });

    // Act
    await flushPromises();

    // Assert
    expect(wrapper.html()).toContain('カテゴリ1');
    expect(wrapper.html()).toContain('ブランド3');
    expect(wrapper.html()).toContain('クルーネック Tシャツ - ブラック');
  });
});

function createPagedListCatalogItemResponse(
  items: CatalogItemResponse[],
): PagedListOfCatalogItemResponse {
  return {
    hasNext: false,
    hasPrevious: false,
    page: 1,
    pageSize: 20,
    totalCount: 3,
    totalPages: 1,
    items: [
      {
        id: 1,
        catalogCategoryId: 1,
        catalogBrandId: 3,
        description: '定番の無地ロングTシャツです。',
        name: 'クルーネック Tシャツ - ブラック',
        price: 1980,
        productCode: 'C000000001',
        assetCodes: [],
        rowVersion: 'byte',
      },
    ] as CatalogItemResponse[],
  };
}

function createBrandResponse(): CatalogBrandResponse {
  return [
    {
      id: 1,
      name: 'ブランド1',
    },
    {
      id: 2,
      name: 'ブランド2',
    },
    {
      id: 3,
      name: 'ブランド3',
    },
  ];
}

function createCategoryResponse(): CatalogCategoryResponse[] {
  return [
    {
      id: 1,
      name: 'カテゴリ1',
    },
    {
      id: 2,
      name: 'カテゴリ2',
    },
    {
      id: 3,
      name: 'カテゴリ3',
    },
  ];
}
