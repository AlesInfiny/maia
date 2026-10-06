import { describe, it, expect, beforeEach } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { createPinia, setActivePinia, type Pinia } from 'pinia'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import type { PostBasketItemsRequest } from '@/system-common/generated/api-client'
import { server } from '@/../mock/node'
import { pagedListDisplayItem } from '@/../mock/data/display-items'
import { displayItemBrands } from '@/../mock/data/display-item-brands'
import { useDisplayItemList, type DisplayItemList } from '../use-display-item-list'

const problem = {
  exceptionId: 'TestException',
  exceptionValues: ['value'],
  title: 'テストのタイトル',
  detail: 'テストの詳細',
  status: HttpStatusCode.InternalServerError,
}

/**
 * 問題の詳細を返すサーバーエラーのレスポンスを生成します。
 * @returns レスポンス。
 */
function problemResponse() {
  return HttpResponse.json(
    { ...problem, instance: '', type: '' },
    { status: HttpStatusCode.InternalServerError },
  )
}

let pinia: Pinia

beforeEach(() => {
  pinia = createPinia()
  setActivePinia(pinia)
})

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * コンポーネントのマウント後に読み込むため、テスト用のコンポーネントを使います。
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
    let query: URLSearchParams | undefined
    server.use(
      http.get('/api/display-items', ({ request }) => {
        query = new URL(request.url).searchParams
        return HttpResponse.json(pagedListDisplayItem)
      }),
    )
    // Act
    list.filter.categoryId = 'category-id'
    await flushPromises()
    // Assert
    expect(query?.get('categoryId')).toBe('category-id')
    expect(query?.has('brandId')).toBe(false)
  })

  it('問題の詳細を返すサーバーエラー_failedになり問題の詳細を持つ', async () => {
    // Arrange
    server.use(http.get('/api/display-items', problemResponse))
    // Act
    const list = await setupList()
    // Assert
    expect(list.status.value).toEqual({ kind: 'failed', problem })
  })

  it('問題の詳細を返さないサーバーエラー_failedになり問題の詳細を持たない', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/display-item-categories',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
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
    let sent: PostBasketItemsRequest | undefined
    server.use(
      http.post<never, PostBasketItemsRequest>('/api/basket-items', async ({ request }) => {
        sent = await request.json()
        return new HttpResponse(null, { status: HttpStatusCode.Created })
      }),
    )
    // Act
    const outcome = await list.addToBasket('display-item-id')
    // Assert
    expect(outcome).toEqual({ kind: 'added' })
    expect(sent).toEqual({ displayItemId: 'display-item-id', addedQuantity: 1 })
  })

  it('問題の詳細を返すサーバーエラー_failedを返し問題の詳細を持つ', async () => {
    // Arrange
    const list = await setupList()
    server.use(http.post('/api/basket-items', problemResponse))
    // Act
    const outcome = await list.addToBasket('display-item-id')
    // Assert
    expect(outcome).toEqual({ kind: 'failed', problem })
  })
})
