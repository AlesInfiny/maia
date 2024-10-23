import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import { createCustomErrorHandler } from './shared/error-handler/custom-error-handler';
import App from './App.vue';
import { router } from './router';

import '@/assets/base.css';
import '@/config/yup.config';

const app = createApp(App);
const pinia = createPinia();
const customErrorHandler = createCustomErrorHandler();

app.use(pinia);
app.use(router);

app.use(globalErrorHandler);
app.use(customErrorHandler);

authenticationGuard(router);

app.mount('#app');
