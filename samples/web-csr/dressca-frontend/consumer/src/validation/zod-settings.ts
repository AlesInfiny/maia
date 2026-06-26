import * as zod from 'zod'
import { i18n } from '@/locales/i18n'

/**
 * カスタムエラーマップ
 * @param issue Zodのエラー情報
 * @param ctx コンテキスト情報
 * @returns カスタムエラーメッセージ
 */
export const customErrorMap: zod.ZodErrorMap = (issue, ctx) => {
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
