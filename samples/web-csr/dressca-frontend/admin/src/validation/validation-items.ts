import { toTypedSchema } from '@vee-validate/zod'
import { z } from 'zod'
import * as zod from 'zod'

// undefined を空文字に変換し、前後の空白を削除する基底スキーマ（共通処理）
export const stringSchema = z
  .string()
  .optional()
  .transform((val) => val?.trim() ?? '')

// 必須バリデーション関数
const required = (message: string) => z.string().min(1, message)

// バリデーション定義（一元化）
export const validationItems = {
  email: stringSchema.pipe(z.string().email('メールアドレスの形式で入力してください。')),
  required: (requiredMessage: string) => stringSchema.pipe(required(requiredMessage)),
  requiredEmail: (requiredMessage: string = '必須項目です。') =>
    stringSchema.pipe(required(requiredMessage).email('メールアドレスの形式で入力してください。')),
}

/**
 * カタログアイテムのバリデーションを定義する Zod スキーマです。
 */
export const catalogItemZodSchema = z.object({
  itemName: stringSchema.pipe(required('アイテム名は必須です。').max(256)),
  itemDescription: stringSchema.pipe(required('説明は必須です。').max(1024)),
  price: stringSchema.pipe(
    required('単価は必須です。').regex(/^[1-9]\d*$/, '1以上の整数を半角数字で入力してください'),
  ),
  productCode: stringSchema.pipe(
    required('商品コードは必須です。')
      .max(128)
      .regex(/^[0-9a-zA-Z]+$/, '半角英数字で入力してください。'),
  ),
})

export type CatalogItemFormValues = z.infer<typeof catalogItemZodSchema>

/**
 * カタログアイテムのバリデーションを定義する型付きのスキーマです。
 */
export const catalogItemSchema = toTypedSchema(catalogItemZodSchema)

/**
 * カスタムエラーマップ
 * @param issue Zodのエラー情報
 * @param ctx コンテキスト情報
 * @returns カスタムエラーメッセージ
 */
const customErrorMap: zod.ZodErrorMap = (issue, ctx) => {
  switch (issue.code) {
    // 型に誤り
    case zod.ZodIssueCode.invalid_type:
      return { message: '正しい形式で入力してください。' }

    case zod.ZodIssueCode.too_big:
      return { message: `${issue.maximum}文字以下で入力してください。` }

    case zod.ZodIssueCode.too_small:
      if (issue.minimum === 1) {
        return { message: '必須項目です。' }
      }
      return { message: `${issue.minimum}文字以上で入力してください。` }

    // 文字列のフォーマット違反
    case zod.ZodIssueCode.invalid_string:
      return { message: '正しい形式で入力してください。' }
  }

  // デフォルトのメッセージを返す
  return { message: ctx.defaultError }
}

zod.setErrorMap(customErrorMap)
