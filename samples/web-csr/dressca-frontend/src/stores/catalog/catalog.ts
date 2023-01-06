import { defineStore } from 'pinia';
import type {
  CatalogCategory,
  CatalogBrand,
  PagedCatalogItemResponse,
} from '@/generated/api-client';
import {
  catalogBrandApi,
  catalogCategoryApi,
  catalogItemApi,
} from '@/api-client';

export const useCatalogStore = defineStore({
  id: 'catalog',
  state: () => ({
    categories: [] as CatalogCategory[],
    brands: [] as CatalogBrand[],
    catalogItemPage: {} as PagedCatalogItemResponse,
  }),
  actions: {
    async fetchCategories() {
      const response = await catalogCategoryApi.getCatalogCategories();
      this.categories = response.data;
      this.categories.unshift({ id: 0, name: 'すべて' });
    },
    async fetchBrands() {
      const response = await catalogBrandApi.getCatalogBrand();
      this.brands = response.data;
      this.brands.unshift({ id: 0, name: 'すべて' });
    },
    async fetchItems(categoryId: number, brandId: number, page?: number) {
      const response = await catalogItemApi.getByQuery(
        brandId === 0 ? undefined : brandId,
        categoryId === 0 ? undefined : categoryId,
        page,
        undefined,
      );
      this.catalogItemPage = response.data;
    },
  },
  getters: {
    getCategories(state) {
      return state.categories;
    },
    getBrands(state) {
      return state.brands;
    },
    getItems(state) {
      return state.catalogItemPage.items;
    },
  },
});
