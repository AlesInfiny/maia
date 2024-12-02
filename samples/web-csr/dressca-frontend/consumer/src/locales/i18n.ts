import { createI18n } from 'vue-i18n';
import messageListEN from '@/locales/en/messageList_en.json';
import messageListJP from '@/locales/ja/messageList_ja.json';
import validationTextListJP from '@/locales/ja/validationTextList_ja.json';
import validationTextListEN from '@/locales/en/validationTextList_en.json';

const langPackage = {
  ja: {
    ...messageListJP,
    ...validationTextListJP,
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
