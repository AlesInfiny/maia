/*
 * 各ドメインが定義したルーティング定義を集約してルーターを構築します。
 *
 * システム共通からコンテキストへの参照は本来禁止ですが、
 * ルーティング定義は全コンテキストを集約する役割を持つため、例外として扱います。
 * 例外の範囲は本モジュールと route-names.ts に限定します。
 */
import { createRouter, createWebHistory } from 'vue-router'
import { authenticationRoutes } from '@/security/authentication/router/authentication'
import { catalogRoutes } from '@/catalog-management/catalog/router/catalog'
import { errorRoutes } from './error'
import { homeRoutes } from './home'

/**
 * vue-routerを作成します。
 */
export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [...errorRoutes, ...homeRoutes, ...catalogRoutes, ...authenticationRoutes],
})
