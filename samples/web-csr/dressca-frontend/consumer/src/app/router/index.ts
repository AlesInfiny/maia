/*
 * アプリケーションのルーターを構築します。
 * ルート定義は file-based routing により、 src/pages 配下の画面ファイルの配置から自動で生成されます。
 * ナビゲーションガードは main.ts で登録します。
 */
import { createRouter, createWebHistory } from 'vue-router'
import { handleHotUpdate, routes } from 'vue-router/auto-routes'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// 開発サーバーで画面ファイルを追加・削除したとき、ルート定義を再読み込みします。
// Vitest の実行時はホットモジュールリプレースメントを使用しないため対象外とします。
if (import.meta.hot && import.meta.env.MODE !== 'test') {
  handleHotUpdate(router)
}
