import './assets/base.css';
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { globalErrorHandler } from '@/shared/error-handler/global-error-handler';
import { createCustomErrorHandler } from '@/shared/error-handler/custom-error-handler';
import { router } from './router';
import App from './App.vue';

/**
 * モックモードでの実行時に、モック用のワーカープロセスが起動していることを確認します。
 * アプリケーションのマウント前にモック用のワーカープロセスが起動している必要があるからです。
 * @returns {Promise<ServiceWorkerRegistration | undefined>}
 */
async function enableMocking(): Promise<ServiceWorkerRegistration | undefined> {
  if (import.meta.env.MODE !== 'mock') {
    return undefined;
  }
  const { worker } = await import('../mock/browser');
  return worker.start({
    onUnhandledRequest: 'bypass',
  });
}

enableMocking().then(() => {
  const app = createApp(App);

  app.use(createPinia());
  app.use(router);

  app.use(globalErrorHandler);
  app.use(createCustomErrorHandler());

  authenticationGuard(router);

  app.mount('#app');
});
