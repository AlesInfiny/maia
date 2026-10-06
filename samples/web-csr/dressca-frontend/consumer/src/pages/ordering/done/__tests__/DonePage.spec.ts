import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createTestingPinia } from '@pinia/testing'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import DonePage from '@/pages/ordering/done/DonePage.vue'
import { router } from '@/app/router'
import { routeNames } from '@/app/router/route-names'
import { i18n } from '@/system-common/locales/i18n'
import { useNotificationStore } from '@/business-common/stores/notification'
import { server } from '@/../mock/node'

const { t } = i18n.global

describe('結果の通知と遷移', () => {
  it('注文を取得できない_トーストを表示し陳列品画面へ遷移する', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/orders/:orderId',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    const pinia = createTestingPinia({ createSpy: vi.fn, stubActions: false })
    await router.push({ name: routeNames.done, params: { orderId: 'order-id' } })
    // Act
    mount(DonePage, { global: { plugins: [pinia, router] } })
    await vi.waitUntil(() => router.currentRoute.value.name === routeNames.displayItem)
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToOrderInformation'))
  })
})
