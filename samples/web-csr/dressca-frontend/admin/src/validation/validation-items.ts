import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'

// バリデーション定義（一元化）
export const validationItems = {
  email: z.string().regex(/^[^\s@]+@[^\s@]+\.[^\s@]+$/, 'メールアドレスの形式で入力してください。'),
  required: (message = '値を入力してください') =>
    z.string().refine((val) => !!val && val.trim().replace(/\u3000/g, '') !== '', message),
}

/**
 * カタログアイテムのバリデーションを定義する Zod スキーマです。
 */
export const catalogItemZodSchema = z.object({
  itemName: z
    .string()
    .pipe(validationItems.required('アイテム名は必須です。'))
    .pipe(z.string().max(256, '256文字以下で入力してください。')),
  itemDescription: z
    .string()
    .pipe(validationItems.required('説明は必須です。'))
    .pipe(z.string().max(1024, '1024文字以下で入力してください。')),
  price: z
    .string()
    .pipe(validationItems.required('単価は必須です。'))
    .pipe(z.string().regex(/^[1-9]\d*$/, '1以上の整数を半角数字で入力してください')),
  productCode: z
    .string()
    .pipe(validationItems.required('商品コードは必須です。'))
    .pipe(
      z
        .string()
        .max(128, '128文字以下で入力してください。')
        .regex(/^[0-9a-zA-Z]+$/, '半角英数字で入力してください。'),
    ),
})

export type CatalogItemFormValues = z.infer<typeof catalogItemZodSchema>

/**
 * カタログアイテムのバリデーションを定義する型付きのスキーマです。
 */
export const catalogItemSchema = toTypedSchema(catalogItemZodSchema)
