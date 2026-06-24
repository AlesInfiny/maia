import { defineStore } from 'pinia'
import type {
  GetDisplayCategoryResponse,
  GetDisplayBrandResponse,
  PagedListOfGetDisplayItemResponse,
} from '@/generated/api-client'
import { displayCategoriesApi, displayBrandsApi, displayItemsApi } from '@/api-client'

/**
 * 掲載情報（掲載カテゴリ・掲載ブランド・掲載品）を管理するストアです。
 */
export const useDisplayStore = defineStore('display', {
  state: (): {
    categories: GetDisplayCategoryResponse[]
    brands: GetDisplayBrandResponse[]
    displayItemPage: PagedListOfGetDisplayItemResponse
  } => ({
    categories: [],
    brands: [],
    displayItemPage: {
      page: 0,
      totalPages: 0,
      pageSize: 0,
      totalCount: 0,
      hasPrevious: false,
      hasNext: false,
      items: [],
    },
  }),
  actions: {
    /**
     * 掲載カテゴリ一覧を取得します。
     */
    async fetchCategories() {
      const response = await displayCategoriesApi().getDisplayCategories()
      this.categories = response.data
      this.categories.unshift({ id: 0, name: 'すべて' })
    },
    /**
     * 掲載ブランド一覧を取得します。
     */
    async fetchBrands() {
      const response = await displayBrandsApi().getDisplayBrands()
      this.brands = response.data
      this.brands.unshift({ id: 0, name: 'すべて' })
    },
    /**
     * 掲載品一覧を取得します。
     * @param categoryId 掲載カテゴリID 。
     * @param brandId 掲載ブランドID 。
     * @param page ページ番号（任意）。
     */
    async fetchItems(categoryId: number, brandId: number, page?: number) {
      const response = await displayItemsApi().getByQuery(
        brandId === 0 ? undefined : brandId,
        categoryId === 0 ? undefined : categoryId,
        page,
        undefined,
      )
      this.displayItemPage = response.data
    },
  },
  getters: {
    /**
     * 掲載カテゴリ一覧を取得します。
     * @param state 状態。
     * @returns 掲載カテゴリ一覧。
     */
    getCategories(state) {
      return state.categories
    },
    /**
     * 掲載ブランド一覧を取得します。
     * @param state 状態。
     * @returns 掲載ブランド一覧。
     */
    getBrands(state) {
      return state.brands
    },
    /**
     * 掲載品の一覧を取得します。
     * @param state 状態。
     * @returns 掲載品一覧。
     */
    getItems(state) {
      return state.displayItemPage.items
    },
    /**
     * 掲載ブランドID から掲載ブランド名を検索する関数を取得します。
     * Pinia で引数つきの getter を実装する場合は関数を経由してください。
     * @see  {@link https://pinia.vuejs.org/core-concepts/getters.html#Passing-arguments-to-getters}
     * @param state 状態。
     * @returns 掲載ブランド名。存在しない ID を指定した場合は undefined 。
     * @example
     * const displayStore = useDisplayStore()
     * const { getBrandName } = storeToRefs(displayStore)
     * const brandName = getBrandName(item.displayBrandId)
     */
    getBrandName: (state) => {
      return (id: number) => state.brands.find((brand) => brand.id === id)?.name
    },
  },
})
