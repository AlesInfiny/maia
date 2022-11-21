import { createApp, markRaw } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';

import '@/assets/base.css';
import '@/config/yup.config';
import '@/config/axios.config';
import 'vue-virtual-carousel/dist/style.css';

import { authenticationGuard } from '@/shared/authentication/authentication-guard';

const app = createApp(App);
const pinia = createPinia();

app.config.errorHandler = (err: unknown, vm, info) => {
  // 本サンプルAPではログの出力とエラー画面への遷移を行っています。
  // APの要件によってはサーバーやログ収集ツールにログを送信し、エラーを握りつぶすこともあります。
  console.log(err, vm, info);
  router.replace({ name: 'error' });
};

pinia.use(({ store }) => {
  store.router = markRaw(router);
});

app.use(pinia);
app.use(router);

authenticationGuard(router);

app.mount('#app');
