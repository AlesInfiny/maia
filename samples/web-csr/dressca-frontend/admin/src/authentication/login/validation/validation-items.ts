import { required } from '@/system-common/validation/validation-rules'

/**
 * Zod を利用したバリデーションルールを返します。
 * @returns バリデーションルールのオブジェクト
 */
export function validationItems() {
  return {
    required,
    requiredEmail: (requiredMessage: string = '必須項目です。') =>
      required(requiredMessage).email('メールアドレスの形式で入力してください。'),
  }
}
