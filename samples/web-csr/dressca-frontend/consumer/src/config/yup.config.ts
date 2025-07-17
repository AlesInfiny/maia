import { i18n } from '@/locales/i18n'
import { setLocale } from 'yup'

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
