import { computed, onMounted, reactive, shallowRef, watch } from 'vue'
import type { ComputedRef } from 'vue'
import type {
  GetDisplayItemBrandsResponse,
  GetDisplayItemCategoriesResponse,
  GetDisplayItemResponse,
} from '@/system-common/generated/api-client'
import {
  useCommonErrorOutcome,
  type ApiProblem,
  type CommonErrorOutcome,
} from '@/system-common/error-handler/common-error-outcome'
import { addItemToBasket } from '../../basket/services/basket-service'
import { fetchCategoriesAndBrands, fetchItems } from '../services/display-item-service'
import { specialContents, type SpecialContent } from '../models/special-content'

/** `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。 */
export type DisplayItemListStatus =
  | { kind: 'loading' }
  | { kind: 'ready' }
  | { kind: 'failed'; problem?: ApiProblem }

export interface DisplayItemFilterOption {
  /** 「すべて」の選択肢は undefined です。 */
  id: string | undefined
  name: string
}

/** undefined は、その条件で絞り込まないことを表します。 */
export interface DisplayItemFilter {
  categoryId: string | undefined
  brandId: string | undefined
}

export interface DisplayItemSummary {
  id: string
  name: string
  price: number
  brandName: string
  assetCodes: string[] | undefined
}

const allOption: DisplayItemFilterOption = { id: undefined, name: 'すべて' }

/**
 * カテゴリやブランドのレスポンスを、絞り込みの選択肢に変換します。
 * @param responses カテゴリやブランドのレスポンス。
 * @returns 先頭に「すべて」を置いた絞り込みの選択肢。
 */
function toFilterOptions(
  responses: (GetDisplayItemCategoriesResponse | GetDisplayItemBrandsResponse)[],
): DisplayItemFilterOption[] {
  return [allOption, ...responses.map(({ id, name }) => ({ id, name }))]
}

export type AddToBasketOutcome = { kind: 'added' } | CommonErrorOutcome

export interface DisplayItemList {
  status: ComputedRef<DisplayItemListStatus>
  specialContents: ComputedRef<SpecialContent[]>
  /** 先頭は「すべて」です。 */
  categories: ComputedRef<DisplayItemFilterOption[]>
  /** 先頭は「すべて」です。 */
  brands: ComputedRef<DisplayItemFilterOption[]>
  /** 変更すると一覧を読み込み直します。 */
  filter: DisplayItemFilter
  items: ComputedRef<DisplayItemSummary[]>
  addToBasket: (displayItemId: string) => Promise<AddToBasketOutcome>
}

/**
 * 陳列品の一覧を表示し、買い物かごに入れるユースケースを提供します。
 * コンポーネントのマウント後に、絞り込みの選択肢と陳列品を読み込みます。
 * @returns 陳列品の一覧のユースケース。
 */
export function useDisplayItemList(): DisplayItemList {
  const handleCommonError = useCommonErrorOutcome()

  const status = shallowRef<DisplayItemListStatus>({ kind: 'loading' })
  const categories = shallowRef<GetDisplayItemCategoriesResponse[]>([])
  const brands = shallowRef<GetDisplayItemBrandsResponse[]>([])
  const items = shallowRef<GetDisplayItemResponse[]>([])
  const filter: DisplayItemFilter = reactive({ categoryId: undefined, brandId: undefined })

  /**
   * 読み込みの処理を実行し、結果を読み込みの状態に反映します。
   * @param task 読み込みの処理。
   */
  async function runLoading(task: () => Promise<void>) {
    try {
      await task()
      status.value = { kind: 'ready' }
    } catch (error) {
      const outcome = await handleCommonError(error)
      if (outcome.kind === 'failed') {
        status.value = outcome
      }
    }
  }

  /**
   * 絞り込みの条件に合う陳列品を取得し、一覧に反映します。
   */
  async function fetchFilteredItems() {
    items.value = (await fetchItems(filter.categoryId, filter.brandId)).items ?? []
  }

  /**
   * 絞り込みの選択肢と、絞り込みの条件に合う陳列品を読み込みます。
   */
  async function load() {
    await runLoading(async () => {
      const options = await fetchCategoriesAndBrands()
      categories.value = options.categories
      brands.value = options.brands
      await fetchFilteredItems()
    })
  }

  /**
   * 絞り込みの条件に合う陳列品を読み込み直します。
   */
  async function reloadItems() {
    await runLoading(fetchFilteredItems)
  }

  /**
   * 陳列品を買い物かごに入れます。
   * @param displayItemId 陳列品の ID 。
   * @returns 買い物かごに入れた結果。
   */
  async function addToBasket(displayItemId: string): Promise<AddToBasketOutcome> {
    try {
      await addItemToBasket(displayItemId)
      return { kind: 'added' }
    } catch (error) {
      return handleCommonError(error)
    }
  }

  // ライフサイクルフックとウォッチャーが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)
  watch(() => [filter.categoryId, filter.brandId], reloadItems)

  return {
    status: computed(() => status.value),
    specialContents: computed(() => specialContents),
    categories: computed(() => toFilterOptions(categories.value)),
    brands: computed(() => toFilterOptions(brands.value)),
    filter,
    items: computed(() =>
      items.value.map((item) => ({
        id: item.id,
        name: item.name,
        price: item.price,
        brandName: brands.value.find((brand) => brand.id === item.displayItemBrandId)?.name ?? '',
        assetCodes: item.assetCodes,
      })),
    ),
    addToBasket,
  }
}
