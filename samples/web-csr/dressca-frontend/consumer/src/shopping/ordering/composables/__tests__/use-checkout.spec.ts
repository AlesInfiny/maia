import { describe, it, expect, beforeEach } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { createPinia, setActivePinia, type Pinia } from 'pinia'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import type { GetBasketItemsResponse, PostOrderRequest } from '@/system-common/generated/api-client'
import { server } from '@/../mock/node'
import { useUserStore } from '../../stores/user'
import { useCheckout, type Checkout } from '../use-checkout'

/**
 * 買い物かごのレスポンスを生成します。
 * @param itemCount 買い物かごに入っている陳列品の数。
 * @returns 買い物かごのレスポンス。
 */
function createBasketResponse(itemCount: number): GetBasketItemsResponse {
  return {
    buyerId: 'buyer-id',
    account: {
      consumptionTaxRate: 0.1,
      consumptionTax: 248,
      deliveryCharge: 500,
      totalItemsPrice: 1980,
      totalPrice: 2728,
    },
    basketItems: Array.from({ length: itemCount }, (_, index) => ({
      displayItemId: `item-${index}`,
      quantity: 1,
      unitPrice: 1980,
      subTotal: 1980,
      displayItem: { id: `item-${index}`, name: `陳列品${index}`, productCode: `C${index}` },
    })),
    deletedItemIds: [],
  }
}

/**
 * 決まった買い物かごを返すハンドラーを設定します。
 * モックの既定のハンドラーは状態を持つため、テストどうしが影響しないよう差し替えます。
 * @param itemCount 買い物かごに入っている陳列品の数。
 */
function useBasketResponse(itemCount: number) {
  server.use(
    http.get('/api/basket-items', () => HttpResponse.json(createBasketResponse(itemCount))),
  )
}

let pinia: Pinia

beforeEach(() => {
  pinia = createPinia()
  setActivePinia(pinia)
})

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * コンポーネントのマウント後に読み込むため、テスト用のコンポーネントを使います。
 * @returns 注文の確認と確定のユースケース。
 */
async function setupCheckout(): Promise<Checkout> {
  let checkout: Checkout | undefined
  mount(
    defineComponent({
      setup() {
        checkout = useCheckout()
        return () => null
      },
    }),
    { global: { plugins: [pinia] } },
  )
  await flushPromises()
  if (!checkout) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return checkout
}

describe('読み込み', () => {
  it('買い物かごに陳列品が入っている_readyになる', async () => {
    // Arrange
    useBasketResponse(2)
    // Act
    const checkout = await setupCheckout()
    // Assert
    expect(checkout.status.value).toEqual({ kind: 'ready' })
    expect(checkout.lines.value.length).toBe(2)
    expect(checkout.account.value?.totalPrice).toBe(2728)
    expect(checkout.address.value).toEqual(useUserStore(pinia).getAddress)
  })

  it('買い物かごが空_emptyになる', async () => {
    // Arrange
    useBasketResponse(0)
    // Act
    const checkout = await setupCheckout()
    // Assert
    expect(checkout.status.value).toEqual({ kind: 'empty' })
  })

  it('サーバーエラー_failedになる', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/basket-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const checkout = await setupCheckout()
    // Assert
    expect(checkout.status.value).toEqual({ kind: 'failed' })
  })
})

describe('注文の確定', () => {
  it('注文を確定できる_orderedを返し注文のIDとお届け先を持つ', async () => {
    // Arrange
    useBasketResponse(1)
    const checkout = await setupCheckout()
    let sent: PostOrderRequest | undefined
    server.use(
      http.post<never, PostOrderRequest>('/api/orders', async ({ request }) => {
        sent = await request.json()
        return new HttpResponse(null, {
          headers: { Location: 'http://localhost/api/orders/order-id' },
          status: HttpStatusCode.Created,
        })
      }),
    )
    // Act
    const outcome = await checkout.placeOrder()
    // Assert
    expect(outcome).toEqual({ kind: 'ordered', orderId: 'order-id' })
    expect(sent).toEqual(useUserStore(pinia).getAddress)
  })

  it('サーバーエラー_failedを返す', async () => {
    // Arrange
    useBasketResponse(1)
    const checkout = await setupCheckout()
    server.use(
      http.post(
        '/api/orders',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const outcome = await checkout.placeOrder()
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
  })
})
