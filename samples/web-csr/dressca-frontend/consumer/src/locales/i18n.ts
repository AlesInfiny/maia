import { createI18n } from 'vue-i18n';
import messageListEN from '@/locales/en/messageList_en.json';
import messageListJP from '@/locales/ja/messageList_jp.json';
import validationTextListJP from '@/locales/ja/validationTextList_jp.json';
import validationTextListEN from '@/locales/en/validationTextList_en.json';
import { languageHelper } from '@/shared/helpers/languageHelper';

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
  locale: languageHelper().toConfigureLocale(),
  messages: langPackage,
});

export { i18n };
