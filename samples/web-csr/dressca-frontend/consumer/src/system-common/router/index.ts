/*
 * 各ドメインが定義したルーティング定義を集約してルーターを構築します。
 *
 * システム共通からドメインへの参照は本来禁止ですが、
 * ルーティング定義は全ドメインを集約する役割を持つため、例外として扱います。
 * 例外の範囲は本モジュールと route-names.ts に限定します。
 */
import { createRouter, createWebHistory } from 'vue-router'
import { authenticationRoutes } from '@/authentication/login/router/authentication'
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
