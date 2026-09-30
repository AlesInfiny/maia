import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv, Plugin } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'
import fs from 'fs'
import path from 'path'
import tailwindcss from '@tailwindcss/vite'

/**
 * Mock Service Worker のワーカースクリプトを削除するプラグインです。
 * 本番ビルド時にビルド成果物からワーカースクリプトを削除するために使用します。
 * @returns Vite のプラグイン
 */
function excludeMsw(): Plugin {
  return {
    name: 'exclude-msw',
    apply: 'build',
    writeBundle(outputOptions) {
      if (!outputOptions.dir) {
        return
      }
      const msWorker = path.resolve(outputOptions.dir, 'mockServiceWorker.js')
      fs.rmSync(msWorker, { force: true })
      // eslint-disable-next-line no-console
      console.log(`Deleted ${msWorker}`)
    },
  }
}

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const plugins = [vue(), vueJsx(), vueDevTools(), tailwindcss()]
  const env = loadEnv(mode, process.cwd())

  return {
    plugins: mode === 'prod' ? [...plugins, excludeMsw()] : plugins,
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      port: 5173,
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
