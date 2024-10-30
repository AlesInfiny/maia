import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import App from './App.vue';
import { createCustomErrorHandler } from './shared/error-handler/custom-error-handler';

const app = createApp(App);

app.use(createPinia());

app.use(globalErrorHandler);
app.use(createCustomErrorHandler());

app.mount('#app');
