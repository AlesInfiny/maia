import { computed, onMounted, ref, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import { useUnexpectedErrorOutcome } from '@/system-common/error-handler/unexpected-error-outcome'
import { postCatalogItem } from '../services/catalog-service'
import type { CatalogItemFormValues } from '../validation/validation-items'
import {
  fetchCatalogItemOptions,
  useCatalogItemForm,
  type CatalogItemForm,
  type CatalogOption,
} from './use-catalog-item-form'

/**
 * 追加画面で使用するカテゴリとブランドの読み込みの状態です。
 */
export type CatalogItemCreatorStatus = 'loading' | 'ready' | 'failed'

/**
 * カタログアイテムの追加の結果です。
 * `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。
 */
export type CreateOutcome = { kind: 'created' } | { kind: 'failed' } | { kind: 'canceled' }

/**
 * カタログアイテムの追加のユースケースです。
 */
export interface CatalogItemCreator {
  /** カテゴリとブランドの読み込みの状態です。 */
  status: ComputedRef<CatalogItemCreatorStatus>
  /** 追加するアイテムの入力フォームです。 */
  form: CatalogItemForm
  /** カテゴリの選択肢です。 */
  categories: ComputedRef<CatalogOption[]>
  /** ブランドの選択肢です。 */
  brands: ComputedRef<CatalogOption[]>
  /** 入力フォームの内容でアイテムをカタログに追加します。 */
  create: () => Promise<CreateOutcome>
}

/**
 * カタログアイテムを追加するユースケースを提供します。
 * コンポーネントのマウント後に、カテゴリとブランドを読み込み、それぞれ先頭の選択肢を選択します。
 * @param initialValues 入力項目の初期値。
 * @returns カタログアイテムの追加のユースケース。
 */
export function useCatalogItemCreator(
  initialValues: Partial<CatalogItemFormValues> = {},
): CatalogItemCreator {
  const handleUnexpectedError = useUnexpectedErrorOutcome()

  const status = ref<CatalogItemCreatorStatus>('loading')
  const categories = shallowRef<CatalogOption[]>([])
  const brands = shallowRef<CatalogOption[]>([])
  const { form } = useCatalogItemForm(initialValues)

  /**
   * カテゴリとブランドを読み込み、それぞれ先頭の選択肢を選択します。
   */
  async function load() {
    try {
      const options = await fetchCatalogItemOptions()
      categories.value = options.categories
      brands.value = options.brands
      form.categoryId = options.categories[0]?.id ?? ''
      form.brandId = options.brands[0]?.id ?? ''
      status.value = 'ready'
    } catch (error) {
      if ((await handleUnexpectedError(error)).kind === 'failed') {
        status.value = 'failed'
      }
    }
  }

  /**
   * 入力フォームの内容でアイテムをカタログに追加します。
   * @returns 追加の結果。
   */
  async function create(): Promise<CreateOutcome> {
    try {
      await postCatalogItem(
        form.itemName,
        form.itemDescription,
        Number(form.price),
        form.productCode,
        form.categoryId,
        form.brandId,
      )
      return { kind: 'created' }
    } catch (error) {
      return handleUnexpectedError(error)
    }
  }

  // ライフサイクルフックが返す Promise の拒否は、 Vue のエラーハンドラーに渡されます。
  onMounted(load)

  return {
    status: computed(() => status.value),
    form,
    categories: computed(() => categories.value),
    brands: computed(() => brands.value),
    create,
  }
}
