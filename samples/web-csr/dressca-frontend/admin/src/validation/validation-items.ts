import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'

// 必須バリデーション関数
const required = (message: string) => z.string().trim().min(1, message)

/**
 * Zod を利用したバリデーションルールを返します。
 * @returns バリデーションルールのオブジェクト
 */
export function validationItems() {
  return {
    email: z.string().email('メールアドレスの形式で入力してください。'),
    required: (requiredMessage: string) => required(requiredMessage),
    requiredEmail: (requiredMessage: string = '必須項目です。') =>
      required(requiredMessage).email('メールアドレスの形式で入力してください。'),
  }
}

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
 * カタログアイテムのバリデーションを定義する型付きのスキーマです。
 */
export const catalogItemSchema = toTypedSchema(catalogItemZodSchema)
