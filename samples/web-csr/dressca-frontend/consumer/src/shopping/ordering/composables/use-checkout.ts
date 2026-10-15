import { computed, onMounted, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import type { GetBasketItemsResponse } from '@/system-common/generated/api-client'
import {
  useCommonErrorOutcome,
  type ApiProblem,
  type CommonErrorOutcome,
} from '@/system-common/error-handler/common-error-outcome'
import { fetchBasket } from '../../basket/services/basket-service'
import {
  containsUnavailableItems,
  toBasketAccount,
  toBasketLines,
  type BasketAccount,
  type BasketLine,
} from '../../basket/models/basket-line'
import { postOrder } from '../services/ordering-service'
import { deliveryAddress, type Address } from '../models/address'

/**
 * 注文内容の読み込みの状態です。
 * `empty` は、買い物かごが空で注文できないことを表します。
 * `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。
 */
export type CheckoutStatus =
  | { kind: 'loading' }
  | { kind: 'ready' }
  | { kind: 'empty' }
  | { kind: 'failed'; problem?: ApiProblem }

/**
 * 注文を確定した結果です。
 */
export type PlaceOrderOutcome = { kind: 'ordered'; orderId: string } | CommonErrorOutcome

/**
 * 注文の確認と確定のユースケースです。
 */
export interface Checkout {
  /** 注文内容の読み込みの状態です。 */
  status: ComputedRef<CheckoutStatus>
  /** 注文する陳列品です。 */
  lines: ComputedRef<BasketLine[]>
  /** 会計です。読み込みが完了するまでは undefined です。 */
  account: ComputedRef<BasketAccount | undefined>
  /** 購入できない陳列品が入っているかどうかです。 */
  hasUnavailableItems: ComputedRef<boolean>
  /** お届け先です。 */
  address: ComputedRef<Address>
  /** 買い物かごの内容とお届け先で注文を確定します。 */
  placeOrder: () => Promise<PlaceOrderOutcome>
}

/**
 * 注文内容を確認し、注文を確定するユースケースを提供します。
 * コンポーネントのマウント後に、注文する買い物かごを読み込みます。
 * @returns 注文の確認と確定のユースケース。
 */
export function useCheckout(): Checkout {
  const handleCommonError = useCommonErrorOutcome()

  const status = shallowRef<CheckoutStatus>({ kind: 'loading' })
  const basket = shallowRef<GetBasketItemsResponse>()
  const lines = computed(() => toBasketLines(basket.value))
  const address = computed(() => deliveryAddress)

  /**
   * 注文する買い物かごを読み込みます。
   */
  async function load() {
    try {
      basket.value = await fetchBasket()
      status.value = lines.value.length === 0 ? { kind: 'empty' } : { kind: 'ready' }
    } catch (error) {
      const outcome = await handleCommonError(error)
      if (outcome.kind === 'failed') {
        status.value = outcome
      }
    }
  }

  /**
   * 買い物かごの内容とお届け先で注文を確定します。
   * @returns 注文を確定した結果。
   */
  async function placeOrder(): Promise<PlaceOrderOutcome> {
    const { fullName, postalCode, todofuken, shikuchoson, azanaAndOthers } = address.value
    try {
      const orderId = await postOrder(fullName, postalCode, todofuken, shikuchoson, azanaAndOthers)
      return { kind: 'ordered', orderId }
    } catch (error) {
      return handleCommonError(error)
    }
  }

  // ライフサイクルフックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)

  return {
    status: computed(() => status.value),
    lines,
    account: computed(() => toBasketAccount(basket.value)),
    hasUnavailableItems: computed(() => containsUnavailableItems(basket.value)),
    address,
    placeOrder,
  }
}
