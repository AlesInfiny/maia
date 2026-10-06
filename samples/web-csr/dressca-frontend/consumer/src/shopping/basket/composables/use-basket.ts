import { computed, onMounted, onUnmounted, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import { useBasketStore } from '@/business-common/stores/basket'
import {
  useUnexpectedErrorOutcome,
  type ApiProblem,
  type UnexpectedErrorOutcome,
} from '@/system-common/error-handler/unexpected-error-outcome'
import { fetchBasket, removeItemFromBasket, updateItemInBasket } from '../services/basket-service'
import {
  toBasketAccount,
  toBasketLines,
  type BasketAccount,
  type BasketLine,
} from '../models/basket-line'

/**
 * 買い物かごの読み込みの状態です。
 * `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。
 */
export type BasketStatus =
  | { kind: 'loading' }
  | { kind: 'ready' }
  | { kind: 'failed'; problem?: ApiProblem }

/**
 * 直前に買い物かごに入れた陳列品です。
 */
export interface AddedBasketItem {
  name: string
  assetCodes: string[] | undefined
  unitPrice: number
}

/**
 * 数量を変更した結果です。
 */
export type ChangeQuantityOutcome = { kind: 'changed' } | UnexpectedErrorOutcome

/**
 * 買い物かごから削除した結果です。
 */
export type RemoveFromBasketOutcome = { kind: 'removed' } | UnexpectedErrorOutcome

/**
 * 注文に進めるかどうかを確認した結果です。
 * `containsUnavailableItems` は、購入できない陳列品が買い物かごに入っていることを表します。
 */
export type ProceedToCheckoutOutcome =
  | { kind: 'ready' }
  | { kind: 'containsUnavailableItems' }
  | UnexpectedErrorOutcome

/**
 * 買い物かごのユースケースです。
 */
export interface Basket {
  /** 買い物かごの読み込みの状態です。 */
  status: ComputedRef<BasketStatus>
  /** 買い物かごに入っている陳列品です。 */
  lines: ComputedRef<BasketLine[]>
  /** 会計です。読み込みが完了するまでは undefined です。 */
  account: ComputedRef<BasketAccount | undefined>
  /** 買い物かごが空かどうかです。 */
  isEmpty: ComputedRef<boolean>
  /** 購入できない陳列品が入っているかどうかです。 */
  hasUnavailableItems: ComputedRef<boolean>
  /** 直前に買い物かごに入れた陳列品です。ない場合は undefined です。 */
  addedItem: ComputedRef<AddedBasketItem | undefined>
  /** 陳列品の数量を変更します。 */
  changeQuantity: (displayItemId: string, quantity: number) => Promise<ChangeQuantityOutcome>
  /** 陳列品を買い物かごから削除します。 */
  remove: (displayItemId: string) => Promise<RemoveFromBasketOutcome>
  /** 最新の買い物かごを取得し、注文に進めるかどうかを確認します。 */
  proceedToCheckout: () => Promise<ProceedToCheckoutOutcome>
}

/**
 * 買い物かごの内容を確認し、数量の変更や削除を行うユースケースを提供します。
 * コンポーネントのマウント後に買い物かごを読み込みます。
 * コンポーネントのアンマウント時に、直前に買い物かごに入れた陳列品の表示を消します。
 * @returns 買い物かごのユースケース。
 */
export function useBasket(): Basket {
  const handleUnexpectedError = useUnexpectedErrorOutcome()
  const basketStore = useBasketStore()

  const status = shallowRef<BasketStatus>({ kind: 'loading' })
  const lines = computed(() => toBasketLines(basketStore.getBasket, basketStore.getDeletedItemIds))
  const hasUnavailableItems = computed(() => basketStore.getDeletedItemIds.length > 0)

  /**
   * 買い物かごを読み込みます。
   */
  async function load() {
    try {
      await fetchBasket()
      status.value = { kind: 'ready' }
    } catch (error) {
      const outcome = await handleUnexpectedError(error)
      if (outcome.kind === 'failed') {
        status.value = outcome
      }
    }
  }

  /**
   * 陳列品の数量を変更します。
   * 失敗した場合も、最新の買い物かごを読み込み直します。
   * @param displayItemId 陳列品の ID 。
   * @param quantity 変更後の数量。
   * @returns 数量を変更した結果。
   */
  async function changeQuantity(
    displayItemId: string,
    quantity: number,
  ): Promise<ChangeQuantityOutcome> {
    try {
      await updateItemInBasket(displayItemId, quantity)
      return { kind: 'changed' }
    } catch (error) {
      return handleUnexpectedError(error)
    }
  }

  /**
   * 陳列品を買い物かごから削除します。
   * 失敗した場合も、最新の買い物かごを読み込み直します。
   * @param displayItemId 陳列品の ID 。
   * @returns 削除した結果。
   */
  async function remove(displayItemId: string): Promise<RemoveFromBasketOutcome> {
    try {
      await removeItemFromBasket(displayItemId)
      return { kind: 'removed' }
    } catch (error) {
      return handleUnexpectedError(error)
    }
  }

  /**
   * 最新の買い物かごを取得し、注文に進めるかどうかを確認します。
   * @returns 注文に進めるかどうかを確認した結果。
   */
  async function proceedToCheckout(): Promise<ProceedToCheckoutOutcome> {
    try {
      await fetchBasket()
    } catch (error) {
      return handleUnexpectedError(error)
    }
    return hasUnavailableItems.value ? { kind: 'containsUnavailableItems' } : { kind: 'ready' }
  }

  // ライフサイクルフックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)
  onUnmounted(() => basketStore.deleteAddedItemId())

  return {
    status: computed(() => status.value),
    lines,
    account: computed(() => toBasketAccount(basketStore.getBasket)),
    isEmpty: computed(() => lines.value.length === 0),
    hasUnavailableItems,
    addedItem: computed(() => {
      const added = basketStore.getAddedItem
      if (!added) {
        return undefined
      }
      return {
        name: added.displayItem?.name ?? '',
        assetCodes: added.displayItem?.assetCodes,
        unitPrice: added.unitPrice,
      }
    }),
    changeQuantity,
    remove,
    proceedToCheckout,
  }
}
