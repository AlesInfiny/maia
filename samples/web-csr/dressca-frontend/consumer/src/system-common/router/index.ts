/*
 * 各ドメインが定義したルーティング定義を集約してルーターを構築します。
 *
 * 参照方向のルール上、共通層からドメインへの参照は本来禁止ですが、
 * ルーティング定義は全ドメインを集約する役割を持つため、
 * アプリケーションシェル（App.vue / main.ts）と同じく例外として扱います。
 * 例外の範囲は本モジュールと route-names.ts に限定します。
 */
import { createRouter, createWebHistory } from 'vue-router'
import { authenticationRoutes } from '@/authentication/router/authentication'
import { displayItemRoutes } from '@/shopping/display-item/router/display-item'
import { basketRoutes } from '@/shopping/basket/router/basket'
import { orderingRoutes } from '@/shopping/ordering/router/ordering'
import { errorRoutes } from './error'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...authenticationRoutes,
    ...displayItemRoutes,
    ...basketRoutes,
    ...orderingRoutes,
    ...errorRoutes,
  ],
})
