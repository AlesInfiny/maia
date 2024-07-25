import { fileURLToPath, URL } from 'url';

import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { setupMockPlugin } from './vite-plugins/setup-mock';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const plugins = [vue(), vueJsx()];
  const env = loadEnv(mode, process.cwd());

  return {
    plugins: mode === 'mock' ? [...plugins, setupMockPlugin()] : plugins,
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      proxy: {
        '/api': {
          target: env.VITE_PROXY_ENDPOINT_ORIGIN,
          changeOrigin: true,
          autoRewrite: true,
          secure: false,
        },
        '/swagger': {
          target: env.VITE_PROXY_ENDPOINT_ORIGIN,
          changeOrigin: true,
          secure: false,
        },
      },
    },
  };
});
