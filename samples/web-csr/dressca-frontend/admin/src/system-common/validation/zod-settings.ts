import { type ZodErrorMap, ZodIssueCode } from 'zod'

// 必須入力項目の最小文字数
const RequiredMinLength = 1

/**
 * カスタムエラーマップ
 * @param issue Zodのエラー情報
 * @param ctx コンテキスト情報
 * @returns カスタムエラーメッセージ
 */
export const customErrorMap: ZodErrorMap = (issue, ctx) => {
  switch (issue.code) {
    // 型に誤り
    case ZodIssueCode.invalid_type:
      return { message: '正しい形式で入力してください。' }

    case ZodIssueCode.too_big:
      return { message: `${issue.maximum}文字以下で入力してください。` }

    case ZodIssueCode.too_small:
      if (issue.minimum === RequiredMinLength) {
        return { message: '必須項目です。' }
      }
      return { message: `${issue.minimum}文字以上で入力してください。` }

    // 文字列のフォーマット違反
    case ZodIssueCode.invalid_string:
      return { message: '正しい形式で入力してください。' }
  }

  // デフォルトのメッセージを返す
  return { message: ctx.defaultError }
}
