import type {
  GetDisplayItemBrandsResponse,
  GetDisplayItemCategoriesResponse,
  PagedListOfGetDisplayItemResponse,
} from '@/system-common/generated/api-client'
import {
  displayItemBrandsApi,
  displayItemCategoriesApi,
  displayItemsApi,
} from '@/system-common/api-client'

/**
 * カテゴリとブランドの一覧を取得します。
 * @returns カテゴリの一覧とブランドの一覧。
 * @example
 * const { categories, brands } = await fetchCategoriesAndBrands()
 */
export async function fetchCategoriesAndBrands(): Promise<{
  categories: GetDisplayItemCategoriesResponse[]
  brands: GetDisplayItemBrandsResponse[]
}> {
  const categories = await displayItemCategoriesApi().getDisplayItemCategories()
  const brands = await displayItemBrandsApi().getDisplayItemBrands()
  return { categories: categories.data, brands: brands.data }
}

/**
 * 指定したカテゴリとブランドに基づいて陳列品の一覧を取得します。
 * @param categoryId 絞り込むカテゴリの ID 。
 * @param brandId 絞り込むブランドの ID 。
 * @returns 陳列品の一覧。
 * @example
 * const page = await fetchItems('category-id', 'brand-id')
 */
export async function fetchItems(
  categoryId?: string,
  brandId?: string,
): Promise<PagedListOfGetDisplayItemResponse> {
  const response = await displayItemsApi().getByQuery(
    brandId || undefined,
    categoryId || undefined,
    undefined,
    undefined,
  )
  return response.data
}
