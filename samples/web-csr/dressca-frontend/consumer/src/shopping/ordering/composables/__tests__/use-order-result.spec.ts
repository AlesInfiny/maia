import { describe, it, expect, vi, beforeEach } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { ServerError } from '@/system-common/error-handler/custom-error'
import { order } from '@/../mock/data/orders'
import { useOrderResult, type OrderResultView } from '../use-order-result'

const { getOrderByIdMock } = vi.hoisted(() => ({
  getOrderByIdMock: vi.fn(),
}))

vi.mock('@/system-common/api-client', () => ({
  ordersApi: () => ({
    getOrderById: getOrderByIdMock,
  }),
}))

beforeEach(() => {
  vi.resetAllMocks()
})

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
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
    getOrderByIdMock.mockResolvedValue({ data: order })
    const firstItem = order.orderItems?.[0]
    // Act
    const view = await setupOrderResult('order-id')
    // Assert
    expect(getOrderByIdMock).toHaveBeenCalledWith('order-id')
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
    getOrderByIdMock.mockRejectedValue(new ServerError(''))
    // Act
    const view = await setupOrderResult('order-id')
    // Assert
    expect(view.status.value).toEqual({ kind: 'failed' })
    expect(view.order.value).toBeUndefined()
  })
})
