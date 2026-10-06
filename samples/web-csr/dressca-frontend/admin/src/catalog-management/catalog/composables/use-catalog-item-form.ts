import { computed, reactive, ref } from 'vue'
import { useForm } from 'vee-validate'
import { fetchCategoriesAndBrands } from '../services/catalog-service'
import { catalogItemTypedSchema, type CatalogItemFormValues } from '../validation/validation-items'

/**
 * カテゴリやブランドの選択肢です。
 */
export interface CatalogOption {
  id: string
  name: string
}

/**
 * カテゴリとブランドの選択肢です。
 */
export interface CatalogItemOptions {
  categories: CatalogOption[]
  brands: CatalogOption[]
}

/**
 * カタログアイテムの入力フォームです。
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
 * 入力フォームに設定する値です。
 */
export type CatalogItemFormInput = Pick<
  CatalogItemForm,
  'itemName' | 'itemDescription' | 'price' | 'productCode' | 'categoryId' | 'brandId'
>

/**
 * カテゴリとブランドの選択肢を取得します。
 * @returns カテゴリとブランドの選択肢。
 */
export async function fetchCatalogItemOptions(): Promise<CatalogItemOptions> {
  const [categories, brands] = await fetchCategoriesAndBrands()
  return {
    categories: categories.map(({ id, name }) => ({ id, name })),
    brands: brands.map(({ id, name }) => ({ id, name })),
  }
}

/**
 * カタログアイテムの入力フォームを生成します。
 * vee-validate を使用するため、コンポーネントの setup の中で呼び出します。
 * @param initialValues 入力項目の初期値。
 * @returns 入力フォームと、入力フォームの値を置き換える関数。
 */
export function useCatalogItemForm(initialValues: Partial<CatalogItemFormValues> = {}): {
  form: CatalogItemForm
  reset: (input: CatalogItemFormInput) => void
} {
  const { errors, meta, defineField, setValues } = useForm<CatalogItemFormValues>({
    validationSchema: catalogItemTypedSchema,
    initialValues: {
      itemName: '',
      itemDescription: '',
      price: '',
      productCode: '',
      ...initialValues,
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
   * 入力フォームの値を置き換えます。
   * @param input 設定する値。
   */
  function reset(input: CatalogItemFormInput) {
    setValues({
      itemName: input.itemName,
      itemDescription: input.itemDescription,
      price: input.price,
      productCode: input.productCode,
    })
    categoryId.value = input.categoryId
    brandId.value = input.brandId
  }

  return { form, reset }
}
