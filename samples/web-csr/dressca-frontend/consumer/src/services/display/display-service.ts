import { useDisplayStore } from '@/stores/displayitem/display'

/**
 * 陳列ストアからカテゴリとブランド情報を取得します。
 * ストアの状態が最新のカテゴリ・ブランド一覧に更新されます。
 * @returns Promise<void>
 * @example
 * await fetchCategoriesAndBrands()
 */
export async function fetchCategoriesAndBrands() {
  const displayStore = useDisplayStore()
  await displayStore.fetchCategories()
  await displayStore.fetchBrands()
}

/**
 * 指定したカテゴリとブランドに基づいて陳列品一覧を取得します。
 * ストアの状態が該当する陳列品一覧に更新されます。
 * @param categoryId - 陳列品を取得するカテゴリ ID
 * @param brandId - 絞り込み対象のブランド ID
 * @returns Promise<void>
 * @example
 * await fetchItems('category-id', 'brand-id')
 */
export async function fetchItems(categoryId?: string, brandId?: string) {
  const displayStore = useDisplayStore()
  await displayStore.fetchItems(categoryId, brandId)
}
