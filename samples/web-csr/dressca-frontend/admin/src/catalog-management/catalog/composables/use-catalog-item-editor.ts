import { computed, reactive, ref, shallowRef, toValue, watch } from 'vue'
import type { ComputedRef, MaybeRefOrGetter } from 'vue'
import { useForm } from 'vee-validate'
import { ConflictError, NotFoundError } from '@/system-common/error-handler/custom-error'
import { useCustomErrorHandler } from '@/system-common/error-handler/custom-error-handler'
import type {
  GetCatalogBrandsResponse,
  GetCatalogCategoriesResponse,
  GetCatalogItemResponse,
} from '@/system-common/generated/api-client'
import {
  deleteCatalogItem,
  fetchCategoriesAndBrands,
  fetchItem,
  updateCatalogItem,
} from '../services/catalog-service'
import { catalogItemTypedSchema, type CatalogItemFormValues } from '../validation/validation-items'

/**
 * 編集対象のカタログアイテムの読み込みの状態です。
 */
export type CatalogItemEditorStatus = 'loading' | 'ready' | 'notFound' | 'failed'

/**
 * 編集前のカタログアイテムの内容です。
 */
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

/**
 * カテゴリやブランドの選択肢です。
 */
export interface CatalogOption {
  id: string
  name: string
}

/**
 * カタログアイテムの編集フォームです。
 * 入力項目は双方向にバインドできます。
 */
export interface CatalogItemForm {
  itemName: string
  itemDescription: string
  price: string
  productCode: string
  categoryId: string
  brandId: string
  /** 入力項目ごとの検証エラーのメッセージです。 */
  readonly errors: Partial<Record<keyof CatalogItemFormValues, string | undefined>>
  /** すべての入力項目が検証に合格しているかどうかです。 */
  readonly isValid: boolean
}

/**
 * カタログアイテムの更新の結果です。
 * `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。
 */
export type UpdateOutcome =
  | { kind: 'updated' }
  | { kind: 'conflict' }
  | { kind: 'notFound' }
  | { kind: 'failed' }
  | { kind: 'canceled' }

/**
 * カタログアイテムの削除の結果です。
 * `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。
 */
export type RemoveOutcome =
  | { kind: 'removed' }
  | { kind: 'conflict' }
  | { kind: 'notFound' }
  | { kind: 'failed' }
  | { kind: 'canceled' }

/**
 * カタログアイテムの編集のユースケースです。
 */
export interface CatalogItemEditor {
  /** 編集対象のアイテムの読み込みの状態です。 */
  status: ComputedRef<CatalogItemEditorStatus>
  /** 編集前のアイテムの内容です。読み込みが完了するまでは undefined です。 */
  current: ComputedRef<CatalogItemSnapshot | undefined>
  /** 編集フォームです。 */
  form: CatalogItemForm
  /** カテゴリの選択肢です。 */
  categories: ComputedRef<CatalogOption[]>
  /** ブランドの選択肢です。 */
  brands: ComputedRef<CatalogOption[]>
  /**
   * 編集フォームの内容でアイテムを更新します。
   * 競合した場合は最新の行バージョンを取り込むので、入力中の値のまま再度更新できます。
   */
  update: () => Promise<UpdateOutcome>
  /**
   * アイテムを削除します。
   * 競合した場合は最新の行バージョンを取り込むので、再度削除できます。
   */
  remove: () => Promise<RemoveOutcome>
}

type FetchItemResult = 'fetched' | 'notFound' | 'failed' | 'canceled'
type UnexpectedErrorOutcome = { kind: 'failed' } | { kind: 'canceled' }
type WriteFailureOutcome = { kind: 'conflict' } | { kind: 'notFound' } | UnexpectedErrorOutcome

/**
 * API のレスポンスを選択肢に変換します。
 * @param response カテゴリまたはブランドのレスポンス。
 * @returns 選択肢。
 */
function toOption(
  response: GetCatalogCategoriesResponse | GetCatalogBrandsResponse,
): CatalogOption {
  return { id: response.id, name: response.name }
}

/**
 * API のレスポンスを編集前のアイテムの内容に変換します。
 * 排他制御の行バージョンは含めません。
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
 * 排他制御の行バージョンと API のエラーの分類は、このコンポーザブルの内部で扱います。
 * @param itemId 編集対象のアイテムの ID 。変化すると読み込み直します。
 * @returns カタログアイテムの編集のユースケース。
 */
export function useCatalogItemEditor(itemId: MaybeRefOrGetter<string>): CatalogItemEditor {
  const handleErrorAsync = useCustomErrorHandler()

  const status = ref<CatalogItemEditorStatus>('loading')
  const item = shallowRef<GetCatalogItemResponse>()
  const categories = shallowRef<CatalogOption[]>([])
  const brands = shallowRef<CatalogOption[]>([])

  const { errors, meta, values, defineField, setValues } = useForm<CatalogItemFormValues>({
    validationSchema: catalogItemTypedSchema,
    initialValues: {
      itemName: '',
      itemDescription: '',
      price: '',
      productCode: '',
    },
  })
  const [itemName] = defineField('itemName')
  const [itemDescription] = defineField('itemDescription')
  const [price] = defineField('price')
  const [productCode] = defineField('productCode')
  const categoryId = ref('')
  const brandId = ref('')

  const form: CatalogItemForm = reactive({
    itemName,
    itemDescription,
    price,
    productCode,
    categoryId,
    brandId,
    errors,
    isValid: computed(() => meta.value.valid),
  })

  /**
   * 想定外のエラーを共通のエラー処理に渡します。
   * 共通のエラー処理が扱えないエラーは、そのまま上位へ送出されます。
   * @param error 発生したエラー。
   * @returns 通信が中断された場合は canceled 、それ以外は failed 。
   */
  async function handleUnexpectedError(error: unknown): Promise<UnexpectedErrorOutcome> {
    let outcome: UnexpectedErrorOutcome = { kind: 'canceled' }
    await handleErrorAsync(error, () => {
      outcome = { kind: 'failed' }
    })
    return outcome
  }

  /**
   * 編集フォームの内容を、アイテムの内容で置き換えます。
   * @param target アイテムの内容。
   */
  function resetForm(target: GetCatalogItemResponse) {
    setValues({
      itemName: target.name,
      itemDescription: target.description,
      price: target.price.toString(),
      productCode: target.productCode,
    })
    categoryId.value = target.catalogCategoryId
    brandId.value = target.catalogBrandId
  }

  /**
   * 最新のアイテムを取得し、編集前の内容と行バージョンを置き換えます。
   * 編集フォームの内容は変更しません。
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
      return (await handleUnexpectedError(error)).kind
    }
  }

  /**
   * 書き込みの失敗を結果に変換します。
   * 競合した場合は、再度操作できるよう最新の行バージョンを取り込みます。
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
    return handleUnexpectedError(error)
  }

  /**
   * 編集対象のアイテムを取得します。
   * 読み込みが完了していない場合は、プログラムの誤りとして例外を送出します。
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
      const [fetchedCategories, fetchedBrands] = await fetchCategoriesAndBrands()
      categories.value = fetchedCategories.map(toOption)
      brands.value = fetchedBrands.map(toOption)
    } catch (error) {
      if ((await handleUnexpectedError(error)).kind === 'failed') {
        status.value = 'failed'
      }
      return
    }

    const result = await fetchLatestItem(id)
    if (result === 'fetched') {
      resetForm(getLoadedItem())
      status.value = 'ready'
    } else if (result !== 'canceled') {
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
        values.itemName,
        values.itemDescription,
        Number(values.price),
        values.productCode,
        categoryId.value,
        brandId.value,
        target.rowVersion,
        target.isDeleted,
      )
    } catch (error) {
      return toWriteFailure(error, target.id)
    }

    // 更新後の内容と行バージョンを取り込みます。
    // 取り込みに失敗しても更新は完了しているため、結果は updated とします。
    if ((await fetchLatestItem(target.id)) === 'fetched') {
      resetForm(getLoadedItem())
    }
    return { kind: 'updated' }
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
