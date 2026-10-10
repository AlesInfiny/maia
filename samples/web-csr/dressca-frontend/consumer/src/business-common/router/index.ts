/*
 * アプリケーションのルーターを構築します。
 * ルート定義は app 層のルート表（ routes.ts ）に集約します。
 * ナビゲーションガードは main.ts で登録します。
 */
import { createRouter, createWebHistory } from 'vue-router'
import { routes } from './routes'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})
