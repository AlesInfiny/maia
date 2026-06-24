import { useDisplayStore } from '@/stores/display/display'

/**
 * 掲載ストアから掲載カテゴリと掲載ブランド情報を取得します。
 * ストアの状態が最新の掲載カテゴリ・掲載ブランド一覧に更新されます。
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
 * 指定した掲載カテゴリと掲載ブランドに基づいて掲載品一覧を取得します。
 * ストアの状態が該当する掲載品一覧に更新されます。
 * @param categoryId - 掲載品を取得する掲載カテゴリ ID
 * @param brandsId - 絞り込み対象の掲載ブランド ID
 * @returns Promise<void>
 * @example
 * await fetchItems(1, 10)
 */
export async function fetchItems(categoryId: number, brandsId: number) {
  const displayStore = useDisplayStore()
  await displayStore.fetchItems(categoryId, brandsId)
}
