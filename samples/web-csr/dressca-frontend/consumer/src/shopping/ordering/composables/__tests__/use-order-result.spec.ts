import { describe, it, expect } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import { server } from '@/../mock/node'
import { order } from '@/../mock/data/orders'
import { useOrderResult, type OrderResultView } from '../use-order-result'

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * コンポーネントのマウント後に読み込むため、テスト用のコンポーネントを使います。
 * @param orderId 注文の ID 。
 * @returns 注文結果の確認のユースケース。
 */
async function setupOrderResult(orderId: string): Promise<OrderResultView> {
  let view: OrderResultView | undefined
  mount(
    defineComponent({
      setup() {
        view = useOrderResult(orderId)
        return () => null
      },
    }),
  )
  await flushPromises()
  if (!view) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return view
}

describe('読み込み', () => {
  it('注文を取得できる_readyになり注文の内容を持つ', async () => {
    // Arrange
    let requestedId: string | readonly string[] | undefined
    server.use(
      http.get('/api/orders/:orderId', ({ params }) => {
        requestedId = params.orderId
        return HttpResponse.json(order)
      }),
    )
    const firstItem = order.orderItems?.[0]
    // Act
    const view = await setupOrderResult('order-id')
    // Assert
    expect(requestedId).toBe('order-id')
    expect(view.status.value).toEqual({ kind: 'ready' })
    expect(view.order.value?.address).toEqual({
      fullName: order.fullName,
      postalCode: order.postalCode,
      todofuken: order.todofuken,
      shikuchoson: order.shikuchoson,
      azanaAndOthers: order.azanaAndOthers,
    })
    expect(view.order.value?.items.length).toBe(order.orderItems?.length)
    expect(view.order.value?.items[0]).toEqual({
      id: firstItem?.id,
      name: firstItem?.itemOrdered?.name,
      assetCodes: firstItem?.itemOrdered?.assetCodes,
      unitPrice: firstItem?.unitPrice,
      quantity: firstItem?.quantity,
      subTotal: firstItem?.subTotal,
    })
  })

  it('サーバーエラー_failedになり注文の内容を持たない', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/orders/:orderId',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const view = await setupOrderResult('order-id')
    // Assert
    expect(view.status.value).toEqual({ kind: 'failed' })
    expect(view.order.value).toBeUndefined()
  })
})
