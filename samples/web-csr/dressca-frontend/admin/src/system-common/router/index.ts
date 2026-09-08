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
import { catalogRoutes } from '@/catalog/router/catalog'
import { errorRoutes } from './error'
import { homeRoutes } from './home'

/**
 * vue-routerを作成します。
 */
export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [...errorRoutes, ...homeRoutes, ...catalogRoutes, ...authenticationRoutes],
})
