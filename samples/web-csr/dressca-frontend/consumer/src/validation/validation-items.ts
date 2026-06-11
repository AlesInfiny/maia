import { z } from 'zod'
import { i18n } from '@/locales/i18n'
import * as zod from 'zod'

// undefined を空文字に変換し、前後の空白を削除する基底スキーマ（共通処理）
const stringSchema = z
  .string()
  .optional()
  .transform((val) => val?.trim() ?? '')

// 必須バリデーション関数
const required = (message: string) => z.string().min(1, message)

/**
 * Zod を利用したバリデーションルールを返します。
 * メッセージは生成時点のロケールで確定するため、ロケール変更後は再生成が必要です。
 * @returns バリデーションルールのオブジェクト
 * @example
 * const rules = ValidationItems()
 * rules.email.safeParse('test@example.com').success // true
 * rules.email.safeParse('invalid').success // false
 */
export function ValidationItems() {
  const { t } = i18n.global
  const validationItems = {
    email: stringSchema.pipe(z.string().email(t('email'))),
    required: (requiredMessage: string = t('required')) =>
      stringSchema.pipe(required(requiredMessage)),
    requiredEmail: (requiredMessage: string = t('required')) =>
      stringSchema.pipe(required(requiredMessage).email(t('email'))),
  }
  return validationItems
}

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
