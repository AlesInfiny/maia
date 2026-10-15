import { describe, it, expect, vi } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import { createTestingPinia } from '@pinia/testing'
import DonePage from '@/pages/ordering/done/DonePage.vue'
import { routeNames } from '@/core/router/route-names'
import { createStubRouter } from '@/pages/__tests__/helpers/stub-router'
import { i18n } from '@/system-common/locales/i18n'
import { ServerError } from '@/system-common/error-handler/custom-error'
import { useNotificationStore } from '@/business-common/stores/notification'

const { getOrderByIdMock } = vi.hoisted(() => ({
  getOrderByIdMock: vi.fn(),
}))

vi.mock('@/system-common/api-client', () => ({
  ordersApi: () => ({
    getOrderById: getOrderByIdMock,
  }),
}))

const { t } = i18n.global

describe('結果の通知と遷移', () => {
  it('注文を取得できない_トーストを表示し陳列品画面へ遷移する', async () => {
    // Arrange
    getOrderByIdMock.mockRejectedValue(new ServerError(''))
    const pinia = createTestingPinia({ createSpy: vi.fn, stubActions: false })
    const router = createStubRouter()
    await router.push({ name: routeNames.done, params: { orderId: 'order-id' } })
    // Act
    mount(DonePage, { global: { plugins: [pinia, router] } })
    await flushPromises()
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToOrderInformation'))
    expect(router.currentRoute.value.name).toBe(routeNames.displayItem)
  })
})
