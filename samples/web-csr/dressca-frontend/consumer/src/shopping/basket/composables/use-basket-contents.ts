import { computed, shallowRef } from 'vue'
import type { ComputedRef, ShallowRef } from 'vue'
import type { GetBasketItemsResponse } from '@/system-common/generated/api-client'
import { fetchBasket } from '../services/basket-service'
import {
  containsUnavailableItems,
  toBasketAccount,
  toBasketLines,
  type BasketAccount,
  type BasketLine,
} from '../models/basket-line'

/**
 * 取得した買い物かごと、そこから導いた内容です。
 */
export interface BasketContents {
  /** 取得した買い物かごです。取得するまでは undefined です。 */
  basket: Readonly<ShallowRef<GetBasketItemsResponse | undefined>>
  /** 買い物かごに入っている陳列品です。 */
  lines: ComputedRef<BasketLine[]>
  /** 会計です。取得するまでは undefined です。 */
  account: ComputedRef<BasketAccount | undefined>
  /** 購入できない陳列品が入っているかどうかです。 */
  hasUnavailableItems: ComputedRef<boolean>
  /** 最新の買い物かごを取得します。 */
  refresh: () => Promise<void>
}

/**
 * 買い物かごを取得し、陳列品の行、会計、購入できない陳列品の有無を導きます。
 * 買い物かごと注文の確認のユースケースで共有します。
 * @returns 取得した買い物かごと、そこから導いた内容。
 */
export function useBasketContents(): BasketContents {
  const basket = shallowRef<GetBasketItemsResponse>()

  /**
   * 最新の買い物かごを取得します。
   */
  async function refresh() {
    basket.value = await fetchBasket()
  }

  return {
    basket,
    lines: computed(() => toBasketLines(basket.value)),
    account: computed(() => toBasketAccount(basket.value)),
    hasUnavailableItems: computed(() => containsUnavailableItems(basket.value)),
    refresh,
  }
}
