import { describe, it, expect, vi, beforeAll, assert } from 'vitest'
import { flushPromises, mount, VueWrapper } from '@vue/test-utils'
import type { Router } from 'vue-router'
import { routeNames, type BasketRouteState } from '@/core/router/route-names'
import { createStubRouter } from '@/pages/__tests__/helpers/stub-router'
import { i18n } from '@/system-common/locales/i18n'
import { createTestingPinia } from '@pinia/testing'
import BasketPage from '@/pages/basket/BasketPage.vue'
import type { GetBasketItemsResponse } from '@/system-common/generated/api-client'
import { ServerError } from '@/system-common/error-handler/custom-error'
import { useNotificationStore } from '@/business-common/stores/notification'
import { BasketItem } from '@/shopping/public-api'
import { createAxiosError, createProblemDetails } from './helpers'

/**
 * 買い物かごに陳列品が入っている状態のモックレスポンスを生成します。
 * 主にテストやスタブデータとして利用することを想定しています。
 * @returns `GetBasketItemsResponse` 型のオブジェクト
 */
function createGetBasketItemsResponse(): GetBasketItemsResponse {
  return {
    buyerId: 'xxxxxxxxxxxxxxxxxxxxxxxxxx',
    account: {
      consumptionTaxRate: 0.1,
      consumptionTax: 248,
      deliveryCharge: 500,
      totalItemsPrice: 1980,
      totalPrice: 2728,
    },
    basketItems: [
      {
        displayItemId: '019b76da-a800-7004-8001-000000000001',
        quantity: 1,
        unitPrice: 1980,
        displayItem: {
          id: '019b76da-a800-7004-8001-000000000001',
          name: 'クルーネック Tシャツ - ブラック',
          productCode: 'C000000001',
          assetCodes: [],
        },
        subTotal: 1980,
      },
      {
        displayItemId: '019b76da-a800-7004-8001-000000000002',
        quantity: 2,
        unitPrice: 4800,
        displayItem: {
          id: '019b76da-a800-7004-8001-000000000002',
          name: '裏起毛 スキニーデニム',
          productCode: 'C000000002',
          assetCodes: ['4aed07c4ed5d45a5b97f11acedfbb601'],
        },
        subTotal: 9600,
      },
    ],
  }
}

/**
 * 空の買い物かご状態のモックレスポンスを生成します。
 * 主に「商品未追加時の画面」やテストケースに利用されます。
 * @returns `GetBasketItemsResponse` 型のオブジェクト
 */
function createEmptyGetBasketItemsResponse(): GetBasketItemsResponse {
  return {
    buyerId: 'xxxxxxxxxxxxxxxxxxxxxxxxxx',
    account: {
      consumptionTaxRate: 0.1,
      consumptionTax: 0,
      deliveryCharge: 500,
      totalItemsPrice: 0,
      totalPrice: 0,
    },
    basketItems: [],
  }
}

/**
 *  vi.mock はファイルの先頭に巻き上げられるので、
 * 複数回 vi.mock を宣言すると、最後に定義されたもので上書きされてしまいます。
 * モックの振る舞いをテストケースごとに変更したい場合は、
 *  vi.hoisted を使用します。
 */
const { getBasketItemsMock } = vi.hoisted(() => {
  return {
    getBasketItemsMock: vi.fn(),
  }
})

vi.mock('@/system-common/api-client', () => ({
  // basketItemsApi() を呼ぶと { getBasketItems: getBasketItemsMock } が返る
  basketItemsApi: () => ({
    getBasketItems: getBasketItemsMock,
  }),
}))

let router: Router

/**
 * コンポーネントをテスト用にマウントするヘルパー関数です。
 * @param stubRouter テストに使用するルーター。
 * @returns マウント済みの Vue Test Utils ラッパー
 */
function getWrapper(stubRouter: Router = createStubRouter()) {
  router = stubRouter
  const pinia = createTestingPinia({
    createSpy: vi.fn, // 明示的に設定する必要があります。
    stubActions: false, // 結合テストなので、アクションはモック化しないように設定します。
  })
  i18n.global.locale.value = 'ja' // デフォルトの jsdom 環境では英語（en）に設定されるので、日本語に変更します。
  return mount(BasketPage, {
    global: { plugins: [pinia, router, i18n] },
  })
}

describe('買い物かごのアイテムを表示する_アイテムが入っている', () => {
  let wrapper: VueWrapper

  beforeAll(() => {
    // onMounted のタイミングで API コールを行っているので、wrapper の作成よりも先にモックする必要があります。
    getBasketItemsMock.mockResolvedValue({ data: createGetBasketItemsResponse() })
    wrapper = getWrapper()
  })

  it('取得したアイテムの情報が表示される', async () => {
    // Arrange
    const expectCount = createGetBasketItemsResponse().basketItems!.length
    // Act
    await flushPromises()
    // Assert
    const basketItem = wrapper.findAllComponents(BasketItem)
    expect(wrapper.html()).toContain('クルーネック Tシャツ - ブラック')
    expect(wrapper.html()).toContain('裏起毛 スキニーデニム')
    expect(basketItem).toHaveLength(expectCount)
  })

  it('「買い物を続ける」ボタンが表示されている', () => {
    // Arrange
    // Act
    // Assert
    const button = wrapper.findAll('button')[0]!
    expect(button.isVisible()).toBe(true)
  })

  it('「レジに進む」ボタンが表示されている', () => {
    // Arrange
    // Act
    // Assert
    const button = wrapper.findAll('button')[1]!
    expect(button.isVisible()).toBe(true)
  })
})

describe('買い物かごのアイテムを表示する_アイテムが0件', () => {
  let wrapper: VueWrapper

  beforeAll(() => {
    getBasketItemsMock.mockResolvedValue({ data: createEmptyGetBasketItemsResponse() })
    wrapper = getWrapper()
  })

  it('0件を示すメッセージが表示される', async () => {
    // Arrange
    // Act
    await flushPromises()
    // Assert
    expect(wrapper.html()).toContain('買い物かごに商品がありません。')
  })

  it('「買い物を続ける」ボタンが表示されている', () => {
    // Arrange
    // Act
    // Assert
    const button = wrapper.findAll('button')[0]!
    expect(button.isVisible()).toBe(true)
  })

  it('「レジに進む」ボタンが表示されていない', () => {
    // Arrange
    // Act
    // Assert
    const button = wrapper.find('[data-testId="orderButton"]')
    expect(button.exists()).toBe(false)
  })
})

describe('買い物かごのアイテムを表示する_サーバーエラー', () => {
  it('サーバーエラー_通知ストアにサーバーエラーを示すメッセージが格納される', async () => {
    // Arrange
    const expectDetail = 'expectDetail'
    const expectExceptionId = 'serverError'
    const expectStatus = 500
    const expectTitle = 'expectTitle'
    const problem = createProblemDetails({
      detail: expectDetail,
      exceptionId: expectExceptionId,
      status: expectStatus,
      title: expectTitle,
    })
    const error = createAxiosError(problem)
    getBasketItemsMock.mockRejectedValue(new ServerError('', error))
    const expectMessage = 'サーバーエラーが発生しました。'
    getWrapper()
    const notificationStore = useNotificationStore()

    // Act
    await flushPromises()

    // Assert
    assert.equal(notificationStore.message, expectMessage)
    assert.equal(notificationStore.id, expectExceptionId)
    assert.equal(notificationStore.title, expectTitle)
    assert.equal(notificationStore.detail, expectDetail)
    assert.equal(notificationStore.status, expectStatus)
  })
})

describe('結果の通知と遷移', () => {
  it('購入できない陳列品が入っている_トーストを表示しレジに進むボタンが非活性', async () => {
    // Arrange
    const response = createGetBasketItemsResponse()
    response.deletedItemIds = [response.basketItems![0]!.displayItemId]
    getBasketItemsMock.mockResolvedValue({ data: response })
    // Act
    const wrapper = getWrapper()
    await flushPromises()
    // Assert
    expect(useNotificationStore().message).toBe(i18n.global.t('basketContainsUnavailableItem'))
    expect(wrapper.find('[data-testId="orderButton"]').attributes('disabled')).toBeDefined()
  })

  it('レジに進む_注文確認画面へ遷移する', async () => {
    // Arrange
    getBasketItemsMock.mockResolvedValue({ data: createGetBasketItemsResponse() })
    const wrapper = getWrapper()
    await flushPromises()
    // Act
    await wrapper.find('[data-testId="orderButton"]').trigger('click')
    await flushPromises()
    // Assert
    expect(router.currentRoute.value.name).toBe(routeNames.checkout)
  })
})

describe('直前に買い物かごに入れた陳列品', () => {
  it('履歴エントリーの state に陳列品の ID がある_その陳列品を表示する', async () => {
    // Arrange
    const response = createGetBasketItemsResponse()
    getBasketItemsMock.mockResolvedValue({ data: response })
    const stubRouter = createStubRouter()
    const state: BasketRouteState = { addedItemId: response.basketItems![0]!.displayItemId }
    await stubRouter.push({ name: routeNames.basket, state })
    // Act
    const wrapper = getWrapper(stubRouter)
    await flushPromises()
    // Assert
    expect(wrapper.text()).toContain(i18n.global.t('addedItemsToBasket'))
  })

  it('履歴エントリーの state に陳列品の ID がない_表示しない', async () => {
    // Arrange
    getBasketItemsMock.mockResolvedValue({ data: createGetBasketItemsResponse() })
    // Act
    const wrapper = getWrapper()
    await flushPromises()
    // Assert
    expect(wrapper.text()).not.toContain(i18n.global.t('addedItemsToBasket'))
  })
})
