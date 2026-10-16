import { describe, it, expect, vi, beforeEach } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import type { GetBasketItemsResponse } from '@/system-common/generated/api-client'
import type { Router } from 'vue-router'
import CheckoutPage from '@/pages/ordering/checkout/CheckoutPage.vue'
import { routeNames } from '@/core/router/route-names'
import { createStubRouter } from '@/pages/__tests__/helpers/stub-router'
import { i18n } from '@/system-common/locales/i18n'
import { ServerError, UnauthorizedError } from '@/system-common/error-handler/custom-error'
import { useNotificationStore } from '@/business-common/stores/notification'

const { getBasketItemsMock, postOrderMock } = vi.hoisted(() => ({
  getBasketItemsMock: vi.fn(),
  postOrderMock: vi.fn(),
}))

vi.mock('@/system-common/api-client', () => ({
  basketItemsApi: () => ({
    getBasketItems: getBasketItemsMock,
  }),
  ordersApi: () => ({
    postOrder: postOrderMock,
  }),
}))

const { t } = i18n.global

let router: Router

beforeEach(() => {
  vi.resetAllMocks()
})

/**
 * 決まった買い物かごを返すように、買い物かごの取得をモックします。
 * @param itemCount 買い物かごに入っている陳列品の数。
 */
function useBasketResponse(itemCount: number) {
  const response: GetBasketItemsResponse = {
    buyerId: 'buyer-id',
    basketItems: Array.from({ length: itemCount }, (_, index) => ({
      displayItemId: `item-${index}`,
      quantity: 1,
      unitPrice: 1980,
      subTotal: 1980,
    })),
    deletedItemIds: [],
  }
  getBasketItemsMock.mockResolvedValue({ data: response })
}

/**
 * 注文確認画面を表示し、読み込みの完了を待ちます。
 * @returns マウント済みの Vue Test Utils のラッパーと TestingPinia インスタンス
 */
async function getWrapper() {
  const pinia: TestingPinia = createTestingPinia({ createSpy: vi.fn, stubActions: false })
  router = createStubRouter()
  await router.push({ name: routeNames.checkout })
  const wrapper = mount(CheckoutPage, { global: { plugins: [pinia, router] } })
  await flushPromises()
  return { wrapper, pinia }
}

/**
 * 注文を確定するボタンを押下します。
 * @param wrapper 注文確認画面のラッパー。
 */
async function clickPlaceOrder(wrapper: Awaited<ReturnType<typeof getWrapper>>['wrapper']) {
  await wrapper.findAll('button')[0]!.trigger('click')
}

describe('注文を確定するボタン', () => {
  it('買い物かごの読み込み中_押せない', async () => {
    // Arrange
    getBasketItemsMock.mockReturnValue(new Promise(() => {}))
    // Act
    const { wrapper } = await getWrapper()
    // Assert
    expect(wrapper.findAll('button')[0]!.attributes('disabled')).toBeDefined()
  })

  it('買い物かごの読み込みが完了した_押せる', async () => {
    // Arrange
    useBasketResponse(1)
    // Act
    const { wrapper } = await getWrapper()
    // Assert
    expect(wrapper.findAll('button')[0]!.attributes('disabled')).toBeUndefined()
  })
})

describe('結果の通知と遷移', () => {
  it('買い物かごが空_陳列品画面へ遷移する', async () => {
    // Arrange
    useBasketResponse(0)
    // Act
    await getWrapper()
    await flushPromises()
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.displayItem)
  })

  it('注文を確定できる_注文完了画面へ遷移する', async () => {
    // Arrange
    useBasketResponse(1)
    postOrderMock.mockResolvedValue({
      headers: { location: 'http://localhost/api/orders/order-id' },
    })
    const { wrapper } = await getWrapper()
    // Act
    await clickPlaceOrder(wrapper)
    await flushPromises()
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.done)
    expect(router.currentRoute.value.params).toEqual({ orderId: 'order-id' })
  })

  it('注文を確定できない_トーストを表示しエラー画面へ遷移する', async () => {
    // Arrange
    useBasketResponse(1)
    postOrderMock.mockRejectedValue(new ServerError(''))
    const { wrapper, pinia } = await getWrapper()
    // Act
    await clickPlaceOrder(wrapper)
    await flushPromises()
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToOrderItems'))
    expect(router.currentRoute.value.name).toBe(routeNames.error)
  })

  it('認証が切れている_画面はトーストも遷移もしない', async () => {
    // Arrange
    useBasketResponse(1)
    postOrderMock.mockRejectedValue(new UnauthorizedError(''))
    const { wrapper, pinia } = await getWrapper()
    // Act
    await clickPlaceOrder(wrapper)
    await flushPromises()
    // Assert
    // ログイン画面への遷移は、共通のエラー処理が発行する認証エラーのイベントを受けて App.vue が行う
    expect(useNotificationStore(pinia).message).not.toBe(t('failedToOrderItems'))
    expect(router.currentRoute.value.name).toBe(routeNames.checkout)
  })
})
