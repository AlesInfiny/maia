import { computed, onMounted, reactive, shallowRef, watch } from 'vue'
import type { ComputedRef } from 'vue'
import { useDisplayItemStore } from '@/business-common/stores/display-item'
import {
  useUnexpectedErrorOutcome,
  type ApiProblem,
  type UnexpectedErrorOutcome,
} from '@/system-common/error-handler/unexpected-error-outcome'
import { addItemToBasket } from '@/shopping/basket/services/basket-service'
import { fetchCategoriesAndBrands, fetchItems } from '../services/display-item-service'
import { useSpecialContentStore } from '../stores/special-content'
import type { SpecialContent } from '../stores/special-content.model'

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
 * 陳列品を買い物かごに入れた結果です。
 */
export type AddToBasketOutcome = { kind: 'added' } | UnexpectedErrorOutcome

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
  const handleUnexpectedError = useUnexpectedErrorOutcome()
  const displayItemStore = useDisplayItemStore()
  const specialContentStore = useSpecialContentStore()

  const status = shallowRef<DisplayItemListStatus>({ kind: 'loading' })
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
      const outcome = await handleUnexpectedError(error)
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
      await fetchCategoriesAndBrands()
      await fetchItems(filter.categoryId, filter.brandId)
    })
  }

  /**
   * 絞り込みの条件に合う陳列品を読み込み直します。
   */
  async function reloadItems() {
    await runLoading(() => fetchItems(filter.categoryId, filter.brandId))
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
      return handleUnexpectedError(error)
    }
  }

  // ライフサイクルフックとウォッチャーが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)
  watch(() => [filter.categoryId, filter.brandId], reloadItems)

  return {
    status: computed(() => status.value),
    specialContents: computed(() => specialContentStore.getSpecialContents),
    categories: computed(() =>
      displayItemStore.getCategories.map(({ id, name }) => ({ id, name })),
    ),
    brands: computed(() => displayItemStore.getBrands.map(({ id, name }) => ({ id, name }))),
    filter,
    items: computed(() =>
      (displayItemStore.getItems ?? []).map((item) => ({
        id: item.id,
        name: item.name,
        price: item.price,
        brandName: displayItemStore.getBrandName(item.displayItemBrandId) ?? '',
        assetCodes: item.assetCodes,
      })),
    ),
    addToBasket,
  }
}
