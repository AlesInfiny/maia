import type { RouteRecordRaw } from 'vue-router'
import { routeNames } from './route-names'

/**
 * アプリケーションのルート表です。
 * 画面（ src/pages ）の配置と URL の対応、認証の要否などのルートの属性をここで一覧できます。
 * `meta.requiresAuth` を指定しないルートは、認証が必要な画面として扱います。
 */
export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: routeNames.home,
    component: () => import('@/pages/HomePage.vue'),
  },
  {
    path: '/authentication/login',
    name: routeNames.login,
    component: () => import('@/pages/authentication/login/LoginPage.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/catalog/items',
    name: routeNames.catalogItems,
    component: () => import('@/pages/catalog/items/ItemsPage.vue'),
  },
  {
    path: '/catalog/items/add',
    name: routeNames.catalogItemsAdd,
    component: () => import('@/pages/catalog/items/add/ItemsAddPage.vue'),
  },
  {
    path: '/catalog/items/edit/:itemId',
    name: routeNames.catalogItemsEdit,
    component: () => import('@/pages/catalog/items/edit/ItemsEditPage.vue'),
  },
  {
    path: '/error',
    name: routeNames.error,
    component: () => import('@/pages/ErrorPage.vue'),
  },
  {
    path: '/:pathMatch(.*)*',
    name: routeNames.notFound,
    component: () => import('@/pages/NotFoundPage.vue'),
  },
]
