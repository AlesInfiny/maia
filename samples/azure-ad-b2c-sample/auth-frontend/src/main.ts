import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import App from './App.vue';

const app = createApp(App);

app.use(createPinia());

app.use(globalErrorHandler);

app.mount('#app');
