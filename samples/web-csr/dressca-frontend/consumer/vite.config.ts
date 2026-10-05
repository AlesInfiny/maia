import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv, type Plugin } from 'vite'
import vueRouter from 'vue-router/vite'
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
  const plugins = [
    // src/pages 配下の画面ファイルからルート定義を生成します（ file-based routing ）。
    // .vue ファイルを変換する前に定義を抽出するため、 vue() より前に配置します。
    vueRouter({
      root: fileURLToPath(new URL('./', import.meta.url)),
      routesFolder: 'src/pages',
      exclude: ['src/pages/**/__tests__/**'],
      dts: 'typed-router.d.ts',
    }),
    vue(),
    vueJsx(),
    vueDevTools(),
    tailwindcss(),
  ]
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
