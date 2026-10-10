import { describe, it, expect } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import { server } from '@/../mock/node'
import { pagedListCatalogItem } from '@/../mock/data/catalog-items'
import { catalogCategories } from '@/../mock/data/catalog-categories'
import { catalogBrands } from '@/../mock/data/catalog-brands'
import { useCatalogItemList, type CatalogItemList } from '../use-catalog-item-list'

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * コンポーネントのマウント後に読み込むため、テスト用のコンポーネントを使います。
 * @returns カタログアイテムの一覧のユースケース。
 */
async function setupList(): Promise<CatalogItemList> {
  let list: CatalogItemList | undefined
  mount(
    defineComponent({
      setup() {
        list = useCatalogItemList()
        return () => null
      },
    }),
  )
  await flushPromises()
  if (!list) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return list
}

describe('読み込み', () => {
  it('アイテムを取得できる_readyになりカテゴリとブランドを名前で表す', async () => {
    // Arrange
    const expected = pagedListCatalogItem.items?.[0]
    if (!expected) {
      throw new Error('モックのアイテムがありません。')
    }
    // Act
    const list = await setupList()
    // Assert
    expect(list.status.value).toBe('ready')
    expect(list.items.value.length).toBe(pagedListCatalogItem.items?.length)
    expect(list.items.value[0]).toEqual({
      id: expected.id,
      name: expected.name,
      description: expected.description,
      price: expected.price,
      productCode: expected.productCode,
      categoryName: catalogCategories.find((c) => c.id === expected.catalogCategoryId)?.name,
      brandName: catalogBrands.find((b) => b.id === expected.catalogBrandId)?.name,
      assetCodes: expected.assetCodes,
      isDeleted: expected.isDeleted,
    })
  })

  it('該当するカテゴリがない_カテゴリ名を空文字にする', async () => {
    // Arrange
    server.use(http.get('/api/catalog-categories', () => HttpResponse.json([])))
    // Act
    const list = await setupList()
    // Assert
    expect(list.items.value[0]!.categoryName).toBe('')
  })

  it('サーバーエラー_failedになりアイテムは空', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/catalog-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const list = await setupList()
    // Assert
    expect(list.status.value).toBe('failed')
    expect(list.items.value).toEqual([])
  })
})
