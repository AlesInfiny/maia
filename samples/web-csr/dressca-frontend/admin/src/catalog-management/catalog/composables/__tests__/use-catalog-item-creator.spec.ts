import { describe, it, expect } from 'vitest'
import { defineComponent } from 'vue'
import { flushPromises, mount } from '@vue/test-utils'
import { http, HttpResponse } from 'msw'
import { HttpStatusCode } from 'axios'
import type { PostCatalogItemRequest } from '@/system-common/generated/api-client'
import { server } from '@/../mock/node'
import { catalogCategories } from '@/../mock/data/catalog-categories'
import { catalogBrands } from '@/../mock/data/catalog-brands'
import type { CatalogItemFormValues } from '../../validation/validation-items'
import { useCatalogItemCreator, type CatalogItemCreator } from '../use-catalog-item-creator'

const validValues: CatalogItemFormValues = {
  itemName: '追加するアイテム',
  itemDescription: '追加するアイテムの説明です。',
  price: '1980',
  productCode: 'T001',
}

/**
 * コンポーネントの中でユースケースコンポーザブルを呼び出し、読み込みの完了を待ちます。
 * vee-validate の useForm はコンポーネントの setup の中で呼び出す必要があるため、テスト用のコンポーネントを使います。
 * @param initialValues 入力項目の初期値。
 * @returns カタログアイテムの追加のユースケース。
 */
async function setupCreator(
  initialValues: Partial<CatalogItemFormValues> = validValues,
): Promise<CatalogItemCreator> {
  let creator: CatalogItemCreator | undefined
  mount(
    defineComponent({
      setup() {
        creator = useCatalogItemCreator(initialValues)
        return () => null
      },
    }),
  )
  await flushPromises()
  if (!creator) {
    throw new Error('ユースケースコンポーザブルを生成できませんでした。')
  }
  return creator
}

describe('読み込み', () => {
  it('カテゴリとブランドを取得できる_readyになり先頭の選択肢を選択する', async () => {
    // Arrange
    // Act
    const creator = await setupCreator()
    // Assert
    expect(creator.status.value).toBe('ready')
    expect(creator.categories.value.length).toBe(catalogCategories.length)
    expect(creator.brands.value.length).toBe(catalogBrands.length)
    expect(creator.form.categoryId).toBe(catalogCategories[0].id)
    expect(creator.form.brandId).toBe(catalogBrands[0].id)
    expect(creator.form.itemName).toBe(validValues.itemName)
    expect(creator.form.isValid).toBe(true)
  })

  it('サーバーエラー_failedになる', async () => {
    // Arrange
    server.use(
      http.get(
        '/api/catalog-categories',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const creator = await setupCreator()
    // Assert
    expect(creator.status.value).toBe('failed')
  })
})

describe('追加', () => {
  it('追加できる_createdを返し入力フォームの内容を送信する', async () => {
    // Arrange
    const creator = await setupCreator()
    let sent: PostCatalogItemRequest | undefined
    server.use(
      http.post<never, PostCatalogItemRequest>('/api/catalog-items', async ({ request }) => {
        sent = await request.json()
        return new HttpResponse(null, { status: HttpStatusCode.Created })
      }),
    )
    creator.form.price = '2500'
    creator.form.brandId = catalogBrands[1].id
    await flushPromises()
    // Act
    const outcome = await creator.create()
    // Assert
    expect(outcome).toEqual({ kind: 'created' })
    expect(sent).toEqual({
      name: validValues.itemName,
      description: validValues.itemDescription,
      price: 2500,
      productCode: validValues.productCode,
      catalogCategoryId: catalogCategories[0].id,
      catalogBrandId: catalogBrands[1].id,
    })
  })

  it('サーバーエラー_failedを返す', async () => {
    // Arrange
    const creator = await setupCreator()
    server.use(
      http.post(
        '/api/catalog-items',
        () => new HttpResponse(null, { status: HttpStatusCode.InternalServerError }),
      ),
    )
    // Act
    const outcome = await creator.create()
    // Assert
    expect(outcome).toEqual({ kind: 'failed' })
  })
})
