import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'
import { required } from '@/system-common/validation/validation-rules'

/**
 * カタログアイテムのバリデーションを定義する Zod スキーマです。
 */
export const catalogItemZodSchema = z.object({
  itemName: required('アイテム名は必須です。').max(256),
  itemDescription: required('説明は必須です。').max(1024),
  price: required('単価は必須です。').regex(
    /^[1-9]\d*$/,
    '1以上の整数を半角数字で入力してください',
  ),
  productCode: required('商品コードは必須です。')
    .max(128)
    .regex(/^[0-9a-zA-Z]+$/, '半角英数字で入力してください。'),
})

export type CatalogItemFormValues = z.infer<typeof catalogItemZodSchema>

/**
 * カタログアイテムのバリデーションを定義する、vee-validate 用の型付きスキーマです。
 */
export const catalogItemTypedSchema = toTypedSchema(catalogItemZodSchema)
