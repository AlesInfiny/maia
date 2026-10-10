import { describe, it, expect, beforeEach } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils'
import { createPinia, setActivePinia, type Pinia } from 'pinia'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import type {
  GetBasketItemsResponse,
  PutBasketItemsRequest,
} from '@/system-common/generated/api-client'
import { useBasketStore } from '@/business-common/stores/basket'
import { server } from '@/../mock/node'
import { useBasket, type Basket } from '../use-basket'

const availableItemId = 'available-item-id'
const deletedItemId = 'deleted-item-id'

const problem = {
  exceptionId: 'TestException',
  exceptionValues: ['value'],
  title: 'テストのタイトル',
  detail: 'テストの詳細',
  status: HttpStatusCode.InternalServerError,
}

/**
 * 買い物かごのレスポンスを生成します。
 * @param deletedItemIds カタログから削除された陳列品の ID 。
 * @returns 買い物かごのレスポンス。
 */
function createBasketResponse(deletedItemIds: string[] = []): GetBasketItemsResponse {
  return {
    buyerId: 'buyer-id',
    account: {
      consumptionTaxRate: 0.1,
      consumptionTax: 248,
      deliveryCharge: 500,
      totalItemsPrice: 1980,
      totalPrice: 2728,
    },
    basketItems: [
      {
        displayItemId: availableItemId,
        quantity: 1,
        unitPrice: 1980,
        subTotal: 1980,
        displayItem: {
          id: availableItemId,
          name: '購入できる陳列品',
          productCode: 'C1',
          assetCodes: ['asset'],
        },
      },
      {
        displayItemId: deletedItemId,
        quantity: 2,
        unitPrice: 1000,
        subTotal: 2000,
        displayItem: { id: deletedItemId, name: '削除された陳列品', productCode: 'C2' },
      },
    ],
    deletedItemIds,
  }
}

/**
 * 決まった買い物かごを返すハンドラーを設定します。
 * モックの既定のハンドラーは状態を持つため、テストどうしが影響しないよう差し替えます。
 * @param deletedItemIds カタログから削除された陳列品の ID 。
 * @returns 買い物かごを取得した回数を返す関数。
 */
function useBasketResponse(deletedItemIds: string[] = []) {
  let fetchedCount = 0
  server.use(
    http.get('/api/basket-items', () => {
      fetchedCount += 1
      return HttpResponse.json(createBasketResponse(deletedItemIds))
    }),
  )
  return () => fetchedCount
}

let pinia: Pinia

beforeEach(() => {
  pinia = createPinia()
  setActivePinia(pinia)
})

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * コンポーネントのマウント後に読み込むため、テスト用のコンポーネントを使います。
 * @returns 買い物かごのユースケースと、マウントしたコンポーネントのラッパー。
 */
async function setupBasket(): Promise<{ basket: Basket; wrapper: VueWrapper }> {
  let basket: Basket | undefined
  const wrapper = mount(
    defineComponent({
      setup() {
        basket = useBasket()
        return () => null
      },
    }),
    { global: { plugins: [pinia] } },
  )
  await flushPromises()
  if (!basket) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return { basket, wrapper }
}

describe('読み込み', () => {
  it('買い物かごを取得できる_readyになり購入できない陳列品を区別する', async () => {
    // Arrange
    useBasketResponse([deletedItemId])
    // Act
    const { basket } = await setupBasket()
    // Assert
    expect(basket.status.value).toEqual({ kind: 'ready' })
    expect(basket.lines.value).toEqual([
      {
        displayItemId: availableItemId,
        name: '購入できる陳列品',
        assetCodes: ['asset'],
        unitPrice: 1980,
        quantity: 1,
        subTotal: 1980,
        available: true,
      },
      {
        displayItemId: deletedItemId,
        name: '削除された陳列品',
        assetCodes: undefined,
        unitPrice: 1000,
        quantity: 2,
        subTotal: 2000,
        available: false,
      },
    ])
    expect(basket.account.value).toEqual({
      totalItemsPrice: 1980,
      deliveryCharge: 500,
      consumptionTax: 248,
      totalPrice: 2728,
    })
    expect(basket.isEmpty.value).toBe(false)
    expect(basket.hasUnavailableItems.value).toBe(true)
  })

  it('問題の詳細を返すサーバーエラー_failedになり問題の詳細を持つ', async () => {
    // Arrange
    server.use(
      http.get('/api/basket-items', () =>
        HttpResponse.json(
          { ...problem, instance: '', type: '' },
          { status: HttpStatusCode.InternalServerError },
        ),
      ),
    )
    // Act
    const { basket } = await setupBasket()
    // Assert
    expect(basket.status.value).toEqual({ kind: 'failed', problem })
  })
})

describe('直前に買い物かごに入れた陳列品', () => {
  it('直前に入れた陳列品がある_表示し画面を離れると消す', async () => {
    // Arrange
    useBasketResponse()
    useBasketStore(pinia).addedItemId = availableItemId
    // Act
    const { basket, wrapper } = await setupBasket()
    // Assert
    expect(basket.addedItem.value).toEqual({
      name: '購入できる陳列品',
      assetCodes: ['asset'],
      unitPrice: 1980,
    })
    wrapper.unmount()
    expect(useBasketStore(pinia).addedItemId).toBeUndefined()
  })
})

describe('数量の変更', () => {
  it('数量を変更できる_changedを返し変更後の数量を送信する', async () => {
    // Arrange
    useBasketResponse()
    const { basket } = await setupBasket()
    let sent: PutBasketItemsRequest[] | undefined
    server.use(
      http.put<never, PutBasketItemsRequest[]>('/api/basket-items', async ({ request }) => {
        sent = await request.json()
        return new HttpResponse(null, { status: HttpStatusCode.NoContent })
      }),
    )
    // Act
    const outcome = await basket.changeQuantity(availableItemId, 3)
    // Assert
    expect(outcome).toEqual({ kind: 'changed' })
    expect(sent).toEqual([{ displayItemId: availableItemId, quantity: 3 }])
  })

  it('サーバーエラー_failedを返し買い物かごを読み込み直す', async () => {
    // Arrange
    const fetchedCount = useBasketResponse()
    const { basket } = await setupBasket()
    server.use(
      http.put(
        '/api/basket-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const outcome = await basket.changeQuantity(availableItemId, 3)
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
    expect(fetchedCount()).toBe(2)
  })
})

describe('削除', () => {
  it('削除できる_removedを返し陳列品のIDを指定して削除する', async () => {
    // Arrange
    useBasketResponse()
    const { basket } = await setupBasket()
    let removedId: string | readonly string[] | undefined
    server.use(
      http.delete('/api/basket-items/:displayItemId', ({ params }) => {
        removedId = params.displayItemId
        return new HttpResponse(null, { status: HttpStatusCode.NoContent })
      }),
    )
    // Act
    const outcome = await basket.remove(deletedItemId)
    // Assert
    expect(outcome).toEqual({ kind: 'removed' })
    expect(removedId).toBe(deletedItemId)
  })

  it('サーバーエラー_failedを返す', async () => {
    // Arrange
    useBasketResponse()
    const { basket } = await setupBasket()
    server.use(
      http.delete(
        '/api/basket-items/:displayItemId',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const outcome = await basket.remove(deletedItemId)
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
  })
})

describe('注文に進む', () => {
  it('購入できない陳列品がない_readyを返す', async () => {
    // Arrange
    useBasketResponse()
    const { basket } = await setupBasket()
    // Act
    const outcome = await basket.proceedToCheckout()
    // Assert
    expect(outcome).toEqual({ kind: 'ready' })
  })

  it('最新の買い物かごに購入できない陳列品がある_containsUnavailableItemsを返す', async () => {
    // Arrange
    useBasketResponse()
    const { basket } = await setupBasket()
    useBasketResponse([deletedItemId])
    // Act
    const outcome = await basket.proceedToCheckout()
    // Assert
    expect(outcome).toEqual({ kind: 'containsUnavailableItems' })
  })

  it('サーバーエラー_failedを返す', async () => {
    // Arrange
    useBasketResponse()
    const { basket } = await setupBasket()
    server.use(
      http.get(
        '/api/basket-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const outcome = await basket.proceedToCheckout()
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
  })
})
