import { fileURLToPath, URL } from 'url';

import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const plugins = [vue(), vueJsx()];
  const env = loadEnv(mode, process.cwd());

  return {
    plugins: plugins,
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      proxy: {
        '/api': {
          target: env.VITE_BACKEND_ENDPOINT_ORIGIN,
          changeOrigin: true,
          configure: (proxy, options) => {
            options.autoRewrite = true;
            options.secure = false;
          },
        },
        '/swagger': {
          target: env.VITE_BACKEND_ENDPOINT_ORIGIN,
          changeOrigin: true,
          secure: false,
        },
      },
    },
  };
});
