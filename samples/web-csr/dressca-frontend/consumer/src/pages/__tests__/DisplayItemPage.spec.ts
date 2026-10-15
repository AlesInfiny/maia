import { describe, it, expect, vi, beforeEach } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import { createTestingPinia, type TestingPinia } from '@pinia/testing'
import DisplayItemPage from '@/pages/DisplayItemPage.vue'
import { router } from '@/core/router'
import { routeNames } from '@/core/router/route-names'
import { i18n } from '@/system-common/locales/i18n'
import { ServerError } from '@/system-common/error-handler/custom-error'
import { useNotificationStore } from '@/business-common/stores/notification'
import { basket } from '@/../mock/data/basket-items'
import { pagedListDisplayItem } from '@/../mock/data/display-items'
import { displayItemBrands } from '@/../mock/data/display-item-brands'
import { displayItemCategories } from '@/../mock/data/display-item-categories'

const {
  getDisplayItemCategoriesMock,
  getDisplayItemBrandsMock,
  getByQueryMock,
  postBasketItemMock,
  getBasketItemsMock,
} = vi.hoisted(() => ({
  getDisplayItemCategoriesMock: vi.fn(),
  getDisplayItemBrandsMock: vi.fn(),
  getByQueryMock: vi.fn(),
  postBasketItemMock: vi.fn(),
  getBasketItemsMock: vi.fn(),
}))

vi.mock('@/system-common/api-client', () => ({
  displayItemCategoriesApi: () => ({
    getDisplayItemCategories: getDisplayItemCategoriesMock,
  }),
  displayItemBrandsApi: () => ({
    getDisplayItemBrands: getDisplayItemBrandsMock,
  }),
  displayItemsApi: () => ({
    getByQuery: getByQueryMock,
  }),
  basketItemsApi: () => ({
    postBasketItem: postBasketItemMock,
    getBasketItems: getBasketItemsMock,
  }),
}))

const { t } = i18n.global

beforeEach(() => {
  vi.resetAllMocks()
  // ストアがレスポンスの配列を書き換えるため、呼び出しごとに複製を返します。
  getDisplayItemCategoriesMock.mockImplementation(() =>
    Promise.resolve({
      data: structuredClone(displayItemCategories),
    }),
  )
  getDisplayItemBrandsMock.mockImplementation(() =>
    Promise.resolve({
      data: structuredClone(displayItemBrands),
    }),
  )
  getByQueryMock.mockImplementation(() =>
    Promise.resolve({ data: structuredClone(pagedListDisplayItem) }),
  )
  postBasketItemMock.mockResolvedValue({})
  getBasketItemsMock.mockImplementation(() => Promise.resolve({ data: structuredClone(basket) }))
})

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
  it('買い物かごに入れられる_入れた陳列品を履歴エントリーの state で渡して買い物かご画面へ遷移する', async () => {
    // Arrange
    const pinia = createPinia()
    const wrapper = await getWrapper(pinia)
    // Act
    await wrapper.findAll('button')[0]!.trigger('click')
    await vi.waitUntil(() => router.currentRoute.value.name === routeNames.basket)
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.basket)
    const { displayItemId } = postBasketItemMock.mock.calls[0]![0] as { displayItemId: string }
    expect(window.history.state.addedItemId).toBe(displayItemId)
  })

  it('買い物かごに入れられない_トーストを表示し陳列品画面にとどまる', async () => {
    // Arrange
    const pinia = createPinia()
    const wrapper = await getWrapper(pinia)
    postBasketItemMock.mockRejectedValue(new ServerError(''))
    // Act
    await wrapper.findAll('button')[0]!.trigger('click')
    await flushPromises()
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToAddItemToCarts'))
    expect(router.currentRoute.value.name).toBe(routeNames.displayItem)
  })

  it('陳列品を取得できない_トーストを表示する', async () => {
    // Arrange
    getByQueryMock.mockRejectedValue(new ServerError(''))
    const pinia = createPinia()
    // Act
    await getWrapper(pinia)
    // Assert
    expect(useNotificationStore(pinia).message).toBe(t('failedToGetItems'))
  })
})
