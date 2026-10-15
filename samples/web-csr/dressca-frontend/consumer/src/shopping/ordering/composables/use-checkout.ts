import { computed, onMounted, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import {
  useCommonErrorOutcome,
  type ApiProblem,
  type CommonErrorOutcome,
} from '@/system-common/error-handler/common-error-outcome'
import type { BasketAccount, BasketLine } from '../../basket/models/basket-line'
import { useBasketContents } from '../../basket/composables/use-basket-contents'
import { postOrder } from '../services/ordering-service'
import { deliveryAddress, type Address } from '../models/address'

/**
 * `empty` は、買い物かごが空で注文できないことを表します。
 * `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。
 */
export type CheckoutStatus =
  | { kind: 'loading' }
  | { kind: 'ready' }
  | { kind: 'empty' }
  | { kind: 'failed'; problem?: ApiProblem }

export type PlaceOrderOutcome = { kind: 'ordered'; orderId: string } | CommonErrorOutcome

export interface Checkout {
  status: ComputedRef<CheckoutStatus>
  lines: ComputedRef<BasketLine[]>
  /** 読み込みが完了するまでは undefined です。 */
  account: ComputedRef<BasketAccount | undefined>
  hasUnavailableItems: ComputedRef<boolean>
  address: ComputedRef<Address>
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
  const { lines, account, hasUnavailableItems, refresh } = useBasketContents()
  const address = computed(() => deliveryAddress)

  /**
   * 注文する買い物かごを読み込みます。
   */
  async function load() {
    try {
      await refresh()
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
    account,
    hasUnavailableItems,
    address,
    placeOrder,
  }
}
