import { computed, ref, shallowRef, toValue, watch } from 'vue'
import type { ComputedRef, MaybeRefOrGetter } from 'vue'
import { ConflictError, NotFoundError } from '@/system-common/error-handler/custom-error'
import {
  useCommonErrorOutcome,
  type CommonErrorOutcome,
} from '@/system-common/error-handler/common-error-outcome'
import type { GetCatalogItemResponse } from '@/system-common/generated/api-client'
import {
  fetchCatalogItemOptions,
  deleteCatalogItem,
  fetchItem,
  updateCatalogItem,
  type CatalogOption,
} from '../services/catalog-service'
import { useCatalogItemForm, type CatalogItemForm } from './use-catalog-item-form'

export type CatalogItemEditorStatus = 'loading' | 'ready' | 'notFound' | 'failed'

export interface CatalogItemSnapshot {
  id: string
  name: string
  description: string
  price: number
  productCode: string
  categoryId: string
  brandId: string
  assetCodes: string[] | undefined
}

/** `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。 */
export type UpdateOutcome =
  | { kind: 'updated' }
  | { kind: 'conflict' }
  | { kind: 'notFound' }
  | CommonErrorOutcome

/** `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。 */
export type RemoveOutcome =
  | { kind: 'removed' }
  | { kind: 'conflict' }
  | { kind: 'notFound' }
  | CommonErrorOutcome

export interface CatalogItemEditor {
  status: ComputedRef<CatalogItemEditorStatus>
  /** 読み込みが完了するまでは undefined です。 */
  current: ComputedRef<CatalogItemSnapshot | undefined>
  form: CatalogItemForm
  categories: ComputedRef<CatalogOption[]>
  brands: ComputedRef<CatalogOption[]>
  /** 競合した場合は最新の行バージョンを取り込むので、入力中の値のまま再度更新できます。 */
  update: () => Promise<UpdateOutcome>
  /** 競合した場合は最新の行バージョンを取り込むので、再度削除できます。 */
  remove: () => Promise<RemoveOutcome>
}

type FetchItemResult = 'fetched' | 'notFound' | CommonErrorOutcome['kind']
type WriteFailureOutcome = { kind: 'conflict' } | { kind: 'notFound' } | CommonErrorOutcome

/**
 * API のレスポンスを編集前のアイテムの内容に変換します。
 * @param item カタログアイテムのレスポンス。
 * @returns 編集前のアイテムの内容。
 */
function toSnapshot(item: GetCatalogItemResponse): CatalogItemSnapshot {
  return {
    id: item.id,
    name: item.name,
    description: item.description,
    price: item.price,
    productCode: item.productCode,
    categoryId: item.catalogCategoryId,
    brandId: item.catalogBrandId,
    assetCodes: item.assetCodes,
  }
}

/**
 * カタログアイテムを編集するユースケースを提供します。
 * 呼び出すと、対象のアイテムとカテゴリ、ブランドの読み込みを開始します。
 * @param itemId 編集対象のアイテムの ID 。変化すると読み込み直します。
 * @returns カタログアイテムの編集のユースケース。
 */
export function useCatalogItemEditor(itemId: MaybeRefOrGetter<string>): CatalogItemEditor {
  const handleCommonError = useCommonErrorOutcome()

  const status = ref<CatalogItemEditorStatus>('loading')
  const item = shallowRef<GetCatalogItemResponse>()
  const categories = shallowRef<CatalogOption[]>([])
  const brands = shallowRef<CatalogOption[]>([])
  const { form, reset } = useCatalogItemForm()

  /**
   * 編集フォームの内容を、アイテムの内容で置き換えます。
   * @param target アイテムの内容。
   */
  function resetForm(target: GetCatalogItemResponse) {
    reset({
      itemName: target.name,
      itemDescription: target.description,
      price: target.price.toString(),
      productCode: target.productCode,
      categoryId: target.catalogCategoryId,
      brandId: target.catalogBrandId,
    })
  }

  /**
   * 最新のアイテムを取得し、編集前の内容と行バージョンを置き換えます。
   * @param id アイテムの ID 。
   * @returns 取得の結果。
   */
  async function fetchLatestItem(id: string): Promise<FetchItemResult> {
    try {
      item.value = await fetchItem(id)
      return 'fetched'
    } catch (error) {
      if (error instanceof NotFoundError) {
        return 'notFound'
      }
      return (await handleCommonError(error)).kind
    }
  }

  /**
   * 書き込みの失敗を結果に変換します。
   * @param error 発生したエラー。
   * @param id アイテムの ID 。
   * @returns 書き込みの失敗の結果。
   */
  async function toWriteFailure(error: unknown, id: string): Promise<WriteFailureOutcome> {
    if (error instanceof NotFoundError) {
      return { kind: 'notFound' }
    }
    if (error instanceof ConflictError) {
      const result = await fetchLatestItem(id)
      return result === 'notFound' ? { kind: 'notFound' } : { kind: 'conflict' }
    }
    return handleCommonError(error)
  }

  /**
   * 編集対象のアイテムを取得します。
   * @returns 編集対象のアイテム。
   */
  function getLoadedItem(): GetCatalogItemResponse {
    if (!item.value) {
      throw new Error('編集対象のカタログアイテムの読み込みが完了していません。')
    }
    return item.value
  }

  /**
   * カテゴリ、ブランド、編集対象のアイテムを読み込みます。
   * @param id アイテムの ID 。
   */
  async function load(id: string) {
    status.value = 'loading'
    try {
      const options = await fetchCatalogItemOptions()
      categories.value = options.categories
      brands.value = options.brands
    } catch (error) {
      if ((await handleCommonError(error)).kind === 'failed') {
        status.value = 'failed'
      }
      return
    }

    const result = await fetchLatestItem(id)
    if (result === 'fetched') {
      resetForm(getLoadedItem())
      status.value = 'ready'
    } else if (result === 'notFound' || result === 'failed') {
      status.value = result
    }
  }

  /**
   * 編集フォームの内容でアイテムを更新し、更新後の内容を取り込みます。
   * @returns 更新の結果。
   */
  async function update(): Promise<UpdateOutcome> {
    const target = getLoadedItem()
    try {
      await updateCatalogItem(
        target.id,
        form.itemName,
        form.itemDescription,
        Number(form.price),
        form.productCode,
        form.categoryId,
        form.brandId,
        target.rowVersion,
        target.isDeleted,
      )
    } catch (error) {
      return toWriteFailure(error, target.id)
    }

    await reloadFormIfFetched(target.id)
    return { kind: 'updated' }
  }

  /**
   * 最新のアイテムを取得できた場合だけ、編集フォームをその内容に戻します。
   * @param id アイテムの ID 。
   */
  async function reloadFormIfFetched(id: string) {
    if ((await fetchLatestItem(id)) === 'fetched') {
      resetForm(getLoadedItem())
    }
  }

  /**
   * アイテムを削除します。
   * @returns 削除の結果。
   */
  async function remove(): Promise<RemoveOutcome> {
    const target = getLoadedItem()
    try {
      await deleteCatalogItem(target.id, target.rowVersion)
    } catch (error) {
      return toWriteFailure(error, target.id)
    }
    return { kind: 'removed' }
  }

  // watch のコールバックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  watch(() => toValue(itemId), load, { immediate: true })

  return {
    status: computed(() => status.value),
    current: computed(() => (item.value ? toSnapshot(item.value) : undefined)),
    form,
    categories: computed(() => categories.value),
    brands: computed(() => brands.value),
    update,
    remove,
  }
}
