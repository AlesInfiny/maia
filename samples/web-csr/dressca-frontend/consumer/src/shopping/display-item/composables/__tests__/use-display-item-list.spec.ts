import { describe, it, expect, vi, beforeEach } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { createPinia, setActivePinia, type Pinia } from 'pinia'
import { HttpStatusCode } from 'axios'
import { ServerError } from '@/system-common/error-handler/custom-error'
import { basket } from '@/../mock/data/basket-items'
import { pagedListDisplayItem } from '@/../mock/data/display-items'
import { displayItemBrands } from '@/../mock/data/display-item-brands'
import { displayItemCategories } from '@/../mock/data/display-item-categories'
import { useDisplayItemList, type DisplayItemList } from '../use-display-item-list'

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

const problem = {
  exceptionId: 'TestException',
  exceptionValues: ['value'],
  title: 'テストのタイトル',
  detail: 'テストの詳細',
  status: HttpStatusCode.InternalServerError,
}

/**
 * 問題の詳細を持つサーバーエラーを生成します。
 * @returns サーバーエラー。
 */
function createProblemError(): ServerError {
  return new ServerError(
    '',
    Object.assign(new Error(), { response: { data: { ...problem, instance: '', type: '' } } }),
  )
}

let pinia: Pinia

beforeEach(() => {
  vi.resetAllMocks()
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
  getBasketItemsMock.mockImplementation(() => Promise.resolve({ data: structuredClone(basket) }))
  pinia = createPinia()
  setActivePinia(pinia)
})

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * @returns 陳列品の一覧のユースケース。
 */
async function setupList(): Promise<DisplayItemList> {
  let list: DisplayItemList | undefined
  mount(
    defineComponent({
      setup() {
        list = useDisplayItemList()
        return () => null
      },
    }),
    { global: { plugins: [pinia] } },
  )
  await flushPromises()
  if (!list) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return list
}

describe('読み込み', () => {
  it('陳列品を取得できる_readyになり選択肢の先頭はすべてでブランドを名前で表す', async () => {
    // Arrange
    const expected = pagedListDisplayItem.items?.[0]
    if (!expected) {
      throw new Error('モックの陳列品がありません。')
    }
    // Act
    const list = await setupList()
    // Assert
    expect(list.status.value).toEqual({ kind: 'ready' })
    expect(list.categories.value[0]).toEqual({ id: '', name: 'すべて' })
    expect(list.brands.value[0]).toEqual({ id: '', name: 'すべて' })
    expect(list.items.value.length).toBe(pagedListDisplayItem.items?.length)
    expect(list.items.value[0]).toEqual({
      id: expected.id,
      name: expected.name,
      price: expected.price,
      brandName: displayItemBrands.find((brand) => brand.id === expected.displayItemBrandId)?.name,
      assetCodes: expected.assetCodes,
    })
    expect(list.specialContents.value.length).toBeGreaterThan(0)
  })

  it('絞り込みの条件を変更する_条件を指定して読み込み直す', async () => {
    // Arrange
    const list = await setupList()
    // Act
    list.filter.categoryId = 'category-id'
    await flushPromises()
    // Assert
    // getByQuery の引数は、ブランドの ID 、カテゴリの ID 、ページ番号、ページサイズの順です。
    expect(getByQueryMock).toHaveBeenLastCalledWith(undefined, 'category-id', undefined, undefined)
  })

  it('問題の詳細を返すサーバーエラー_failedになり問題の詳細を持つ', async () => {
    // Arrange
    getByQueryMock.mockRejectedValue(createProblemError())
    // Act
    const list = await setupList()
    // Assert
    expect(list.status.value).toEqual({ kind: 'failed', problem })
  })

  it('問題の詳細を返さないサーバーエラー_failedになり問題の詳細を持たない', async () => {
    // Arrange
    getDisplayItemCategoriesMock.mockRejectedValue(new ServerError(''))
    // Act
    const list = await setupList()
    // Assert
    expect(list.status.value).toEqual({ kind: 'failed' })
  })
})

describe('買い物かごに入れる', () => {
  it('買い物かごに入れられる_addedを返し陳列品のIDを送信する', async () => {
    // Arrange
    const list = await setupList()
    postBasketItemMock.mockResolvedValue({})
    // Act
    const outcome = await list.addToBasket('display-item-id')
    // Assert
    expect(outcome).toEqual({ kind: 'added' })
    expect(postBasketItemMock).toHaveBeenCalledWith({
      displayItemId: 'display-item-id',
      addedQuantity: 1,
    })
  })

  it('問題の詳細を返すサーバーエラー_failedを返し問題の詳細を持つ', async () => {
    // Arrange
    const list = await setupList()
    postBasketItemMock.mockRejectedValue(createProblemError())
    // Act
    const outcome = await list.addToBasket('display-item-id')
    // Assert
    expect(outcome).toEqual({ kind: 'failed', problem })
  })
})
