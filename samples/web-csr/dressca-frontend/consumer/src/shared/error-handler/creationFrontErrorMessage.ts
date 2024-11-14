import { i18n } from '@/locales/i18n';

const { t } = i18n.global;

export const errorMessageFormat = (id: string, values: string[]) => {
  const template = t(id);
  return template.replace(/\[(\d+)\]/g, (match, index) => values[index] || '');
};
