import { describe, it, expect, vi } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import type { GetBasketItemsResponse } from '@/system-common/generated/api-client'
import CheckoutPage from '@/pages/ordering/checkout/CheckoutPage.vue'
import { router } from '@/business-common/router'
import { routeNames } from '@/business-common/router/route-names'
import { i18n } from '@/system-common/locales/i18n'
import { useNotificationStore } from '@/business-common/stores/notification'
import { server } from '@/../mock/node'

const { t } = i18n.global

/**
 * 買い物かごのレスポンスを返すハンドラーを設定します。
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
  server.use(http.get('/api/basket-items', () => HttpResponse.json(response)))
}

/**
 * 注文確認画面を表示し、読み込みの完了を待ちます。
 * @returns マウント済みの Vue Test Utils のラッパーと TestingPinia インスタンス
 */
async function getWrapper() {
  const pinia: TestingPinia = createTestingPinia({ createSpy: vi.fn, stubActions: false })
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
  await wrapper.findAll('button')[0].trigger('click')
}

describe('結果の通知と遷移', () => {
  it('買い物かごが空_陳列品画面へ遷移する', async () => {
    // Arrange
    useBasketResponse(0)
    // Act
    await getWrapper()
    await vi.waitUntil(() => router.currentRoute.value.name === routeNames.displayItem)
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.displayItem)
  })

  it('注文を確定できる_注文完了画面へ遷移する', async () => {
    // Arrange
    useBasketResponse(1)
    server.use(
      http.post(
        '/api/orders',
        () =>
          new HttpResponse(null, {
            headers: { Location: 'http://localhost/api/orders/order-id' },
            status: HttpStatusCode.Created,
          }),
      ),
    )
    const { wrapper } = await getWrapper()
    // Act
    await clickPlaceOrder(wrapper)
    await vi.waitUntil(() => router.currentRoute.value.name === routeNames.done)
    // Assert
    expect(router.currentRoute.value.params).toEqual({ orderId: 'order-id' })
  })

  it('注文を確定できない_トーストを表示しエラー画面へ遷移する', async () => {
    // Arrange
    useBasketResponse(1)
    server.use(
      http.post(
        '/api/orders',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    const { wrapper, pinia } = await getWrapper()
    // Act
    await clickPlaceOrder(wrapper)
    await vi.waitUntil(() => router.currentRoute.value.name === routeNames.error)
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToOrderItems'))
  })
})
