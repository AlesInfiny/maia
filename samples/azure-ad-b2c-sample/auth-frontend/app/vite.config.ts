import { fileURLToPath, URL } from 'url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const plugins = [vue(), vueJsx(), vueDevTools()]
  const env = loadEnv(mode, process.cwd())

  return {
    plugins,
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
  }
})
