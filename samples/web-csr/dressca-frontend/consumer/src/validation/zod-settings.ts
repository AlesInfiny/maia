import { type ZodErrorMap, ZodIssueCode } from 'zod'
import { i18n } from '@/locales/i18n'

// 必須入力項目の最小文字数
const RequiredMinLength = 1

/**
 * カスタムエラーマップ
 * @param issue Zodのエラー情報
 * @param ctx コンテキスト情報
 * @returns カスタムエラーメッセージ
 */
export const customErrorMap: ZodErrorMap = (issue, ctx) => {
  const { t } = i18n.global
  switch (issue.code) {
    // 型に誤り
    case ZodIssueCode.invalid_type:
      return { message: t('invalidFormat') }

    case ZodIssueCode.too_big:
      return { message: t('tooBig', [issue.maximum]) }

    case ZodIssueCode.too_small:
      if (issue.minimum === RequiredMinLength) {
        return { message: t('required') }
      }
      return { message: t('tooSmall', [issue.minimum]) }

    // 文字列のフォーマット違反
    case ZodIssueCode.invalid_string:
      return { message: t('invalidFormat') }
  }

  // デフォルトのメッセージを返す
  return { message: ctx.defaultError }
}
