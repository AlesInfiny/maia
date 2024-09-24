import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { errorHandlerPlugin } from '@/shared/error-handler/error-handler-plugin';
import App from './App.vue';
import { router } from './router';

import '@/assets/base.css';
import '@/config/yup.config';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);

app.use(errorHandlerPlugin);

authenticationGuard(router);

app.mount('#app');
