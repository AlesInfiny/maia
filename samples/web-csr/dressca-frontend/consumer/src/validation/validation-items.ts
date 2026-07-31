import { z } from 'zod'
import { i18n } from '@/locales/i18n'

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
