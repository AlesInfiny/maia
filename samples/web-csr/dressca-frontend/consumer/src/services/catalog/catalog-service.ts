import { useCatalogStore } from '@/stores/catalog/catalog';

const catalogStore = useCatalogStore();

export async function fetchCategoriesAndBrands() {
  catalogStore.fetchCategories();
  catalogStore.fetchBrands();
}

export async function fetchItems(categoryId: number, brandsId: number) {
  await catalogStore.fetchItems(categoryId, brandsId);
}
