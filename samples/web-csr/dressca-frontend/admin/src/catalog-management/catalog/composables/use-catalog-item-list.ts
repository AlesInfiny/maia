import { computed, onMounted, ref, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import { useCommonErrorOutcome } from '@/system-common/error-handler/common-error-outcome'
import type { GetCatalogItemResponse } from '@/system-common/generated/api-client'
import {
  fetchCatalogItemOptions,
  fetchItems,
  type CatalogItemOptions,
} from '../services/catalog-service'

export type CatalogItemListStatus = 'loading' | 'ready' | 'failed'

export interface CatalogItemSummary {
  id: string
  name: string
  description: string
  price: number
  productCode: string
  categoryName: string
  brandName: string
  assetCodes: string[] | undefined
  isDeleted: boolean
}

export interface CatalogItemList {
  status: ComputedRef<CatalogItemListStatus>
  /** 読み込みが完了するまでは空です。 */
  items: ComputedRef<CatalogItemSummary[]>
}

/**
 * API のレスポンスを一覧に表示するアイテムの内容に変換します。
 * @param item カタログアイテムのレスポンス。
 * @param options カテゴリとブランドの選択肢。
 * @returns 一覧に表示するアイテムの内容。
 */
function toSummary(item: GetCatalogItemResponse, options: CatalogItemOptions): CatalogItemSummary {
  return {
    id: item.id,
    name: item.name,
    description: item.description,
    price: item.price,
    productCode: item.productCode,
    categoryName:
      options.categories.find((category) => category.id === item.catalogCategoryId)?.name ?? '',
    brandName: options.brands.find((brand) => brand.id === item.catalogBrandId)?.name ?? '',
    assetCodes: item.assetCodes,
    isDeleted: item.isDeleted,
  }
}

/**
 * カタログアイテムの一覧を表示するユースケースを提供します。
 * コンポーネントのマウント後に読み込みます。
 * @returns カタログアイテムの一覧のユースケース。
 */
export function useCatalogItemList(): CatalogItemList {
  const handleCommonError = useCommonErrorOutcome()

  const status = ref<CatalogItemListStatus>('loading')
  const items = shallowRef<CatalogItemSummary[]>([])

  /**
   * アイテム、カテゴリ、ブランドを読み込みます。
   */
  async function load() {
    try {
      const pagedList = await fetchItems()
      const options = await fetchCatalogItemOptions()
      items.value = (pagedList.items ?? []).map((item) => toSummary(item, options))
      status.value = 'ready'
    } catch (error) {
      if ((await handleCommonError(error)).kind === 'failed') {
        status.value = 'failed'
      }
    }
  }

  // ライフサイクルフックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)

  return {
    status: computed(() => status.value),
    items: computed(() => items.value),
  }
}
