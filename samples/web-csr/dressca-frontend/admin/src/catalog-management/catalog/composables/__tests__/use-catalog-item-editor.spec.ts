import { describe, it, expect } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import type { PutCatalogItemRequest } from '@/system-common/generated/api-client'
import { server } from '@/../mock/node'
import { catalogItems } from '@/../mock/data/catalog-items'
import { useCatalogItemEditor, type CatalogItemEditor } from '../use-catalog-item-editor'

const item = catalogItems[0]!
const itemUrl = '/api/catalog-items/:catalogItemId'

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * vee-validate の useForm はコンポーネントの setup の中で呼び出す必要があるため、テスト用のコンポーネントを使います。
 * @param itemId 編集対象のアイテムの ID 。
 * @returns カタログアイテムの編集のユースケース。
 */
async function setupEditor(itemId: string = item.id): Promise<CatalogItemEditor> {
  let editor: CatalogItemEditor | undefined
  mount(
    defineComponent({
      setup() {
        editor = useCatalogItemEditor(itemId)
        return () => null
      },
    }),
  )
  await flushPromises()
  if (!editor) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return editor
}

/**
 * 指定した HTTP ステータスを返すレスポンスを生成します。
 * @param status HTTP ステータス。
 * @returns レスポンス。
 */
function statusResponse(status: number) {
  return () => new HttpResponse(null, { status })
}

/**
 * 最新のアイテムとして、行バージョンが変わったアイテムを返すハンドラーです。
 */
const latestItemHandler = http.get(itemUrl, () =>
  HttpResponse.json({ ...item, rowVersion: 'latest' }, { status: HttpStatusCode.Ok }),
)

describe('読み込み', () => {
  it('アイテムを取得できる_readyになり編集前の内容とフォームが設定される', async () => {
    // Arrange
    // Act
    const editor = await setupEditor()
    // Assert
    expect(editor.status.value).toBe('ready')
    expect(editor.current.value).toEqual({
      id: item.id,
      name: item.name,
      description: item.description,
      price: item.price,
      productCode: item.productCode,
      categoryId: item.catalogCategoryId,
      brandId: item.catalogBrandId,
      assetCodes: item.assetCodes,
    })
    expect(editor.form.itemName).toBe(item.name)
    expect(editor.form.price).toBe(item.price.toString())
    expect(editor.form.categoryId).toBe(item.catalogCategoryId)
    expect(editor.form.isValid).toBe(true)
    expect(editor.categories.value.length).toBeGreaterThan(0)
    expect(editor.brands.value.length).toBeGreaterThan(0)
  })

  it('アイテムが存在しない_notFoundになる', async () => {
    // Arrange
    server.use(http.get(itemUrl, statusResponse(HttpStatusCode.NotFound)))
    // Act
    const editor = await setupEditor()
    // Assert
    expect(editor.status.value).toBe('notFound')
    expect(editor.current.value).toBeUndefined()
  })

  it('サーバーエラー_failedになる', async () => {
    // Arrange
    server.use(http.get(itemUrl, statusResponse(HttpStatusCode.InternalServerError)))
    // Act
    const editor = await setupEditor()
    // Assert
    expect(editor.status.value).toBe('failed')
  })
})

describe('更新', () => {
  it('更新できる_updatedを返し読み込んだ行バージョンで更新する', async () => {
    // Arrange
    const editor = await setupEditor()
    let sent: PutCatalogItemRequest | undefined
    server.use(
      http.put<never, PutCatalogItemRequest>(itemUrl, async ({ request }) => {
        sent = await request.json()
        return new HttpResponse(null, { status: HttpStatusCode.NoContent })
      }),
    )
    editor.form.itemName = '変更後のアイテム名'
    editor.form.price = '2000'
    await flushPromises()
    // Act
    const outcome = await editor.update()
    // Assert
    expect(outcome).toEqual({ kind: 'updated' })
    expect(sent).toMatchObject({
      name: '変更後のアイテム名',
      price: 2000,
      rowVersion: item.rowVersion,
      isDeleted: item.isDeleted,
    })
  })

  it('競合した_conflictを返し入力中の値のまま最新の行バージョンで再度更新できる', async () => {
    // Arrange
    const editor = await setupEditor()
    server.use(
      http.put(itemUrl, statusResponse(HttpStatusCode.Conflict), { once: true }),
      latestItemHandler,
    )
    editor.form.itemName = '変更後のアイテム名'
    await flushPromises()
    // Act
    const outcome = await editor.update()
    // Assert
    expect(outcome).toEqual({ kind: 'conflict' })
    expect(editor.form.itemName).toBe('変更後のアイテム名')

    // Arrange
    let sent: PutCatalogItemRequest | undefined
    server.use(
      http.put<never, PutCatalogItemRequest>(itemUrl, async ({ request }) => {
        sent = await request.json()
        return new HttpResponse(null, { status: HttpStatusCode.NoContent })
      }),
    )
    // Act
    const retried = await editor.update()
    // Assert
    expect(retried).toEqual({ kind: 'updated' })
    expect(sent).toMatchObject({ name: '変更後のアイテム名', rowVersion: 'latest' })
  })

  it('競合したあとアイテムが削除されていた_notFoundを返す', async () => {
    // Arrange
    const editor = await setupEditor()
    server.use(
      http.put(itemUrl, statusResponse(HttpStatusCode.Conflict)),
      http.get(itemUrl, statusResponse(HttpStatusCode.NotFound)),
    )
    // Act
    const outcome = await editor.update()
    // Assert
    expect(outcome).toEqual({ kind: 'notFound' })
  })

  it('アイテムが存在しない_notFoundを返す', async () => {
    // Arrange
    const editor = await setupEditor()
    server.use(http.put(itemUrl, statusResponse(HttpStatusCode.NotFound)))
    // Act
    const outcome = await editor.update()
    // Assert
    expect(outcome).toEqual({ kind: 'notFound' })
  })

  it('サーバーエラー_failedを返す', async () => {
    // Arrange
    const editor = await setupEditor()
    server.use(http.put(itemUrl, statusResponse(HttpStatusCode.InternalServerError)))
    // Act
    const outcome = await editor.update()
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
  })
})

describe('削除', () => {
  it('削除できる_removedを返し読み込んだ行バージョンで削除する', async () => {
    // Arrange
    const editor = await setupEditor()
    let sentRowVersion: string | null = null
    server.use(
      http.delete(itemUrl, ({ request }) => {
        sentRowVersion = new URL(request.url).searchParams.get('rowVersion')
        return new HttpResponse(null, { status: HttpStatusCode.NoContent })
      }),
    )
    // Act
    const outcome = await editor.remove()
    // Assert
    expect(outcome).toEqual({ kind: 'removed' })
    expect(sentRowVersion).toBe(item.rowVersion)
  })

  it('競合した_conflictを返し最新の行バージョンで再度削除できる', async () => {
    // Arrange
    const editor = await setupEditor()
    server.use(
      http.delete(itemUrl, statusResponse(HttpStatusCode.Conflict), { once: true }),
      latestItemHandler,
    )
    // Act
    const outcome = await editor.remove()
    // Assert
    expect(outcome).toEqual({ kind: 'conflict' })

    // Arrange
    let sentRowVersion: string | null = null
    server.use(
      http.delete(itemUrl, ({ request }) => {
        sentRowVersion = new URL(request.url).searchParams.get('rowVersion')
        return new HttpResponse(null, { status: HttpStatusCode.NoContent })
      }),
    )
    // Act
    const retried = await editor.remove()
    // Assert
    expect(retried).toEqual({ kind: 'removed' })
    expect(sentRowVersion).toBe('latest')
  })

  it('アイテムが存在しない_notFoundを返す', async () => {
    // Arrange
    const editor = await setupEditor()
    server.use(http.delete(itemUrl, statusResponse(HttpStatusCode.NotFound)))
    // Act
    const outcome = await editor.remove()
    // Assert
    expect(outcome).toEqual({ kind: 'notFound' })
  })

  it('サーバーエラー_failedを返す', async () => {
    // Arrange
    const editor = await setupEditor()
    server.use(http.delete(itemUrl, statusResponse(HttpStatusCode.InternalServerError)))
    // Act
    const outcome = await editor.remove()
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
  })
})
