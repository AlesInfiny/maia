import { computed, onMounted, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import {
  useUnexpectedErrorOutcome,
  type ApiProblem,
} from '@/system-common/error-handler/unexpected-error-outcome'
import type { GetOrderByIdResponse } from '@/system-common/generated/api-client'
import type { BasketAccount } from '@/shopping/basket/models/basket-line'
import { getOrder } from '../services/ordering-service'
import type { Address } from '../stores/user.model'

/**
 * 注文結果の読み込みの状態です。
 * `failed` の `problem` は、 API が問題の詳細を返した場合にだけ設定されます。
 */
export type OrderResultStatus =
  | { kind: 'loading' }
  | { kind: 'ready' }
  | { kind: 'failed'; problem?: ApiProblem }

/**
 * 注文した陳列品です。
 */
export interface OrderedItem {
  id: string
  name: string
  assetCodes: string[] | undefined
  unitPrice: number
  quantity: number
  subTotal: number
}

/**
 * 確定した注文の内容です。
 */
export interface OrderResult {
  /** お届け先です。 */
  address: Address
  /** 会計です。 */
  account: BasketAccount | undefined
  /** 注文した陳列品です。 */
  items: OrderedItem[]
}

/**
 * 注文結果の確認のユースケースです。
 */
export interface OrderResultView {
  /** 注文結果の読み込みの状態です。 */
  status: ComputedRef<OrderResultStatus>
  /** 確定した注文の内容です。読み込みが完了するまでは undefined です。 */
  order: ComputedRef<OrderResult | undefined>
}

/**
 * API のレスポンスを確定した注文の内容に変換します。
 * @param response 注文のレスポンス。
 * @returns 確定した注文の内容。
 */
function toOrderResult(response: GetOrderByIdResponse): OrderResult {
  const { fullName, postalCode, todofuken, shikuchoson, azanaAndOthers, account } = response
  return {
    address: { fullName, postalCode, todofuken, shikuchoson, azanaAndOthers },
    account: account && {
      totalItemsPrice: account.totalItemsPrice,
      deliveryCharge: account.deliveryCharge,
      consumptionTax: account.consumptionTax,
      totalPrice: account.totalPrice,
    },
    items: (response.orderItems ?? []).map((item) => ({
      id: item.id,
      name: item.itemOrdered?.name ?? '',
      assetCodes: item.itemOrdered?.assetCodes,
      unitPrice: item.unitPrice,
      quantity: item.quantity,
      subTotal: item.subTotal,
    })),
  }
}

/**
 * 確定した注文の内容を確認するユースケースを提供します。
 * コンポーネントのマウント後に、注文の内容を読み込みます。
 * @param orderId 注文の ID 。
 * @returns 注文結果の確認のユースケース。
 */
export function useOrderResult(orderId: string): OrderResultView {
  const handleUnexpectedError = useUnexpectedErrorOutcome()

  const status = shallowRef<OrderResultStatus>({ kind: 'loading' })
  const order = shallowRef<OrderResult>()

  /**
   * 注文の内容を読み込みます。
   */
  async function load() {
    try {
      order.value = toOrderResult(await getOrder(orderId))
      status.value = { kind: 'ready' }
    } catch (error) {
      const outcome = await handleUnexpectedError(error)
      if (outcome.kind === 'failed') {
        status.value = outcome
      }
    }
  }

  // ライフサイクルフックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)

  return {
    status: computed(() => status.value),
    order: computed(() => order.value),
  }
}
