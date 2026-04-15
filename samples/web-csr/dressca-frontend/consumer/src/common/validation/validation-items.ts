import * as yup from 'yup'

/**
 * Yup を利用したバリデーションルールを返します。
 * 現在はメールアドレス形式のチェックのみを提供しています。
 * @returns バリデーションルールのオブジェクト
 * @example
 * const rules = ValidationItems()
 * await rules.email.validate('test@example.com') // OK
 * await rules.email.validate('invalid') // エラー
 */
export function ValidationItems() {
  const validationItems = {
    email: yup.string().email(),
  }
  return validationItems
}
