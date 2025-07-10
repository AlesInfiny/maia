import { useCatalogStore } from '@/stores/catalog/catalog'

export async function fetchCategoriesAndBrands() {
  const catalogStore = useCatalogStore()
  await catalogStore.fetchCategories()
  await catalogStore.fetchBrands()
}

export async function fetchItems(categoryId: number, brandsId: number) {
  const catalogStore = useCatalogStore()
  await catalogStore.fetchItems(categoryId, brandsId)
}
