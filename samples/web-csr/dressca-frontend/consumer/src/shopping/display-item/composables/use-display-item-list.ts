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

/**
 * 陳列品の一覧の読み込みの状態です。
 * `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。
 */
export type DisplayItemListStatus =
  | { kind: 'loading' }
  | { kind: 'ready' }
  | { kind: 'failed'; problem?: ApiProblem }

/**
 * カテゴリやブランドの絞り込みの選択肢です。
 * ID が空文字の選択肢は「すべて」を表します。
 */
export interface DisplayItemFilterOption {
  id: string
  name: string
}

/**
 * 陳列品の絞り込みの条件です。
 * 空文字は絞り込まないことを表します。
 */
export interface DisplayItemFilter {
  categoryId: string
  brandId: string
}

/**
 * 一覧に表示する陳列品の内容です。
 * ブランドは名前で表します。
 */
export interface DisplayItemSummary {
  id: string
  name: string
  price: number
  brandName: string
  assetCodes: string[] | undefined
}

/**
 * 絞り込みの選択肢の先頭に置く「すべて」です。
 */
const allOption: DisplayItemFilterOption = { id: '', name: 'すべて' }

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

/**
 * 陳列品を買い物かごに入れた結果です。
 */
export type AddToBasketOutcome = { kind: 'added' } | CommonErrorOutcome

/**
 * 陳列品の一覧のユースケースです。
 */
export interface DisplayItemList {
  /** 一覧の読み込みの状態です。 */
  status: ComputedRef<DisplayItemListStatus>
  /** 特集コンテンツです。 */
  specialContents: ComputedRef<SpecialContent[]>
  /** カテゴリの絞り込みの選択肢です。先頭は「すべて」です。 */
  categories: ComputedRef<DisplayItemFilterOption[]>
  /** ブランドの絞り込みの選択肢です。先頭は「すべて」です。 */
  brands: ComputedRef<DisplayItemFilterOption[]>
  /** 絞り込みの条件です。変更すると一覧を読み込み直します。 */
  filter: DisplayItemFilter
  /** 一覧に表示する陳列品です。 */
  items: ComputedRef<DisplayItemSummary[]>
  /** 陳列品を買い物かごに入れます。 */
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
  const filter: DisplayItemFilter = reactive({ categoryId: '', brandId: '' })

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
   * 絞り込みの選択肢と、絞り込みの条件に合う陳列品を読み込みます。
   */
  async function load() {
    await runLoading(async () => {
      const options = await fetchCategoriesAndBrands()
      categories.value = options.categories
      brands.value = options.brands
      items.value = (await fetchItems(filter.categoryId, filter.brandId)).items ?? []
    })
  }

  /**
   * 絞り込みの条件に合う陳列品を読み込み直します。
   */
  async function reloadItems() {
    await runLoading(async () => {
      items.value = (await fetchItems(filter.categoryId, filter.brandId)).items ?? []
    })
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
