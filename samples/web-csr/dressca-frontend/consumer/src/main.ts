import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { errorHandlerPlugin } from '@/shared/error-handler/error-handler-plugin';
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

app.use(errorHandlerPlugin);
app.use(customErrorHandler);

authenticationGuard(router);

app.mount('#app');
