import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import { globalErrorHandler } from './shared/error-handler/global-error-handler';

const app = createApp(App);

app.use(createPinia());

app.use(globalErrorHandler);

app.mount('#app');
