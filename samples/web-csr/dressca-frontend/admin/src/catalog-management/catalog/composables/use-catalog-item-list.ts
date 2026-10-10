import { computed, onMounted, ref, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import { useUnexpectedErrorOutcome } from '@/system-common/error-handler/unexpected-error-outcome'
import type { GetCatalogItemResponse } from '@/system-common/generated/api-client'
import { fetchItems } from '../services/catalog-service'
import { fetchCatalogItemOptions, type CatalogItemOptions } from './use-catalog-item-form'

/**
 * カタログアイテムの一覧の読み込みの状態です。
 */
export type CatalogItemListStatus = 'loading' | 'ready' | 'failed'

/**
 * 一覧に表示するカタログアイテムの内容です。
 * カテゴリとブランドは名前で表します。
 */
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

/**
 * カタログアイテムの一覧のユースケースです。
 */
export interface CatalogItemList {
  /** 一覧の読み込みの状態です。 */
  status: ComputedRef<CatalogItemListStatus>
  /** 一覧に表示するアイテムです。読み込みが完了するまでは空です。 */
  items: ComputedRef<CatalogItemSummary[]>
}

/**
 * API のレスポンスを一覧に表示するアイテムの内容に変換します。
 * 該当するカテゴリやブランドがない場合、名前は空文字にします。
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
 * 画面を開くたびに最新の一覧を表示するため、コンポーネントのマウント後に読み込みます。
 * @returns カタログアイテムの一覧のユースケース。
 */
export function useCatalogItemList(): CatalogItemList {
  const handleUnexpectedError = useUnexpectedErrorOutcome()

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
      if ((await handleUnexpectedError(error)).kind === 'failed') {
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
