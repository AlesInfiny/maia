import { useI18n } from 'vue-i18n';
import { setLocale } from 'yup';

export function configureYup(): void {
  const { t } = useI18n({ useScope: 'global' });
  setLocale({
    mixed: {
      required: t('validationTextList.required'),
    },
    string: {
      email: t('validationTextList.email'),
    },
  });
}
