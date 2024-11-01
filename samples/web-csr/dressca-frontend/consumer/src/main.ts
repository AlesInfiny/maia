import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import uiTextListJP from '@/locales/ja/uiTextList_jp.json';
import messageListJP from '@/locales/ja/messageList_jp.json';
import validationTextListJP from '@/locales/ja/validationTextList_jp.json';
import uiTextListEN from '@/locales/en/uiTextList_en.json';
import { createI18n } from 'vue-i18n';
import App from './App.vue';
import { router } from './router';

import '@/assets/base.css';
import { languageHelper } from './shared/helpers/languageHelper';

const app = createApp(App);

const langPackage = {
  ja: {
    ...uiTextListJP,
    ...messageListJP,
    ...validationTextListJP,
  },
  en: {
    ...uiTextListEN,
  },
};

const i18n = createI18n({
  legacy: false,
  locale: languageHelper().toConfigureLocale(),
  messages: langPackage,
});

app.use(createPinia());
app.use(router);
app.use(i18n);

app.use(globalErrorHandler);
app.use(createCustomErrorHandler());

authenticationGuard(router);

app.mount('#app');
