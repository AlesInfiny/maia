import { describe, it, expect, vi } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import DisplayItemPage from '@/pages/DisplayItemPage.vue'
import { router } from '@/business-common/router'
import { routeNames } from '@/business-common/router/route-names'
import { i18n } from '@/system-common/locales/i18n'
import { useNotificationStore } from '@/business-common/stores/notification'
import { server } from '@/../mock/node'

const { t } = i18n.global

/**
 * テスト用の Pinia ストアを生成します。
 * 結合テストのため、アクションはモック化しません。
 * @returns 初期化済みの TestingPinia インスタンス
 */
function createPinia(): TestingPinia {
  return createTestingPinia({ createSpy: vi.fn, stubActions: false })
}

/**
 * 陳列品画面を表示し、読み込みの完了を待ちます。
 * @param pinia - テストに使用する TestingPinia インスタンス
 * @returns マウント済みの Vue Test Utils のラッパー
 */
async function getWrapper(pinia: TestingPinia) {
  await router.push({ name: routeNames.displayItem })
  const wrapper = mount(DisplayItemPage, { global: { plugins: [pinia, router] } })
  await flushPromises()
  return wrapper
}

describe('結果の通知と遷移', () => {
  it('買い物かごに入れられる_買い物かご画面へ遷移する', async () => {
    // Arrange
    const pinia = createPinia()
    const wrapper = await getWrapper(pinia)
    // Act
    await wrapper.findAll('button')[0]!.trigger('click')
    await vi.waitUntil(() => router.currentRoute.value.name === routeNames.basket)
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.basket)
  })

  it('買い物かごに入れられない_トーストを表示し陳列品画面にとどまる', async () => {
    // Arrange
    const pinia = createPinia()
    const wrapper = await getWrapper(pinia)
    server.use(
      http.post(
        '/api/basket-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    await wrapper.findAll('button')[0]!.trigger('click')
    await flushPromises()
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToAddItemToCarts'))
    expect(router.currentRoute.value.name).toBe(routeNames.displayItem)
  })

  it('陳列品を取得できない_トーストを表示する', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/display-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    const pinia = createPinia()
    // Act
    await getWrapper(pinia)
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToGetItems'))
  })
})
