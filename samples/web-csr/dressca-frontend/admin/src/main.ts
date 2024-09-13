import './assets/base.css';
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { authenticationGuard } from '@/shared/authentication/authentication-guard';
import { errorHandlerPlugin } from '@/shared/error-handler/error-handler-plugin';
import { router } from './router';
import App from './App.vue';

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
  const pinia = createPinia();

  app.use(pinia);
  app.use(router);

  app.use(errorHandlerPlugin);

  authenticationGuard(router);

  app.mount('#app');
});
