/* eslint-disable import/no-default-export */
import { fileURLToPath, URL } from 'node:url';
import { defineConfig, loadEnv, Plugin } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import fs from 'fs';
import path from 'path';

/**
 * Mock Service Worker のワーカースクリプトを削除するプラグインです。
 * 本番ビルド時にワーカースクリプトを削除するために使用します。
 * @returns Vite のプラグイン
 */
function excludeMsw(): Plugin {
  return {
    name: 'exclude-msw',
    resolveId: (source) => {
      return source === 'virtual-module' ? source : null;
    },
    renderStart() {
      const outDir = './public';
      const msWorker = path.resolve(outDir, 'mockServiceWorker.js');
      // eslint-disable-next-line no-console
      fs.rm(msWorker, () => console.log(`Deleted ${msWorker}`));
    },
  };
}

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const plugins = [vue(), vueJsx()];
  const env = loadEnv(mode, process.cwd());

  return {
    plugins: mode === 'prod' ? [...plugins, excludeMsw()] : plugins,
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      port: 6173,
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
