import { useCatalogStore } from '@/stores/catalog/catalog'

/**
 * カタログストアからカテゴリとブランド情報を取得します。
 * ストアの状態が最新のカテゴリ・ブランド一覧に更新されます。
 * @returns Promise<void>
 * @example
 * await fetchCategoriesAndBrands()
 */
export async function fetchCategoriesAndBrands() {
  const catalogStore = useCatalogStore()
  await catalogStore.fetchCategories()
  await catalogStore.fetchBrands()
}

/**
 * 指定したカテゴリとブランドに基づいてカタログアイテム一覧を取得します。
 * ストアの状態が該当するカタログアイテム一覧に更新されます。
 * @param categoryId - カタログアイテムを取得するカテゴリ ID
 * @param brandsId - 絞り込み対象のブランド ID
 * @returns Promise<void>
 * @example
 * await fetchItems(1, 10)
 */
export async function fetchItems(categoryId: number, brandsId: number) {
  const catalogStore = useCatalogStore()
  await catalogStore.fetchItems(categoryId, brandsId)
}
