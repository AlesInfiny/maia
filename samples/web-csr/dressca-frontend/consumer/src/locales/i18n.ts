import { createI18n } from 'vue-i18n';
import messageListEN from '@/locales/en/messageList_en.json';
import messageListJA from '@/locales/ja/messageList_ja.json';
import validationTextListJA from '@/locales/ja/validationTextList_ja.json';
import validationTextListEN from '@/locales/en/validationTextList_en.json';

const langPackage = {
  ja: {
    ...messageListJA,
    ...validationTextListJA,
  },
  en: {
    ...messageListEN,
    ...validationTextListEN,
  },
};

const i18n = createI18n({
  legacy: false,
  locale: window.navigator.language,
  fallbackLocale: 'en',
  messages: langPackage,
});

export { i18n };
