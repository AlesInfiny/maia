import { describe, it, expect, vi } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { router } from '@/router';
import { createPinia, setActivePinia } from 'pinia';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import ItemsAddView from '@/views/catalog/ItemsAddView.vue';
import type {
  CatalogBrandResponse,
  CatalogCategoryResponse,
} from '@/generated/api-client';
import { catalogItemsApi } from '@/api-client';

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
    postCatalogItem: vi.fn().mockImplementation(async ({
      name,
      description,
      price,
      productCode,
      catalogCategoryId,
      catalogBrandId,
    }) => {
      return Promise.resolve();
    }),
  },
}));


describe('アイテム追加画面のテスト', () => {
  it('アイテムを追加できる', async () => {
    // Arrange
    const pinia = createPinia();
    setActivePinia(pinia);
    const customErrorHandler = createCustomErrorHandler();
    const wrapper = mount(ItemsAddView, {
      global: { plugins: [pinia, router, customErrorHandler] },
    });
    await flushPromises();
    expect(wrapper.html()).toContain('カタログアイテム追加');

    // Act
    wrapper.find('input[id="item-name"]').setValue('テストアイテム');
    wrapper.find('textarea[id="description"]').setValue('テストアイテムの説明');
    wrapper.find('input[id="unit-price"]').setValue('1000');
    wrapper.find('input[id="product-code"]').setValue('TEST-001');
    wrapper.find('select[id="category"]').setValue('2');
    wrapper.find('select[id="brand"]').setValue('2');
    wrapper.find('button').trigger('click');
    await flushPromises();

    // Assert
    expect(vi.mocked(catalogItemsApi.postCatalogItem)).toHaveBeenCalledWith({
      name: 'テストアイテム',
      description: 'テストアイテムの説明',
      price: 1000,
      productCode: 'TEST-001',
      catalogCategoryId: 2,
      catalogBrandId: 2,
    });
    // expect(wrapper.html()).toContain('カタログアイテムを追加しました。');
  });
});

function createBrandResponse(): CatalogBrandResponse[] {
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
