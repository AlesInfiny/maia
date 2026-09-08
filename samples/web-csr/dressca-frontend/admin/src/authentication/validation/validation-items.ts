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
