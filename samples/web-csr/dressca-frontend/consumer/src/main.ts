import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import App from './App.vue';
import { router } from './router';
import { i18n } from './locales/i18n';

import '@/assets/base.css';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(i18n);
app.use(globalErrorHandler);
app.use(createCustomErrorHandler());

authenticationGuard(router);

app.mount('#app');
