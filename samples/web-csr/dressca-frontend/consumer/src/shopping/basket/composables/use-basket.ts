import { computed, onMounted, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import {
  useCommonErrorOutcome,
  type ApiProblem,
  type CommonErrorOutcome,
} from '@/system-common/error-handler/common-error-outcome'
import { removeItemFromBasket, updateItemInBasket } from '../services/basket-service'
import type { BasketAccount, BasketLine } from '../models/basket-line'
import { useBasketContents } from './use-basket-contents'

/** `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。 */
export type BasketStatus =
  | { kind: 'loading' }
  | { kind: 'ready' }
  | { kind: 'failed'; problem?: ApiProblem }

export interface AddedBasketItem {
  name: string
  assetCodes: string[] | undefined
  unitPrice: number
}

export interface BasketOptions {
  addedItemId?: string
}

export type ChangeQuantityOutcome = { kind: 'changed' } | CommonErrorOutcome

export type RemoveFromBasketOutcome = { kind: 'removed' } | CommonErrorOutcome

export type ProceedToCheckoutOutcome =
  | { kind: 'ready' }
  | { kind: 'containsUnavailableItems' }
  | CommonErrorOutcome

export interface Basket {
  status: ComputedRef<BasketStatus>
  lines: ComputedRef<BasketLine[]>
  /** 読み込みが完了するまでは undefined です。 */
  account: ComputedRef<BasketAccount | undefined>
  isEmpty: ComputedRef<boolean>
  hasUnavailableItems: ComputedRef<boolean>
  addedItem: ComputedRef<AddedBasketItem | undefined>
  changeQuantity: (displayItemId: string, quantity: number) => Promise<ChangeQuantityOutcome>
  remove: (displayItemId: string) => Promise<RemoveFromBasketOutcome>
  /** 最新の買い物かごを取得し、注文に進めるかどうかを確認します。 */
  proceedToCheckout: () => Promise<ProceedToCheckoutOutcome>
}

/**
 * 買い物かごの内容を確認し、数量の変更や削除を行うユースケースを提供します。
 * コンポーネントのマウント後に買い物かごを読み込みます。
 * 数量の変更や削除をすると、直前に買い物かごに入れた陳列品の表示を消します。
 * @param options 買い物かごのユースケースの引数。
 * @returns 買い物かごのユースケース。
 */
export function useBasket(options: BasketOptions = {}): Basket {
  const handleCommonError = useCommonErrorOutcome()

  const status = shallowRef<BasketStatus>({ kind: 'loading' })
  const { basket, lines, account, hasUnavailableItems, refresh } = useBasketContents()
  const addedItemId = shallowRef(options.addedItemId)

  /**
   * 買い物かごを変更し、成功しても失敗しても最新の買い物かごを取得します。
   * @param change 買い物かごを変更する処理。
   */
  async function changeAndRefresh(change: () => Promise<void>) {
    try {
      await change()
    } finally {
      await refresh()
    }
  }

  /**
   * 買い物かごを読み込みます。
   */
  async function load() {
    try {
      await refresh()
      status.value = { kind: 'ready' }
    } catch (error) {
      const outcome = await handleCommonError(error)
      if (outcome.kind === 'failed') {
        status.value = outcome
      }
    }
  }

  /**
   * 陳列品の数量を変更します。
   * @param displayItemId 陳列品の ID 。
   * @param quantity 変更後の数量。
   * @returns 数量を変更した結果。
   */
  async function changeQuantity(
    displayItemId: string,
    quantity: number,
  ): Promise<ChangeQuantityOutcome> {
    addedItemId.value = undefined
    try {
      await changeAndRefresh(() => updateItemInBasket(displayItemId, quantity))
      return { kind: 'changed' }
    } catch (error) {
      return handleCommonError(error)
    }
  }

  /**
   * 陳列品を買い物かごから削除します。
   * @param displayItemId 陳列品の ID 。
   * @returns 削除した結果。
   */
  async function remove(displayItemId: string): Promise<RemoveFromBasketOutcome> {
    addedItemId.value = undefined
    try {
      await changeAndRefresh(() => removeItemFromBasket(displayItemId))
      return { kind: 'removed' }
    } catch (error) {
      return handleCommonError(error)
    }
  }

  /**
   * 最新の買い物かごを取得し、注文に進めるかどうかを確認します。
   * @returns 注文に進めるかどうかを確認した結果。
   */
  async function proceedToCheckout(): Promise<ProceedToCheckoutOutcome> {
    try {
      await refresh()
    } catch (error) {
      return handleCommonError(error)
    }
    return hasUnavailableItems.value ? { kind: 'containsUnavailableItems' } : { kind: 'ready' }
  }

  // ライフサイクルフックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)

  return {
    status: computed(() => status.value),
    lines,
    account,
    isEmpty: computed(() => lines.value.length === 0),
    hasUnavailableItems,
    addedItem: computed(() => {
      const added = basket.value?.basketItems?.find(
        (item) => item.displayItemId === addedItemId.value,
      )
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
