import { useCatalogStore } from '@/stores/catalog/catalog'

export async function fetchCategoriesAndBrands() {
  const catalogStore = useCatalogStore()
  catalogStore.fetchCategories()
  catalogStore.fetchBrands()
}

export async function fetchItems(categoryId: number, brandsId: number) {
  const catalogStore = useCatalogStore()
  await catalogStore.fetchItems(categoryId, brandsId)
}
