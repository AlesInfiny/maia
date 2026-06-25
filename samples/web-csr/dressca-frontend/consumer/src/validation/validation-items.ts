import { z } from 'zod'
import { i18n } from '@/locales/i18n'
import * as zod from 'zod'

// 必須バリデーション関数
const required = (message: string) => z.string().trim().min(1, message)

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
    email: z.string().email(t('email')),
    required: (requiredMessage: string = t('required')) => required(requiredMessage),
    requiredEmail: (requiredMessage: string = t('required')) =>
      required(requiredMessage).email(t('email')),
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
  const { t } = i18n.global
  switch (issue.code) {
    // 型に誤り
    case zod.ZodIssueCode.invalid_type:
      return { message: t('invalidFormat') }

    case zod.ZodIssueCode.too_big:
      return { message: t('tooBig', [issue.maximum]) }

    case zod.ZodIssueCode.too_small:
      if (issue.minimum === 1) {
        return { message: t('required') }
      }
      return { message: t('tooSmall', [issue.minimum]) }

    // 文字列のフォーマット違反
    case zod.ZodIssueCode.invalid_string:
      return { message: t('invalidFormat') }
  }

  // デフォルトのメッセージを返す
  return { message: ctx.defaultError }
}

zod.setErrorMap(customErrorMap)
