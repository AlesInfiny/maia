import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';

import '@/assets/base.css';

import { msalInstance } from '@/shared/authentication/authentication-config';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(msalInstance);

app.mount('#app');
