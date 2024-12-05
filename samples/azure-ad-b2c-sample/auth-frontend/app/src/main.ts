import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';

async function enableMocking(): Promise<ServiceWorkerRegistration | undefined> {
  const { worker } = await import('../mock/browser');
  return worker.start({
    onUnhandledRequest: 'bypass',
  });
}

if (import.meta.env.MODE === 'mock') {
  try {
    await enableMocking();
  } catch (error) {
    // eslint-disable-next-line no-console
    console.error('モック用のワーカープロセスの起動に失敗しました。', error);
  }
}

const app = createApp(App);

app.use(createPinia());

app.mount('#app');
