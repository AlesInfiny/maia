import { i18n } from '@/locales/i18n'
import { setLocale } from 'yup'

/**
 * Yup のバリデーションメッセージを i18n を用いて翻訳後のメッセージに変換するよう設定します。
 * アプリケーション起動時に呼び出してください。
 */
export function configureYup(): void {
  const { t } = i18n.global
  setLocale({
    mixed: {
      required: t('required'),
    },
    string: {
      email: t('email'),
    },
  })
}
