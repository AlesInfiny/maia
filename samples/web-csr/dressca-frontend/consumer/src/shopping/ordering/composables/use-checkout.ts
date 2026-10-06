import { computed, onMounted, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import { useBasketStore } from '@/business-common/stores/basket'
import {
  useUnexpectedErrorOutcome,
  type ApiProblem,
  type UnexpectedErrorOutcome,
} from '@/system-common/error-handler/unexpected-error-outcome'
import { fetchBasket } from '@/shopping/basket/services/basket-service'
import {
  toBasketAccount,
  toBasketLines,
  type BasketAccount,
  type BasketLine,
} from '@/shopping/basket/models/basket-line'
import { postOrder } from '../services/ordering-service'
import { useUserStore } from '../stores/user'
import type { Address } from '../stores/user.model'

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
export type PlaceOrderOutcome = { kind: 'ordered'; orderId: string } | UnexpectedErrorOutcome

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
  const handleUnexpectedError = useUnexpectedErrorOutcome()
  const basketStore = useBasketStore()
  const userStore = useUserStore()

  const status = shallowRef<CheckoutStatus>({ kind: 'loading' })
  const lines = computed(() => toBasketLines(basketStore.getBasket, basketStore.getDeletedItemIds))
  const address = computed(() => userStore.getAddress)

  /**
   * 注文する買い物かごを読み込みます。
   */
  async function load() {
    try {
      await fetchBasket()
      status.value = lines.value.length === 0 ? { kind: 'empty' } : { kind: 'ready' }
    } catch (error) {
      const outcome = await handleUnexpectedError(error)
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
      return handleUnexpectedError(error)
    }
  }

  // ライフサイクルフックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)

  return {
    status: computed(() => status.value),
    lines,
    account: computed(() => toBasketAccount(basketStore.getBasket)),
    hasUnavailableItems: computed(() => basketStore.getDeletedItemIds.length > 0),
    address,
    placeOrder,
  }
}
