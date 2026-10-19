import { computed, onMounted, ref, shallowRef } from 'vue'
import type { ComputedRef } from 'vue'
import {
  useCommonErrorOutcome,
  type CommonErrorOutcome,
} from '@/system-common/error-handler/common-error-outcome'
import {
  fetchCatalogItemOptions,
  postCatalogItem,
  type CatalogOption,
} from '../services/catalog-service'
import type { CatalogItemFormValues } from '../validation/validation-items'
import { useCatalogItemForm, type CatalogItemForm } from './use-catalog-item-form'

export type CatalogItemCreatorStatus = 'loading' | 'ready' | 'failed'

/** `canceled` は、ログアウトなどの利用者の操作で通信が中断されたことを表します。 */
export type CreateOutcome = { kind: 'created' } | CommonErrorOutcome

export interface CatalogItemCreator {
  status: ComputedRef<CatalogItemCreatorStatus>
  form: CatalogItemForm
  categories: ComputedRef<CatalogOption[]>
  brands: ComputedRef<CatalogOption[]>
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
  const handleCommonError = useCommonErrorOutcome()

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
      if ((await handleCommonError(error)).kind === 'failed') {
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
      return handleCommonError(error)
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
