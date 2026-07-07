import { createI18n } from 'vue-i18n'
import messageListEN from '@/locales/en/messageList_en.json'
import messageListJA from '@/locales/ja/messageList_ja.json'
import validationTextListJA from '@/locales/ja/validationTextList_ja.json'
import validationTextListEN from '@/locales/en/validationTextList_en.json'

const langPackage = {
  ja: {
    ...messageListJA,
    ...validationTextListJA,
  },
  en: {
    ...messageListEN,
    ...validationTextListEN,
  },
}

type SupportedLocale = 'ja' | 'en'

/**
 * ブラウザの言語設定を取得して、サポートされているロケールにマッピングします。
 * @returns サポートされているロケール（'ja' または 'en'）
 */
function getLocale(): SupportedLocale {
  const lang = window.navigator.language.split('-')[0]
  return lang === 'ja' ? 'ja' : 'en'
}

type MessageSchema = typeof langPackage.ja
const i18n = createI18n<[MessageSchema], 'ja' | 'en', false>({
  legacy: false,
  locale: getLocale(),
  fallbackLocale: 'en',
  messages: langPackage,
})

export { i18n }
