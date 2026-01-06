import { defineStore } from 'pinia'
import type {
  CatalogCategoryResponse,
  CatalogBrandResponse,
  PagedListOfCatalogItemResponse,
} from '@/generated/api-client'
import { catalogCategoriesApi, catalogBrandsApi, catalogItemsApi } from '@/api-client'

/**
 * カタログ情報（カテゴリ・ブランド・アイテム）を管理するストアです。
 */
export const useCatalogStore = defineStore('catalog', {
  state: () => ({
    categories: [] as CatalogCategoryResponse[],
    brands: [] as CatalogBrandResponse[],
    catalogItemPage: {} as PagedListOfCatalogItemResponse,
  }),
  actions: {
    /**
     * カテゴリ一覧を取得します。
     */
    async fetchCategories() {
      const response = await catalogCategoriesApi().getCatalogCategories()
      this.categories = response.data
      this.categories.unshift({ id: 0, name: 'すべて' })
    },
    /**
     * ブランド一覧を取得します。
     */
    async fetchBrands() {
      const response = await catalogBrandsApi().getCatalogBrands()
      this.brands = response.data
      this.brands.unshift({ id: 0, name: 'すべて' })
    },
    /**
     * カタログアイテム一覧を取得します。
     * @param categoryId カテゴリID 。
     * @param brandId ブランドID 。
     * @param page ページ番号（任意）。
     */
    async fetchItems(categoryId: number, brandId: number, page?: number) {
      const response = await catalogItemsApi().getByQuery(
        brandId === 0 ? undefined : brandId,
        categoryId === 0 ? undefined : categoryId,
        page,
        undefined,
      )
      this.catalogItemPage = response.data
    },
  },
  getters: {
    /**
     * カテゴリ一覧を取得します。
     * @param state 状態。
     * @returns カテゴリ一覧。
     */
    getCategories(state) {
      return state.categories
    },
    /**
     * ブランド一覧を取得します。
     * @param state 状態。
     * @returns ブランド一覧。
     */
    getBrands(state) {
      return state.brands
    },
    /**
     * カタログアイテムの一覧を取得します。
     * @param state 状態。
     * @returns カタログアイテム一覧。
     */
    getItems(state) {
      return state.catalogItemPage.items
    },
    /**
     * ブランドID からブランド名を検索する関数を取得します。
     * Pinia で引数つきの getter を実装する場合は関数を経由してください。
     * @see  {@link https://pinia.vuejs.org/core-concepts/getters.html#Passing-arguments-to-getters}
     * @param state 状態。
     * @returns ブランド名。存在しない ID を指定した場合は undefined 。
     * @example
     * const catalogStore = useCatalogStore()
     * const { getBrandName } = storeToRefs(catalogStore)
     * const brandName = getBrandName(item.catalogBrandId)
     */
    getBrandName: (state) => {
      return (id: number) => state.brands.find((brand) => brand.id === id)?.name
    },
  },
})
